<template>
  <div id="container"></div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  name: "map-view",
  dara(){
    return {
      map: null,
    }
  },
  mounted() {
    this.initAMap();
  },
  beforeDestroy() {
    if (this.map) {
      this.map.destroy();
    }
  },
  methods: {
    initAMap() {
      window._AMapSecurityConfig = {
        securityJsCode: "f8d5ea082b4817f61cec5a6f757ae907",
      };
      AMapLoader.load({
        key: "4cb4e39b657f9849e3b39e14fc41c2b6", // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ["AMap.ElasticMarker", "AMap.ToolBar","AMap.Scale", "AMap.HawkEye", "AMap.ControlBar", "AMap.MapType", "AMap.Geolocation", "AMap.AutoComplete"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
      })
        .then((AMap) => {
          this.map = new AMap.Map("container", {
            // 设置地图容器id
            viewMode: "3D", // 是否为3D地图模式
            zoom: 11, // 初始化地图级别
            center: [103.796001, 25.490100], // 初始化地图中心点位置
            layers: [new AMap.TileLayer.Satellite()], //设置图层,可设置成包含一个或多个图层的数组
            mapStyle: "amap://styles/normal", //设置地图的显示样式 dark normal whitesmoke macaron macaron graffiti fresh grey light darkblue blue
          });
          this.addMarker(AMap); // 添加点标记
          this.addMapClickListener(AMap); // 监听地图点击事件
          this.map.addControl(new AMap.Scale()); //添加比例尺
          this.map.addControl(new AMap.MapType()); //添加地图类型切换
          this.map.addControl(new AMap.ElasticMarker()); //添加弹性标记

        })
        .catch((e) => {
          console.log(e);
        });
    },
    addMarker(AMap) {
      const marker = new AMap.Marker({
        position: new AMap.LngLat(116.39, 39.9),
      });
      this.map.add(marker);
    },
    addMapClickListener(AMap) {
      this.map.on("click", (e) => {
        console.log(
          '您在[ ' +
            e.lnglat.getLng() +
            "," +
            e.lnglat.getLat() +
            " ]的位置点击了地图！"
        );
      });
    },
  },
};
</script>

<style scoped>
#container {
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 100%;
}
</style>