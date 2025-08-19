<template>
  <div class="vehicle-container" ref="container">
    <!-- 卡片视图 -->
    <div v-if="viewMode === 'card'" class="vehicle-info-container">
      <div class="info-header">
        <h3>嫌疑车辆详细信息</h3>
      </div>
      <div class="info-grid">
        <!-- 第一行：车牌号和创建时间 -->
        <div class="info-row">
          <div class="info-item">
            <span class="info-label">车牌号：</span>
            <span class="info-value zhuyao">{{ vehicleData.plate || '暂无数据' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建时间：</span>
            <span class="info-value">{{ vehicleData.create_time || '暂无数据' }}</span>
          </div>
        </div>

        <!-- 其他字段 -->
        <div class="info-row">
          <div class="info-item">
            <span class="info-label">车型风险得分：</span>
            <span class="info-value">{{ vehicleData.vehicle_type_score || '暂无数据' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">品牌风险得分：</span>
            <span class="info-value warning">{{ vehicleData.brand_score || '暂无数据' }}</span>
          </div>
        </div>

        <div class="info-row">
          <div class="info-item">
            <span class="info-label">子品牌风险得分：</span>
            <span class="info-value">{{ vehicleData.sub_brand_score || '暂无数据' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">车牌风险得分：</span>
            <span class="info-value warning">{{ vehicleData.plate_risk_score || '暂无数据' }}</span>
          </div>
        </div>

        <div class="info-row">
          <div class="info-item">
            <span class="info-label">人脸风险得分：</span>
            <span class="info-value">{{ vehicleData.face_score || '暂无数据' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">时间风险得分：</span>
            <span class="info-value">{{ vehicleData.time_score || '暂无数据' }}</span>
          </div>
        </div>

        <div class="info-row">
          <div class="info-item">
            <span class="info-label">月份风险得分：</span>
            <span class="info-value">{{ vehicleData.month_score || '暂无数据' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">地点风险得分：</span>
            <span class="info-value">{{ vehicleData.location_score || '暂无数据' }}</span>
          </div>
        </div>

        <div class="info-row">
          <div class="info-item full-row">
            <span class="info-label">总风险分：</span>
            <span class="info-value warning">{{ vehicleData.total_score || '暂无数据' }}</span>
          </div>
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
            <th>车牌号</th>
            <th>照片</th>
            <th>视频</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="vehicle in filteredVehicles" :key="vehicle.licensePlate">
            <td>
              <span class="license-plate">{{ vehicle.licensePlate }}</span>
            </td>
            <td>
              <button @click="showImage(vehicle)" class="btn-image">查看照片</button>
            </td>
            <td>
              <button @click="showVideo(vehicle)" class="btn-video">播放视频</button>
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
        plate: '暂无数据',
        vehicle_type_score: '暂无数据',
        brand_score: '暂无数据',
        sub_brand_score: '暂无数据',
        plate_risk_score: '暂无数据',
        face_score: '暂无数据',
        time_score: '暂无数据',
        month_score: '暂无数据',
        location_score: '暂无数据',
        total_score: '暂无数据',
        create_time: '暂无数据'
      },

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
      console.log('切换视图模式:', mode);
      this.viewMode = mode;
    },

    // 卡片视图方法
    updateData(newData) {
      this.vehicleData = Object.assign({}, this.vehicleData, newData);
    },
    handleForceShowItem(data) {
      console.log('!!!!@@@@')
      console.log('接收到强制显示数据:', data);
      this.viewMode = 'card'; // 确保切换到卡片视图
      this.updateData(data);
      this.lastClickTime = Date.now();
    },
    handleNewVisibleItem(data) {
      if (Date.now() - this.lastClickTime > 2000) {
        this.updateData({
          plate: data.plate || '暂无数据',
          vehicle_type_score: data.vehicle_type_score || '暂无数据',
          brand_score: data.brand_score || '暂无数据',
          sub_brand_score: data.sub_brand_score || '暂无数据',
          plate_risk_score: data.plate_risk_score || '暂无数据',
          face_score: data.face_score || '暂无数据',
          time_score: data.time_score || '暂无数据',
          month_score: data.month_score || '暂无数据',
          location_score: data.location_score || '暂无数据',
          total_score: data.total_score || '暂无数据',
          create_time: data.create_time || '暂无数据',
        });
      }
    },

    // 表格视图方法
    handleSearch(params) {
      // 1. 强制切换到表格视图
      this.viewMode = 'table';

      // 2. 更新搜索参数
      this.searchParams = params;

      // 3. 执行过滤
      this.filterVehicles();

      // 4. 调试输出（可选）
      console.log("当前视图模式:", this.viewMode);
      console.log("搜索参数:", this.searchParams);
      console.log("过滤后数据:", this.filteredVehicles);
    },
    handleNewData(newData) {
      console.log('!!@@@newData',newData)
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
        plate: vehicle.licensePlate,
        vehicle_type_score: vehicle.details.carType,
        brand_score: vehicle.suspicionLevel,
        sub_brand_score: vehicle.details.color,
        plate_risk_score: vehicle.details.detectionPoint,
        total_score: '来自表格数据'
      });
    }
  }
};
</script>

<style scoped>
/* 公共基础样式 */
.vehicle-container {
  width: 100%;
  height: 100%;
  font-family: inherit;
}

/* 卡片视图样式 - 放大版 */
.vehicle-info-container {
  width: 100%;
  height: 100%;
  padding: 14px;  /* 增大内边距 */
  box-sizing: border-box;
  overflow-y: auto;
}

.info-header {
  text-align: center;
  margin-bottom: 14px;  /* 增大间距 */
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.info-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 17px;  /* 增大标题字号 */
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;  /* 增大行间距 */
}

.info-row {
  display: flex;
  gap: 12px;  /* 增大列间距 */
}

.info-item {
  flex: 1;
  min-height: 40px;  /* 增大最小高度 */
  padding: 10px 14px;  /* 增大内边距 */
  background: rgba(16, 42, 67, 0.7);
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.info-item.full-row {
  flex: 0 0 calc(100% - 30px);
}

.info-label {
  color: rgba(255, 255, 255, 0.6);
  min-width: 90px;  /* 增大标签宽度 */
  font-size: 14px;  /* 增大字号 */
  flex-shrink: 0;
}

.info-value {
  color: #fff;
  font-size: 14px;  /* 增大字号 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-grow: 1;
}

/* 特殊颜色样式 */
.zhuyao {
  color: #00eaff !important;
  font-size: 15px !important;  /* 重点字段更大 */
}

.warning {
  color: #E6A23C !important;
  font-size: 15px !important;  /* 警告字段更大 */
}

/* 表格视图样式 - 同步放大 */
.vehicle-table-container {
  width: 100%;
  height: 100%;
  padding: 16px;
  box-sizing: border-box;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.table-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 17px;
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
  font-size: 14px;  /* 表格字体放大 */
}

.vehicle-table th,
.vehicle-table td {
  padding: 12px 15px;  /* 增大单元格内边距 */
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
  font-size: 15px;  /* 车牌号更大 */
}

.btn-image, .btn-video {
  padding: 6px 12px;  /* 按钮增大 */
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 14px;  /* 按钮文字放大 */
  transition: all 0.3s;
  min-width: 100px;  /* 按钮最小宽度增大 */
}

.btn-image {
  background: rgba(0, 114, 255, 0.7);
  color: white;
}

.btn-video {
  background: rgba(220, 20, 60, 0.7);
  color: white;
}

.btn-image:hover, .btn-video:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

.vehicle-table td {
  text-align: center;
}
</style>
