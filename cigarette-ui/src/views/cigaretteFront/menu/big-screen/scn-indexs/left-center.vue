<template>
  <div class="container">
    <div class="left-content">
      <div
        v-for="type in earlyWarningTypes"
        :key="type.value"
        class="channel-item"
        :class="{ active: activeType === type.value }"
        @click="selectType(type.value)"
      >
        {{ type.name }}
      </div>
    </div>
    <div class="right-content">
      <div class="chart-container">
        <Echart
          id="leftCenter"
          :options="options"
          class="left_center_inner"
          v-if="pageflag"
          ref="charts"
        />
        <Reacquire v-else @onclick="getData" style="line-height:200px">
          重新获取
        </Reacquire>
      </div>
    </div>
  </div>
</template>

<script>
import Reacquire from "@/components/scn-reacquire/reacquire.vue";
import Echart from "@/components/scn-echart/index.vue";
import {
  getUpToDataDegreeSuspicionAll
} from '@/api/cigarette/vehicle/vehicleBehavior';
import { listDetection } from '@/api/cigarette/detection/detection';

import {tenList, overIdList, allList, byIdGetVideoPath} from "@/api/cigarette/trafficData/trafficData"


// 预定义一组丰富的颜色（共24种）
const COLOR_PALETTE = [
  '#FF6B6B', '#FFA07A', '#FFD700', '#98FB98', '#87CEFA', '#9370DB',
  '#FF6347', '#40E0D0', '#FF69B4', '#7B68EE', '#00FA9A', '#1E90FF',
  '#BA55D3', '#FF4500', '#00CED1', '#FF8C00', '#9932CC', '#8FBC8F',
  '#E9967A', '#8A2BE2', '#00BFFF', '#FF00FF', '#7CFC00', '#FF1493'
];

