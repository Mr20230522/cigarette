<template>
  <div>
    <!-- 时间范围选择按钮 -->
    <div class="channel-selector">
      <div
        v-for="label in timeOptions"
        :key="label.value"
        class="channel-item"
        :class="{ active: selectTimeType === label.value }"
        @click="setTimeRange(label.value)"
      >
        {{ label.label }}
      </div>
    </div>

    <!-- 统计值列表 -->
    <ul class="user_Overview flex">
      <li class="user_Overview-item" style="color: #00fdfa">
        <div class="user_Overview_nums allnum">
          <dv-digital-flop :config="config" style="width:100%;height:100%;" />
        </div>
        <p>车辆总数</p>
      </li>
      <li class="user_Overview-item" style="color: #07f7a8">
        <div class="user_Overview_nums online">
          <dv-digital-flop :config="onlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>正常车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #f5023d">
        <div class="user_Overview_nums laramnum">
          <dv-digital-flop :config="laramnumconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #e3b337">
        <div class="user_Overview_nums offline">
          <dv-digital-flop :config="offlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆占比</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { listVehicleBehaviorVoAll, getUpToDataDegreeSuspicion } from "@/api/cigarette/vehicle/vehicleBehavior";

export default {
  data() {
    return {
      selectTimeType: 1, // 默认选择最近一天
      selectedTime: '',
      timeOptions: [
        { label: '最近一天', value: 1 },
        { label: '最近一月', value: 2 },
        { label: '最近一星期', value: 3 }
      ],
      allData: [], // 存储所有数据（包括定期获取的新数据）
      vehicleBehaviorList: [], // 存储计算结果
      queryParams: {
        pageNum: 1,
        pageSize: 1000,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },
      queryParams2: {
        pageNum: 1,
        pageSize: 1000,
        degreeSuspicion: null,
        behaviorId: null // 用于获取比此ID更新的数据
      },
      config: {
        number: [0],
        content: '{nt}',
        style: { fontSize: 24, fill: "#00fdfa" }
      },
      onlineconfig: {
        number: [0],
        content: '{nt}',
        style: { fontSize: 24, fill: "#07f7a8" }
      },
      offlineconfig: {
        number: [0],
        content: '{nt}%',
        style: { fontSize: 24, fill: "#e3b337" }
      },
      laramnumconfig: {
        number: [0],
        content: '{nt}',
        style: { fontSize: 24, fill: "#f5023d" }
      },
      refreshTimer: null, // 定时器
      refreshInterval: 5000 // 5秒刷新一次
    }
  },
  created() {
    this.initData();
  },
  beforeDestroy() {
    clearInterval(this.refreshTimer);
  },
  methods: {
    // 初始化数据
    initData() {
      this.calculateTime(this.selectTimeType);
      // 先清除现有定时器
      clearInterval(this.refreshTimer);
      this.getInitialData();
      this.startAutoRefresh();
    },

    // 获取初始数据（完全替换）
    async getInitialData() {
      try {
        const params = {
          ...this.queryParams,
          startTime: this.selectedTime,
          endTime: this.formatDate(new Date())
        };

        const response = await listVehicleBehaviorVoAll(params);
        console.log('初始数据获取完成:', response);

        if (response && response.length > 0) {
          // 使用Vue.set确保响应式更新
          this.$set(this, 'allData', response);
          this.queryParams2.behaviorId = response[response.length - 1].behaviorId;

          // 数据获取完成后再处理并启动定时器
          this.processData();
          this.startAutoRefresh();
        } else {
          console.error("数据格式异常:", response);
          this.setDefaultData();
        }
      } catch (error) {
        console.error("获取初始数据失败:", error);
        this.setDefaultData();
      }
    },

    // 开始定时刷新
    startAutoRefresh() {
      // 先清除现有定时器
      clearInterval(this.refreshTimer);

      // 创建新定时器
      this.refreshTimer = setInterval(async () => {
        await this.getNewData();
      }, this.refreshInterval);

      console.log('定时器已启动，间隔:', this.refreshInterval);
    },

    // 获取新增数据（追加）
    async getNewData() {
      try {
        if (!this.queryParams2.behaviorId) {
          console.log('无behaviorId，跳过获取新数据');
          return;
        }

        console.log('正在获取新增数据...');
        const response = await getUpToDataDegreeSuspicion(this.queryParams2);
        console.log('新增数据获取完成:', response);

        if (response && response.length > 0) {
          // 使用Vue.set确保响应式
          this.$set(this, 'allData', [...this.allData, ...response]);
          this.queryParams2.behaviorId = response[response.length - 1].behaviorId;

          // 数据更新完成后再处理
          this.processData();
        }
      } catch (error) {
        console.error("获取新增数据失败:", error);
      }
    },

    // 处理数据并计算统计结果
    processData() {
      const selectedDate = new Date(this.selectedTime).getTime();
      let alarmCount = 0;
      let normalCount = 0;
      let totalInRange = 0;

      this.allData.forEach(item => {
        if (item && item.createTime) {
          const itemTime = new Date(item.createTime).getTime();

          if (itemTime >= selectedDate) {
            totalInRange++;

            const suspicion = Number(item.degreeSuspicion) || 0;
            if (suspicion > 60) {
              alarmCount++;
            } else {
              normalCount++;
            }
          }
        }
      });

      this.vehicleBehaviorList = [{
        totalNum: totalInRange,
        onlineNum: normalCount,
        alarmNum: alarmCount
      }];

      this.updateCharts();
    },

    // 更新所有图表
    updateCharts() {
      const data = this.vehicleBehaviorList[0] || {
        totalNum: 0,
        onlineNum: 0,
        alarmNum: 0
      };

      this.config = {
        ...this.config,
        number: [data.totalNum]
      };

      this.onlineconfig = {
        ...this.onlineconfig,
        number: [data.onlineNum]
      };

      this.laramnumconfig = {
        ...this.laramnumconfig,
        number: [data.alarmNum]
      };

      const alarmRatio = data.totalNum > 0
        ? Math.round((data.alarmNum / data.totalNum) * 100)
        : 0;

      this.offlineconfig = {
        ...this.offlineconfig,
        number: [alarmRatio]
      };
    },

    // 设置默认数据
    setDefaultData() {
      this.allData = [];
      this.vehicleBehaviorList = [{
        totalNum: 0,
        onlineNum: 0,
        alarmNum: 0
      }];
      this.updateCharts();
    },

    // 时间范围切换
    setTimeRange(range) {
      this.selectTimeType = range;
      // 切换时间时重新初始化数据（会自动清除旧定时器）
      this.initData();
    },

    // 计算时间范围
    calculateTime(range) {
      const now = new Date();
      let startTime = new Date();

      switch (range) {
        case 1: // 最近一天
          startTime.setDate(now.getDate() - 1);
          break;
        case 2: // 最近一月
          startTime.setMonth(now.getMonth() - 1);
          break;
        case 3: // 最近一周
          startTime.setDate(now.getDate() - 7);
          break;
        default:
          startTime.setDate(now.getDate() - 1);
      }

      startTime.setHours(0, 0, 0, 0);
      this.selectedTime = this.formatDate(startTime);
    },

    // 格式化日期
    formatDate(date) {
      if (!(date instanceof Date)) {
        date = new Date(date);
      }

      const pad = n => n.toString().padStart(2, '0');
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    }
  }
};
</script>

