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
/* 修改日期选择器弹窗的样式 */
.transparent-datepicker {
  background-color: rgba(0, 20, 40, 0.98) !important;
  border: 1px solid rgba(0, 150, 255, 0.6) !important;
  color: #e0f7ff !important;
}

.transparent-datepicker .el-picker-panel__body-wrapper,
.transparent-datepicker .el-picker-panel__body {
  background-color: transparent !important;
}

/* 时间选择面板 */
.transparent-datepicker .el-time-panel {
  background-color: rgba(0, 20, 40, 0.98) !important;
  border: 1px solid rgba(0, 150, 255, 0.6) !important;
}

/* 表头文本颜色 */
.transparent-datepicker .el-date-table th {
  color: #a0d2ff !important;
}

/* 不能选择的日期（禁用状态） */
.transparent-datepicker .el-date-table td.disabled div,
.transparent-datepicker .el-date-table td.disabled span {
  color: rgba(255, 255, 255, 0.3) !important;
  background-color: transparent !important;
  cursor: not-allowed !important;
}

/* 不能选择的日期背景 */
.transparent-datepicker .el-date-table td.disabled {
  background-color: rgba(100, 100, 100, 0.1) !important;
}

/* 当前日期（今天） */
.transparent-datepicker .el-date-table td.today span {
  color: #00eaff !important;
  font-weight: bold;
}

/* 当前选中日期 */
.transparent-datepicker .el-date-table td.current:not(.disabled) span {
  background-color: rgba(0, 150, 255, 0.7) !important;
  color: #fff !important;
}

/* 在范围内的日期 */
.transparent-datepicker .el-date-table td.in-range div,
.transparent-datepicker .el-date-table td.in-range div:hover,
.transparent-datepicker .el-date-table.is-week-mode .el-date-table__row.current div {
  background-color: rgba(0, 100, 200, 0.3) !important;
}

/* 开始和结束日期 */
.transparent-datepicker .el-date-table td.end-date span,
.transparent-datepicker .el-date-table td.start-date span {
  background-color: rgba(0, 150, 255, 0.8) !important;
  color: #fff !important;
}

/* 可用的日期悬停效果 */
.transparent-datepicker .el-date-table td.available:hover {
  background-color: rgba(0, 150, 255, 0.2) !important;
}

/* 时间选择器的项目 */
.transparent-datepicker .el-time-spinner__item.active:not(.disabled) {
  color: #00eaff !important;
}

/* 时间选择器项目悬停 */
.transparent-datepicker .el-time-spinner__item:hover:not(.disabled):not(.active) {
  background: rgba(0, 150, 255, 0.2) !important;
  color: #a0d2ff !important;
}

/* 按钮统一颜色 */
.transparent-datepicker .el-button,
.transparent-datepicker .el-picker-panel__icon-btn,
.transparent-datepicker .el-date-picker__header-label,
.transparent-datepicker .el-time-panel__header,
.transparent-datepicker .el-time-panel__content::before {
  color: #a0d2ff !important;
}

/* 按钮悬停 */
.transparent-datepicker .el-button:hover,
.transparent-datepicker .el-picker-panel__icon-btn:hover {
  color: #00eaff !important;
}

/* 确定按钮和此刻按钮 */
.transparent-datepicker .el-picker-panel__footer .el-button--text,
.transparent-datepicker .el-picker-panel__footer .el-button--default {
  color: #a0d2ff !important;
  background-color: transparent !important;
  border-color: rgba(0, 150, 255, 0.5) !important;
}

/* 确定/此刻按钮悬停 */
.transparent-datepicker .el-picker-panel__footer .el-button--text:hover,
.transparent-datepicker .el-picker-panel__footer .el-button--default:hover {
  color: #00eaff !important;
  background-color: rgba(0, 150, 255, 0.1) !important;
  border-color: rgba(0, 150, 255, 0.8) !important;
}

/* 日期选择器底部边框 */
.transparent-datepicker .el-picker-panel__footer {
  border-top: 1px solid rgba(0, 150, 255, 0.3) !important;
  background-color: rgba(0, 10, 25, 0.8) !important;
}

/* 月份/年份选择器 */
.transparent-datepicker .el-year-table td .cell,
.transparent-datepicker .el-month-table td .cell {
  color: #e0f7ff !important;
}

.transparent-datepicker .el-year-table td.current:not(.disabled) .cell,
.transparent-datepicker .el-month-table td.current:not(.disabled) .cell {
  color: #00eaff !important;
  background-color: rgba(0, 150, 255, 0.2) !important;
}

/* 禁用状态的年份/月份 */
.transparent-datepicker .el-year-table td.disabled .cell,
.transparent-datepicker .el-month-table td.disabled .cell {
  color: rgba(255, 255, 255, 0.3) !important;
  background-color: transparent !important;
}

/* 下拉箭头图标 */
.transparent-datepicker .el-icon-arrow-left:before,
.transparent-datepicker .el-icon-arrow-right:before,
.transparent-datepicker .el-icon-d-arrow-left:before,
.transparent-datepicker .el-icon-d-arrow-right:before {
  color: #a0d2ff !important;
}

/* 日期范围分隔线 */
.transparent-datepicker .el-date-range-picker__time-header,
.transparent-datepicker .el-date-range-picker__time-header .el-icon-arrow-right {
  color: #a0d2ff !important;
  border-color: rgba(0, 150, 255, 0.3) !important;
}
</style>
