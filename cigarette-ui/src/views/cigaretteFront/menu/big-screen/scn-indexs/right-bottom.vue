<template>
  <div class="vehicle-info-container" ref="container">
    <!-- 标题 -->
    <div class="info-header">
      <h3>嫌疑车辆详细信息</h3>
    </div>

    <!-- 动态数据绑定的网格 -->
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
</template>

<script>
import {ScnEventBus} from "@/utils/scn-event-bus";
import {listDetection} from "@/api/cigarette/detection/detection";
import {getDicts} from "@/api/system/dict/data";

export default {
  data() {
    return {
      // 默认数据，防止未传值时显示空白
      vehicleData: {
        licensePlate: '暂无数据',
        carType: '暂无数据',
        suspicionLevel: '暂无数据',
        color: '暂无数据',
        detectionPoint: '暂无数据',
        remark: '暂无数据'
      },
      detectionOptions: [],
      vehicleType: [],
      // 显示配置项
      displayItems: [
        {label: '车牌号', field: 'licensePlate', highlight: true},
        {label: '车辆类型', field: 'carType'},
        {label: '嫌疑程度', field: 'suspicionLevel', warning: true},
        {label: '颜色', field: 'color'},
        {label: '监测点', field: 'detectionPoint', fullRow: true,},
        {label: '备注', field: 'remark', fullRow: true, warning: true}
      ]
    };
  },
  created() {
    this.loadDetectionOptions()
    this.loadDict()
  },
  mounted() {
    // 绑定事件监听
    ScnEventBus.$on('suspected-information', this.updateData);
  },
  beforeDestroy() {
    // 移除事件监听，防止内存泄漏
    ScnEventBus.$off('suspected-information', this.updateData);
  },
  methods: {
    // 更新数据（确保响应式）
    updateData(newData) {
      // 使用 Object.assign 确保 Vue 能检测到变化
      this.vehicleData = Object.assign({}, this.vehicleData, newData);
      this.vehicleData.detectionPoint = this.getDetectionName(this.vehicleData.detectionPoint)
      this.vehicleData.carType=this.getVehicleTypeName(this.vehicleData.carType)
      // 调试日志（可选）
    },

    // 安全获取字段值，防止 undefined
    getFieldValue(field) {
      return this.vehicleData[field] || '暂无数据';
    },
    loadDict() {
      getDicts('tob_vehicle_type').then(response => {
        this.vehicleType = response.data.map(item => ({
          carTypeId: item.dictValue,
          carType: item.dictLabel,
        }))
      }).catch(error => {
        console.error("Failed to load vehicleType options:", error);
      });
    },
    // 加载检测点选项
    loadDetectionOptions() {
      listDetection().then(response => {
        this.detectionOptions = response.rows.map(item => ({
          detectionId: item.detectionId,
          districtId: item.districtId,
          detectionName: item.detectionName
        }));
      }).catch(error => {
        console.error("Failed to load detection options:", error);
      });
    },
    getVehicleTypeName(carTypeId) {
      const vType = this.vehicleType.find(item => String(item.carTypeId) === String(carTypeId))
      return vType ? vType.carType : '未知车型';
    },
    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知监测点';
    },
  }
};
</script>

<style scoped>
.vehicle-info-container {
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow: hidden;
  box-sizing: border-box;
  font-family: inherit;
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
</style>
