<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
      @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav" />
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <search id="header-search" class="right-menu-item" />

        <el-tooltip :content="noticeContent" effect="dark" placement="bottom">
          <el-badge :value="noticeCount" class="right-menu-item hover-effect"
            :class="{ 'badge-custom': noticeCount > 0 }">
            <i class="el-icon-message-solid" @click="showNoticeListDialog"></i>
          </el-badge>
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <router-link to="/cigaretteIndex">
            <el-dropdown-item>进入前台</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 通知列表对话框 -->
    <el-dialog title="通知公告" :visible.sync="showNoticeList" width="50%">
      <el-table :data="noticeList" @row-click="handleNoticeClick" max-height="450" :default-sort = "{prop: 'createTime'}">
        <el-table-column prop="noticeTitle" label="公告标题" />
        <el-table-column prop="noticeType" label="公告类型" :formatter="formatNoticeType" width="100" />
        <el-table-column prop="createTime" sortable label="发布时间" width="180" />
      </el-table>
    </el-dialog>

    <!-- 通知详情对话框 -->
    <el-dialog :title="noticeDetail.noticeTitle" :visible.sync="showNoticeDetail" width="50%">
      <div v-html="noticeDetail.noticeContent"></div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { listNotice, getNotice } from "@/api/system/notice";

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  data() {
    return {
      noticeContent: '',//通知内容
      noticeCount: 0,//通知数量
      showNoticeList: false, // 是否显示通知列表对话框
      showNoticeDetail: false, // 是否显示通知详情对话框
      noticeList: [], // 通知列表
      noticeDetail: {} // 通知详情
    }
  },
  created() {
    this.poll();
  },
  mounted() {
    // 启动轮询
    this.startPolling();
  },
  beforeDestroy() {
    // 在组件销毁之前清除定时器，防止内存泄漏
    this.stopPolling();
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => { });
    },
    showNoticeListDialog() {
      this.showNoticeList = true;
      this.fetchNoticeList();
    },
    fetchNoticeList() {
      listNotice().then(response => {
        // 过滤掉状态为停用的通知（'status' 是状态字段，'0' 表示启用状态）
        this.noticeList = response.rows.filter(notice => notice.status === '0');
      });
    },
    handleNoticeClick(row) {
      getNotice(row.noticeId).then(response => {
        this.noticeDetail = response.data;
        this.showNoticeDetail = true;
      });
    },
    // toNoticePage(){
    //   //前往通知公告管理页面
    //   this.$router.push("/system/notice");
    // },
    startPolling() {
      // 每隔一定时间执行轮询任务
      this.intervalId = setInterval(() => {
        this.poll();
      }, 60000*20); // 20分钟轮询一次，根据需要调整间隔时间
    },
    stopPolling() {
      // 清除定时器，停止轮询任务    ！！！！重要，防止内存泄露
      clearInterval(this.intervalId);
    },
    poll() {
      // 在这里执行轮询的任务，可以是发送请求或执行其他操作
      listNotice().then(response => {
        // 过滤掉停用状态的通知（'status' 字段表示状态）
        const enabledNotices = response.rows.filter(notice => notice.status === '0');
        // 更新通知数量为启用状态的通知数目
        this.noticeCount = enabledNotices.length;
        this.noticeContent = "您有" + this.noticeCount + "条信息";//定制内容
      }).catch(error => {
        this.stopPolling(); // 如果发生错误，停止轮询
      });
    },
    formatNoticeType(row, column, value) {
      const typeMap = {
        '1': '公告',
        '2': '通知',
      };
      return typeMap[value] || '未知类型';
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }

    ::v-deep .el-badge__content {
      margin-top: 9px;
      /* 调整一下上下左右你喜欢的位置 */
      margin-right: 7px;
    }

    // .badge-custom {
    //   animation: blink-animation 0.5s infinite alternate; /* 设置动画效果 */
    // }
    //
    // @keyframes blink-animation {
    //   0% {
    //     transform: scale(1); /* 初始状态 */
    //   }
    //   100% {
    //     transform: scale(1.3); /* 最终状态 */
    //   }
    // }

  }
}
</style>
