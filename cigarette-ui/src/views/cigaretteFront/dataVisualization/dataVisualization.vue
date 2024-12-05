<template>
  
  <div class="map-container">
    <div class="top-container">
 
<el-menu
  :default-active="activeIndex2"
  class="el-menu-demo"
  mode="horizontal"
  background-color="#545c64"
  text-color="#fff"
  active-text-color="#ffd04b">
  <el-submenu index="1">
  <template slot="title">{{ districtName }}</template>
  <el-menu-item
    v-for="(item, index) in menuItems"
    :key="index"
    @click="handleSubdistrictSelect(item)" 
    :index="`1-${index + 1}`"
  >
  
    {{ item }}
  </el-menu-item>
</el-submenu>

  <el-menu-item index="4"><a href="https://www.ele.me" target="_blank">返回旧版</a></el-menu-item>
</el-menu>
      
    </div>
    <div id="container" style="height:100%;width:100%"></div>
    <div class="canva1">
      <RadarChart />
    </div>
    <div class="canva2">
      <RadarChart2 />
    </div>
    <div class="canva3">
      <RadarChart3 />
    </div>
    <div class="canva4">
      <RadarChart />
    </div>
    <div class="canva5">
      <RadarChart />
    </div>
    <div class="canva6">
      <RadarChart />
    </div>
    <div class="canva7">
      <RadarChart />
    </div>
    <div class="canva8">
      <RadarChart />
    </div>

  </div>
</template>


<script>

// 导入依赖
import AMapLoader from '@amap/amap-jsapi-loader'          // 用于加载高德地图JS API。
import RadarChart from './echarts/radarChart.vue'
import RadarChart2 from './echarts/radarChart2.vue'
import RadarChart3 from './echarts/radarChart3.vue'

const DESTINATION_POINT = [103.796288, 25.490866] // 目标坐标

window._AMapSecurityConfig = {
  securityJsCode: "f8d5ea082b4817f61cec5a6f757ae907",
};
export default {
  name: "MapLoca",
  components: {
    RadarChart,
    RadarChart2,
    RadarChart3,
  },
  
  data() {
    return {
      isLoading: false,     // 是否正在加载地图数据
      map: null,            // 地图对象
      loca: null,           // 地图对象
      AMap: null,        // 定义高德地图对象
      activeIndex: '1',
      activeIndex2: '1',
      // 经度
      longitude: 104.140288,
      // 纬度
      latitude: 25.520866,
      districtName: '曲靖市',
      selectSubdistrict: 1,
      selectZoom:9.25,
      menuItems: [
        // {
        //   districtName: '曲靖市',
        //   selectSubdistrict: 1,
        //   selectZoom:9.25,
        //   longitude:104.140288, 
        //   latitude:25.520866,
        // },
        '曲靖市',
        '会泽县',
        '马龙区',
        '师宗县',
        '陆良县',
        '麒麟区',
        '宣威市',
        '沾益区',
        '罗平县',
        '富源县'
      ],

    }
  },
  mounted() {
    this.loadMap();
  },

  computed: {

  },
  methods: {
    loadMap() {
      AMapLoader.load({
        key: "4cb4e39b657f9849e3b39e14fc41c2b6", // 开发应用的 ID
        version: "2.0",   // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ['AMap.DistrictSearch', 'AMap.Driving'],          // 需要加载的插件列表
        Loca: {
          version: '2.0.0',             // 指定要加载的 Loca 版本，缺省时默认为 1.3.2
        },
        AMapUI: {             // 是否加载 AMapUI，缺省不加载
          version: '1.1',   // AMapUI 缺省 1.1
          plugins: [],       // 需要加载的 AMapUI ui插件
        },

      }).then(AMap  => {
        this.AMap  = AMap 
        this.map = new AMap.Map('container', {
          viewMode: '3D',            // 切换视图模式
          zoom: this.selectZoom,                  // 缩放级别
          rotation: 83,               // 旋转角度
          pitch: 32,                 // 俯仰角度
          features: ['bg', 'road', 'point',], // 显示建筑物、道路、点标记
          center: new AMap.LngLat(this.longitude,this.latitude),          // 地图中心点
          mapStyle: "amap://styles/c37ade710caaca4fad54ccee74c92ae4", //设置地图的显示样式         
          depth:2,
          //   showBuildingBlock: true, // 显示建筑物
          //   showLabel: false, // 不显示地名
        })

        if (!this.map) {
        console.error('地图实例创建失败');
        return;
      }


        //显示省级地图
        new AMap.DistrictSearch({
          extensions: "all",
          subdistrict: this.selectSubdistrict,
        }).search(this.districtName, (status, result) => {
          if (status === 'complete' && result.info === 'OK') {
            console.log("result");
          // 外多边形坐标数组和内多边形坐标数组
          var outer = [
            new AMap.LngLat(-360, 90, true),
            new AMap.LngLat(-360, -90, true),
            new AMap.LngLat(360, -90, true),
            new AMap.LngLat(360, 90, true),
          ];
          var holes = result.districtList[0].boundaries;
          var pathArray = [outer];
          pathArray.push.apply(pathArray, holes);
          var polygon = new AMap.Polygon({
            pathL: pathArray,
            strokeColor: "#00eeff", //边框线颜色
            strokeWeight: 2,
            fillColor: "#091b2e", //遮罩图层颜色
            fillOpacity: 0.95,
          });
          polygon.setPath(pathArray);
          this.map.add(polygon);
          
          var disProvince =new AMap.DistrictLayer.Province({
        zIndex: 12,
        depth: 2,
        styles: {
          'fill': function (properties) {
          },
          'province-stroke': 'cornflowerblue',
          'city-stroke': 'white', // 中国地级市边界
          'county-stroke': 'rgba(255,255,255,0.5)' // 中国区县边界
        }
      });
      this.map.add(disProvince);
        }
        });
      }).catch((e) => {
        console.error('地图加载失败', e);
      });
    },
    // handleSelect(key, keyPath) {
    //     console.log(key, keyPath);
    //   },
    handleSubdistrictSelect(item) {
      this.districtName = item;
      if(item == '曲靖市'){
        this.selectSubdistrict = 1;
      }else {
        this.selectSubdistrict = 2;
      }
      this.loadMap();
      },
    },
  beforeDestroy() {
    if (this.map) {
      this.map.destroy();
    }
  }


}
</script>

<style lang="scss" scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.top-container{
  position: absolute;
  top: 0%;
  left: 10;
  width: 100%;
  height: 25%;
  z-index: 40;
}
.canva1{
  position: absolute;
  top: 10%;
  left: 1%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva2{
  position: absolute;
  top:40%;
  left: 1%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva3{
  position: absolute;
  top: 70%;
  left: 0;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva4{
  position: absolute;
  top: 10%;
  right: 1%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva5{
  position: absolute;
  top: 40%;
  right: 1%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva6{
  position: absolute;
  top: 70%;
  right: 1%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva7{
  position: absolute;
  top: 70%;
  left: 22%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
.canva8{
  position: absolute;
  top: 70%;
  right:22%;
  width: 20%;
  height: 28%;
  z-index: 40;
}
</style>
