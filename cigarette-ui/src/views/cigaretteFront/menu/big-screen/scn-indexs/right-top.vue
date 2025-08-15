<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def">
    <transition-group name="list" tag="ul" class="right_center">
      <li
        class="right_center_item"
        v-for="(item, i) in visibleList"
        :key="item.id || i"
        :class="{ 'show': item.show }"
        @click="handleItemClick(item)"
      >
        <span class="orderNum">{{ getDisplayIndex(i) }}</span>
        <div class="inner_right">
          <div class="dibu"></div>
          <div class="content-wrapper">
            <div class="image-container" @click.stop="enlargeImage(item.picUrl)">
              <img
                :src="item.picUrl"
                @error="handleImgError(item)"
                alt="车辆图片"
                loading="lazy"
              >
            </div>
            <div class="text-container">
              <div class="text-row">
                <div class="info">
                  <span class="labels">车牌号：</span>
                  <span class="contents zhuyao">{{ item.plate }}</span>
                </div>
                <div class="info">
                  <span class="labels">车辆类型：</span>
                  <span class="contents">{{ item.plateType}}</span>
                </div>
              </div>
              <div class="text-row">
                <div class="info">
                  <span class="labels">嫌疑程度：</span>
                  <span class="contents warning">{{ item.level | montionFilter }}</span>
                </div>
                <div class="info">
                  <span class="labels">时间：</span>
                  <span class="contents" style="font-size:12px">{{ item.captureTime }}</span>
                </div>
              </div>
              <div class="text-row address-alert">
                <div class="info address">
                  <span class="labels">监测点：</span>
                  <span class="contents ciyao">{{ item.cameraName }}</span>
                </div>
<!--                <div class="info alert">-->
<!--                  <span class="labels">备注：</span>-->
<!--                  <span class="contents ciyao" :class="{ warning: item.alertdetail }">-->
<!--                    {{ item.remark || '无' }}-->
<!--                  </span>-->
<!--                </div>-->
              </div>
            </div>
          </div>
        </div>
      </li>
    </transition-group>

    <div v-if="loading" class="loading-tip">
      <i class="el-icon-loading"></i>
      数据加载中...
    </div>

    <div v-if="enlargedImage" class="image-preview-overlay" @click="enlargedImage = null">
      <div class="image-preview-container">
        <img :src="enlargedImage" @error="handleImageError" class="enlarged-image">
      </div>
    </div>
  </div>
  <Reacquire v-else @onclick="initData" style="line-height:200px"/>
</template>

<script>
import {getUpToDataDegreeSuspicion} from '@/api/cigarette/vehicle/vehicleBehavior'
import Reacquire from "@/components/scn-reacquire/reacquire.vue"
import {ScnEventBus} from "@/utils/scn-event-bus";
import {getVehicleBehaviorVideo} from "@/api/cigarette/multimediaResource/keyVideo"
import {listDetection} from "@/api/cigarette/detection/detection";
import {tenList, overIdList, allList, byIdGetVideoPath} from "@/api/cigarette/trafficData/trafficData"


