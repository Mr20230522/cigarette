<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def" :class="{ 'overflow-y-auto': !sbtxSwiperFlag }">
    <component :is="components" :data="list" :class-option="defaultOption">
      <ul class="right_center">
        <li class="right_center_item" v-for="(item, i) in list" :key="i">
          <span class="orderNum">{{ i + 1 }}</span>
          <div class="inner_right">
            <div class="dibu"></div>
            <div class="content-wrapper">
              <!-- 左侧视频 -->
              <div class="video-container">
                <video :src="item.video" controls>
                  您的浏览器不支持视频标签。
                </video>
              </div>
              <!-- 右侧文字信息 -->
              <div class="text-container">
                <!-- 第一行：设备ID和型号 -->
                <div class="text-row">
                  <div class="info">
                    <span class="labels">设备ID：</span>
                    <span class="contents zhuyao">{{ item.gatewayno }}</span>
                  </div>
                  <div class="info">
                    <span class="labels">型号：</span>
                    <span class="contents">{{ item.terminalno }}</span>
                  </div>
                </div>
                <!-- 第二行：告警值和时间 -->
                <div class="text-row">
                  <div class="info">
                    <span class="labels">告警值：</span>
                    <span class="contents warning">{{ item.alertvalue | montionFilter }}</span>
                  </div>
                  <div class="info">
                    <span class="labels">时间：</span>
                    <span class="contents" style="font-size:12px">{{ item.createtime }}</span>
                  </div>
                </div>
                <div class="text-row address-alert">
                  <div class="info address">
                    <span class="labels">地址：</span>
                    <span class="contents ciyao">{{ item.provinceName }}/{{ item.cityName }}/{{ item.countyName }}</span>
                  </div>
                  <div class="info alert">
                    <span class="labels">报警内容：</span>
                    <span class="contents ciyao" :class="{ warning: item.alertdetail }">{{ item.alertdetail || '无' }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </component>
  </div>
  <Reacquire v-else @onclick="getData" style="line-height:200px" />
</template>

<script>
import { currentGET } from '@/api/scn-modules'
import vueSeamlessScroll from 'vue-seamless-scroll'
import Kong from '@/components/scnKong.vue'
import Reacquire from "@/components/scn-reacquire/reacquire.vue";


export default {
  components: {vueSeamlessScroll, Kong,Reacquire},
  data() {
    return {
      list: [
        {
          gatewayno: "DEV-001",
          terminalno: "TX-1000",
          alertvalue: 85,
          provinceName: "浙江省",
          cityName: "杭州市",
          countyName: "西湖区",
          createtime: "2023-07-20 10:30:45",
          alertdetail: "温度过高",
          video: "http://127.0.0.1:8081/01/01.mp4" // 替换为实际视频地址
        },
        {
          gatewayno: "DEV-002",
          terminalno: "TX-2000",
          alertvalue: 45,
          provinceName: "江苏省",
          cityName: "南京市",
          countyName: "鼓楼区",
          createtime: "2023-07-20 11:15:22",
          alertdetail: "电压异常",
          video: "http://127.0.0.1:8081/01/02.mp4" // 替换为实际视频地址
        },
        {
          gatewayno: "DEV-003",
          terminalno: "TX-3000",
          alertvalue: 92,
          provinceName: "广东省",
          cityName: "深圳市",
          countyName: "南山区",
          createtime: "2023-07-20 09:45:18",
          alertdetail: "湿度超标",
          image: "http://127.0.0.1:8081/01/03.mp4"
        },
        {
          gatewayno: "DEV-004",
          terminalno: "TX-4000",
          alertvalue: 30,
          provinceName: "北京市",
          cityName: "北京市",
          countyName: "海淀区",
          createtime: "2023-07-20 14:20:33",
          alertdetail: "",
          image: "http://127.0.0.1:8081/01/04.mp4"
        },
        {
          gatewayno: "DEV-005",
          terminalno: "TX-5000",
          alertvalue: 78,
          provinceName: "上海市",
          cityName: "上海市",
          countyName: "浦东新区",
          createtime: "2023-07-20 13:10:57",
          alertdetail: "信号中断",
          image: "http://127.0.0.1:8081/01/05.mp4"
        }
      ],
      pageflag: true,
      defaultOption: {
        ...this.$store.state.scnSettings.defaultOption,
        limitMoveNum: 3,
        singleHeight: 270, // 微调高度以适应视频
        step: 0,
      }
    };
  },
  computed: {
    sbtxSwiperFlag() {
      let ssyjSwiper = this.$store.state.scnSettings.ssyjSwiper
      if (ssyjSwiper) {
        this.components = vueSeamlessScroll
      } else {
        this.components = Kong
      }
      return ssyjSwiper
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.pageflag = true
      // 模拟原接口请求延迟效果
      let timer = setTimeout(() => {
        clearTimeout(timer)
        this.defaultOption.step = this.$store.state.scnSettings.defaultOption.step
      }, this.$store.state.scnSettings.defaultOption.waitTime);
    },
    handleImgError(item) {
      // 图片加载失败时替换为默认图片
      item.image = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='40'%3E%3Crect fill='%23f5f5f5' width='80' height='40'/%3E%3Ctext x='40' y='20' font-size='10' text-anchor='middle'%3E无图片%3C/text%3E%3C/svg%3E";
    }
  },
};
</script>

<style lang='scss' scoped>
.right_center {
  width: 100%;
  height: 100%;

  .right_center_item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: auto;
    padding: 10px;
    font-size: 14px;
    color: #fff;

    .orderNum {
      margin: 0 20px 0 -20px;
    }

    .inner_right {
      position: relative;
      height: 100%;
      width: 450px;
      flex-shrink: 0;
      line-height: 1.5;

      .dibu {
        position: absolute;
        height: 2px;
        width: 104%;
        background-image: url("~@/assets/scn-img/zuo_xuxian.png");
        bottom: -12px;
        left: -2%;
        background-size: cover;
      }

      .content-wrapper {
        display: flex;
        align-items: center;
        width: 100%;

        .video-container {
          width: 80px;
          height: 60px;
          margin-right: 10px;
          flex-shrink: 0;
          video {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .text-container {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: space-between;

          .text-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 5px;

            .info {
              display: flex;
              align-items: center;

              .labels {
                flex-shrink: 0;
                font-size: 12px;
                color: rgba(255, 255, 255, 0.6);
              }

              .zhuyao {
                color: $primary-color;
                font-size: 15px;
              }

              .ciyao {
                color: rgba(255, 255, 255, 0.8);
              }

              .warning {
                color: #E6A23C;
                font-size: 15px;
              }
            }
          }
        }
      }
    }
  }
}

.right_center_wrap {
  overflow: hidden;
  width: 100%;
  height: 440px;
}

.overflow-y-auto {
  overflow-y: auto;
}

.text-row.address-alert {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0; // 如果还想再紧凑，可设为 0
  .address {
    flex: 1 1 60%; // 地址占一半
    padding-right: 6px;
  }

  .alert {
    flex: 1 1 40%; // 报警内容占一半
    padding-left: 6px;
  }
}
</style>
