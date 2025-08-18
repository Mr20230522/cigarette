<template>
  <div class="center_bottom">
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
      <div class="camera-control-wrapper">
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
import { listCamera } from "@/api/cigarette/detection/camera";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
  data() {
    return {
      detectionOptions: [],
      detections: [],
      total: 0,
      cameraList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 9999,
        cameraIp: null,
        cameraModel: null,
        cameraManufacturer: null,
        detectionId: null,
        resolutionRatio: null,
        frameRate: null,
        nightVision: null,
        connectionType: null,
        installationDate: null,
        guaranteePeriod: null,
        longitude: null,
        latitude: null,
        cameraType: null,
        cameraGroupIndication: null,
        cameraApplicationType: null,
        status: null,
        detectionName: null,
        districtId: null,
      },
      isSuspectMode: false,
      currentMode: 1,
      selectedCameras: [],
      modes: [
        { value: 1, label: '1分屏' },
        { value: 2, label: '2分屏' },
        { value: 3, label: '3分屏' },
        { value: 4, label: '4分屏' }
      ],
      cameras: [] // 初始化为空数组，将通过API数据填充
    };
  },
  methods: {
    changeMode(mode) {
      this.currentMode = mode;
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
      if (this.selectedCameras.length !== this.currentMode) {
        alert(`请选择 ${this.currentMode} 个摄像头`);
        return;
      }
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
    async getList() {
      try {
        // 1. 获取检测点数据
        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        // 2. 获取摄像头数据
        const cameraResponse = await listCamera(this.queryParams);
        this.cameraList = cameraResponse.rows;
        this.total = cameraResponse.total;

        // 3. 动态生成cameras数组
        this.cameras = this.cameraList.map(camera => {
          const detection = this.detections.find(d => d.detectionId === camera.detectionId);
          return {
            id: camera.cameraId,
            name: `${camera.cameraId}${detection ? '-' + detection.detectionName : ''}`,
            url: 'http://127.0.0.1:8000/'+camera.cameraUrl.replace('/profile/', '')
          };
        });

        // 4. 默认选中第一个摄像头
        if (this.cameras.length > 0) {
          this.selectedCameras = [this.cameras[0].id];
          this.updateDisplay();
        }
      } catch (error) {
        console.error("数据加载失败:", error);
      }
    }
  },
  created() {
    this.getList();
    this.currentMode = this.modes[0].value;
  }
};
</script>

<style lang="scss" scoped>
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

    /* 新增的摄像头控制容器 */
    .camera-control-wrapper {
      max-height: 120px; /* 三行高度（每行约40px） */
      overflow-y: auto;  /* 垂直滚动 */
      padding-right: 5px; /* 为滚动条留出空间 */

      /* 自定义滚动条样式 */
      &::-webkit-scrollbar {
        width: 6px;
      }
      &::-webkit-scrollbar-thumb {
        background: rgba(255, 255, 255, 0.3);
        border-radius: 3px;
      }
      &::-webkit-scrollbar-track {
        background: rgba(0, 0, 0, 0.1);
      }
    }

    .camera-control {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 10px;
      min-height: 40px; /* 确保至少一行高度 */

      .camera-btn {
        padding: 8px 15px;
        background: rgba(100, 100, 100, 0.2);
        border: 1px solid rgba(100, 100, 100, 0.5);
        border-radius: 4px;
        color: #fff;
        cursor: pointer;
        transition: all 0.3s;
        flex-shrink: 0; /* 防止按钮被压缩 */

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

