<template>
  <div ref="chartContainer" style="width: 100%; height: 200px;"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'RadarChart2',
  data() {
    return {
      chart: null
    };
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      const chartDom = this.$refs.chartContainer;
      this.chart = echarts.init(chartDom);
      const option = {
        title: [
          {
            text: ''
          }
        ],
        polar: {
          radius: [30, '80%']
        },
        angleAxis: {
          max: 4,
          startAngle: 75
        },
        radiusAxis: {
          type: 'category',
          data: ['a', 'b', 'c', 'd']
        },
        tooltip: {},
        series: {
          type: 'bar',
          data: [2, 1.2, 2.4, 3.6],
          coordinateSystem: 'polar',
          label: {
            show: true,
            position: 'middle',
            formatter: '{b}: {c}'
          }
        }
      };
      this.chart.setOption(option);
    }
  }
};
</script>

<style>
/* 可以在这里添加一些样式 */
</style>