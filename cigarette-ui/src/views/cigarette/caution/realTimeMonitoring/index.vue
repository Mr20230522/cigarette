<!--<template>-->
<!--  &lt;!&ndash;  <iframe src="https://dg.qjyc.cn/smartzm_web/" width="100%" height="500px" frameborder="0"></iframe>&ndash;&gt;-->
<!--  &lt;!&ndash;  <iframe src="http://localhost:3000/doc/page/login.asp" width="100%" height="500px" frameborder="0"></iframe>&ndash;&gt;-->
<!--  &lt;!&ndash;  <iframe src="http://localhost:3000/doc/page/realdata.asp?version=V4.0.1.82806build240719" width="100%" height="500px"&ndash;&gt;-->
<!--  &lt;!&ndash;          frameborder="0"></iframe>&ndash;&gt;-->
<!--  <template>-->
<template>
  <el-container style="height: 690px; border: 1px solid #eee">
    <el-aside width="360px" style="background-color: rgb(238, 241, 246); overflow-y: auto;">
      <!-- 固定部分：图片区域 -->
      <div class="image-container" style="padding: 0">

        <!-- 头像图片 -->
        <img src="../realTimeMonitoring/imgTest/0_2_20250511_000003248_云D6F798_P1.jpg" alt="头像图片"
             style="max-width: 100%;">
        <!-- 分割线 -->
        <!--        <el-divider style="padding: 0;"></el-divider>-->
      </div>

      <!-- 可滚动部分：菜单和表格 -->
      <div class="scrollable-content">
        <!-- 原始菜单内容保持不变 -->
        <el-menu :default-openeds="['1', '3']">

          <!-- 其他导航项省略，保持原样 -->
          <!-- 可复制原来的 el-submenu 内容 -->

          <!-- 表格区域 -->
          <div class="table-container">
            <!-- 使用 el-table 渲染表格 -->
            <el-table :data="imgData" border style="width: 100%" :show-header="false">
              <el-table-column prop="key" label="" width="130"></el-table-column>
              <el-table-column prop="value" label=""></el-table-column>
            </el-table>
          </div>
        </el-menu>
      </div>
    </el-aside>
    <el-container>
      <el-main>

        <el-table :data="tableData">

          <el-table-column prop="" label="">
            <el-checkbox v-model="isChecked"></el-checkbox>
          </el-table-column>
          <el-table-column type="index" label="序号" width="50">
          </el-table-column>
          <el-table-column prop="captureTime" label="抓拍时间" width="150">
          </el-table-column>
          <el-table-column prop="plateNumber" label="车牌号码" width="120">
          </el-table-column>
          <el-table-column prop="plateColor" label="车牌颜色">
          </el-table-column>
          <el-table-column prop="plateType" label="车牌类型" width="100">
          </el-table-column>
          <el-table-column prop="dataType" label="数据类型">
          </el-table-column>
          <el-table-column prop="violationType" label="违章类型">
          </el-table-column>
          <el-table-column prop="direction" label="方向">
          </el-table-column>
          <el-table-column prop="speed" label="速度(km/h)" width="120">
          </el-table-column>
          <el-table-column prop="channelId" label="通道号">
          </el-table-column>
          <el-table-column prop="laneNumber" label="车道号">
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-container>
</template>
<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>

<script>
import axios from "axios";
import request from "@/utils/request";

export default {

  data() {
    let obj = this.tableData;
    return {
      imgd: [],
      tableData: [],
      imgData: [],
      isChecked: false
    }
  },
  methods: {
    fetchData() {
      request({
        url: '/toVehicleMonitoring/list',
        method: 'get'
      }).then(res => {
        this.tableData = res.data || [];
        this.imgd = res.data[0];

        //console.log('车辆数据列表：');
        //console.log(this.imgd);

        // 动态生成 imgData
        this.generateImgData();

        //alert(this.imgd.captureTime);
      });
    },
    generateImgData() {
      if (this.imgd) {
        this.imgData = [
          {key: "抓拍时间:", value: this.imgd.captureTime},
          {key: "车牌号码:", value: this.imgd.plateNumber},
          {key: "车牌颜色:", value: this.imgd.plateColor},
          {key: "车牌类型:", value: this.imgd.plateType},
          {key: "数据类型:", value: this.imgd.dataType},
          {key: "违章类型:", value: this.imgd.violationType},
          {key: "方向:", value: this.imgd.direction},
          {key: "速度(km/h):", value: this.imgd.speed},
          {key: "通道号:", value: this.imgd.channelId},
          {key: "车道号:", value: this.imgd.laneNumber}
        ];
      }
    }
  },
  mounted() {
    this.fetchData();
  }
};
</script>
