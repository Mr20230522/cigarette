<template>
  <div class="vehicle-container" ref="container">
    <!-- 卡片视图 -->
    <div v-if="viewMode === 'card'" class="vehicle-info-container">
      <div class="info-header">
        <h3>嫌疑车辆嫌疑度因子信息分析</h3>
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
            <th>嫌疑值</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="vehicle in filteredVehicles" :key="vehicle.id">
            <td>
              <span class="license-plate">{{ vehicle.licensePlate }}</span>
            </td>
            <td>
              <button @click="enlargeImage(vehicle.imageUrl)" class="btn-image">查看照片</button>
            </td>
            <td>
              <span class="suspicion-text">{{ vehicle.suspicionLevel }}</span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <!-- 分页条（Ruo 封装版，带“前往”输入框） -->
      <div class="pagination-wrapper">
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="searchQuery.pageNum"
          :limit.sync="searchQuery.pageSize"
          layout="prev, pager, next, jumper"
          @pagination="handlePagination"
        />
      </div>
    </div>
    <div v-if="enlargedImage" class="image-preview-overlay" @click.self="closeEnlarge">
      <div class="image-preview-container">
        <img :src="enlargedImage" class="enlarged-image" @error="enlargedImage = getDefaultImage()">
      </div>
    </div>
  </div>
</template>
<script>
import {ScnEventBus} from "@/utils/scn-event-bus";
import {byIdGetVideoPath, searchVehicle} from '@/api/cigarette/trafficData/trafficData';
import Pagination from '@/components/Pagination'

export default {
  components: {Pagination},   // 关键
  data() {
    return {
      enlargedImage: null,
      total: 0,        // 总条数
      searchQuery: {
        plate: '',
        suspicionMin: 0,
        suspicionMax: 0,
        startDate: '',
        endDate: '',
        pageNum: 1,    // 当前页
        pageSize: 7   // 每页条数（与截图一致）
      },
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
    handlePagination(pagination) {
      console.log('【分页】事件触发', pagination)
      this.searchQuery.pageNum = pagination.page
      this.searchQuery.pageSize = pagination.limit
      this.fetchTableData()
    },
    async fetchTableData() {
      console.log('【fetchTableData】发起请求，参数：', this.searchQuery)
      const res = await searchVehicle(this.searchQuery)   // 直接用当前分页参数
      console.log('【fetchTableData】后端返回：', res)

      if (res?.rows) {
        this.total = res.total
        // 后端已分页，直接拿来显示
        this.filteredVehicles = res.rows.map(it => ({
          licensePlate: it.plate || '无车牌',
          suspicionLevel: it.level ?? 0,
          imageUrl: it.picUrl
            ? 'http://127.0.0.1:8000/' + it.picUrl.replace(/^\/+/, '')
            : this.getDefaultImage()
        }))
        console.log('【fetchTableData】表格数据已更新，条数：', this.filteredVehicles.length)
        console.log('【fetchTableData】：', this.filteredVehicles)
      } else {
        this.filteredVehicles = []
        this.total = 0
        console.warn('【fetchTableData】无数据返回')
      }
    },
    enlargeImage(url) {
      // 没图或地址异常就用默认图
      this.enlargedImage = url || this.getDefaultImage()
    },
    closeEnlarge() {
      this.enlargedImage = null
    },

    async fetchData() {
      try {
        const response = await searchVehicle(this.searchQuery);
        console.log("!!!!!!", response)
        return response;
      } catch (error) {
        console.error('获取视频失败:', error);
        return null;
      }
    },
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

    async handleSearch(params) {
      console.log('【right-bottom】收到搜索条件 >>>', params)
      /* 1. 合并条件并回到第1页 */
      Object.assign(this.searchQuery, params, {pageNum: 1})
      /* 2. 切视图 */
      this.viewMode = 'table'
      /* 3. 拉数据 */
      await this.fetchTableData()
    },


    handleNewData(newData) {
      console.log('!!@@@newData', newData)
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
    },
    getDefaultImage() {
      return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="80" height="60"%3E%3Crect fill="%23e0e0e0" width="80" height="60"/%3E%3Ctext x="50%" y="50%" font-size="10" text-anchor="middle" dominant-baseline="middle" fill="%23666"%3E图片加载失败%3C/text%3E%3C/svg%3E';
    }
  }
};
</script>

<style scoped>
/* 公共基础样式 */
.vehicle-container {
  width: 100%;
  height: auto;
  max-height: 430px;
  font-family: inherit;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 卡片视图样式 */
.vehicle-info-container {
  width: 100%;
  height: auto;
  padding: 8px;
  box-sizing: border-box;
  overflow-y: auto;
  flex: 1;
}

filterCameras
.info-header {
  text-align: center;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.info-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 15px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-row {
  display: flex;
  gap: 6px;
}

.info-item {
  flex: 1;
  min-height: 30px;
  padding: 6px 8px;
  background: rgba(16, 42, 67, 0.7);
  border-radius: 3px;
  display: flex;
  align-items: center;
}

.info-item.full-row {
  flex: 0 0 calc(100% - 20px);
}

.info-label {
  color: rgba(255, 255, 255, 0.6);
  min-width: 75px;
  font-size: 12px;
  flex-shrink: 0;
}

.info-value {
  color: #fff;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-grow: 1;
}

/* 特殊颜色样式 */
.zhuyao {
  color: #00eaff !important;
  font-size: 13px !important;
}

.warning {
  color: #E6A23C !important;
  font-size: 13px !important;
}

/* 表格视图样式 */
.vehicle-table-container {
  width: 100%;
  height: auto;
  padding: 10px;
  box-sizing: border-box;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(0, 114, 255, 0.3);
}

.table-header h3 {
  color: #00eaff;
  margin: 0;
  font-size: 15px;
}

.table-summary {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.vehicle-table-wrapper {
  width: 100%;
  flex: 1;
  overflow-y: auto;
}

.vehicle-table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
  font-size: 12px;
}

.vehicle-table th,
.vehicle-table td {
  padding: 8px 10px;
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
  font-size: 13px;
}

.btn-image, .btn-video {
  padding: 4px 8px;
  border: none;
  border-radius: 2px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
  min-width: 80px;
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

/* ===== 大屏预览 ===== */
.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, .8);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: zoom-out;
}

.image-preview-container {
  width: 80%;
  max-width: 800px;
  max-height: 80vh;
}

.enlarged-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border: 2px solid #0072ff;
  box-shadow: 0 0 20px rgba(0, 114, 255, .5);
}

.vehicle-table-container {
  display: flex;
  flex-direction: column;
  height: 100%; /* 撑满外部高度 */
}

.vehicle-table-wrapper {
  flex: 1; /* 表格占剩余空间 */
  overflow-y: auto;
}

.pagination-container { /* 分页始终贴底 */
  margin-top: auto;
  padding-top: 8px;
}



/* ========== 外层容器：比父容器窄 + 居中 + 己土米黄 ========== */
.pagination-wrapper {
  width: 85%; /* 缩窄 20 % */

  margin: 15px 10px; /* 水平居中 */
  background: black; /* 己土米黄 */
  //padding: 8px 0; /* 上下留点呼吸感 */
  border-radius: 4px; /* 圆角 */
}

.pagination-container {
  background: black;
  padding: 8px;
  border-radius: 4px;
}
</style>
