<template>
  <div class="centermap">
    <div class="maptitle">
      <div class="zuo"></div>
      <span class="titletext">{{ maptitle }}</span>
      <div class="you"></div>
    </div>
    <div class="mapwrap">
      <dv-border-box-13>
        <div class="video-main-container">
          <!-- 显示接收到的图片和嫌疑程度 -->
          <div class="display-container" v-if="receivedData.picUrl">
            <div class="image-wrapper">
              <img
                :src="receivedData.picUrl"
                alt="车辆图片"
                class="display-image"
                @error="handleImageError"
              />
              <div class="level-display">{{ receivedData.level }}</div>
            </div>
          </div>

          <!-- 无数据时的提示 -->
          <div v-else class="no-data-prompt">
            <i class="el-icon-picture-outline"></i>
            <p>等待接收车辆数据...</p>
          </div>
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
      maptitle: "车辆监控显示",
      receivedData: {
        picUrl: null,
        level: null
      }
    };
  },
  mounted() {
    // 监听A组件发送的数据
    ScnEventBus.$on('vehicle-data-received', this.handleVehicleData);
  },
  beforeDestroy() {
    ScnEventBus.$off('vehicle-data-received', this.handleVehicleData);
  },
  methods: {
    handleVehicleData(data) {
      // 接收A组件发送的数据
      this.receivedData = {
        picUrl: data.picUrl,
        level: data.level
      };
    },
    handleImageError(event) {
      // 图片加载失败时的处理
      event.target.src = this.getDefaultImage();
    },
    getDefaultImage() {
      return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg " width="400" height="300"%3E%3Crect fill="%23343f4f" width="400" height="300"%3E%3C/rect%3E%3Ctext x="50%" y="50%" font-size="16" text-anchor="middle" dominant-baseline="middle" fill="%23666"%3E图片加载失败%3C/text%3E%3C/svg%3E';
    }
  }
};
</script>

<style lang="scss" scoped>
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
      display: flex;
      align-items: center;
      justify-content: center;

      .display-container {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        .image-wrapper {
          position: relative;
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;

          .display-image {
            width: 100%;
            height: 100%;
            object-fit: cover; /* 强制全屏显示 */
            border: 2px solid rgba(0, 114, 255, 0.5);
            border-radius: 6px;
            box-shadow: 0 0 20px rgba(0, 114, 255, 0.3);
          }

          .level-display {
            position: absolute;
            top: 20px;
            right: 20px;
            font-size: 42px;
            font-weight: bold;
            color: #E6A23C;
            text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.9);
            padding: 12px 24px;
            background: rgba(0, 0, 0, 0.6);
            border-radius: 10px;
            border: 2px solid rgba(230, 162, 60, 0.7);
            z-index: 10;
          }
        }
      }

      .no-data-prompt {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: rgba(255, 255, 255, 0.5);
        font-size: 18px;

        i {
          font-size: 48px;
          margin-bottom: 15px;
        }
      }
    }
  }
}
</style>
