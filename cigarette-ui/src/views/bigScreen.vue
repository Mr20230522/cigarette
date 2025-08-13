<template>
  <div class="fixed-container">
    <div class="color">
      <ScaleScreen
        :width="1920"
        :height="1080"
        class="scale-wrap"
        :selfAdaption="$store.state.scnSettings.isScale"
      >
        <div class="bg">
          <dv-loading v-if="loading">Loading...</dv-loading>
          <div v-else class="host-body">
            <!-- 头部 s -->
            <div class="d-flex jc-center title_wrap">
              <div class="zuojuxing"></div>
              <div class="youjuxing"></div>
              <div class="guang"></div>
              <div class="d-flex jc-center">
                <div class="title">
                  <span class="title-text">曲靖烟草可视化平台</span>
                </div>
              </div>
              <div class="timers">
                {{ dateYear }} {{ dateWeek }} {{ dateDay }}
                <i class="el-icon-full-screen" style="margin-left: 10px; cursor: pointer;" @click="toggleFullscreen"></i>
              </div>
            </div>
            <!-- 头部 e-->
            <!-- 内容 s-->
            <Index />
            <!-- 内容 e -->
          </div>
        </div>
        <Setting ref="setting"/>
      </ScaleScreen>
    </div>
  </div>
</template>

<script>
import screenfull from 'screenfull';
import {formatTime} from "@/utils/scnIndex.js";
import Setting from "@/views/cigaretteFront/menu/big-screen/scnSetting.vue";
import ScaleScreen from "@/components/scn-scale-screen/scale-screen.vue";
import Index from "@/views/cigaretteFront/menu/big-screen/scn-indexs/index.vue";

export default {
  components: {Setting, ScaleScreen, Index},
  data() {
    return {
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  created() {
    this.autoLogin();
  },
  mounted() {
    this.timeFn();
    this.cancelLoading();
    window.addEventListener('beforeunload', this.handleBeforeUnload);
  },
  beforeDestroy() {
    clearInterval(this.timing);
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
    this.$store.dispatch('LogOut');
  },
  methods: {
    async autoLogin() {
      try {
        await this.$store.dispatch('Login', {
          username: 'admin',
          password: 'admin123'
        });
      } catch (error) {
        this.$message.error('大屏系统自动登录失败，请联系管理员');
      }
    },
    toggleFullscreen() {
      const element = document.querySelector('.fixed-container');
      if (screenfull.isEnabled) {
        screenfull.toggle(element);
      } else {
        this.$message.warning('您的浏览器不支持全屏功能');
      }
    },
    timeFn() {
      this.timing = setInterval(() => {
        this.dateDay = formatTime(new Date(), "HH: mm: ss");
        this.dateYear = formatTime(new Date(), "yyyy-MM-dd");
        this.dateWeek = this.weekday[new Date().getDay()];
      }, 1000);
    },
    cancelLoading() {
      setTimeout(() => {
        this.loading = false;
      }, 500);
    },
    async handleBeforeUnload(event) {
      try {
        await this.$store.dispatch('LogOut');
      } catch (error) {
        console.error('退出登录失败:', error);
      }
    }
  },
};
</script>

<style lang="scss">
@import "@/views/cigaretteFront/menu/big-screen/scnHome3";

/* 新增的最外层容器样式 */
.fixed-container {
  //position: fixed;
  //top: 0;
  //left: 0;
  //width: 100vw;
  height: 100vh;
  overflow: hidden;
  //display: flex;
  //justify-content: center;
  //align-items: center;
  background-color: #000;

  .color {
    position: relative;
    width: 1920px;
    height: 1080px;

    .scale-wrap {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }
  }
}

/* 全屏模式特殊处理 */
:fullscreen .fixed-container {
  .color {
    transform: none !important;
  }
}

/* 响应式缩放 - 保持原始比例 */
@media (max-width: 1920px) {
  .fixed-container .color {
    transform: scale(calc(100vw / 1920));
  }
}

@media (max-height: 1080px) {
  .fixed-container .color {
    transform: scale(calc(100vh / 1080));
  }
}

/* 全屏按钮样式调整 */
.el-icon-full-screen {
  font-size: 18px;
  color: #fff;
  transition: all 0.3s;

  &:hover {
    color: #409EFF;
    transform: scale(1.2);
  }
}
</style>
