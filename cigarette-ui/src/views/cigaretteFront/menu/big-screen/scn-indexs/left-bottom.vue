<template>
  <div class="suspect-chart-container">
    <div ref="chart" class="suspect-chart"></div>
    <div v-if="loading" class="loading-mask">数据加载中...</div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { overIdList } from "@/api/cigarette/trafficData/trafficData"
import { getSuspicionLevel } from "@/api/cigarette/caution/suspicion";


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
    // 获取前7天的日期（不包括今天），正确处理跨年情况
    getPreviousSevenDays() {
      const dates = [];
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      for (let i = 1; i <= 7; i++) {
        const date = new Date(today);
        date.setDate(date.getDate() - i);
        dates.push(date);
      }

      // 按日期从早到晚排序（解决跨年问题）
      dates.sort((a, b) => a - b);

      // 格式化为 YYYY-MM-DD
      return dates.map(date => date.toISOString().split('T')[0]);
    },

    // 处理API返回的数据为图表需要的格式
    processChartData(apiData, dateRange) {
      // 创建一个按日期索引的对象
      const dateCountMap = {};

      // 初始化所有日期为0
      dateRange.forEach(date => {
        dateCountMap[date] = 0;
      });

      // 填充实际数据
      apiData.forEach(item => {
        if (item.captureTime) {
          const date = item.captureTime.split(' ')[0]; // 提取日期部分
          if (dateCountMap.hasOwnProperty(date)) {
            dateCountMap[date]++;
          }
        }
      });

      // 转换为chartData格式，保持原始日期顺序
      this.chartData = [
        dateRange, // 已经排好序的日期数组
        dateRange.map(date => dateCountMap[date]) // 对应的数量数组
      ];

      console.log('处理后的图表数据:', this.chartData);
    },

    async fetchData() {
      this.loading = true;
      try {
        const previousSevenDays = this.getPreviousSevenDays();
        const currentLevel = await getSuspicionLevel();


        // 调用API获取数据
        const response = await overIdList({
          endTime: previousSevenDays[6] + ' 23:59:59',
          level:currentLevel.data
        });

        console.log('API响应数据1111:', response);
        this.processChartData(response, previousSevenDays);
      } catch (error) {
        console.error('获取数据失败:', error);
        this.generateMockData();
      } finally {
        this.loading = false;
      }
    },

    // 模拟数据生成（备用）
    generateMockData() {
      const dates = this.getPreviousSevenDays();
      const counts = dates.map(() => Math.floor(Math.random() * 20) + 5);
      this.chartData = [dates, counts];
    },

    initChart() {
      const chartDom = this.$refs.chart;
      const myChart = echarts.init(chartDom);

      // 格式化日期显示（月-日）
      const formatDateLabel = (dateStr) => {
        const [year, month, day] = dateStr.split('-');
        return `${month}-${day}`;
      };

      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: params => {
            const date = params[0].axisValue;
            const count = params[0].data;
            return `${date}<br/>嫌疑车辆: ${count}辆`;
          }
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '10%',
          top: '25%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: true,
          data: this.chartData[0].map(formatDateLabel), // 显示为月-日格式
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
          data: this.chartData[1],
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
/* 样式保持不变 */
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
