<template>
    <div class="map-container">
        <div id="container" style="height:800px;width:100%"></div>
    </div>
</template>


<script>

// 导入依赖
import AMapLoader from '@amap/amap-jsapi-loader'          // 用于加载高德地图JS API。
import { mapState } from "vuex"                                // 从Vuex状态管理中导入地图状态。
import GEO_PROVINCE_DATA from './province.json'              // 省份地理数据

const DESTINATION_POINT = [103.796288, 25.490866] // 目标坐标

window._AMapSecurityConfig = {
    securityJsCode: "f8d5ea082b4817f61cec5a6f757ae907",
  };
export default {
    name: "MapLoca",
    data() {
        return {
        isLoading: false,     // 是否正在加载地图数据
          map: null,            // 地图对象
          loca: null,           // 地图对象
          AMap: null ,        // 定义高德地图对象

        }
    },
    mounted() {
        this. loadMap() ;

    

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

      }).then(map => {
          AMap = map
          this.map = new AMap.Map('container', {
              viewMode: '3D',            // 切换视图模式
              zoom: 10,                  // 缩放级别
              pitch: 32,                 // 俯仰角度
              features: ['bg', 'road', 'point',], // 显示建筑物、道路、点标记
              center:  new AMap.LngLat(103.796288, 25.490866),          // 地图中心点
              mapStyle: "amap://styles/c37ade710caaca4fad54ccee74c92ae4", //设置地图的显示样式         
            //   showBuildingBlock: true, // 显示建筑物
            //   showLabel: false, // 不显示地名
          })
        this.searchProvince('云南省'); // 替换为你要显示的省份名称
      }).catch((e) => {
        console.error(e);
      });
      
    },
    searchProvince(provinceName) {
      const districtSearch = new AMap.DistrictSearch({
        level: 'province',
        subdistrict: 0, // 不返回下级行政区
        extensions: 'all', // 返回行政区边界坐标组等信息
      });

      districtSearch.search(provinceName, (status, result) => {
        if (status === 'complete' && result.districtList.length > 0) {
          const bounds = result.districtList[0].boundaries;
          const mask = bounds.map((boundary) => boundary.map(coordinate => new AMap.LngLat(coordinate.lng, coordinate.lat)));
          this.map.setMask(mask);
          console.log("我TM来了");
        }
      });
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
}
</style>
