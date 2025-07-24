<template>
  <div
    v-if="pageflag"
    class="left_boottom_wrap beautify-scroll-def"
    :class="{ 'overflow-y-auto': !sbtxSwiperFlag }"
  >
    <component :is="components" :data="list" :class-option="defaultOption">
      <ul class="left_boottom">
        <li class="left_boottom_item" v-for="(item, i) in list" :key="i">
          <span class="orderNum doudong">{{ i + 1 }}</span>
          <div class="inner_right">
            <div class="dibu"></div>
            <div class="flex">
              <div class="info">
                <span class="labels">设备ID：</span>
                <span class="contents zhuyao doudong wangguan">
                  {{ item.gatewayno }}</span
                >
              </div>
              <div class="info">
                <span class="labels">时间：</span>
                <span class="contents " style="font-size: 12px">
                  {{ item.createTime }}</span
                >
              </div>
            </div>

            <span
              class="types doudong"
              :class="{
                  typeRed: item.onlineState == 0,
                  typeGreen: item.onlineState == 1,
                }"
            >{{ item.onlineState == 1 ? "上线" : "下线" }}</span
            >

            <div class="info addresswrap">
              <span class="labels">地址：</span>
              <span class="contents ciyao" style="font-size: 12px">
                {{ addressHandle(item) }}</span
              >
            </div>
          </div>
        </li>
      </ul>
    </component>
  </div>

  <Reacquire v-else @onclick="getData" style="line-height: 200px" />
</template>

<script>
import vueSeamlessScroll from "vue-seamless-scroll";
import Kong from "@/components/scnKong.vue";
import Reacquire from "@/components/scn-reacquire/reacquire.vue";

export default {
  components: { vueSeamlessScroll, Kong, Reacquire },
  data() {
    return {
      list: [],
      pageflag: true,
      components: vueSeamlessScroll,
      defaultOption: {
        step: 0.5, // 数值越大速度滚动越快
        limitMoveNum: 5, // 开始无缝滚动的数据量
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
      },
      provinces: ['北京市', '上海市', '天津市', '重庆市', '河北省', '山西省', '辽宁省', '吉林省', '黑龙江省', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '海南省', '四川省', '贵州省', '云南省', '陕西省', '甘肃省', '青海省'],
      cities: ['市辖区', '石家庄市', '唐山市', '秦皇岛市', '邯郸市', '邢台市', '保定市', '张家口市', '承德市', '沧州市', '廊坊市', '衡水市'],
      counties: ['东城区', '西城区', '朝阳区', '丰台区', '石景山区', '海淀区', '顺义区', '通州区', '大兴区', '房山区', '门头沟区', '昌平区', '平谷区', '密云区', '延庆区']
    };
  },
  computed: {
    sbtxSwiperFlag() {
      let sbtxSwiper = true; // 默认开启滚动
      if (sbtxSwiper) {
        this.components = vueSeamlessScroll;
      } else {
        this.components = Kong;
      }
      return sbtxSwiper;
    },
  },
  mounted() {
    this.generateMockData();
  },
  methods: {
    // 生成模拟数据
    generateMockData() {
      const mockData = [];
      const status = [0, 1]; // 0:下线, 1:上线

      for (let i = 0; i < 20; i++) {
        const province = this.provinces[Math.floor(Math.random() * this.provinces.length)];
        const city = this.cities[Math.floor(Math.random() * this.cities.length)];
        const county = this.counties[Math.floor(Math.random() * this.counties.length)];
        const onlineState = status[Math.floor(Math.random() * status.length)];

        mockData.push({
          gatewayno: `GW${Math.floor(10000 + Math.random() * 90000)}`, // 5位随机设备ID
          createTime: this.generateRandomTime(),
          onlineState: onlineState,
          provinceName: province,
          cityName: city,
          countyName: county
        });
      }

      this.list = mockData;
      // 模拟数据加载完成后设置滚动
      setTimeout(() => {
        this.defaultOption.step = 0.5;
      }, this.defaultOption.waitTime);
    },

    // 生成随机时间 (最近30天内)
    generateRandomTime() {
      const now = new Date();
      const randomDays = Math.floor(Math.random() * 30);
      const randomHours = Math.floor(Math.random() * 24);
      const randomMinutes = Math.floor(Math.random() * 60);

      const date = new Date(now);
      date.setDate(now.getDate() - randomDays);
      date.setHours(randomHours);
      date.setMinutes(randomMinutes);

      return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    },

    addressHandle(item) {
      let name = item.provinceName;
      if (item.cityName) {
        name += "/" + item.cityName;
        if (item.countyName) {
          name += "/" + item.countyName;
        }
      }
      return name;
    },

    getData() {
      this.pageflag = true;
      this.generateMockData();
    }
  }
};
</script>

<style lang='scss' scoped>
.left_boottom_wrap {
  overflow: hidden;
  width: 100%;
  height: 100%;
}

.doudong {
  overflow: hidden;
  -webkit-backface-visibility: hidden;
  -moz-backface-visibility: hidden;
  -ms-backface-visibility: hidden;
  backface-visibility: hidden;
}

.overflow-y-auto {
  overflow-y: auto;
}

.left_boottom {
  width: 100%;
  height: 100%;

  .left_boottom_item {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 8px;
    font-size: 14px;
    margin: 10px 0;
    .orderNum {
      margin: 0 16px 0 -20px;
    }

    .info {
      margin-right: 10px;
      display: flex;
      align-items: center;
      color: #fff;

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
        color: #e6a23c;
        font-size: 15px;
      }
    }

    .inner_right {
      position: relative;
      height: 100%;
      width: 380px;
      flex-shrink: 0;
      line-height: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      .dibu {
        position: absolute;
        height: 2px;
        width: 104%;
        background-image: url("~@/assets/scn-img/zuo_xuxian.png");
        bottom: -10px;
        left: -2%;
        background-size: cover;
      }
      .addresswrap {
        width: 100%;
        display: flex;
        margin-top: 8px;
      }
    }

    .wangguan {
      color: #1890ff;
      font-weight: 900;
      font-size: 15px;
      width: 80px;
      flex-shrink: 0;
    }

    .time {
      font-size: 12px;
      color: #fff;
    }

    .types {
      width: 30px;
      flex-shrink: 0;
    }

    .typeRed {
      color: #fc1a1a;
    }

    .typeGreen {
      color: #29fc29;
    }
  }
}
</style>
