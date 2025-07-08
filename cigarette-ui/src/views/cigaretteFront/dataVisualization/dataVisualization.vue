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
            :index="`1-${index + 1}`">
            {{ item }}
          </el-menu-item>
        </el-submenu>
        <el-menu-item index="4">
          <a href="https://www.ele.me" target="_blank">返回旧版</a>
        </el-menu-item>
      </el-menu>
    </div>
    <div id="container" style="height:100%;width:100%"></div>
    <ChartComponent :pieData="vehicleTypeInformation" :colorData="vehicleColorData"/>
<!--    <div class="canva1">-->
<!--      <RadarChart />-->
<!--    </div>-->
  </div>
</template>

<script>

import AMapLoader from '@amap/amap-jsapi-loader' // 用于加载高德地图JS API。
import RadarChart from './echarts/radarChart.vue'
import ChartComponent from   "./echarts/ChartComponent.vue";
import {getDistrictId, listDistrict} from "@/api/cigarette/detection/district";
import {listDetection} from "@/api/cigarette/detection/detection";
import {listVehicleBehaviorVo,getVehicleTypeData,getVehicleColorData} from "@/api/cigarette/vehicle/vehicleBehavior";

const DESTINATION_POINT = [103.796288, 25.490866] // 目标坐标

window._AMapSecurityConfig = {
  securityJsCode: "6a4f1e5c3d919254bc27e1ad3fd9c836",
};

