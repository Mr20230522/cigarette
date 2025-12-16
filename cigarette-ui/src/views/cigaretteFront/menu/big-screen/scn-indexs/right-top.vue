 <template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def" @wheel="handleWheel">
    <transition-group name="list" tag="ul" class="right_center">
      <li
        class="right_center_item"
        v-for="(item, i) in visibleList"
        :key="i"
        :class="{ 'show': item.show }"
        @click="handleItemClick(item)"
      >
        <span class="orderNum">{{ }}</span>
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
                  <span class="labels">车牌类型：</span>
                  <span class="contents">{{ item.plateType }}</span>
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
import Reacquire from "@/components/scn-reacquire/reacquire.vue"
import {ScnEventBus} from "@/utils/scn-event-bus";
import {listDetection} from "@/api/cigarette/detection/detection";
import {tenList, byIdGetVideoPath} from "@/api/cigarette/trafficData/trafficData"
import {getByTrafficDataId} from "@/api/cigarette/caution/vehicleFieldScore"
import {getSuspicionLevel} from "@/api/cigarette/caution/suspicion";

export default {
  components: {Reacquire},
  dicts: ['tob_vehicle_type'],
  data() {
    return {
      detectionOptions: [],
      videoData: [],
      vehicleData: null,
      pageflag: true,
      allData: [],          // 所有加载的数据
      visibleList: [],       // 实际显示的数据
      loading: false,
      enlargedImage: null,
      batchSize: 5,         // 每次加载的数据量
      displayInterval: 2000, // 每条数据显示间隔
      fetchInterval: 4000,   // 获取新数据的间隔
      displayTimer: null,
      fetchTimer: null,
      queryParams: {
        level: null,
        Id: null
      },
      vehicleTypeMap: {},
      dictLoaded: false,
      maxItems: 100,        // 最大显示条数
      currentDisplayIndex: 0 // 当前显示到的索引
    }
  },
  filters: {
    montionFilter(value) {
      return value
    }
  },
  created() {
    this.initDictWatch();
    this.initData()

  },
  methods: {
    handleImageError(event) {
      event.target.src = this.getDefaultImage();
    },
    getDefaultImage() {
      return 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="80" height="60"%3E%3Crect fill="%23e0e0e0" width="80" height="60"%3E%3C/rect%3E%3Ctext x="50%" y="50%" font-size="10" text-anchor="middle" dominant-baseline="middle" fill="%23666666"%3E图片加载失败%3C/text%3E%3C/svg%3E';
    },
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
    buildVehicleTypeMap(dictData) {
      this.vehicleTypeMap = dictData.reduce((map, item) => {
        map[item.value] = item.label;
        return map;
      }, {});
    },
    getVehicleTypeName(typeId) {
      if (!typeId) return '未知类型';
      return this.vehicleTypeMap[typeId] || `未知类型(${typeId})`;
    },
    async handleItemClick(item) {
      const response = await getByTrafficDataId(item.id)

      // 先强制通知切换到卡片视图
      ScnEventBus.$emit('switch-view-mode', 'card');


      // 再发送数据
      const formattedData = {
        plate: response.data.plate || '无车牌',
        vehicle_type_score: response.data.vehicleTypeScore || '0',// 使用字典转换
        brand_score: response.data.brandScore || '0',
        sub_brand_score: response.data.subBrandScore || '0',
        plate_risk_score: response.data.plateRiskScore || '0',
        face_score: response.data.faceScore || '0',
        time_score: response.data.timeScore || '0',
        month_score: response.data.monthScore || '0',
        location_score: response.data.locationScore || '0',
        total_score: response.data.totalScore || '0',
        create_time: response.data.createTime || '0',
      };

      // 添加调试日志
      ScnEventBus.$emit('force-show-item', formattedData);
    },
    async initData() {
      const currentLevel = await getSuspicionLevel();
      this.queryParams.level = currentLevel.data
      this.pageflag = true;
      this.allData = [];
      this.visibleList = [];
      this.currentDisplayIndex = 0;
      await this.fetchData();
      await this.loadDetectionOptions();
    },

    async fetchVideoData(id) {
      try {
        const response = await byIdGetVideoPath({id: id});
        return response;
      } catch (error) {
        console.error('获取视频失败:', error);
        return null;
      }
    },
    async fetchData() {
      try {
        this.loading = true;
        const response = await tenList(this.queryParams);
        response.reverse();

        if (response && response.length > 0) {
          this.queryParams.id = response[response.length - 1].id;
          const newData = response.map(item => ({
            ...item,
            picUrl: item.picUrl
              // ? 'http://10.145.16.196:8000/' + item.picUrl
              ? 'http://127.0.0.1:8000/' + item.picUrl
              : this.getDefaultImage(),
            show: false
          }));
          console.log('!!newData.picUrl',newData.picUrl)

          // 添加到数据池
          this.allData = [...this.allData, ...newData];

          // 限制最大数量
          if (this.allData.length > this.maxItems) {
            this.allData = this.allData.slice(-this.maxItems);
          }

          // 启动逐条显示
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
    startDisplay() {
      clearInterval(this.displayTimer);

      this.displayTimer = setInterval(() => {
        if (this.currentDisplayIndex < this.allData.length && !this.isUserScrolling) {
          const newItem = {
            ...this.allData[this.currentDisplayIndex],
            show: true
          };

          // 新数据添加到顶部（从上往下显示）
          this.visibleList.unshift(newItem);

          // 发送数据给B组件 - 确保这是正确的代码位置
          try {
            ScnEventBus.$emit('vehicle-data-received', {
              picUrl: newItem.picUrl,
              level: newItem.level
            });
            console.log('数据已发送到B组件:', { picUrl: newItem.picUrl, level: newItem.level });
          } catch (error) {
            console.error('发送数据到B组件失败:', error);
          }

          // 限制显示数量
          if (this.visibleList.length > this.maxItems) {
            this.visibleList = this.visibleList.slice(0, this.maxItems);
          }

          this.currentDisplayIndex++;

          // 自动滚动到顶部显示新数据
          if (!this.isUserScrolling && this.$refs.scrollContainer) {
            this.$nextTick(() => {
              this.$refs.scrollContainer.scrollTop = 0;
            });
          }
        } else if (this.currentDisplayIndex >= this.allData.length) {
          clearInterval(this.displayTimer);
          this.prepareNextBatch();
        }
      }, this.displayInterval);
    },
    showNextItem() {
      if (this.currentDisplayIndex < this.allData.length) {
        const newItem = {
          ...this.allData[this.currentDisplayIndex],
          show: true
        };

        // 添加到显示列表顶部（新数据在上方）
        this.visibleList.unshift(newItem);

        // 限制显示数量
        if (this.visibleList.length > this.batchSize) {
          this.visibleList = this.visibleList.slice(0, this.batchSize);
        }

        this.currentDisplayIndex++;
      }
    },
    prepareNextBatch() {
      clearTimeout(this.fetchTimer);
      this.fetchTimer = setTimeout(() => {
        this.fetchData();
      }, this.fetchInterval);
    },
    getDisplayIndex(i) {
      return this.allData.length - this.currentDisplayIndex + i + 1;
    },
    enlargeImage(url) {
      this.enlargedImage = url;
    },
    handleImgError(item) {
      item.picUrl = this.getDefaultImage();
    },
    handleWheel(event) {
      const container = this.$refs.scrollContainer;
      if (!container) return;
      // 向下滚动且接近底部时加载更多
      if (event.deltaY > 0 &&
        container.scrollTop + container.clientHeight >= container.scrollHeight - 50) {
        this.loadMoreItems();
      }
    },
    loadMoreItems() {
      // 如果已经显示全部数据则不加载
      if (this.visibleList.length >= this.allData.length) return;

      // 每次加载5条
      const remaining = this.allData.length - this.visibleList.length;
      const loadCount = Math.min(5, remaining);

      for (let i = 0; i < loadCount; i++) {
        const index = this.visibleList.length;
        if (index < this.allData.length) {
          this.visibleList.push({
            ...this.allData[index],
            show: true
          });
        }
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
    }
  },
  beforeDestroy() {
    clearInterval(this.displayTimer);
    clearTimeout(this.fetchTimer);
  }
}
</script>

<style lang='scss' scoped>
.right_center_wrap {
  position: relative;
  width: 100%;
  height: 440px;
  overflow-y: auto; /* 允许垂直滚动 */
  overflow-x: hidden;
  scrollbar-width: none; /* 隐藏滚动条（Firefox） */
  -ms-overflow-style: none; /* 隐藏滚动条（IE 10+） */
  &::-webkit-scrollbar {
    display: none; /* 隐藏滚动条（Chrome、Safari、Opera） */
  }

  .right_center {
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
    list-style: none;
    display: block;
    //flex-direction: column-reverse; /* 新数据从下方插入 */

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

/* 列表动画效果 */
.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter {
  opacity: 0;
  transform: translateY(-30px);
}

.list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

.list-move {
  transition: transform 0.5s;
}
</style>
