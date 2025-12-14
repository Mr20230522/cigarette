<template>
  <div class="suspect-chart-container">
    <div ref="chart" class="suspect-chart"></div>
    <div v-if="loading" class="loading-mask">数据加载中...</div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getDaySliceCache } from '@/api/cigarette/cache/bigScreenCache';

export default {
  data() {
    return {
      loading: false,
      chartData: [[], []], // [dates, suspectCounts]
    };
  },
  created() {
    this.fetchData();
  },
  mounted() {
    this.$nextTick(() => this.initChart());
  },
  methods: {
    /* 生成最近 → 最远 7 个自然日的日期字符串 */
    getSevenDays() {
      const arr = [];
      for (let i = 1; i <= 7; i++) {
        const d = new Date();
        d.setDate(d.getDate() - i);
        arr.push(d.toISOString().split('T')[0]);
      }
      return arr; // [昨天, 前天, ... 7 天前]
    },

    /* 拉缓存 → 拼装数据 → 画图表 */
    async fetchData() {
      this.loading = true;
      try {
        const obj = await getDaySliceCache();
        const dates = this.getSevenDays();
        const counts = [
          obj.suspectD1, obj.suspectD2, obj.suspectD3, obj.suspectD4,
          obj.suspectD5, obj.suspectD6, obj.suspectD7
        ].map(v => Number(v) || 0);
        this.chartData = [dates, counts];
        this.refreshChart();
      } catch (e) {
        console.error(e);
        this.mockData();
      } finally {
        this.loading = false;
      }
    },

    /* 兜底假数据 */
    mockData() {
      const dates = this.getSevenDays();
      const counts = dates.map(() => Math.floor(Math.random() * 20) + 5);
      this.chartData = [dates, counts];
      this.refreshChart();
    },

    /* 首次建图 */
    initChart() {
      const chartDom = this.$refs.chart;
      this.myChart = echarts.init(chartDom);
      this.buildOption();
      this.myChart.setOption(this.option);

      const resizeHandler = () => this.myChart.resize();
      window.addEventListener('resize', resizeHandler);
      this.$once('hook:beforeDestroy', () => {
        window.removeEventListener('resize', resizeHandler);
        this.myChart.dispose();
      });
    },

    /* 只更新数据 */
    refreshChart() {
      if (!this.myChart) return;
      this.buildOption();
      this.myChart.setOption(this.option, { notMerge: false });
    },

    /* 拼装 option */
    buildOption() {
      const formatDateLabel = str => {
        const [_, m, d] = str.split('-');
        return `${m}-${d}`;
      };
      this.option = {
        tooltip: {
          trigger: 'axis',
          formatter: p => {
            const date = p[0].axisValue;
            const count = p[0].data;
            return `${date}<br/>嫌疑车辆: ${count}辆`;
          }
        },
        grid: {
          left: '5%', right: '5%', bottom: '10%', top: '25%', containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: true,
          data: this.chartData[0].map(formatDateLabel),
          axisLine: { lineStyle: { color: '#999' } }
        },
        yAxis: {
          type: 'value',
          name: '车辆数量',
          axisLine: { show: true },
          splitLine: { lineStyle: { type: 'dashed' } }
        },
        series: [{
          name: '嫌疑车辆',
          type: 'line',
          data: this.chartData[1],
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: { color: '#ff4d4f' },
          lineStyle: { width: 3, color: '#ff4d4f' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(255, 77, 79, 0.5)' },
              { offset: 1, color: 'rgba(255, 77, 79, 0.1)' }
            ])
          },
          markPoint: {
            data: [{ type: 'max', name: '最大值' }, { type: 'min', name: '最小值' }]
          }
        }]
      };
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
