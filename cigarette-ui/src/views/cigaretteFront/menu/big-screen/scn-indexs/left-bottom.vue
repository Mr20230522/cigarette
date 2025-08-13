<template>
  <div class="suspect-chart-container">
    <div ref="chart" class="suspect-chart"></div>
    <div v-if="loading" class="loading-mask">数据加载中...</div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { byDateGetSuspicionVehicleBehavior } from '@/api/cigarette/vehicle/vehicleBehavior';

export default {
  data() {
    return {
      loading: false,
      chartData: [[], []], // [dates, counts]
    };
  },
  mounted() {
    this.fetchData().then(() => {
      this.initChart();
    });
  },
  methods: {
    async fetchData() {
      this.loading = true;

      try {
        // 获取今天的日期
        const today = new Date();
        today.setHours(0, 0, 0, 0); // 设置时间为 00:00:00

        // 生成前七天的日期数据（不包括今天）
        const getPreviousSevenDays = (today) => {
          const dates = [];
          for (let i = 1; i <= 7; i++) {
            const newDate = new Date(today);
            newDate.setDate(newDate.getDate() - i);
            const year = newDate.getFullYear();
            const month = String(newDate.getMonth() + 1).padStart(2, '0');
            const day = String(newDate.getDate()).padStart(2, '0');
            dates.push(`${year}-${month}-${day} 00:00:00`);
          }
          return dates;
        };

        const previousSevenDays = getPreviousSevenDays(today);

        // 调用API方法
        const response = await byDateGetSuspicionVehicleBehavior({
          begin: previousSevenDays[6], // 最早的日期（6号 00:00:00）
          end: previousSevenDays[0].split(' ')[0] + ' 23:59:59', // 最近的日期（12号 23:59:59）
          degreeSuspicion: 60 // 可根据需要调整嫌疑度阈值
        });

        // 处理返回的数据
        this.chartData = response;
      } catch (error) {
        console.error('获取数据失败:', error);
        // 模拟数据作为后备
        this.generateMockData();
      } finally {
        this.loading = false;
      }
    },

    // 后备模拟数据生成
    generateMockData() {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // 设置时间为 00:00:00

      const dates = [];
      const counts = [];

      for (let i = 1; i <= 7; i++) {
        const newDate = new Date(today);
        newDate.setDate(newDate.getDate() - i);
        const year = newDate.getFullYear();
        const month = String(newDate.getMonth() + 1).padStart(2, '0');
        const day = String(newDate.getDate()).padStart(2, '0');
        dates.push(`${year}-${month}-${day}`);

        const dayOfWeek = newDate.getDay();
        let baseCount = 10;
        if (dayOfWeek === 0 || dayOfWeek === 6) {
          baseCount = 15;
        }
        counts.push(Math.round(baseCount + Math.random() * 8));
      }

      this.chartData = [dates, counts];
    },

    initChart() {
      const chartDom = this.$refs.chart;
      const myChart = echarts.init(chartDom);

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>嫌疑车辆: {c}辆'
        },
        grid: {
          left: '5%',  // 增加左边距，给第一个数据点留出空间
          right: '5%',
          bottom: '10%',
          top: '25%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: true,  // 改为true，使第一个数据点不与y轴重叠
          data: this.chartData[0], // 日期数组
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '车辆数量',
          axisLine: {
            show: true
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        series: [{
          name: '嫌疑车辆',
          type: 'line',
          data: this.chartData[1], // 数量数组
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: {
            color: '#ff4d4f'
          },
          lineStyle: {
            width: 3,
            color: '#ff4d4f'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {offset: 0, color: 'rgba(255, 77, 79, 0.5)'},
              {offset: 1, color: 'rgba(255, 77, 79, 0.1)'}
            ])
          },
          markPoint: {
            data: [
              {type: 'max', name: '最大值'},
              {type: 'min', name: '最小值'}
            ]
          }
        }]
      };

      myChart.setOption(option);

      // 响应式调整
      const resizeHandler = () => myChart.resize();
      window.addEventListener('resize', resizeHandler);
      this.$once('hook:beforeDestroy', () => {
        window.removeEventListener('resize', resizeHandler);
        myChart.dispose();
      });
    }
  }
};
</script>

<style scoped>
.suspect-chart-container {
  width: 100%;
  height: 280px;
  position: relative;
}

.suspect-chart {
  width: 100%;
  height: 100%;
}

.loading-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #666;
}
</style>
