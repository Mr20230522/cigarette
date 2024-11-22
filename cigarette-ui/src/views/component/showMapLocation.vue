<template>
    <div id="container" v-show="!loading"></div>
  </template>
  <script>
  import AMapLoader from "@amap/amap-jsapi-loader";
  
import { listCamera, getCamera, delCamera, addCamera, updateCamera, listCameraByDistrictId } from "@/api/cigarette/detection/camera";
import cos from "highlight.js/lib/languages/cos";
  export default {
    name: "showMapLocation",
      data() {
        return {
          map: null,
          loading: true,
          paramLongitude: null,
          paramLatitude: null,
          cameraList:[],
          total: 0,
        };
      }, 
      created() {
        this.getList();
     },   
    mounted() {
      this.paramLongitude = this.$route.params.paramLongitude;
      this.paramLatitude = this.$route.params.paramLatitude;
      console.log("this.paramLongitude");
      console.log(this.paramLongitude);
      console.log("this.paramLatitude");
      console.log(this.paramLatitude);
      if(this.paramLongitude){
      this.initAMap();
      }
      this.loading = false;
    },
    unmounted() {
      this.map?.destroy();
    },
    methods: {
      async getList() {
            try {
                listCamera(this.queryParams).then(response => {
                    this.cameraList = response.rows;
                    this.total = response.total;
                });
            } catch (error) {
                console.error('Failed to fetch data:', error);
                this.loading = false;
            }
        },
      initAMap() {
        window._AMapSecurityConfig = {
          securityJsCode: "f8d5ea082b4817f61cec5a6f757ae907",
        };
        AMapLoader.load({
          key: "4cb4e39b657f9849e3b39e14fc41c2b6", // 申请好的Web端开发者Key，首次调用 load 时必填
          version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
          plugins: ["AMap.Scale"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
        })
          .then((AMap) => {
            this.map = new AMap.Map("container", {
              // 设置地图容器id
              viewMode: "3D", // 是否为3D地图模式
              zoom: 11, // 初始化地图级别
              center: new AMap.LngLat(this.paramLongitude,this.paramLatitude,),          // 地图中心点
              // center: [116.397428, 39.90923], // 初始化地图中心点位置
            });
                        
            let img =require("../../assets/images/location.png");
            // 将 Icon 实例添加到 marker 上:
            const marker = new AMap.Marker({
                position: new AMap.LngLat(102.7518916, 25.0659521),
                zIndex: 30,
                icon: new AMap.Icon({
                  size: new AMap.Size(50, 50),    // 图标尺寸
                  image: img,  // Icon的图像路径
                  imageSize: new AMap.Size(50, 50)   // 根据所设置的大小拉伸或压缩图片,
                  }),
                title: '昆明',     // 鼠标滑过点标记显示的标题
                zoom: 13     // 鼠标滑过点标记显示的图标级别
            });
            console.log("到我了");
            this.map.add(marker); 
   
            
          })
          .catch((e) => {
            console.log(e);
          });
      },
    },
  };
  </script>
  <style scoped>
  #container {
    width: 100%;
    height: 800px;
  }
  </style>
  