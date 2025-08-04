<template>
  <div class="center_bottom">
    <!-- 保持原有容器结构 -->
    <div class="control-container">
      <!-- 分屏模式选择 -->
      <div class="mode-control">
        <div
          v-for="mode in modes"
          :key="mode.value"
          class="mode-btn"
          :class="{ 'active': currentMode === mode.value }"
          @click="changeMode(mode.value)"
        >
          {{ mode.label }}
        </div>
      </div>

      <!-- 摄像头选择 -->
      <div class="camera-control">
        <div
          v-for="camera in cameras"
          :key="camera.id"
          class="camera-btn"
          :class="{
            'active': selectedCameras.includes(camera.id),
            'disabled': isCameraDisabled(camera.id)
          }"
          @click="selectCamera(camera)"
        >
          {{ camera.name }}
        </div>
      </div>
      <div class="camera-control">
        <div class="camera-btn" @click="confirmSelection">
          确定
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ScnEventBus } from '@/utils/scn-event-bus';

export default {
  data() {
    return {
      isSuspectMode: false,
      currentMode: 1,
      selectedCameras: [],
      modes: [
        { value: 1, label: '1分屏' },
        { value: 2, label: '2分屏' },
        { value: 3, label: '3分屏' },
        { value: 4, label: '4分屏' }
      ],
      cameras: [
        { id: 1, name: '摄像头1', url: 'http://127.0.0.1:8000/upload/monitorData/192.168.10.2/卡口数据/20250509/09/0_2_20250328_000536940_云DTH993.mp4' },
        { id: 2, name: '摄像头2', url: 'http://127.0.0.1:8000/upload/monitorData/192.168.10.2/卡口数据/20250509/09/0_2_20250328_000536940_云DTH993.mp4' },
        { id: 3, name: '摄像头3', url: 'http://127.0.0.1:8000/upload/monitorData/192.168.10.2/卡口数据/20250509/09/0_2_20250328_000536940_云DTH993.mp4' },
        { id: 4, name: '摄像头4', url: 'http://127.0.0.1:8000/upload/monitorData/192.168.10.2/卡口数据/20250509/09/0_2_20250328_000536940_云DTH993.mp4' },
      ]
    };
  },
  methods: {
    changeMode(mode) {
      this.currentMode = mode;
      // 清空已选择的摄像头
      this.selectedCameras = [];
      if (mode === 1 && this.cameras.length > 0) {
        this.selectedCameras.push(this.cameras[0].id);
      }
    },
    selectCamera(camera) {
      const index = this.selectedCameras.indexOf(camera.id);
      if (index === -1) {
        if (this.selectedCameras.length < this.currentMode) {
          this.selectedCameras.push(camera.id);
        }
      } else {
        this.selectedCameras.splice(index, 1);
      }
    },
    isCameraDisabled(cameraId) {
      return !this.selectedCameras.includes(cameraId) &&
        this.selectedCameras.length >= this.currentMode;
    },
    confirmSelection() {
      // 验证是否选择了足够的摄像头
      if (this.selectedCameras.length !== this.currentMode) {
        alert(`请选择 ${this.currentMode} 个摄像头`);
        return;
      }
      // 触发显示逻辑
      this.updateDisplay();
    },
    updateDisplay() {
      const selectedCameras = this.selectedCameras.map(id =>
        this.cameras.find(cam => cam.id === id)
      ).filter(Boolean);

      ScnEventBus.$emit('video-display-change', {
        mode: this.currentMode,
        cameras: selectedCameras
      });
    },
  },
  created(){
    // 初次加载时默认选择“1分屏”
    this.currentMode = this.modes[0].value;
  },
  mounted(){
    // 确保页面渲染完成后，如果摄像头数组不为空，触发显示逻辑
    this.$nextTick(() => {
      if (this.cameras.length > 0) {
        this.selectedCameras.push(this.cameras[0].id); // 添加第一个摄像头的 id
        this.updateDisplay(); // 触发显示逻辑
      } else {
        console.warn('摄像头列表为空，无法选择默认摄像头');
      }
    });
  }
};
</script>

<style lang="scss" scoped>
/* 保持原有所有样式不变 */
.center_bottom {
  width: 100%;
  height: 100%;

  .control-container {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 10px;
    max-width: 1200px;
    margin: 0 auto;

    .mode-control {
      display: flex;
      justify-content: center;
      gap: 10px;

      .mode-btn {
        padding: 8px 15px;
        background: rgba(100, 100, 100, 0.2);
        border: 1px solid rgba(100, 100, 100, 0.5);
        border-radius: 4px;
        color: #fff;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background: rgba(59, 64, 151, 0.4);
        }

        &.active {
          background: rgba(0, 24, 62, 0.8);
          box-shadow: 0 0 5px rgba(0, 67, 143, 0.8);
        }
      }
    }

    .camera-control {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 10px;

      .camera-btn {
        padding: 8px 15px;
        background: rgba(100, 100, 100, 0.2);
        border: 1px solid rgba(100, 100, 100, 0.5);
        border-radius: 4px;
        color: #fff;
        cursor: pointer;
        transition: all 0.3s;

        &:hover:not(.disabled) {
          background: rgba(59, 64, 151, 0.4);
        }

        &.active {
          background: rgba(0, 24, 62, 0.8);
          box-shadow: 0 0 5px rgba(0, 67, 143, 0.8);
        }

        &.disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }
      }
    }
  }
}
</style>