export default {
  components: {Reacquire},
  // 声明依赖的字典（与第一个组件一致）
  dicts: ['tob_vehicle_type'],
  data() {
    return {
      detectionOptions: [],
      videoData: [],
      vehicleData: null,
      pageflag: true,
      allData: [],
      visibleList: [],
      currentIndex: 0,
      loading: false,
      enlargedImage: null,
      batchSize: 5,
      displayInterval: 2000,
      fetchInterval: 4000,
      displayTimer: null,
      fetchTimer: null,
      queryParams: {
        level: null,
        Id: null
      },
      // 车辆类型映射表
      vehicleTypeMap: {},
      // 字典加载状态
      dictLoaded: false
    }
  },
  filters: {
    montionFilter(value) {
      return value
    }
  },
  created() {
    // 初始化字典监听
    this.initDictWatch();
    this.initData()
    this.newData()
  },

  methods: {
    async newData(){
      try{
        const data1=await tenList({level:null})
        const data2=await overIdList({level:null})
        const data3=await allList({level:null,Id:null})
        console.log('!!!data1',data1)
        console.log('@@@data2',data2)
        console.log('###data1',data3)


      }catch{

      }
    },
    handleImageError(event) {
      event.target.src = this.getDefaultImage(); // 强制替换为占位图
    },
    getDefaultImage() {
      // 灰色背景 + 文字提示（可自定义颜色和文字）
      return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="80" height="60"%3E%3Crect fill="%23e0e0e0" width="80" height="60"%3E%3C/rect%3E%3Ctext x="50%" y="50%" font-size="10" text-anchor="middle" dominant-baseline="middle" fill="%23666666"%3E图片加载失败%3C/text%3E%3C/svg%3E';
    },
    // 初始化字典监听
    initDictWatch() {
      this.$watch(
        () => this.dict.type.tob_vehicle_type,
        (newVal) => {
          if (newVal && newVal.length) {
            this.buildVehicleTypeMap(newVal);
            this.dictLoaded = true;
          }
        },
        {immediate: true}
      );
    },

    // 构建车辆类型映射关系
    buildVehicleTypeMap(dictData) {
      this.vehicleTypeMap = dictData.reduce((map, item) => {
        map[item.value] = item.label;
        return map;
      }, {});
    },

    // 安全获取车辆类型名称
    getVehicleTypeName(typeId) {
      if (!typeId) return '未知类型';
      return this.vehicleTypeMap[typeId] || `未知类型(${typeId})`;
    },

    handleItemClick(item) {
      ScnEventBus.$emit('force-show-item', {
        licensePlate: item.plate,
        carType: item.cameraName, // 修改为显示名称
        suspicionLevel: item.level,
        color: item.vehicleColor,
      });
      this.showSuspectedVideo(item.id);
    },

    async initData() {
      this.pageflag = true
      this.allData = []
      this.visibleList = []
      this.currentIndex = 0
      await this.fetchData()
      await this.loadDetectionOptions();
    },

    async showSuspectedVideo(id) {
      try {
        const videoDataTemp = await this.fetchVideoData(id)
        console.log('!!!videoDataTemp',videoDataTemp)
        if (videoDataTemp) {
          if (videoDataTemp.videoPath) {
            this.videoData.push({
              id: 1,
              name: videoDataTemp.cameraName,
              url: 'http://127.0.0.1:8000/' + videoDataTemp.videoPath,
            })
          } else {
            this.$message.warning('没有的视频记录！！！');
          }
          ScnEventBus.$emit('suspected-video', {
            mode: 1,
            cameras: this.videoData
          })
          this.videoData = []
        }else{
          this.$message.warning('没有找到该车辆的行为记录');
        }

      } catch (error) {
        console.error('处理视频数据失败:', error);
      }
    },

    async fetchVideoData(id) {
      try {
        console.log('!@#id',id)
        const response = await byIdGetVideoPath({id:id});
        return response
      } catch (error) {
        console.error('获取视频失败:', error);
        return null
      }
    },

    async fetchData() {
      try {
        this.loading = true;
        const response = await tenList(this.queryParams);
        response.reverse()

        if (response && response.length > 0) {
          this.queryParams.id = response[response.length - 1].id;
          const newData = response.map(item => ({
            ...item,
            image: item.image
              ? 'http://127.0.0.1:8000/' + item.image
              : this.getDefaultImage(),
            show: false
          }));
          this.allData = [...this.allData, ...newData];
          this.startDisplay();
        } else {
          this.prepareNextBatch();
        }
      } catch (error) {
        console.error('数据获取失败:', error);
        this.prepareNextBatch();
      } finally {
        this.loading = false;
      }
    },

    loadDetectionOptions() {
      listDetection().then(response => {
        this.detectionOptions = response.rows.map(item => ({
          detectionId: item.detectionId,
          districtId: item.districtId,
          detectionName: item.detectionName
        }));
      }).catch(error => {
        console.error("Failed to load detection options:", error);
      });
    },

    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知监测点';
    },

    startDisplay() {
      clearInterval(this.displayTimer)

      this.displayTimer = setInterval(() => {
        if (this.currentIndex < this.allData.length) {
          const newItem = {
            ...this.allData[this.currentIndex],
            show: true
          }

          this.visibleList = [...this.visibleList, newItem]

          if (this.visibleList.length > this.batchSize) {
            this.visibleList = this.visibleList.slice(-this.batchSize)
          }

          ScnEventBus.$emit('new-visible-item', newItem)

          this.currentIndex++
        } else {
          clearInterval(this.displayTimer)
          this.prepareNextBatch()
        }
      }, this.displayInterval)
    },

    prepareNextBatch() {
      clearTimeout(this.fetchTimer)
      this.fetchTimer = setTimeout(() => {
        this.fetchData()
      }, this.fetchInterval)
    },

    getDisplayIndex(i) {
      return this.currentIndex - this.visibleList.length + i + 1
    },

    enlargeImage(url) {
      this.enlargedImage = url
    },

    handleImgError(item) {
      item.image = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg   ' width='80' height='40'%3E%3Crect fill='%23f5f5f5' width='80' height='40'/%3E%3Ctext x='40' y='20' font-size='10' text-anchor='middle'%3E无图片%3C/text%3E%3C/svg%3E"
    },
  },
  beforeDestroy() {
    clearInterval(this.displayTimer)
    clearTimeout(this.fetchTimer)
  }
}
</script>

<style lang='scss' scoped>
.right_center_wrap {
  position: relative;
  width: 100%;
  height: 440px;
  overflow: hidden;

  .right_center {
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
    list-style: none;

    .right_center_item {
      display: flex;
      align-items: center;
      justify-content: center;
      height: auto;
      padding: 10px;
      font-size: 14px;
      color: #fff;
      opacity: 0;
      transform: translateY(20px);
      transition: all 0.5s ease;

      &.show {
        opacity: 1;
        transform: translateY(0);
      }

      .orderNum {
        margin: 0 20px 0 -20px;
        min-width: 20px;
        text-align: right;
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
            cursor: zoom-in;

            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
              border: 1px solid rgba(0, 114, 255, 0.3);
              border-radius: 2px;
              transition: transform 0.3s;

              &:hover {
                transform: scale(1.05);
              }
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

            .text-row.address-alert {
              display: flex;
              justify-content: space-between;
              margin-bottom: 0;

              .address {
                flex: 1 1 60%;
                padding-right: 6px;
              }

              .alert {
                flex: 1 1 40%;
                padding-left: 6px;
              }
            }
          }
        }
      }
    }
  }

  .loading-tip {
    position: absolute;
    bottom: 10px;
    left: 0;
    right: 0;
    text-align: center;
    color: rgba(255, 255, 255, 0.7);
    font-size: 12px;

    .el-icon-loading {
      margin-right: 5px;
      animation: rotating 2s linear infinite;
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

  .image-preview-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.8);
    z-index: 9999;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: zoom-out;

    .image-preview-container {
      width: 80%;
      max-width: 800px;
      max-height: 80vh;

      .enlarged-image {
        width: 100%;
        height: 100%;
        object-fit: contain;
        border: 2px solid $primary-color;
        box-shadow: 0 0 20px rgba(0, 114, 255, 0.5);
      }
    }
  }
}
</style>
