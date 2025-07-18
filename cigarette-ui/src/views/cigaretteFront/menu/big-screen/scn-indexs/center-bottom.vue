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
    </div>
  </div>
</template>

<script>
import { ScnEventBus } from '@/utils/scn-event-bus';

export default {
  data() {
    return {
      currentMode: 1,
      selectedCameras: [],
      modes: [
        { value: 1, label: '1分屏' },
        { value: 2, label: '2分屏' },
        { value: 3, label: '3分屏' },
        { value: 4, label: '4分屏' }
      ],
      cameras: [
        { id: 1, name: '摄像头1', url: 'url1' },
        { id: 2, name: '摄像头2', url: 'url2' },
        { id: 3, name: '摄像头3', url: 'url3' },
        { id: 4, name: '摄像头4', url: 'url4' }
      ]
    };
  },
  methods: {
    changeMode(mode) {
      this.currentMode = mode;
      if (this.selectedCameras.length > mode) {
        this.selectedCameras = this.selectedCameras.slice(0, mode);
      }
      this.updateDisplay();
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
      this.updateDisplay();
    },
    isCameraDisabled(cameraId) {
      return !this.selectedCameras.includes(cameraId) &&
          this.selectedCameras.length >= this.currentMode;
    },
    updateDisplay() {
      const selectedCameras = this.selectedCameras.map(id =>
          this.cameras.find(cam => cam.id === id)
      ).filter(Boolean);

      ScnEventBus.$emit('video-display-change', {
        mode: this.currentMode,
        cameras: selectedCameras
      });
    }
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