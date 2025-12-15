<template>
  <div class="container">
    <!-- 左侧维度切换 -->
    <div class="left-content">
      <div
        v-for="t in earlyWarningTypes"
        :key="t.value"
        class="channel-item"
        :class="{ active: activeType === t.value }"
        @click="selectType(t.value)"
      >
        {{ t.name }}
      </div>
    </div>

    <!-- 右侧饼图 -->
    <div class="right-content">
      <div class="chart-container">
        <Echart
          id="leftCenter"
          ref="charts"
          class="left_center_inner"
          :options="options"
          v-if="pageflag"
        />
        <Reacquire v-else @onclick="loadDimension(activeType)">
          重新获取
        </Reacquire>
      </div>
    </div>
  </div>
</template>

<script>
import Reacquire from '@/components/scn-reacquire/reacquire.vue'
import Echart from '@/components/scn-echart/index.vue'
import {
  getCameraCache,
  getDaynightCache,
  getSeasonCache,
  getVehicleColorCache,
  getVehicleTypeCache,
  getHourCache
} from '@/api/cigarette/cache/bigScreenCache'

/* 颜色池 */
const COLOR_PALETTE = [
  '#FF6B6B','#FFA07A','#FFD700','#98FB98','#87CEFA','#9370DB',
  '#FF6347','#40E0D0','#FF69B4','#7B68EE','#00FA9A','#1E90FF',
  '#BA55D3','#FF4500','#00CED1','#FF8C00','#9932CC','#8FBC8F',
  '#E9967A','#8A2BE2','#00BFFF','#FF00FF','#7CFC00','#FF1493'
]

/* 维度 → 接口 */
const API_MAP = {
  quarter: getSeasonCache,
  dayAndNight: getDaynightCache,
  hours: getHourCache,
  vehicleType: getVehicleTypeCache,
  vehicleColor: getVehicleColorCache,
  detection: getCameraCache
}

/* 英文→中文 显示映射 */
const DISPLAY_NAME = {
  quarter: { spring: '春季', summer: '夏季', autumn: '秋季', winter: '冬季' },
  dayAndNight: { dayNum: '白天', nightNum: '夜间' },
  hours: {},                       // h00~h23 保持 hh:00 格式
  vehicleColor: {
    white: '白色', black: '黑色', gray: '灰色', red: '红色', blue: '蓝色',
    deepBlue: '深蓝', brown: '棕色', green: '绿色', yellow: '黄色',
    silver: '银色', otherC: '其它'
  },
  vehicleType: {
    sedan: '轿车', suvMpv: 'SUV/MPV', truck: '卡车', largeBus: '大客车',
    mediumBus: '中巴', lightTruck: '轻卡', pickup: '皮卡', van: '厢货',
    twowheeler: '两轮车', threewheeler: '三轮车', nonMotor: '非机动车',
    pedestrian: '行人', smallcar: '小型车', largeVehicle: '大型车',
    suvonly: 'SUV', other: '其它', unknown: '未知'
  },
  detection: {   // 站点英文→中文
    banqiao326Gas: '板桥镇326国道中国石化加油站旁卡口',
    banqiaoExit: '板桥收费站出口一体机',
    g248Xixiang: 'G248国道与西响线交叉口南向卡口',
    laoDifang: '老地方饭店附近卡口',
    luliangKaikou: '陆良县326国道经官中学附近卡口',
    luopingAgang: '罗平县阿岗收费站出口卡口',
    other: '其它'
  }
}