<style lang='scss' scoped>
/* 保持原有样式不变 */
.user_Overview {
  li {
    flex: 1;

    p {
      text-align: center;
      height: 16px;
      font-size: 16px;
    }

    .user_Overview_nums {
      width: 100px;
      height: 100px;
      text-align: center;
      line-height: 100px;
      font-size: 22px;
      margin: 50px auto 30px;
      background-size: cover;
      background-position: center center;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
      }

      &.bgdonghua::before {
        animation: rotating 14s linear infinite;
      }
    }

    .allnum {
      &::before {
        background-image: url("~@/assets/scn-img/left_top_lan.png");
      }
    }

    .online {
      &::before {
        background-image: url("~@/assets/scn-img/left_top_lv.png");
      }
    }

    .offline {
      &::before {
        background-image: url("~@/assets/scn-img/left_top_huang.png");
      }
    }

    .laramnum {
      &::before {
        background-image: url("~@/assets/scn-img/left_top_hong.png");
      }
    }
  }
}

.channel-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 10px;
  justify-content: center;
}

.channel-item {
  padding: 8px 15px;
  background: rgba(0, 114, 255, 0.2);
  border: 1px solid rgba(0, 114, 255, 0.5);
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    background: rgba(0, 114, 255, 0.4);
    transform: scale(1.05);
  }

  &.active {
    background: rgba(0, 114, 255, 0.8);
    box-shadow: 0 0 10px rgba(0, 114, 255, 0.8);
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
