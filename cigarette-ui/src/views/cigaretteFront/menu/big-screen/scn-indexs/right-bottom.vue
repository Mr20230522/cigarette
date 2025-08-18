<template>
  <div class="vehicle-container" ref="container">
    <!-- 卡片视图 -->
    <div v-if="viewMode === 'card'" class="vehicle-info-container">
      <div class="info-header">
        <h3>嫌疑车辆详细信息</h3>
      </div>
      <div class="info-grid">
        <div
          v-for="(item, index) in displayItems"
          :key="index"
          class="info-item"
          :class="{ 'full-row': item.fullRow }"
        >
          <span class="info-label">{{ item.label }}：</span>
          <span
            class="info-value"
            :class="{
              'zhuyao': item.highlight,
              'warning': item.warning
            }"
          >
            {{ getFieldValue(item.field) }}
          </span>
        </div>
      </div>
    </div>

    <!-- 表格视图 -->
    <div v-else class="vehicle-table-container">
      <div class="table-header">
        <h3>嫌疑车辆列表</h3>
        <div class="table-summary">共 {{ filteredVehicles.length }} 条记录</div>
      </div>

      <div class="vehicle-table-wrapper">
        <table class="vehicle-table">
          <thead>
          <tr>
            <th>序号</th>
            <th>车牌号</th>
            <th>嫌疑程度</th>
            <th>抓拍时间</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(vehicle, index) in filteredVehicles" :key="index">
            <td>{{ index + 1 }}</td>
            <td>
              <span class="license-plate">{{ vehicle.licensePlate }}</span>
            </td>
            <td>
                <span class="suspicion-level" :class="getSuspicionClass(vehicle.suspicionLevel)">
                  {{ vehicle.suspicionLevel }}%
                </span>
            </td>
            <td>{{ formatTime(vehicle.captureTime) }}</td>
            <td class="action-buttons">
              <button @click="showImage(vehicle)" class="btn-image">图片</button>
              <button @click="showVideo(vehicle)" class="btn-video">视频</button>
              <button @click="showDetails(vehicle)" class="btn-details">详情</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ScnEventBus } from "@/utils/scn-event-bus";

export default {
  data() {
    return {
      // 视图模式：'card'或'table'
      viewMode: 'card',

      // 卡片视图数据
      vehicleData: {
        licensePlate: '暂无数据',
        carType: '暂无数据',
        suspicionLevel: '暂无数据',
        color: '暂无数据',
        detectionPoint: '暂无数据',
        remark: '暂无数据'
      },
      displayItems: [
        {label: '车牌号', field: 'licensePlate', highlight: true},
        {label: '车辆类型', field: 'carType'},
        {label: '嫌疑程度', field: 'suspicionLevel', warning: true},
        {label: '颜色', field: 'color'},
        {label: '监测点', field: 'detectionPoint', fullRow: true},
        {label: '备注', field: 'remark', fullRow: true, warning: true}
      ],

      // 表格视图数据
      allVehicles: [],
      filteredVehicles: [],
      searchParams: {},
      lastClickTime: 0
    };
  },
  created() {
    console.log('A组件初始化，开始监听事件')
    // 监听各种事件
    ScnEventBus.$on('new-visible-item', this.handleNewVisibleItem);
    ScnEventBus.$on('force-show-item', this.handleForceShowItem);
    ScnEventBus.$on('vehicle-search', this.handleSearch);
    ScnEventBus.$on('new-vehicle-data', this.handleNewData);
    ScnEventBus.$on('switch-view-mode', this.handleSwitchViewMode);
  },
  beforeDestroy() {
    ScnEventBus.$off('new-visible-item', this.handleNewVisibleItem);
    ScnEventBus.$off('force-show-item', this.handleForceShowItem);
    ScnEventBus.$off('vehicle-search', this.handleSearch);
    ScnEventBus.$off('new-vehicle-data', this.handleNewData);
    ScnEventBus.$off('switch-view-mode', this.handleSwitchViewMode);
  },
  methods: {
    // 切换视图模式
    handleSwitchViewMode(mode) {
      this.viewMode = mode;
    },

    // 卡片视图方法
    updateData(newData) {
      this.vehicleData = Object.assign({}, this.vehicleData, newData);
    },
    getFieldValue(field) {
      return this.vehicleData[field] || '暂无数据';
    },
    handleForceShowItem(data) {
      this.updateData(data);
      this.lastClickTime = Date.now();
    },
    handleNewVisibleItem(data) {
      if (Date.now() - this.lastClickTime > 2000) {
        this.updateData({
          licensePlate: data.plate || '无车牌',
          carType: data.vehicleType || '未知类型',
          suspicionLevel: data.level || 0,
          color: data.vehicleColor || '未知颜色',
          detectionPoint: data.cameraId || null,
          remark: data.remark || '无'
        });
      }
    },

    // 表格视图方法
    handleSearch(params) {
      this.searchParams = params;
      this.filterVehicles();
    },
    handleNewData(newData) {
      const vehicle = {
        licensePlate: newData.plate || '无车牌',
        suspicionLevel: newData.level || 0,
        captureTime: newData.captureTime || new Date().toISOString(),
        imageUrl: newData.imageUrl,
        videoUrl: newData.videoUrl,
        details: {
          carType: newData.vehicleType || '未知类型',
          color: newData.vehicleColor || '未知颜色',
          detectionPoint: newData.cameraId || '未知监测点'
        }
      };
      this.allVehicles.unshift(vehicle);
      this.filterVehicles();
    },
    filterVehicles() {
      if (!this.searchParams || Object.keys(this.searchParams).length === 0) {
        this.filteredVehicles = [...this.allVehicles];
        return;
      }

      this.filteredVehicles = this.allVehicles.filter(vehicle => {
        if (this.searchParams.plate &&
          !vehicle.licensePlate.includes(this.searchParams.plate)) {
          return false;
        }

        const suspicion = parseFloat(vehicle.suspicionLevel);
        if (this.searchParams.suspicionMin &&
          suspicion < this.searchParams.suspicionMin) {
          return false;
        }
        if (this.searchParams.suspicionMax &&
          suspicion > this.searchParams.suspicionMax) {
          return false;
        }

        if (this.searchParams.startDate || this.searchParams.endDate) {
          const captureTime = new Date(vehicle.captureTime);
          const startDate = this.searchParams.startDate ? new Date(this.searchParams.startDate) : null;
          const endDate = this.searchParams.endDate ? new Date(this.searchParams.endDate) : null;

          if (startDate && captureTime < startDate) return false;
          if (endDate && captureTime > endDate) return false;
        }

        return true;
      });
    },
    getSuspicionClass(level) {
      if (level >= 80) return 'high';
      if (level >= 50) return 'medium';
      return 'low';
    },
    formatTime(time) {
      if (!time) return '未知时间';
      return new Date(time).toLocaleString();
    },
    showImage(vehicle) {
      console.log('查看图片:', vehicle.imageUrl);
      // 实际项目中这里可以打开图片预览模态框
    },
    showVideo(vehicle) {
      console.log('查看视频:', vehicle.videoUrl);
      // 实际项目中这里可以打开视频播放器
    },
    showDetails(vehicle) {
      console.log('查看详情:', vehicle.details);
      // 可以切换到卡片视图显示详情
      this.viewMode = 'card';
      this.updateData({
        licensePlate: vehicle.licensePlate,
        carType: vehicle.details.carType,
        suspicionLevel: vehicle.suspicionLevel,
        color: vehicle.details.color,
        detectionPoint: vehicle.details.detectionPoint,
        remark: '来自表格数据'
      });
    }
  }
};
</script>

