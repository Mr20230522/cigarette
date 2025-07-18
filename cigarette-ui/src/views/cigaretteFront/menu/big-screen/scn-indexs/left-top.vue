<!--
 * @Author: daidai
 * @Date: 2022-02-28 16:16:42
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-20 17:57:11
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\left-center.vue
-->
<template>
  <div>
    <!-- 时间范围选择按钮 -->
    <div class="channel-selector">
      <div
          v-for="label in timeOptions"
          class="channel-item"
          @click="setTimeRange(label.value)"
      >
        {{ label.label }}
      </div>
    </div>

    <!-- 统计值列表 -->
    <ul class="user_Overview flex" v-if="pageflag">
      <li class="user_Overview-item" style="color: #00fdfa">
        <div class="user_Overview_nums allnum">
          <dv-digital-flop :config="config" style="width:100%;height:100%;" />
        </div>
        <p>车辆总数</p>
      </li>
      <li class="user_Overview-item" style="color: #07f7a8">
        <div class="user_Overview_nums online">
          <dv-digital-flop :config="onlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>正常车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #f5023d">
        <div class="user_Overview_nums laramnum">
          <dv-digital-flop :config="laramnumconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆</p>
      </li>
      <li class="user_Overview-item" style="color: #e3b337">
        <div class="user_Overview_nums offline">
          <dv-digital-flop :config="offlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>嫌疑车辆占比</p>
      </li>
    </ul>
    <Reacquire v-else @onclick="getData" line-height="200px">
      重新获取
    </Reacquire>
  </div>
</template>

<script>
import { currentGET } from '@/api/scn-modules'
import Reacquire from "@/components/scn-reacquire/reacquire.vue";
let style = {
    fontSize: 24
}
export default {
  components:{Reacquire},
    data() {
        return {
            selectTimeType:0,
            selectedTime:'',
            timeOptions: [ // 时间范围选项数组
                { label: '最近一天', value: 1 },
                { label: '最近一月', value: 2 },
                { label: '最近一星期', value: 3 }
            ],
            options: {},
            userOverview: {
                alarmNum: 0,
                offlineNum: 0,
                onlineNum: 0,
                totalNum: 0,
            },
            pageflag: true,
            timer: null,
            config: {
                number: [100],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#00fdfa",
                    fill: "#00fdfa",
                },
            },
            onlineconfig: {
                number: [0],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#07f7a8",
                    fill: "#07f7a8",
                },
            },
            offlineconfig: {
                number: [0],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#e3b337",
                    fill: "#e3b337",
                },
            },
            laramnumconfig: {
                number: [0],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#f5023d",
                    fill: "#f5023d",
                },
            },
            setTimeRange(range) {
              this.selectTimeType = range;
            }

        };
    },
    watch:{
      selectTimeType(newVal){
        this.calculateTime(newVal)
      }
    },
    filters: {
        numsFilter(msg) {
            return msg || 0;
        },
    },
    created() {
        this.getData()
        this.selectTimeType = 1;
        this.calculateTime(this.selectTimeType)
    },
    mounted() {
    },
    beforeDestroy() {
        this.clearData()

    },
    methods: {
      calculateTime(range) {
        const now = new Date();
        let selectedDate;

        switch (range) {
          case 1: // 最近一天
            selectedDate = new Date(now.getTime() - 24 * 60 * 60 * 1000);
            break;
          case 2: // 最近一周
            selectedDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
            break;
          case 3: // 最近一月
            selectedDate = new Date(now);
            selectedDate.setMonth(now.getMonth() - 1);
            break;
          default:
            console.error('Invalid range');
            return;
        }
        this.selectedTime = this.formatDate(selectedDate);
        console.log('this.selectedTime',this.selectedTime)
      },
      // 格式化时间
      formatDate(date) {
        const pad = n => n.toString().padStart(2, '0');
        const d = new Date(date);
        const Y = d.getFullYear();
        const M = pad(d.getMonth() + 1);
        const D = pad(d.getDate());
        const h = pad(d.getHours());
        const m = pad(d.getMinutes());
        const s = pad(d.getSeconds());
        const ms = d.getMilliseconds().toString().padEnd(6, '0');
        return `${Y}-${M}-${D} ${h}:${m}:${s}.${ms}`;
      },
        clearData() {
            if (this.timer) {
                clearInterval(this.timer)
                this.timer = null
            }
        },
        getData() {
            this.pageflag = true;
            currentGET("big2").then((res) => {
                if (!this.timer) {
                    console.log("设备总览", res);
                }
                if (res.success) {
                    this.userOverview = res.data;
                    this.onlineconfig = {
                        ...this.onlineconfig,
                        number: [res.data.onlineNum]
                    }
                    this.config = {
                        ...this.config,
                        number: [res.data.totalNum]
                    }
                    this.offlineconfig = {
                        ...this.offlineconfig,
                        number: [res.data.offlineNum]
                    }
                    this.laramnumconfig = {
                        ...this.laramnumconfig,
                        number: [res.data.alarmNum]
                    }
                    this.switper()
                } else {
                    this.pageflag = false;
                    this.$Message.warning(res.msg);
                }
            });
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
        },
    },
};
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

        .allnum {

            // background-image: url("../../assets/img/left_top_lan.png");
            &::before {
                background-image: url("~@/assets/scn-img/left_top_lan.png");

            }
        }

        .online {
            &::before {
                background-image: url("~@/assets/scn-img/left_top_lv.png");

            }
        }

        .offline {
            &::before {
                background-image: url("~@/assets/scn-img/left_top_huang.png");

            }
        }

        .laramnum {
            &::before {
                background-image: url("~@/assets/scn-img/left_top_hong.png");

            }
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
</style>
