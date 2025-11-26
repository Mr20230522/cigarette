<template>
  <div class="center_bottom">
    <div class="control-container">
      <!-- 搜索框 -->
      <div class="search-container">
        <div class="search-field">
          <label for="search-plate">车牌号：</label>
          <input type="text" id="search-plate" v-model="searchParams.plate" placeholder="请输入车牌号">
        </div>
        <div class="search-field">
          <label for="search-suspicion-min">嫌疑程度：</label>
          <input
            type="number"
            id="search-suspicion-min"
            v-model="searchParams.suspicionMin"
            placeholder="最小值(0-99)"
            min="0"
            max="99"
            @input="validateSuspicionMin"
          >
          <span class="separator">至</span>
          <input
            type="number"
            id="search-suspicion-max"
            v-model="searchParams.suspicionMax"
            placeholder="最大值(1-100)"
            min="1"
            max="100"
            @input="validateSuspicionMax"
          >
        </div>
        <div class="search-field">
          <label for="search-start-date">起始日期：</label>
          <el-date-picker
            v-model="searchParams.startDate"
            type="datetime"
            placeholder="选择开始日期"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-ddTHH:mm:ss"
            @change="validateDateRange"
            :picker-options="pickerOptions"
            popper-class="transparent-datepicker"
          />
        </div>
        <div class="search-field">
          <label for="search-end-date">结束日期：</label>
          <el-date-picker
            v-model="searchParams.endDate"
            type="datetime"
            placeholder="选择结束日期"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-ddTHH:mm:ss"
            @change="validateDateRange"
            :picker-options="pickerOptions"
            popper-class="transparent-datepicker"
          />
        </div>
        <div class="search-actions">
          <button @click="performSearch">搜索</button>
          <button @click="resetSearch" class="reset-btn">重置</button>
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
      cameras: [],
      searchParams: {
        plate: null,
        suspicionMin: null,
        suspicionMax: null,
        startDate: null,
        endDate: null
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      }
    };
  },
  methods: {
    async getList() {
      try {
        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        const cameraResponse = await listCamera(this.queryParams);
        this.cameraList = cameraResponse.rows;
        this.total = cameraResponse.total;

        this.cameras = this.cameraList.map(camera => {
          const detection = this.detections.find(d => d.detectionId === camera.detectionId);
          return {
            id: camera.cameraId,
            name: `${camera.cameraId}${detection ? '-' + detection.detectionName : ''}`,
            url: 'http://127.0.0.1:8000/' + camera.cameraUrl.replace('/profile/', ''),
            suspicionLevel: camera.suspicionLevel,
            captureTime: camera.captureTime
          };
        });
      } catch (error) {
        console.error("数据加载失败:", error);
      }
    },
    validateSuspicionMin() {
      if (this.searchParams.suspicionMin === null || this.searchParams.suspicionMin === '') return;

      let value = parseInt(this.searchParams.suspicionMin);

      if (isNaN(value)) {
        this.searchParams.suspicionMin = null;
        return;
      }

      if (value < 0) value = 0;
      if (value > 99) value = 99;

      this.searchParams.suspicionMin = value;

      if (this.searchParams.suspicionMax !== null && this.searchParams.suspicionMax < value) {
        this.searchParams.suspicionMax = Math.min(value + 1, 100);
      }
    },
    validateSuspicionMax() {
      if (this.searchParams.suspicionMax === null || this.searchParams.suspicionMax === '') return;

      let value = parseInt(this.searchParams.suspicionMax);

      if (isNaN(value)) {
        this.searchParams.suspicionMax = null;
        return;
      }

      if (value < 1) value = 1;
      if (value > 100) value = 100;

      this.searchParams.suspicionMax = value;

      if (this.searchParams.suspicionMin !== null && this.searchParams.suspicionMin > value) {
        this.searchParams.suspicionMin = Math.max(value - 1, 0);
      }
    },
    validateDateRange() {
      // 验证时间范围
      if (this.searchParams.startDate && this.searchParams.endDate) {
        const start = new Date(this.searchParams.startDate);
        const end = new Date(this.searchParams.endDate);

        if (start > end) {
          this.$message.warning('开始时间不能大于结束时间');
          this.searchParams.endDate = null;
        }
      }
    },
    performSearch() {
      // 深拷贝参数避免响应式问题
      const params = JSON.parse(JSON.stringify({
        ...this.searchParams,
        // 确保时间格式正确
        startDate: this.searchParams.startDate ? this.searchParams.startDate + ':00' : null,
        endDate: this.searchParams.endDate ? this.searchParams.endDate + ':00' : null
      }))

      console.log('正在发送搜索参数:', params) // 调试日志

      // 使用$nextTick确保DOM更新后触发
      this.$nextTick(() => {
        ScnEventBus.$emit('vehicle-search', params)
      })

      // 本地过滤（可选）
      this.filterCameras()
    },
    resetSearch() {
      this.searchParams = {
        plate: null,
        suspicionMin: null,
        suspicionMax: null,
        startDate: null,
        endDate: null
      };
      // 发送重置后的空参数进行搜索
      ScnEventBus.$emit('vehicle-search', {...this.searchParams});
    },
    filterCameras() {
      // 数组本身保护
      if (!this.cameraList || !this.cameraList.length) {
        this.cameras = [];
        return;
      }

      this.cameras = this.cameraList.filter(camera => {
        let match = true;

        /* 1. 车牌号模糊 */
        const name = (camera.name || '').toLowerCase();
        const plate = (this.searchParams.plate || '').toLowerCase();
        if (plate && !name.includes(plate)) return false;

        /* 2. 嫌疑程度区间 */
        const suspicion = parseFloat(camera.suspicionLevel) || 0;
        const min = this.searchParams.suspicionMin ?? 0;
        const max = this.searchParams.suspicionMax ?? 100;
        if (suspicion < min || suspicion > max) return false;

        /* 3. 时间区间 */
        if (this.searchParams.startDate || this.searchParams.endDate) {
          const capture = new Date(camera.captureTime || 0);
          const start = this.searchParams.startDate ? new Date(this.searchParams.startDate) : null;
          const end = this.searchParams.endDate ? new Date(this.searchParams.endDate) : null;
          if (start && capture < start) return false;
          if (end && capture > end) return false;
        }

        return true;
      });
    }
  },
  created() {
    this.getList();
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
    gap: 12px;
    padding: 12px;
    max-width: 1200px;
    margin: 0 auto;

    .search-container {
      display: flex;
      flex-direction: column;
      gap: 12px;
      margin-bottom: 22px;

      .search-field {
        display: flex;
        align-items: center;
        gap: 12px;

        label {
          font-size: 15px;
          color: #fff;
          width: 125px;
          text-align: right;
        }

        input {
          padding: 9px 14px;
          border: 1px solid rgba(100, 100, 100, 0.5);
          border-radius: 5px;
          background: rgba(0, 0, 0, 0.1);
          color: #fff;
          font-size: 15px;
          width: 160px;

          &[type="number"] {
            width: 130px;
          }
        }

        ::v-deep .el-date-editor {
          width: 220px;

          .el-input__inner {
            background: rgba(0, 0, 0, 0.1);
            border-color: rgba(100, 100, 100, 0.5);
            color: #fff;
          }

          .el-input__prefix, .el-input__suffix {
            color: rgba(255, 255, 255, 0.6);
          }
        }

        .separator {
          color: #fff;
          padding: 0 6px;
          font-size: 14px;
        }
      }

      .search-actions {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-top: 10px;

        button {
          padding: 10px 24px;
          background: rgba(0, 24, 62, 0.8);
          border: none;
          border-radius: 5px;
          color: #fff;
          cursor: pointer;
          font-size: 15px;
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 36, 93, 0.8);
          }
        }

        .reset-btn {
          background: rgba(62, 0, 0, 0.8);

          &:hover {
            background: rgba(93, 0, 0, 0.8);
          }
        }
      }
    }
  }
}
</style>

<style>
/* 全局样式，用于时间选择器的透明背景 */
.transparent-datepicker {
  background-color: rgba(0, 0, 0, 0.7) !important;
  border: 1px solid rgba(0, 114, 255, 0.3) !important;
  color: #fff !important;
}

.transparent-datepicker .el-picker-panel__body-wrapper,
.transparent-datepicker .el-picker-panel__body {
  background-color: transparent !important;
}

.transparent-datepicker .el-time-panel {
  background-color: rgba(0, 0, 0, 0.7) !important;
  border: 1px solid rgba(0, 114, 255, 0.3) !important;
}

.transparent-datepicker .el-date-table th {
  color: rgba(255, 255, 255, 0.6) !important;
}

.transparent-datepicker .el-date-table td.current:not(.disabled) span {
  background-color: rgba(0, 114, 255, 0.5) !important;
}

.transparent-datepicker .el-date-table td.today span {
  color: #00eaff !important;
}

.transparent-datepicker .el-time-spinner__item.active:not(.disabled) {
  color: #00eaff !important;
}
</style>
