<template>
  <el-container class="fathercontainer">
    <!-- 顶部导航栏 -->
    <el-header :class="{ hidden: isNavHidden }" style="height: 70px; border-bottom: 0px solid;">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect"
        style="width: 100%;">
        <el-menu-item index="1" @click="goto('cigaretteIndex')" style="margin-left: 25%;font-size: 25px;">logo</el-menu-item>
        <el-submenu  index="2" style="margin-left: 15%;">
          <el-menu-item slot="title" index="2" >智慧监控</el-menu-item>
          <el-menu-item index="2-1" @click="goto('dataVisualization')">数据可视化</el-menu-item>
          <el-menu-item index="2-2" @click="goto('personelInformation')">人车信息案底一览</el-menu-item>
          <el-menu-item index="2-3" @click="goto('vehicleInformation')">车辆行踪预测一览</el-menu-item>
        </el-submenu>
        <el-submenu index="3">
          <el-menu-item slot="title" index="3" >预警中心</el-menu-item>
          <el-menu-item index="3-1" @click="goto('uploadCaution')">上报预警效果</el-menu-item>
          <el-menu-item index="3-2" @click="goto('uploadVehicle')">上报车辆信息</el-menu-item>
          <el-menu-item index="3-3" @click="goto('uploadPersonnel')">上报人员信息</el-menu-item>
        </el-submenu>
        <el-submenu index="4">
          <el-menu-item slot="title" index="4" >人员文档</el-menu-item>
          <el-menu-item index="4-1" @click="goto('notice')">通知公告</el-menu-item>
          <el-menu-item index="4-2" @click="goto('document')">执行文档</el-menu-item>
          <el-menu-item index="4-3" @click="goto('clockLog')">出勤信息</el-menu-item>
          <el-menu-item index="4-4" @click="goto('dutyLog')">执勤信息</el-menu-item>
          <el-menu-item index="4-5" @click="goto('feedBack')">反馈信息</el-menu-item>
        </el-submenu>
        <div class="right-menu">
            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
              <div class="avatar-wrapper">
                <img :src="avatar" class="user-avatar">
                <i class="el-icon-caret-bottom" />
              </div>
              <el-dropdown-menu slot="dropdown">
                <router-link to="/profile">
                  <el-dropdown-item>个人中心</el-dropdown-item>
                </router-link>
                <el-dropdown-item divided>
                  <router-link to="/index">
                  进入后台
                </router-link>
                </el-dropdown-item>
                <el-dropdown-item divided @click.native="logout">
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
        </div>
      </el-menu>
    </el-header>
    <!-- 左侧边栏区域 -->
    <el-container class="childrencontainer">
      <!-- 内容主体区域 -->
      <el-main style="width: 100%; min-height: 100%; margin: 0; padding: 0; padding-top: 70px;">
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
    <!-- 底部区域 -->
    <el-footer style="height: 100px;">
      <!-- 第一行 -->
      <!-- 第二行 -->
      <div class="tworow">
        <div>
          占位
        </div>
        <div>
          隐私
        </div>
        <div>备案</div>
        <div>法律</div>
      </div>
      <!-- 第三行 -->
      <div class="threerow">
        （版权号）
      </div>
    </el-footer>
  </el-container>
</template>

<script>

import { mapGetters } from 'vuex'
export default {
  name: 'Menu',
  data() {
    return {
      activeIndex: sessionStorage.getItem("activeIndex") || '4', // 假设默认选中第一个菜单项
      isNavHidden: false, // 初始状态为导航栏显示
      lastScrollTop: 0, // 上次滚动位置
      timeDifference: 0, // 初始时间差值为 0
      specifiedTime: new Date('2023-10-17T00:00:00').getTime(), // 指定时间
    }
  },
  computed: {
    ...mapGetters([
      'avatar',
    ]),
  },
  created() {
    window.addEventListener('scroll', this.handleScrollheader);
  },
  destroyed() {
    window.removeEventListener('scroll', this.handleScrollheader)
  },

  methods: {
    // 隐藏与显示导航栏
    handleScrollheader() {
      const st = window.pageYOffset || document.documentElement.scrollTop
      if (st > this.lastScrollTop) {
        this.isNavHidden = true // 向下滚动，隐藏导航栏
      } else {
        this.isNavHidden = false // 向上滚动，显示导航栏
      }
      this.lastScrollTop = st
    },
    // 点击路由实现跳转功能
    handleSelect(index, goto) {
      sessionStorage.setItem("activeIndex", index)
      this.activeIndex = sessionStorage.getItem("activeIndex", index)
      if (goto) {
        this.goto(goto)
      }
    },
    // 跳转网页
    goto(url) {
      this.$router.push({ path: url })
    },
    goToLogin() {
      this.$router.push('/index');
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
  },
}
</script>

<style scoped>
/* 总页布局 */
.fathercontainer {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  min-height: 100%;
  background-size: 100%;
  background-attachment: fixed;
}

/* 顶部导航栏 */
.el-header {
  z-index: 20;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  transition: top 0.3s;
  /* 添加过渡效果 */
}

.hidden {
  top: -70px;
  /* 隐藏导航栏 */
}

.el-menu-demo {
  position: absolute;
 
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0px;
  padding: 0px;
  width: 100%;
  /* height: 80px; */

}

.el-menu-demo .el-menu-item {
  text-align: center;
  /* 居中文本 */
  height: 70px;
  line-height: 70px;
  font-size: 18px;

  font-weight: bold;
}

/* .el-menu-demo .el-menu-item:hover {
  background-color: #1059d7;

  color: #e4e2dc;

} */

.left-menu,
.centered-menu,
.right-menu {
  display: flex;
  align-items: center;
}

.right-menu {
  margin-left: auto;
}

.centered-menu .el-menu-item {
  margin: 0 10px;
  /* 调整菜单项之间的间距 */
}

/* 中下布局 */
.childrencontainer {
  width: 100%;
  margin: 0px;
  padding: 0PX;
  margin-top: 0;
  /* 移除负的 margin-top */
}

/* 展示区区域 */
.el-main {
  z-index: 10;
  padding-top: 70px;
  /* 确保内容不会被导航栏覆盖 */
}

/* 底部区域 */
.el-footer {
  width: 100%;
  margin: 0;
  padding: 0;
  background: #fbfbfb;
  color: #000;
}

.tworow {
  width: 100%;
  height: 70px;
  margin: 0;
  padding: 0;
}

.tworow {
  display: flex;
  /* 使用 Flexbox 布局 */
  justify-content: space-between;
  /* 均匀分布五个列 */
  align-items: center;
  /* 垂直居中 */
}

.tworow>div {
  flex: 1;
  /* 利用 flex 属性使五个列宽度相等 */
  text-align: center;
  /* 文本水平居中 */
  margin: 0 5px;
  /* 左右间距为 5px */
  align-items: center;
  /* 垂直居中 */
}

.threerow {
  width: 100%;
  height: 30px;
  margin: 0;
  padding: 0;
  text-align: center;
  /* 文本水平居中 */
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
 
</style>