export default {
  components: {
    Reacquire,
    Echart
  },
  data() {
    return {
      options: {},
      earlyWarningTypes: [
        { name: '季节', value: 'quarter' },
        { name: '昼夜', value: 'dayAndNight' },
        { name: '小时', value: 'hours' },
        { name: '车辆类型', value: 'vehicleType' },
        { name: '车辆颜色', value: 'vehicleColor' },
        { name: '站点出现频率', value: 'detection' },
      ],
      activeType: 'quarter',
      warningData: {
        quarter: { total: 0, categories: [] },
        dayAndNight: { total: 0, categories: [] },
        hours: { total: 0, categories: [] },
        vehicleType: { total: 0, categories: [] },
        vehicleColor: { total: 0, categories: [] },
        detection: { total: 0, categories: [] },
      },
      rawData: [],
      pageflag: true,
      timer: null,
      queryParams: {
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: 60,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },
      queryParams2: {
        level: null,
        Id: null
      }
    };
  },
  created() {
    this.getData();
  },
  beforeDestroy() {
    this.clearRefreshTimer();
  },
  methods: {
    // 获取颜色 - 根据索引从调色板中循环获取颜色
    getColor(index) {
      return COLOR_PALETTE[index % COLOR_PALETTE.length];
    },

    clearRefreshTimer() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    startRefreshTimer() {
      this.clearRefreshTimer();
      this.timer = setInterval(() => {
        if (this.queryParams2.Id) {
          overIdList(this.queryParams2).then(response => {
            console.log('11111response',response)
            if (response && response.length) {
              response.reverse()
              this.queryParams2.Id = response[response.length - 1].Id;
              this.rawData = [...this.rawData, ...response];
              this.processIncrementalData(response);
              this.initChart();
            }
          });
        }
      }, 2000);
    },

    getData() {
      this.pageflag = true;
      overIdList(this.queryParams2).then(response => {
        if (response && response.length) {
          response.reverse()
          this.queryParams2.Id = response[response.length - 1].Id;
          this.rawData = response;
          this.processFullData();
          this.initChart();
          this.startRefreshTimer();
        } else {
          this.pageflag = false;
        }
      }).catch(() => {
        this.pageflag = false;
      });
    },

    selectType(type) {
      if (this.activeType === type) return;

      this.clearRefreshTimer();
      this.activeType = type;
      this.processFullData();
      this.initChart();
      this.startRefreshTimer();
    },

    processFullData() {
      switch (this.activeType) {
        case 'quarter':
          this.warningData.quarter = this.calculateQuarterData(this.rawData);
          break;
        case 'dayAndNight':
          this.warningData.dayAndNight = this.calculateDayNightData(this.rawData);
          break;
        case 'hours':
          this.warningData.hours = this.calculateHoursData(this.rawData);
          break;
        case 'vehicleType':
          this.warningData.vehicleType = this.calculateVehicleTypeData(this.rawData);
          break;
        case 'vehicleColor':
          this.warningData.vehicleColor = this.calculateVehicleColorData(this.rawData);
          break;
        case 'detection':
          this.calculateDetectionData(this.rawData);
          break;
      }
    },

    processIncrementalData(newData) {
      switch (this.activeType) {
        case 'quarter':
          this.mergeQuarterData(newData);
          break;
        case 'dayAndNight':
          this.mergeDayNightData(newData);
          break;
        case 'hours':
          this.mergeHoursData(newData);
          break;
        case 'vehicleType':
          this.mergeVehicleTypeData(newData);
          break;
        case 'vehicleColor':
          this.mergeVehicleColorData(newData);
          break;
        case 'detection':
          this.mergeDetectionData(newData);
          break;
      }
    },

    calculateQuarterData(data) {
      let spring = 0, summer = 0, autumn = 0, winter = 0;
      data.forEach(item => {
        const month = new Date(item.captureTime).getMonth();
        if (month >= 0 && month <= 2) spring++;
        else if (month >= 3 && month <= 5) summer++;
        else if (month >= 6 && month <= 8) autumn++;
        else winter++;
      });
      return {
        total: spring + summer + autumn + winter,
        categories: [
          {name: '春季', value: spring, color: this.getColor(0)},
          {name: '夏季', value: summer, color: this.getColor(1)},
          {name: '秋季', value: autumn, color: this.getColor(2)},
          {name: '冬季', value: winter, color: this.getColor(3)}
        ]
      };
    },

    calculateDayNightData(data) {
      let day = 0, night = 0;
      data.forEach(item => {
        const hour = new Date(item.captureTime).getHours();
        hour < 18 ? day++ : night++;
      });
      return {
        total: day + night,
        categories: [
          {name: '白天', value: day, color: this.getColor(4)},
          {name: '夜间', value: night, color: this.getColor(5)}
        ]
      };
    },

    calculateHoursData(data) {
      const hours = Array(24).fill(0);
      data.forEach(item => {
        const hour = new Date(item.captureTime).getHours();
        hours[hour]++;
      });
      return {
        total: data.length,
        categories: hours.map((value, hour) => ({
          name: `${hour}:00`,
          value: value,
          color: this.getColor(hour)
        })).filter(v => v.value > 0)
      };
    },

    calculateVehicleTypeData(data) {
      const typeCounts = data.reduce((acc, {vehicleType}) => {
        acc[vehicleType] = (acc[vehicleType] || 0) + 1;
        return acc;
      }, {});
      return {
        total: data.length,
        categories: Object.entries(typeCounts).map(([key, val], i) => ({
          name: key, value: val, color: this.getColor(i)
        }))
      };
    },

    calculateVehicleColorData(data) {
      const colorCounts = data.reduce((acc, {vehicleColor}) => {
        if (vehicleColor) acc[vehicleColor] = (acc[vehicleColor] || 0) + 1;
        return acc;
      }, {});
      return {
        total: data.length,
        categories: Object.entries(colorCounts).map(([key, val], i) => ({
          name: key, value: val, color: this.getColor(i)
        }))
      };
    },

    calculateDetectionData(data) {
      // 1. 统计每个cameraName出现的次数
      const detectionStats = {};
      let total = 0;

      data.forEach(item => {
        if (item.cameraName) {
          detectionStats[item.cameraName] = (detectionStats[item.cameraName] || 0) + 1;
          total++;
        }
      });

      // 2. 转换为图表需要的格式
      const categories = Object.entries(detectionStats)
        .map(([name, count], index) => ({
          name: name,
          value: count,
          color: this.getColor(index)
        }))
        .sort((a, b) => b.value - a.value); // 按数量降序排序

      // 3. 更新数据并渲染图表
      this.warningData.detection = {
        total: total,
        categories: categories
      };

      this.initChart();
    },

    mergeQuarterData(newData) {
      const newStats = this.calculateQuarterData(newData);
      const current = this.warningData.quarter;
      current.total += newStats.total;
      newStats.categories.forEach(newItem => {
        const existing = current.categories.find(item => item.name === newItem.name);
        if (existing) {
          existing.value += newItem.value;
        } else {
          current.categories.push(newItem);
        }
      });
    },

    mergeDayNightData(newData) {
      const newStats = this.calculateDayNightData(newData);
      const current = this.warningData.dayAndNight;
      current.total += newStats.total;
      current.categories[0].value += newStats.categories[0].value;
      current.categories[1].value += newStats.categories[1].value;
    },

    mergeHoursData(newData) {
      const newStats = this.calculateHoursData(newData);
      const current = this.warningData.hours;
      current.total += newStats.total;
      newStats.categories.forEach(newItem => {
        const existing = current.categories.find(item => item.name === newItem.name);
        if (existing) {
          existing.value += newItem.value;
        } else {
          current.categories.push(newItem);
        }
      });
    },

    mergeVehicleTypeData(newData) {
      const newStats = this.calculateVehicleTypeData(newData);
      const current = this.warningData.vehicleType;
      current.total += newStats.total;
      newStats.categories.forEach(newItem => {
        const existing = current.categories.find(item => item.name === newItem.name);
        if (existing) {
          existing.value += newItem.value;
        } else {
          current.categories.push(newItem);
        }
      });
    },

    mergeVehicleColorData(newData) {
      const newStats = this.calculateVehicleColorData(newData);
      const current = this.warningData.vehicleColor;
      current.total += newStats.total;
      newStats.categories.forEach(newItem => {
        const existing = current.categories.find(item => item.name === newItem.name);
        if (existing) {
          existing.value += newItem.value;
        } else {
          current.categories.push(newItem);
        }
      });
    },

    mergeDetectionData(newData) {
      const newStats = this.calculateDetectionData(newData);
      const current = this.warningData.detection;
      current.total += newStats.total;
      newStats.categories.forEach(newItem => {
        const existing = current.categories.find(item => item.name === newItem.name);
        if (existing) {
          existing.value += newItem.value;
        } else {
          current.categories.push(newItem);
        }
      });
    },

    initChart() {
      const currentData = this.warningData[this.activeType];
      const colors = currentData.categories.map(item => item.color);

      this.options = {
        title: {
          text: ["{value|" + currentData.total + "}", "{name|总数}"].join("\n"),
          top: "center",
          left: "center",
          textStyle: {
            rich: {
              value: {color: "#fff", fontSize: 24, fontWeight: "bold", lineHeight: 20},
              name: {color: "#fff", lineHeight: 20},
            },
          },
        },
        tooltip: {
          trigger: "item",
          formatter: params => {
            const percent = ((params.value / currentData.total) * 100).toFixed(1);
            return `${params.name}<br/>数量: ${params.value}<br/>占比: ${percent}%`;
          }
        },
        series: [{
          type: "pie",
          radius: ["42%", "65%"],
          data: currentData.categories.map(item => ({
            value: item.value,
            name: item.name,
            itemStyle: {color: item.color}
          })),
          label: {show: false},
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  height: 100%;
  width: 100%;
  background: rgba(13, 42, 67, 0.5);
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 114, 255, 0.2);
}

.left-content {
  width: 25%;
  padding: 15px 10px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 80%;
  overflow-y: auto;
  border-right: 1px solid rgba(0, 114, 255, 0.3);
}

.right-content {
  width: 75%;
  padding: 10px;
}

.chart-container {
  width: 100%;
  height: 100%;
}

.left_center_inner {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

.channel-item {
  text-align: center;
  padding: 10px 0;
  background: rgba(0, 114, 255, 0.1);
  border: 1px solid rgba(0, 114, 255, 0.3);
  border-radius: 6px;
  color: #e0e0e0;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;

  &:hover {
    background: rgba(0, 114, 255, 0.3);
    transform: translateX(5px);
    box-shadow: 0 0 10px rgba(0, 114, 255, 0.3);
  }

  &.active {
    background: rgba(0, 114, 255, 0.5);
    border-color: rgba(0, 114, 255, 0.8);
    box-shadow: 0 0 15px rgba(0, 114, 255, 0.5);
    color: #fff;
    font-weight: bold;
  }
}

.left-content::-webkit-scrollbar {
  width: 4px;
}

.left-content::-webkit-scrollbar-thumb {
  background: rgba(0, 114, 255, 0.5);
  border-radius: 4px;
}

.left-content::-webkit-scrollbar-track {
  background: transparent;
}
</style>
