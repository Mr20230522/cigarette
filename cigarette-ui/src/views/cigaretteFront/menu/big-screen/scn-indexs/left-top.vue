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
          <dv-digital-flop v-if="vehicleBehaviorList.length > 0" :config="config" style="width:100%;height:100%;" />
        </div>
        <p>车辆总数</p>
      </li>
      <li class="user_Overview-item" style="color: #07f7a8">
        <div class="user_Overview_nums online">
          <dv-digital-flop v-if="vehicleBehaviorList.length > 0" :config="onlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>正常车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #f5023d">
        <div class="user_Overview_nums laramnum">
          <dv-digital-flop v-if="vehicleBehaviorList.length > 0" :config="laramnumconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #e3b337">
        <div class="user_Overview_nums offline">
          <dv-digital-flop v-if="vehicleBehaviorList.length > 0" :config="offlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆占比</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { listVehicleBehaviorVo } from "@/api/cigarette/vehicle/vehicleBehavior";

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
      vehicleBehaviorList: [],
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
      }
    }
  },
  created() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      this.calculateTime(this.selectTimeType);
      this.getList();
    },

    // 获取数据
    getList() {
      // 添加时间参数到查询条件
      const params = {
        ...this.queryParams,
        startTime: this.selectedTime,
        endTime: this.formatDate(new Date())
      };

      listVehicleBehaviorVo(params).then(response => {
        if (response.code === 200 && response.rows) {
          this.processData(response.rows);
        } else {
          console.error("数据格式异常:", response);
          this.setDefaultData();
        }
      }).catch(error => {
        console.error("获取数据失败:", error);
        this.setDefaultData();
      });
    },

    // 处理返回的数据
    // 处理返回的数据
    processData(data) {
      let alarmCount = 0;
      let normalCount = 0;
      let totalInRange = 0; // 新增：统计时间范围内的总数

      // 将selectedTime转为Date对象（精确到毫秒）
      const selectedDate = new Date(this.selectedTime).getTime();

      data.forEach(item => {
        // 确保createTime存在
        if (item.createTime) {
          // 将createTime转为时间戳
          const itemTime = new Date(item.createTime).getTime();

          // 筛选条件：createTime >= selectedTime
          if (itemTime >= selectedDate) {
            totalInRange++; // 只在时间范围内计数

            // 统计嫌疑程度
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
        totalNum: totalInRange, // 改为使用时间范围内的总数
        onlineNum: normalCount,
        alarmNum: alarmCount
      }];

      this.updateCharts();
    },

    // 更新所有图表
    updateCharts() {
      if (this.vehicleBehaviorList.length === 0) {
        this.setDefaultData();
        return;
      }

      const data = this.vehicleBehaviorList[0];
      this.$set(this.config, 'number', [data.totalNum || 0]);
      this.$set(this.onlineconfig, 'number', [data.onlineNum || 0]);
      this.$set(this.laramnumconfig, 'number', [data.alarmNum || 0]);

      // 计算嫌疑车辆占比
      const alarmRatio = data.totalNum > 0
        ? Math.round((data.alarmNum / data.totalNum) * 100)
        : 0;
      this.$set(this.offlineconfig, 'number', [alarmRatio]);
    },

    // 设置默认数据
    setDefaultData() {
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
      this.calculateTime(range);

      this.vehicleBehaviorList = [];
      this.$nextTick(() => {
        this.getList();
      });    },

    // 计算时间范围
    calculateTime(range) {
      const now = new Date();
      let startTime = new Date();

      switch (range) {
        case 1: // 最近一天
          startTime = new Date(now.getTime() - 24 * 60 * 60 * 1000);
          break;
        case 2: // 最近一月
          startTime = new Date();
          startTime.setMonth(now.getMonth() - 1);
          break;
        case 3: // 最近一周
          startTime = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
          break;
      }
      this.selectedTime = this.formatDate(startTime);
    },

    // 格式化日期
    formatDate(date) {
      const pad = n => n.toString().padStart(2, '0');
      const d = new Date(date);
      return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    }
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
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