<style scoped>
/* 公共样式 */
.vehicle-container {
  width: 100%;
  height: 100%;
  font-family: inherit;
}

/* 卡片视图样式 */
.vehicle-info-container {
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow: hidden;
  box-sizing: border-box;
}

.info-header {
  text-align: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.info-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: calc(100% - 60px);
  overflow: auto;
}

.info-item {
  display: flex;
  min-height: 40px;
  align-items: center;
  padding: 0 15px;
  background: rgba(16, 42, 67, 0.7);
  border-radius: 4px;
  overflow: hidden;
}

.info-label {
  color: rgba(255, 255, 255, 0.6);
  min-width: 80px;
  font-size: 14px;
  flex-shrink: 0;
}

.info-value {
  color: #fff;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-grow: 1;
}

.zhuyao {
  color: #00eaff !important;
}

.warning {
  color: #E6A23C !important;
}

.full-row {
  grid-column: span 2;
}

/* 表格视图样式 */
.vehicle-table-container {
  width: 100%;
  height: 100%;
  padding: 15px;
  box-sizing: border-box;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.table-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 18px;
}

.table-summary {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

.vehicle-table-wrapper {
  width: 100%;
  height: calc(100% - 50px);
  overflow-y: auto;
}

.vehicle-table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
}

.vehicle-table th,
.vehicle-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.vehicle-table th {
  background: rgba(16, 42, 67, 0.7);
  color: #00eaff;
  font-weight: normal;
  position: sticky;
  top: 0;
}

.vehicle-table tbody tr:hover {
  background: rgba(0, 114, 255, 0.1);
}

.license-plate {
  color: #00eaff;
  font-weight: bold;
}

.suspicion-level {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 3px;
}

.suspicion-level.high {
  background: rgba(255, 0, 0, 0.2);
  color: #ff4d4f;
}

.suspicion-level.medium {
  background: rgba(255, 165, 0, 0.2);
  color: #faad14;
}

.suspicion-level.low {
  background: rgba(0, 255, 0, 0.2);
  color: #52c41a;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-buttons button {
  padding: 5px 10px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.3s;
}

.btn-image {
  background: rgba(0, 114, 255, 0.7);
  color: white;
}

.btn-video {
  background: rgba(220, 20, 60, 0.7);
  color: white;
}

.btn-details {
  background: rgba(0, 201, 167, 0.7);
  color: white;
}

.action-buttons button:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}
</style>
