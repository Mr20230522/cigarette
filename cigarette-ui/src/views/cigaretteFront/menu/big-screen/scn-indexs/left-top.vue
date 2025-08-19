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
import { overIdList } from "@/api/cigarette/trafficData/trafficData"
import { getSuspicionLevel } from "@/api/cigarette/caution/suspicion";

export default {
  data() {
    return {
      level:null,
      selectTimeType: 1, // 默认选择最近一天
      selectedTime: '',
      timeOptions: [
        {label: '最近一天', value: 1},
        {label: '最近一月', value: 2},
        {label: '最近一星期', value: 3}
      ],
      allData: [],
      vehicleBehaviorList: [],
      queryParams: {
        id: null,
        captureTime: null
      },
      config: {
        number: [0],
        content: '{nt}',
        style: {fontSize: 24, fill: "#00fdfa"}
      },
      onlineconfig: {
        number: [0],
        content: '{nt}',
        style: {fontSize: 24, fill: "#07f7a8"}
      },
      offlineconfig: {
        number: [0],
        content: '{nt}%',
        style: {fontSize: 24, fill: "#e3b337"}
      },
      laramnumconfig: {
        number: [0],
        content: '{nt}',
        style: {fontSize: 24, fill: "#f5023d"}
      },
      refreshTimer: null,
      refreshInterval: 2000,
    }
  },
  created() {
    this.initData();
  },
  beforeDestroy() {
    clearInterval(this.refreshTimer);
  },
  methods: {
    initData() {
      this.calculateTime(this.selectTimeType);
      clearInterval(this.refreshTimer);
      this.getInitialData();
    },

    async getInitialData() {
      try {
        // 首先获取嫌疑程度标准线
        const currentLevel = await getSuspicionLevel();
        this.level = currentLevel.data;
        this.queryParams.captureTime = this.selectedTime;

        const response = await overIdList(this.queryParams);

        if (response && response.length > 0) {
          this.allData = response;
          this.queryParams.id = response[response.length - 1].id;
          this.processData();
          this.startAutoRefresh();
        } else {
          this.setDefaultData();
          this.startAutoRefresh();
        }
      } catch (error) {
        console.error("获取数据失败:", error);
        this.setDefaultData();
      }
    },


    startAutoRefresh() {
      clearInterval(this.refreshTimer);
      this.refreshTimer = setInterval(() => {
        console.log('自动刷新数据...');
        this.getNewData();
      }, this.refreshInterval);
    },

    async getNewData() {
      try {
        if (!this.queryParams.id) {
          console.warn('缺少必要参数，终止轮询');
          return;
        }

        this.queryParams.captureTime=this.selectedTime // 必须带上相同时间范围


        const response = await overIdList(this.queryParams);

        if (response && response.length > 0) {
          console.log('!@!@response',response)
          this.queryParams.id = response[response.length - 1].id;
          // 确保不超过数据限制
          this.allData = [...this.allData, ...response];
          this.processData();
        } else {
          console.log('没有获取到新数据');
        }
      } catch (error) {
        console.error("获取新增数据失败:", error);
      }
    },

    processData() {
      let alarmCount = 0;
      let normalCount = 0;

      this.allData.forEach(item => {
        const suspicion = Number(item.level) || 0;
        // 使用从后端获取的level作为判断标准
        if (suspicion >= this.level) {  // 修改为大于等于阈值
          alarmCount++;
        } else {
          normalCount++;
        }
      });

      this.vehicleBehaviorList = [{
        totalNum: this.allData.length,
        onlineNum: normalCount,
        alarmNum: alarmCount
      }];

      this.updateCharts();
    },

    updateCharts() {
      const data = this.vehicleBehaviorList[0] || {
        totalNum: 0,
        onlineNum: 0,
        alarmNum: 0
      };

      // 使用Vue.set或创建新对象确保响应式更新
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

    setDefaultData() {
      this.allData = [];
      this.vehicleBehaviorList = [{
        totalNum: 0,
        onlineNum: 0,
        alarmNum: 0
      }];
      this.updateCharts();
    },

    setTimeRange(range) {
      if (this.selectTimeType === range) return;

      this.selectTimeType = range;
      this.initData();
    },

    calculateTime(range) {
      const now = new Date();
      let startTime = new Date();

      switch (range) {
        case 1:
          startTime.setDate(now.getDate() - 1);
          break;
        case 2:
          startTime.setMonth(now.getMonth() - 1);
          break;
        case 3:
          startTime.setDate(now.getDate() - 7);
          break;
        default:
          startTime.setDate(now.getDate() - 1);
      }

      startTime.setHours(0, 0, 0, 0);
      this.selectedTime = this.formatDate(startTime);
      this.queryParams.id = null; // 重置ID
      console.log('计算后的时间范围:', this.selectedTime);
    },

    formatDate(date) {
      if (!(date instanceof Date)) {
        date = new Date(date);
      }
      const pad = n => n.toString().padStart(2, '0');
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}.00`;
    },
  }
};
</script>

<style lang='scss' scoped>
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

    .allnum::before {
      background-image: url("~@/assets/scn-img/left_top_lan.png");
    }

    .online::before {
      background-image: url("~@/assets/scn-img/left_top_lv.png");
    }

    .offline::before {
      background-image: url("~@/assets/scn-img/left_top_huang.png");
    }

    .laramnum::before {
      background-image: url("~@/assets/scn-img/left_top_hong.png");
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
