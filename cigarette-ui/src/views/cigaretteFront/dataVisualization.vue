<template>
    <div style="margin: 0;
      padding: 0;
      height: 100%;">
   
      <div id="container" style="position: relative;width: 100%; min-height:100%;"></div>
 
  </div>
  </template>
  
  <script>
  import MapLoader from '@/assets/map/map'
  
  export default {
    name: 'dataVisualization',
    data() {
      return {
        map: '',
      };
    },
    methods: {
      initAMap() {
              let that = this
              MapLoader().then(AMap => {
                  that.map = new AMap.Map('container', {
                      zoom: 11, // 级别
                      center: [116.397428, 39.90923], // 中心点坐标
                      viewMode: '3D' // 使用3D视图
                  })
                  // 异步加载插件
                  // AMap.ToolBar: 滑动工具条， AMap.Scale: 比例尺
                  // 一次加载多个
                  AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], function () {
                      that.map.addControl(new AMap.ToolBar())
                      that.map.addControl(new AMap.Scale())
                  })
                  // AMap.Geolocation: 定位
                  AMap.plugin('AMap.Geolocation', function () {
                      let geolocation = new AMap.Geolocation({
                          enableHighAccuracy: true, //是否使用高精度定位，默认:true
                          timeout: 10000,           //超过10秒后停止定位，默认：无穷大
                          maximumAge: 0,            //定位结果缓存0毫秒，默认：0
                          convert: true,            //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
                          showButton: true,         //显示定位按钮，默认：true
                          buttonPosition: 'LB',     //定位按钮停靠位置，默认：'LB'，左下角
                          buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                          showMarker: true,         //定位成功后在定位到的位置显示点标记，默认：true
                          showCircle: true,         //定位成功后用圆圈表示定位精度范围，默认：true
                          panToLocation: true,      //定位成功后将定位到的位置作为地图中心点，默认：true
                          zoomToAccuracy: true       //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                      })
                      that.map.addControl(geolocation)
  
                      // 获取当前定位的信息
                      geolocation.getCurrentPosition((status, result) => {
                          console.log(result.addressComponent)
                      })
                      // AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
                      // AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
                  })
              })
            }
    },
    computed: {
      currentContent() {
        const tab = this.tabs.find(tab => tab.id === this.currentTab);
        return tab || {};
      }
    },
    mounted() {
        this.initAMap()
    },
  };
  </script>
  
  <style scoped>

  
  /*隐藏高德地图logo，版本号*/
  .amap-logo {
      visibility: hidden;
  }
  
  .amap-copyright {
      visibility: hidden;
  }
  </style>
  