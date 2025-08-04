<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def">
    <transition-group name="list" tag="ul" class="right_center">
      <li
        class="right_center_item"
        v-for="(item, i) in visibleList"
        :key="item.id || i"
        :class="{ 'show': item.show }"
        @click="showSuspectedVideo(item.behaviorId)"
      >
        <span class="orderNum">{{ getDisplayIndex(i) }}</span>
        <div class="inner_right">
          <div class="dibu"></div>
          <div class="content-wrapper">
            <!-- 左侧图片 -->
            <div class="image-container" @click.stop="enlargeImage(item.picture)">
              <img
                :src="item.picture"
                @error="handleImgError(item)"
                alt="车辆图片"
                loading="lazy"
              >
            </div>
            <!-- 右侧文字信息 -->
            <div class="text-container">
              <!-- 第一行：车牌号和车辆类型 -->
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
              <!-- 第二行：嫌疑程度和时间 -->
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
                  <span class="contents ciyao" :class="{ warning: item.alertdetail }">
                    {{ item.remark || '无' }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </li>
    </transition-group>

    <!-- 数据加载提示 -->
    <div v-if="loading" class="loading-tip">
      <i class="el-icon-loading"></i>
      数据加载中...
    </div>

    <!-- 图片预览 -->
    <div v-if="enlargedImage" class="image-preview-overlay" @click="enlargedImage = null">
      <div class="image-preview-container">
        <img :src="enlargedImage" class="enlarged-image">
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

export default {
  components: {Reacquire},
  data() {
    return {
      videoData: [],
      pageflag: true,
      allData: [],           // 所有获取的数据
      visibleList: [],       // 当前显示的数据
      currentIndex: 0,       // 当前显示的数据索引
      loading: false,       // 数据加载状态
      enlargedImage: null,   // 放大查看的图片

      // 配置参数
      batchSize: 5,          // 每次加载的条数
      displayInterval: 2000,  // 每条数据显示时间(ms)
      fetchInterval: 4000,   // 数据获取间隔(ms)

      // 定时器
      displayTimer: null,
      fetchTimer: null,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,        // 每次获取10条数据
        degreeSuspicion: 60,
        behaviorId: null
        // 其他查询参数...
      }
    }
  },
  filters: {
    montionFilter(value) {
      // 您的过滤逻辑
      return value
    }
  },
  created() {
    this.initData()
  },
  methods: {
    // 初始化数据
    async initData() {
      this.pageflag = true
      this.allData = []
      this.visibleList = []
      this.currentIndex = 0
      await this.fetchData()
    },
    async showSuspectedVideo(behaviorId) {
      try {
        const videoDataTemp = await this.fetchVideoData(behaviorId)
        if (videoDataTemp && videoDataTemp.videoPath) {
          this.videoData.push({
            id: 1,
            name: videoDataTemp.detectionName,
            url: 'http://127.0.0.1:8000/' + videoDataTemp.videoPath.replace('/profile/', ''),
          })
          ScnEventBus.$emit('suspected-video', {
            mode:1,
            cameras:this.videoData
          })
        }else{
          console.warn('未获取到视频数据');
        }
      } catch {
        console.error('处理视频数据失败:', error);
      }
    },
    async fetchVideoData(behaviorId) {
      try {
        const response = await getVehicleBehaviorVideo(behaviorId);
        console.log('视频数据:', response);
        // 处理视频数据（如赋值给data中的变量）
        // this.videoUrl = response.data.url;
        return response.data
      } catch (error) {
        console.error('获取视频失败:', error);
        return null
      }
    },


    // 获取数据
// 获取数据方法优化
    async fetchData() {
      try {
        this.loading = true;
        const response = await getUpToDataDegreeSuspicion(this.queryParams);
        response.reverse()
        // console.log('response.length',)

        if (response && response.length > 0) {
          this.queryParams.behaviorId = response[response.length - 1].behaviorId;
          const newData = response.map(item => ({
            ...item,
            picture: 'http://127.0.0.1:8000/' + item.picture.replace('/profile/', ''),
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
    // 开始逐条显示数据
    startDisplay() {
      clearInterval(this.displayTimer)

      this.displayTimer = setInterval(() => {
        if (this.currentIndex < this.allData.length) {
          // 显示下一条数据
          const newItem = {
            ...this.allData[this.currentIndex],
            show: true
          }

          // 添加到可见列表
          this.visibleList = [...this.visibleList, newItem]

          // 控制可见列表长度
          if (this.visibleList.length > this.batchSize) {
            this.visibleList = this.visibleList.slice(-this.batchSize)
          }

          this.currentIndex++
        } else {
          // 当前批次数据全部显示完毕
          clearInterval(this.displayTimer)
          this.prepareNextBatch()
        }
      }, this.displayInterval)
    },

    // 准备获取下一批数据
    prepareNextBatch() {
      clearTimeout(this.fetchTimer)
      this.fetchTimer = setTimeout(() => {
        this.fetchData()
      }, this.fetchInterval)
    },

    // 计算显示序号
    getDisplayIndex(i) {
      return this.currentIndex - this.visibleList.length + i + 1
    },

    // 图片放大
    enlargeImage(url) {
      this.enlargedImage = url
    },

    // 图片加载失败处理
    handleImgError(item) {
      item.picture = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='40'%3E%3Crect fill='%23f5f5f5' width='80' height='40'/%3E%3Ctext x='40' y='20' font-size='10' text-anchor='middle'%3E无图片%3C/text%3E%3C/svg%3E"
    },
  }
  ,
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
