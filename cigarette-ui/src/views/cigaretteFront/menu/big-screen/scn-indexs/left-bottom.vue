<template>
  <div class="suspect-chart-container">
    <div ref="chart" class="suspect-chart"></div>

    <!-- 悬停卡片 -->
    <div
      v-if="hoverData.show"
      class="hover-card"
      :style="{ left: hoverData.x + 'px', top: hoverData.y + 'px' }"
    >
      <div class="hd">日期：{{ hoverData.date }}</div>
      <div class="bd">嫌疑车辆：<span class="num">{{ hoverData.count }}</span></div>
    </div>

    <div v-if="loading" class="loading-mask">数据加载中...</div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDaySliceCache } from '@/api/cigarette/cache/bigScreenCache'

export default {
  data() {
    return {
      loading: false,
      chartData: [[], []], // [dates, counts]
      hoverData: { show: false, date: '', count: 0, x: 0, y: 0 },
      myChart: null
    }
  },
  created() {
    this.fetchData()
  },
  mounted() {
    this.$nextTick(() => this.initChart())
  },
  beforeDestroy() {
    if (this.myChart) this.myChart.dispose()
  },
  methods: {
    /* 7 天日期 [昨天..7天前] */
    getSevenDays() {
      const arr = []
      for (let i = 1; i <= 7; i++) {
        const d = new Date()
        d.setDate(d.getDate() - i)
        arr.push(d.toISOString().split('T')[0])
      }
      return arr
    },

    /* 拉数据 */
    async fetchData() {
      this.loading = true
      try {
        const obj   = await getDaySliceCache()
        const dates = this.getSevenDays()
        const counts = [
          obj.suspectD1, obj.suspectD2, obj.suspectD3, obj.suspectD4,
          obj.suspectD5, obj.suspectD6, obj.suspectD7
        ].map(v => Number(v) || 0)
        this.chartData = [dates, counts]
        this.refreshChart()
      } catch (e) {
        console.error(e)
        this.mockData()
      } finally {
        this.loading = false
      }
    },

    /* 兜底假数据 */
    mockData() {
      const dates = this.getSevenDays()
      const counts = dates.map(() => Math.floor(Math.random() * 20) + 5)
      this.chartData = [dates, counts]
      this.refreshChart()
    },

    /* 首次建图 */
    initChart() {
      const chartDom = this.$refs.chart
      this.myChart = echarts.init(chartDom)
      this.buildOption()
      this.myChart.setOption(this.option)

      const resizeHandler = () => this.myChart.resize()
      window.addEventListener('resize', resizeHandler)
      this.$once('hook:beforeDestroy', () => {
        window.removeEventListener('resize', resizeHandler)
        this.myChart.dispose()
      })
    },

    /* 更新数据不重建 */
    refreshChart() {
      if (!this.myChart) return
      this.buildOption()
      this.myChart.setOption(this.option, { notMerge: false })
    },

    /* 配置：红点无字 + 悬停卡片 */
    buildOption() {
      const fmtDate = str => {
        const [_, m, d] = str.split('-')
        return `${m}-${d}`
      }
      const data = this.chartData[0].map((date, i) => ({
        date,
        value: this.chartData[1][i]
      }))

      this.option = {
        tooltip: { show: false },
        grid: { left: '5%', right: '5%', bottom: '10%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.chartData[0].map(fmtDate),
          axisLine: { lineStyle: { color: '#999' } }
        },
        yAxis: {
          type: 'value',
          name: '车辆数量',
          axisLine: { show: true },
          splitLine: { lineStyle: { type: 'dashed' } }
        },
        series: [{
          type: 'line',
          data: data,
          symbol: 'circle',
          symbolSize: 10,
          itemStyle: { color: '#ff4d4f' },
          lineStyle: { width: 3, color: '#ff4d4f' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(255, 77, 79, 0.5)' },
              { offset: 1, color: 'rgba(255, 77, 79, 0.1)' }
            ])
          },
          label: { show: false },              // 关键点：无数字
          emphasis: { scale: 1.4, itemStyle: { shadowBlur: 12, color: '#ff4d4f' } }
        }]
      }

      /* 悬停事件 */
      this.$nextTick(() => {
        this.myChart.off('mouseover')
        this.myChart.off('mouseout')
        this.myChart.on('mouseover', params => {
          this.hoverData = {
            show: true,
            date: params.data.date,
            count: params.data.value,
            x: params.event.offsetX + 15,
            y: params.event.offsetY - 10
          }
        })
        this.myChart.on('mouseout', () => { this.hoverData.show = false })
      })
    }
  }
}
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

/* 悬停卡片 */
.hover-card {
  position: absolute;
  background: rgba(0, 0, 0, 0.75);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  padding: 8px 12px;
  color: #fff;
  font-size: 13px;
  pointer-events: none;
  z-index: 99;
  backdrop-filter: blur(4px);
  line-height: 1.4;
}
.hover-card .hd {
  margin-bottom: 4px;
  color: rgba(255, 255, 255, 0.8);
}
.hover-card .bd .num {
  color: #ff4d4f;
  font-weight: bold;
}
</style>
