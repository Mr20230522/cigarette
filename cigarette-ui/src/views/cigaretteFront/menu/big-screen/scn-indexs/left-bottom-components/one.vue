<template>
  <div class="chart-container" ref="chartDom"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'one',
  data() {
    return {
      chartInstance: null,
      chartData: [120, 200, 150, 80, 70, 110, 130] // 示例数据
    }
  },
  mounted() {
    this.initChart();
    // 窗口变化时自适应
    window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeChart);
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  },
  methods: {
    initChart() {
      this.chartInstance = echarts.init(this.$refs.chartDom);
      this.updateChart();
    },
    updateChart() {
      const option = {
        title: {
          text: '我是One图表',
          left: 'center'
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.chartData,
          type: 'bar',
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)'
          }
        }]
      };
      this.chartInstance.setOption(option);
    },
    resizeChart() {
      this.chartInstance && this.chartInstance.resize();
    }
  }
}
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 300px; /* 必须给高度 */
}
</style>