export default {
  name: "MapLoca",
  components: {
    RadarChart,
    ChartComponent
  },
  data() {
    return {
      colorNameToHex: {
        '红色': '#c23531',
        '蓝色': '#2f4554',
        '绿色': '#61a0a8',
        '黄色': '#d48265',
        '紫色': '#91c7ae',
        '橙色': '#749f83',
        '青色': '#ca8622',
        // 可以继续添加更多映射
        '黑色': '#000000',
        '白色': '#ffffff',
        '灰色': '#808080',
        '深蓝色':'#1c2932',
        '未知':'#3a572e'
      },
      vehicleColorData: {
        categories: [],
        values: [],
        colors:[]
      },
      vehicleTypeInformation:[],
      districtId:-1,//初始化一个点击地址id
      vehicleBehaviorList:[],
      selectRegion:'',
      isLoading: false, // 是否正在加载地图数据
      map: null, // 地图对象
      AMap: null, // 定义高德地图对象
      activeIndex: '1',
      activeIndex2: '1',
      longitude: 104.140288, // 经度
      latitude: 25.520866, // 纬度
      districtName: '曲靖市',
      selectSubdistrict: 1,
      selectZoom: 9.25,
      queryParams: {
        pageNum: 1,
        pageSize: 1000,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null,
        districtId:null
      },
      menuItems: [
        '曲靖市',
        '会泽县',
        '马龙区',
        '师宗县',
        '陆良县',
        '麒麟区',
        '宣威市',
        '沾益区',
        '罗平县',
        '富源县',
        '芳华镇',
      ],
    };
  },
  mounted() {
    this.loadMap();
  },
  methods: {
    getTownName(address) {
      // 正则表达式匹配镇或街道名称
      const regex = /(?:省|市|自治区|自治州|盟|地区|自治州|市辖区|县|自治县|旗|自治旗|特区|林区|县辖区)\s*([^省|市|自治区|自治州|盟|地区|自治州|市辖区|县|自治县|旗|自治旗|特区|林区|市|区]+?)(?:镇|街道)/;
      const match = address.match(regex);
      return match ? match[1] : '位置信息不明确';
    },
    loadMap() {
      AMapLoader.load({
        key: "2ce2abb23184bebe5a2ac74e274540b9", // 开发应用的 ID
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ['AMap.DistrictSearch', 'AMap.Geocoder', 'AMap.Marker'], // 需要加载的插件列表
        Loca: {
          version: '2.0.0', // 指定要加载的 Loca 版本，缺省时默认为 1.3.2
        },
        AMapUI: { // 是否加载 AMapUI，缺省不加载
          version: '1.1', // AMapUI 缺省 1.1
          plugins: [], // 需要加载的 AMapUI ui插件
        },
      }).then(AMap => {
        this.AMap = AMap;
        this.map = new AMap.Map('container', {
          viewMode: '3D', // 切换视图模式
          zoom: this.selectZoom, // 缩放级别
          rotation: 83, // 旋转角度
          pitch: 32, // 俯仰角度
          features: ['bg', 'road', 'point'], // 显示建筑物、道路、点标记
          center: new AMap.LngLat(this.longitude, this.latitude), // 地图中心点
          mapStyle: "amap://styles/c37ade710caaca4fad54ccee74c92ae4", // 设置地图的显示样式
          depth: 2,
        });

        if (!this.map) {
          console.error('地图实例创建失败');
          return;
        }

        this.map.on('click', this.handleClick); // 添加地图点击事件

        // 显示省级地图
        new AMap.DistrictSearch({
          extensions: "all",
          subdistrict: this.selectSubdistrict,
        }).search(this.districtName, (status, result) => {
          if (status === 'complete' && result.info === 'OK') {
            console.log("result");
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
              strokeColor: "#00eeff", // 边框线颜色
              strokeWeight: 2,
              fillColor: "#091b2e", // 遮罩图层颜色
              fillOpacity: 0.95,
            });
            polygon.setPath(pathArray);
            this.map.add(polygon);

            var disProvince = new AMap.DistrictLayer.Province({
              zIndex: 12,
              depth: 2,
              styles: {
                'fill': function (properties) {},
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
    handleClick(e) {
      const lnglat = e.lnglat;
      this.geocoder = new this.AMap.Geocoder({
        radius: 1000,
        extensions: 'all'
      });

      this.geocoder.getAddress(lnglat, async (status, result) => {
        if (status === 'complete' && result.info === 'OK') {
          let address = result.regeocode.formattedAddress;
          this.selectRegion = this.getTownName(address) + '镇';

          if (this.selectRegion) {
            await this.fetchDistrictId(this.selectRegion);
            await this.getVehicleData();
            await this.getVehicleColorData();
          }

          this.$message({
            message: `当前选择区域：${this.selectRegion}`,
            type: 'success',
            duration: 2000,
            showClose: true
          });
        } else {
          this.$message.error('获取地名信息失败');        }
      });
    },
    async fetchDistrictId(districtName) {
      try {
        this.districtId = -1;
        const response = await getDistrictId(districtName);
        if (response && response.data) {
          this.districtId = response.data;
        }
        console.log('this.districtId', this.districtId);
      } catch (err) {
        this.districtId = -1;
        console.error('API调用失败:', err);
      }
    },
    async getList() {
      try {
        this.queryParams.districtId=this.districtId
        listVehicleBehaviorVo(this.queryParams).then(response => {
          this.vehicleBehaviorList = response.rows;
          this.total = response.total;
          console.log('@@@this.vehicleBehaviorList', this.vehicleBehaviorList);
        });
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    },
    async getVehicleData() {
      this.vehicleTypeInformation=[]
      try {
        this.queryParams.districtId = this.districtId; // 确保属性名正确
        const response = await getVehicleTypeData(this.queryParams);
        // 假设这个函数返回一个Promise
        for (let i = 0; i < response.length; i++) { // 修正for循环
          this.vehicleTypeInformation.push({ // 使用push添加元素
            value: response[i].value,
            name: response[i].name
          });
          // console.log('getVehicleData:', response[i].name, response[i].value);
        }
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    },
    // 在 methods 中修改 getVehicleColorData 方法
    async getVehicleColorData() {
      try {
        const response = await getVehicleColorData(this.queryParams);
        const colors = (response.categories || []).map(name =>
          this.colorNameToHex[name] || '#cccccc'
        );

        this.vehicleColorData = {
          categories: response.categories || [],
          values: response.values || [],
          colors: colors
        };

        console.log('Formatted vehicleColorData:', this.vehicleColorData);
      } catch (error) {
        console.error('Failed to fetch vehicle color data:', error);
        this.vehicleColorData = {
          categories: [],
          values: [],
          colors: []
        };
      }
    },
    handleSubdistrictSelect(item) {
      this.districtName = item;
      if (item === '曲靖市') {
        this.selectSubdistrict = 1;
      } else {
        this.selectSubdistrict = 0;
      }
      this.loadMap();
    },
  },
  beforeDestroy() {
    if (this.map) {
      this.map.destroy();
    }
  }
};
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
//.canva1{
//  position: absolute;
//  top: 10%;
//  left: 1%;
//  width: 20%;
//  height: 28%;
//  z-index: 40;
//}

</style>