export default {
  components: { Reacquire, Echart },
  data() {
    return {
      options: {},
      pageflag: true,
      timer: null,
      activeType: 'quarter',
      earlyWarningTypes: [
        { name: '季节', value: 'quarter' },
        { name: '昼夜', value: 'dayAndNight' },
        { name: '小时', value: 'hours' },
        { name: '车辆类型', value: 'vehicleType' },
        { name: '车辆颜色', value: 'vehicleColor' },
        { name: '站点出现频率', value: 'detection' }
      ],
      rawObj: {}   // 当前维度裸对象
    }
  },
  created() {
    this.loadDimension(this.activeType)
    this.timer = setInterval(() => this.loadDimension(this.activeType), 5_000)
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    /* 拉接口 + 刷新图表 */
    async loadDimension(type) {
      try {
        this.pageflag = true
        this.rawObj = await API_MAP[type]()
        this.buildOption()
      } catch {
        this.pageflag = false
      }
    },

    /* 维度切换 */
    selectType(type) {
      if (this.activeType === type) return
      this.activeType = type
      this.loadDimension(type)   // 立即刷新
    },

    /* 生成 ECharts 配置 */
    /* 生成 ECharts 配置（总数字号随位数线性缩小） */
    buildOption() {
      const obj  = this.rawObj
      const map  = DISPLAY_NAME[this.activeType] || {}
      let list   = []

      if (this.activeType === 'hours') {
        // 小时：h00~h23
        for (let i = 0; i < 24; i++) {
          const key = 'h' + String(i).padStart(2, '0')
          list.push({ name: `${i}:00`, value: obj[key] || 0 })
        }
      } else {
        // 其余维度
        list = Object.entries(obj)
          .filter(([k]) => !['id', 'timeType', 'updateTime'].includes(k))
          .map(([k, v]) => ({
            name: map[k] || k,   // 中文映射
            value: v || 0
          }))
      }

      const total  = list.reduce((s, o) => s + o.value, 0)
      const digit  = String(total).length
      const titleFs = Math.max(12, 32 - digit * 2) // 每多1位减2px，最小12px

      this.options = {
        title: {
          text: [`{value|${total}}`, '{name|总数}'].join('\n'),
          top: 'center',
          left: 'center',
          textStyle: {
            rich: {
              value: {
                color: '#fff',
                fontSize: titleFs, // ← 动态字号
                fontWeight: 'bold',
                lineHeight: 20
              },
              name: { color: '#fff', lineHeight: 20 }
            }
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: p => {
            const percent = ((p.value / total) * 100).toFixed(1)
            return `${p.name}<br/>数量: ${p.value}<br/>占比: ${percent}%`
          }
        },
        series: [{
          type: 'pie',
          radius: ['42%', '65%'],
          data: list.map((o, i) => ({
            name: o.name,
            value: o.value,
            itemStyle: { color: COLOR_PALETTE[i % COLOR_PALETTE.length] }
          })),
          label: { show: false },
          emphasis: {
            itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,.5)' }
          }
        }]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
/* 原样式，未改动 */
.container {
  display: flex;
  height: 100%;
  width: 100%;
  background: rgba(13, 42, 67, .5);
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 114, 255, .2);
}

.left-content {
  width: 25%;
  padding: 15px 10px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 80%;
  overflow-y: auto;
  border-right: 1px solid rgba(0, 114, 255, .3);
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
  background: rgba(0, 114, 255, .1);
  border: 1px solid rgba(0, 114, 255, .3);
  border-radius: 6px;
  color: #e0e0e0;
  cursor: pointer;
  transition: all .3s;
  font-size: 14px;

  &:hover {
    background: rgba(0, 114, 255, .3);
    transform: translateX(5px);
    box-shadow: 0 0 10px rgba(0, 114, 255, .3);
  }

  &.active {
    background: rgba(0, 114, 255, .5);
    border-color: rgba(0, 114, 255, .8);
    box-shadow: 0 0 15px rgba(0, 114, 255, .5);
    color: #fff;
    font-weight: bold;
  }
}

.left-content::-webkit-scrollbar {
  width: 4px;
}

.left-content::-webkit-scrollbar-thumb {
  background: rgba(0, 114, 255, .5);
  border-radius: 4px;
}

.left-content::-webkit-scrollbar-track {
  background: transparent;
}
</style>
