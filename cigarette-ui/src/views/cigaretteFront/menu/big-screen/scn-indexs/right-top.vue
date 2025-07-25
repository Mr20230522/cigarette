<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def" :class="{ 'overflow-y-auto': !sbtxSwiperFlag }">
    <component :is="components" :data="list" :class-option="defaultOption">
      <ul class="right_center">
        <li class="right_center_item" v-for="(item, i) in list" :key="i">
          <span class="orderNum">{{ i + 1 }}</span>
          <div class="inner_right">
            <div class="dibu"></div>
            <div class="content-wrapper">
              <!-- 左侧图片 -->
              <div class="image-container">
                <img :src="item.image" @error="handleImgError(item)" alt="设备图片">
              </div>
              <!-- 右侧文字信息 -->
              <div class="text-container">
                <!-- 第一行：设备ID和型号 -->
                <div class="text-row">
                  <div class="info">
                    <span class="labels">车牌号：</span>
                    <span class="contents zhuyao">{{ item.licensePlate }}</span>
                  </div>
                  <div class="info">
                    <span class="labels">车辆类型：</span>
                    <span class="contents">{{ item.carTypeId }}</span>
                  </div>
                </div>
                <!-- 第二行：告警值和时间 -->
                <div class="text-row">
                  <div class="info">
                    <span class="labels">嫌疑程度：</span>
                    <span class="contents warning">{{ item.degreeSuspicion | montionFilter }}</span>
                  </div>
                  <div class="info">
                    <span class="labels">时间：</span>
                    <span class="contents" style="font-size:12px">{{ item.createTime }}</span>
                  </div>
                </div>
                <div class="text-row address-alert">
                  <div class="info address">
                    <span class="labels">监测点：</span>
                    <span class="contents ciyao">{{ item.detectionId }}</span>
                  </div>
                  <div class="info alert">
                    <span class="labels">备注：</span>
                    <span class="contents ciyao" :class="{ warning: item.alertdetail }">{{ item.remark || '无' }}</span>
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
import {listVehicleBehaviorVo} from '@/api/cigarette/vehicle/vehicleBehavior'


export default {
  components: { vueSeamlessScroll, Kong ,Reacquire},
  data() {
    return {
      list: [
        ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },
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
    this.getList()
    this.getData()
  },
  methods: {
    getList(){
      listVehicleBehaviorVo(this.queryParams).then(response=>{
        // 这里需要先得到前10条违法程度高于10的数据
        // this.list=response.rows.reduce((arr,item)=>{
        //   if(item.degreeSuspicion>=60){
        //
        //   }
        // })
        this.list=response.rows
      })
      console.log('this.list',this.list)
    },
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

        .image-container {
          width: 80px;
          height: 60px;
          margin-right: 10px;
          flex-shrink: 0;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border: 1px solid rgba(0, 114, 255, 0.3);
            border-radius: 2px;
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
//修改显示大小
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
  margin-bottom: 0;   // 如果还想再紧凑，可设为 0
  .address {
    flex: 1 1 60%;    // 地址占一半
    padding-right: 6px;
  }
  .alert {
    flex: 1 1 40%;    // 报警内容占一半
    padding-left: 6px;
  }
}
</style>
