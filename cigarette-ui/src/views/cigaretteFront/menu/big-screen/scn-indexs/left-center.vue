<!--
 * @Author: daidai
 * @Date: 2022-02-28 16:16:42
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-10-25 09:18:22
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\left-center.vue
-->
<template>
  <div class="container">
    <div class="left-content">
      <!-- 左侧内容 -->
      <div
          v-for="type in earlyWarningAnalysis"
          class="channel-item"
          @click="selectChannel(channel)"
      >
        {{ type.name }}
      </div>
    </div>
    <div class="right-content">
      <!-- 右侧内容 -->
      <div class="chart-container">
        <Echart id="leftCenter" :options="options" class="left_center_inner" v-if="pageflag" ref="charts" />
        <Reacquire v-else @onclick="getData" style="line-height:200px">
          重新获取
        </Reacquire>
      </div>
    </div>
  </div>
</template>

<script>
import { currentGET } from '@/api/scn-modules'
import Reacquire   from "@/components/scn-reacquire/reacquire.vue";
import Echart from "@/components/scn-echart/index.vue"


export default {
  components:{
    Reacquire,
    Echart
  },
  data() {
    return {
      options: {
        // // 新增grid配置：限制绘图区域
        // grid: {
        //   top: '10%',    // 上边距
        //   right: '10%',  // 右边距
        //   bottom: '10%', // 下边距
        //   left: '10%',   // 左边距
        //   containLabel: true // 强制所有标签在区域内
        // },
        // series: [{
        //   type: 'pie',
        //   // 缩小半径，给标签留空间
        //   radius: ['35%', '65%'], // 原先是 ['42%','65%']
        //   // 标签线设置
        //   labelLine: {
        //     length: 10,  // 第一段线缩短
        //     length2: 15, // 第二段线缩短
        //     maxSurfaceAngle: 45 // 限制线条角度，防止平铺时溢出
        //   },
        //   // 其他保持原有配置...
        // }]
      },
      earlyWarningAnalysis:[{name:'预警分析1',value:1},
                            {name:'预警分析2',value:2},
                            {name:'预警分析3',value:3},
                            {name:'预警分析4',value:4},
                            {name:'预警分析5',value:5},
                            {name:'预警分析6',value:6},
                            {name:'预警分析7',value:7},
                            {name:'预警分析8',value:8}],
      countUserNumData: {
        lockNum: 0,
        onlineNum: 0,
        offlineNum: 0,
        totalNum: 0
      },
      pageflag: true,
      timer: null
    };
  },
  created() {
    this.getData()
  },
  mounted() {
  },
  beforeDestroy() {
    this.clearData()

  },
  methods: {
    clearData() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    getData() {
      this.pageflag = true
      // this.pageflag =false

      currentGET('big1').then(res => {
        //只打印一次
        if (!this.timer) {
          console.log("设备总览", res);
        }
        if (res.success) {
          this.countUserNumData = res.data
          this.$nextTick(() => {
            this.init()
          })

        } else {
          this.pageflag = false
          this.$Message({
            text: res.msg,
            type: 'warning'
          })
        }
      })
    },
    //轮询
    switper() {
      if (this.timer) {
        return
      }
      let looper = (a) => {
        this.getData()
      };
      this.timer = setInterval(looper, this.$store.state.setting.echartsAutoTime);
      let myChart = this.$refs.charts.chart
      myChart.on('mouseover', params => {
        this.clearData()
      });
      myChart.on('mouseout', params => {
        this.timer = setInterval(looper, this.$store.state.setting.echartsAutoTime);
      });
    },
    init() {
      let total = this.countUserNumData.totalNum;
      let colors = ["#ECA444", "#33A1DB", "#56B557"];
      let piedata = {
        name: "用户总览",
        type: "pie",
        radius: ["42%", "65%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 2,
        },

        color: colors,
        data: [
          // {
          //   value: 0,
          //   name: "告警",
          //   label: {
          //     shadowColor: colors[0],
          //   },
          // },
          {
            value: this.countUserNumData.lockNum,
            name: "锁定",
            label: {
              shadowColor: colors[0],
            },
          },
          {
            value: this.countUserNumData.onlineNum,
            name: "在线",
            label: {
              shadowColor: colors[2],
            },
          },
          {
            value: this.countUserNumData.offlineNum,
            name: "离线",
            label: {
              shadowColor: colors[1],
            },
          },


        ],
      };
      this.options = {
        title: {
          // zlevel: 0,
          text: ["{value|" + total + "}", "{name|总数}"].join("\n"),
          top: "center",
          left: "center",
          textStyle: {
            rich: {
              value: {
                color: "#ffffff",
                fontSize: 24,
                fontWeight: "bold",
                lineHeight: 20,
              },
              name: {
                color: "#ffffff",
                lineHeight: 20,
              },
            },
          },
        },
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(0,0,0,.6)",
          borderColor: "rgba(147, 235, 248, .8)",
          textStyle: {
            color: "#FFF",
          },
        },
        legend: {
          show: false,
          top: "5%",
          left: "center",
        },
        series: [
          //展示圆点
          {
            ...piedata,
            tooltip: { show: true },
            label: {
              formatter: "   {b|{b}}   \n   {c|{c}个}   {per|{d}%}  ",
              //   position: "outside",
              rich: {
                b: {
                  color: "#fff",
                  fontSize: 12,
                  lineHeight: 26,
                },
                c: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
                per: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
              },
            },
            labelLine: {
              length: 20, // 第一段线 长度
              length2: 36, // 第二段线 长度
              show: true,

            },
              emphasis: {
                show: true,
              },
          },
          {
            ...piedata,
            tooltip: { show: true },
            itemStyle: {},
            label: {
              backgroundColor: "inherit", //圆点颜色，auto：映射的系列色
              height: 0,
              width: 0,
              lineHeight: 0,
              borderRadius: 2.5,
              shadowBlur: 8,
              shadowColor: "auto",
              padding: [2.5, -2.5, 2.5, -2.5],
            },
            labelLine: {
              length: 20, // 第一段线 长度
              length2: 36, // 第二段线 长度
              show: false,
            },
          },
        ],
      };
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  display: flex;
  height: 100%;
  width: 100%;
}

.left-content {
  width: 20%;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px; /* 项间距 */
  max-height: 80%; /* 限制最大高度 */
  overflow-y: auto; /* 超出时显示滚动条 */
  overflow-x: hidden; /* 横向溢出隐藏 */
}

.right-content {
  width: 80%;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
}

.chart-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_center_inner {
  width: 100%;
  height: 100%;
}

.channel-item {
  text-align: center;
  padding: 6px 0;
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
/*确保图表容器有固定高度 */
.left_center_inner {
  width: 100%;
  height: 400px; /* 固定高度 */
  min-height: 300px; /* 最小高度 */
}
</style>
