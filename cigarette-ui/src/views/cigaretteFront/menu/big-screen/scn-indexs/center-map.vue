<template>
  <div class="centermap">
    <div class="maptitle">
      <div class="zuo"></div>
      <span class="titletext">{{ maptitle }}</span>
      <div class="you"></div>
    </div>
    <div class="mapwrap">
      <dv-border-box-13>
        <!-- 保持原有容器结构 -->
        <div class="video-main-container">
          <!-- 动态分屏显示 -->
          <div class="video-grid" :class="`grid-${displayMode}`">
            <div
                v-for="(camera, index) in displayedCameras"
                :key="index"
                class="grid-item"
            >
              <video
                  :ref="`videoPlayer${index}`"
                  :src="camera.url"
                  autoplay
                  muted
                  controls
              ></video>
              <div class="camera-label">{{ camera.name }}</div>
            </div>
          </div>

          <!-- 保持原有的无视频提示 -->
<!--          <div v-if="displayedCameras.length === 0" class="no-video">-->
<!--            请选择摄像头-->
<!--          </div>-->
        </div>
      </dv-border-box-13>
    </div>
  </div>
</template>

<script>
import { ScnEventBus } from '@/utils/scn-event-bus';

export default {
  data() {
    return {
      maptitle: "",
      displayMode: 1,
      displayedCameras: []
    };
  },
  mounted() {
    ScnEventBus.$on('video-display-change', this.updateDisplay);
    ScnEventBus.$on('suspected-video', this.updateDisplay);
  },
  beforeDestroy() {
    ScnEventBus.$off('video-display-change', this.updateDisplay);
    ScnEventBus.$off('suspected-video', this.updateDisplay);
  },
  methods: {
    updateDisplay({ mode, cameras }) {
      this.displayMode = mode;
      this.displayedCameras = cameras;

      this.$nextTick(() => {
        this.displayedCameras.forEach((_, index) => {
          const player = (this.$refs[`videoPlayer${index}`] && this.$refs[`videoPlayer${index}`][0]) || null;
          if (player) {
            player.load();
            player.play().catch(e => console.error('播放失败:', e));
          }
        });
      });
    }
  }
};
</script>

<style lang="scss" scoped>
/* 保持原有所有样式不变 */
.centermap {
  margin-bottom: 30px;

  .maptitle {
    height: 60px;
    display: flex;
    justify-content: center;
    padding-top: 10px;
    box-sizing: border-box;

    .titletext {
      font-size: 28px;
      font-weight: 900;
      letter-spacing: 6px;
      background: linear-gradient(
              92deg,
              #0072ff 0%,
              #00eaff 48.8525390625%,
              #01aaff 100%
      );
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin: 0 10px;
    }

    .zuo,
    .you {
      background-size: 100% 100%;
      width: 29px;
      height: 20px;
      margin-top: 8px;
    }

    .zuo {
      background: url("~@/assets/scn-img/xiezuo.png") no-repeat;
    }

    .you {
      background: url("~@/assets/scn-img/xieyou.png") no-repeat;
    }
  }

  .mapwrap {
    height: 548px;
    width: 100%;
    position: relative;

    .video-main-container {
      width: 100%;
      height: 100%;
      position: relative;

      .video-grid {
        width: 100%;
        height: 100%;
        display: grid;
        gap: 5px;
        padding: 5px;

        &.grid-1 {
          grid-template-columns: 1fr;
          grid-template-rows: 1fr;
        }

        &.grid-2 {
          grid-template-columns: 1fr 1fr;
          grid-template-rows: 1fr;
        }

        &.grid-3 {
          grid-template-columns: 1fr 1fr;
          grid-template-rows: 1fr 1fr;
          .grid-item:nth-child(3) {
            grid-column: span 2;
          }
        }

        &.grid-4 {
          grid-template-columns: 1fr 1fr;
          grid-template-rows: 1fr 1fr;
        }

        .grid-item {
          position: relative;
          overflow: hidden;
          border: 1px solid rgba(0, 114, 255, 0.3);

          video {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }

          .camera-label {
            position: absolute;
            bottom: 5px;
            left: 5px;
            background: rgba(0, 0, 0, 0.5);
            color: white;
            padding: 2px 5px;
            border-radius: 3px;
            font-size: 12px;
          }
        }
      }

      .no-video {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        color: #fff;
        font-size: 20px;
      }
    }
  }
}
</style>

