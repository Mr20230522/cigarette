<template>
  <div class="echart-floating-container">
    <!-- 左上角 - 颜色统计柱状图 -->
    <div class="chart-container top-left">
      <div ref="colorChart" class="chart"></div>
    </div>

    <!-- 右上角 - 饼图 -->
    <div class="chart-container top-right">
      <div ref="pieChart" class="chart"></div>
    </div>

<!--    &lt;!&ndash; 左下角 - 折线图 &ndash;&gt;-->
<!--    <div class="chart-container bottom-left">-->
<!--      <div ref="lineChart" class="chart"></div>-->
<!--    </div>-->

<!--    &lt;!&ndash; 右下角 - 散点图 &ndash;&gt;-->
<!--    <div class="chart-container bottom-right">-->
<!--      <div ref="scatterChart" class="chart"></div>-->
<!--    </div>-->
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'ChartComponent',
  props:{
    // pieData:Array,
    // required:true
    pieData:{
      type:Array,
      required:true,
    },
    colorData: {
      type: Object,
      required: true,
      default: () => ({
        categories: [],
        values: [],
        colors:[],
      }),
      validator: (value) => {
        return value &&
          Array.isArray(value.categories) &&
          Array.isArray(value.values) &&
          value.categories.length === value.values.length;
      }
    }
  },

  data() {
    return {
      colorChart: null,
      pieChart: null,
      lineChart: null,
      scatterChart: null,

      // 颜色统计柱状图数据
      // colorData: {
      //   categories: ['红色', '蓝色', '绿色', '黄色', '紫色', '橙色', '青色'],
      //   values: [45, 78, 36, 52, 28, 65, 42],
      //   colors: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622']
      // },

      // 饼图数据


      // 折线图数据
      lineData: {
        categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
        values: [820, 932, 901, 934, 1290, 1330, 1320, 801, 102, 230, 432, 789]
      },

      // 散点图数据
      scatterData: [
        [10.0, 8.04],
        [8.0, 6.95],
        [13.0, 7.58],
        [9.0, 8.81],
        [11.0, 8.33],
        [14.0, 9.96],
        [6.0, 7.24],
        [4.0, 4.26],
        [12.0, 10.84],
        [7.0, 4.82],
        [5.0, 5.68]
      ]
    };
  },
  watch: {
    pieData: {
      handler(newData) {
        console.log('!!!!!这个数据被修改了',newData)
        if (this.pieChart) {
          // 只更新数据部分，保留其他配置
          this.pieChart.setOption({
            series: [{
              data: newData
            }]
          });
        }
      },
      deep: true // 深度监听，确保数组内容变化也能触发
    },
    colorData: {
      handler(newVal) {
        if (this.colorChart) {
          this.setColorChartOption();
        }
      },
      deep: true,  // 深度监听对象内部变化
      immediate: true  // 立即执行一次
    },
  },
  mounted() {
    this.initCharts();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
    if (this.colorChart) this.colorChart.dispose();
    if (this.pieChart) this.pieChart.dispose();
    if (this.lineChart) this.lineChart.dispose();
    if (this.scatterChart) this.scatterChart.dispose();
  },
  methods: {
    initCharts() {
      // 初始化颜色统计柱状图
      this.colorChart = echarts.init(this.$refs.colorChart);
      this.setColorChartOption();

      // 初始化饼图
      this.pieChart = echarts.init(this.$refs.pieChart);
      this.setPieChartOption();

      // 初始化折线图
      this.lineChart = echarts.init(this.$refs.lineChart);
      this.setLineChartOption();

      // 初始化散点图
      this.scatterChart = echarts.init(this.$refs.scatterChart);
      this.setScatterChartOption();
    },

    setColorChartOption() {
      // 防御性检查
      if (!this.colorData || !Array.isArray(this.colorData.values) || !Array.isArray(this.colorData.colors)) {
        console.warn('Invalid colorData:', this.colorData);
        return;
      }
      const option = {
        backgroundColor: 'transparent',
        title: {
          text: '颜色统计',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        tooltip: {
          backgroundColor: 'rgba(0,0,0,0.7)',
          textStyle: {
            color: '#fff'
          }
        },
        xAxis: {
          data: this.colorData.categories,
          axisLabel: {
            rotate: 30,
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          }
        },
        yAxis: {
          axisLabel: {
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(100, 100, 100, 0.2)'
            }
          }
        },
        series: [{
          name: '数量',
          type: 'bar',
          data: this.colorData.values.map((value, index) => ({
            value,
            itemStyle: {
              color: this.colorData.colors[index] || '#ccc' // 使用传入的颜色，没有则默认灰色
            }
          })),
          barWidth: '60%'
        }],
        grid: {
          containLabel: true,
          backgroundColor: 'transparent'
        }
      };
      this.colorChart.setOption(option);
    },

    setPieChartOption() {
      const option = {
        backgroundColor: 'transparent',
        title: {
          text: '占比分析',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0,0,0,0.7)',
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          textStyle: {
            color: '#ccc'
          }
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '50%',
            data: this.pieData,
            itemStyle: {
              borderColor: '#222',
              borderWidth: 1
            },
            label: {
              color: '#eee'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(255, 255, 255, 0.5)'
              }
            }
          }
        ]
      };
      this.pieChart.setOption(option);
    },

    setLineChartOption() {
      const option = {
        backgroundColor: 'transparent',
        title: {
          text: '月度趋势',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0,0,0,0.7)',
          textStyle: {
            color: '#fff'
          }
        },
        xAxis: {
          type: 'category',
          data: this.lineData.categories,
          axisLabel: {
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(100, 100, 100, 0.2)'
            }
          }
        },
        series: [
          {
            data: this.lineData.values,
            type: 'line',
            smooth: true,
            lineStyle: {
              width: 2,
              color: '#5470C6'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(84, 112, 198, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(84, 112, 198, 0.1)'
                }
              ])
            },
            symbol: 'circle',
            symbolSize: 6,
            itemStyle: {
              color: '#5470C6'
            }
          }
        ],
        grid: {
          containLabel: true
        }
      };
      this.lineChart.setOption(option);
    },

    setScatterChartOption() {
      const option = {
        backgroundColor: 'transparent',
        title: {
          text: '相关性分析',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        tooltip: {
          backgroundColor: 'rgba(0,0,0,0.7)',
          textStyle: {
            color: '#fff'
          }
        },
        xAxis: {
          axisLabel: {
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(100, 100, 100, 0.2)'
            }
          }
        },
        yAxis: {
          axisLabel: {
            color: '#ccc'
          },
          axisLine: {
            lineStyle: {
              color: '#666'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(100, 100, 100, 0.2)'
            }
          }
        },
        series: [
          {
            symbolSize: 8,
            data: this.scatterData,
            type: 'scatter',
            itemStyle: {
              color: '#91cc75'
            }
          }
        ],
        grid: {
          containLabel: true
        }
      };
      this.scatterChart.setOption(option);
    },

    handleResize() {
      if (this.colorChart) this.colorChart.resize();
      if (this.pieChart) this.pieChart.resize();
      if (this.lineChart) this.lineChart.resize();
      if (this.scatterChart) this.scatterChart.resize();
    }
  }
};
</script>

<style scoped>

.four-corner-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background-color: #8e8d8d; /* 深色背景更好展示透明效果 */
}

.echart-floating-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 允许事件穿透 */
}
.chart-container {
  pointer-events: auto; /* 图表区域可交互 */
  position: absolute;
  width: 25%;
  height: 25%;
  transition: all 0.3s ease;
}

.chart {
  width: 100%;
  height: 100%;
  background-color: rgba(83, 83, 83, 0.7); /* 半透明背景 */
  border-radius: 6px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

/* 鼠标悬停放大效果 */
.chart-container:hover {
  transform: scale(1.2);
  z-index: 10;
}

.top-left {
  top: 70px;
  left: 10px;
}

.top-left:hover{
  top: 115px;
  left: 50px;
}

.top-right {
  top: 70px;
  right: 15px;
}

.top-right:hover{
  top: 115px;
  right: 75px;
}

.bottom-left {
  bottom: 10px;
  left: 10px;
}

.bottom-left:hover {
  bottom: 50px;
  left: 50px;
}

.bottom-right {
  bottom: 10px;
  right: 10px;
}


.bottom-right:hover {
  bottom: 50px;
  right: 50px;
}
</style>
