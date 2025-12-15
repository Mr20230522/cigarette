<template>
  <div class="left-top-wrapper">   <!-- 唯一根节点 -->
    <!-- 时间范围选择按钮 -->
    <div class="channel-selector">
      <div
        v-for="item in timeOptions"
        :key="item.value"
        class="channel-item"
        :class="{ active: selectTimeType === item.value }"
        @click="changeRange(item.value)"
      >
        {{ item.label }}
      </div>
    </div>

    <!-- 统计值列表 -->
    <ul class="user_Overview flex">
      <li style="color:#00fdfa">
        <div class="user_Overview_nums allnum">
          <dv-digital-flop :config="configTotal" style="width:100%;height:100%"/>
        </div>
        <p>车辆总数</p>
      </li>
      <li style="color:#07f7a8">
        <div class="user_Overview_nums online">
          <dv-digital-flop :config="configNormal" style="width:100%;height:100%"/>
        </div>
        <p>正常车辆</p>
      </li>
      <li style="color:#f5023d">
        <div class="user_Overview_nums laramnum">
          <dv-digital-flop :config="configAlarm" style="width:100%;height:100%"/>
        </div>
        <p>嫌疑车辆</p>
      </li>
      <li style="color:#e3b337">
        <div class="user_Overview_nums offline">
          <dv-digital-flop :config="configRatio" style="width:100%;height:100%"/>
        </div>
        <p>嫌疑占比</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { getDaySliceCache } from '@/api/cigarette/cache/bigScreenCache'

export default {
  data() {
    return {
      selectTimeType: 1,          // 1 本日  2 本周  3 本月
      timeOptions: [
        { label: '本日', value: 1 },
        { label: '本周', value: 2 },
        { label: '本月', value: 3 }
      ],
      raw: {},                    // 接口返回的原始对象
      timer: null,                // 5秒定时器
      // 四个翻牌器配置
      configTotal:  { number:[0], content:'{nt}', style:{fontSize:24,fill:'#00fdfa'}},
      configNormal: { number:[0], content:'{nt}', style:{fontSize:24,fill:'#07f7a8'}},
      configAlarm:  { number:[0], content:'{nt}', style:{fontSize:24,fill:'#f5023d'}},
      configRatio:  { number:[0], content:'{nt}%',style:{fontSize:24,fill:'#e3b337'}}
    }
  },
  created() {
    this.load()          // 首次
    this.timer = setInterval(this.load, 5_000)
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    async load() {
      this.raw = await getDaySliceCache()
      if (this.raw) this.calc()
    },
    calc() {
      const r = this.raw
      let total = 0, alarm = 0

      switch (this.selectTimeType) {
        case 1:               // 本日
          total = r.d0
          alarm = r.suspectD0
          break
        case 2:               // 本周  d0~d6
          total = r.d0 + r.d1 + r.d2 + r.d3 + r.d4 + r.d5 + r.d6
          alarm = r.suspectD0 + r.suspectD1 + r.suspectD2 + r.suspectD3 +
            r.suspectD4 + r.suspectD5 + r.suspectD6
          break
        case 3:               // 本月
          total = r.monthCurr
          alarm = r.suspectMonthCurr
          break
      }

      const normal = total - alarm
      const ratio  = total ? Math.round((alarm / total) * 100) : 0

      /* ===== 自适应字号：位数越多字越小 ===== */
      const digit  = String(total).length          // 总位数
      const fontSz = Math.max(8, 24 - digit * 2)  // 每多1位减2px，最小8px（原来是10px）

      console.log(`位数: ${digit}, 字体: ${fontSz}px, 总数: ${total}`)

      /* 翻牌器配置（带动态字号） */
      this.configTotal  = { ...this.configTotal,  number: [total], style: { fontSize: fontSz, fill: '#00fdfa' } }
      this.configNormal = { ...this.configNormal, number: [normal], style: { fontSize: fontSz, fill: '#07f7a8' } }
      this.configAlarm  = { ...this.configAlarm,  number: [alarm],  style: { fontSize: fontSz, fill: '#f5023d' } }
      this.configRatio  = { ...this.configRatio,  number: [ratio],  style: { fontSize: fontSz, fill: '#e3b337' } }
    },

    changeRange(val) {
      this.selectTimeType = val
      if (this.raw && this.raw.d0 !== undefined) this.calc()
    }
  }
}
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
