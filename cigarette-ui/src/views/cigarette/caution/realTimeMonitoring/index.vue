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
        <div v-if="selectedRows.length > 0" class="batch-actions">
          <span>已选 {{ selectedRows.length }} 条</span>
          <el-button @click="handleExport" size="mini">导出</el-button>
          <el-button @click="handleDelete" size="mini">删除</el-button>
          <el-button @click="clearSelection" size="mini" type="danger">取消选择</el-button>
        </div>
        <el-table :data="tableData">

          <el-table-column label="选择" width="60">
            <template slot-scope="{row}">
              <el-checkbox v-model="row.isChecked"></el-checkbox>
            </template>
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

.batch-actions {
  margin-bottom: 15px;
  padding: 10px 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.batch-actions span {
  font-size: 14px;
  color: #666;
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
  computed: {
    // 获取所有被选中的行
    selectedRows() {
      return this.tableData.filter(row => row.isChecked);
    },
    // 是否全部选中
    isAllSelected: {
      get() {
        return this.selectedRows.length === this.tableData.length && this.tableData.length > 0;
      },
      set(value) {
        this.tableData.forEach(row => (row.isChecked = value));
      }
    },
    // 半选状态（即部分选中）
    isIndeterminate() {
      return this.selectedRows.length > 0 && !this.isAllSelected;
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
    },
    // 导出方法
    handleExport() {
      console.log('导出的数据为：', this.selectedRows);
      alert(`共导出 ${this.selectedRows.length} 条记录`);
    },

    // 删除方法
    handleDelete() {
      this.tableData = this.tableData.filter(row => !row.isChecked);
    },

    // 清除所有选中
    clearSelection() {
      this.tableData.forEach(row => (row.isChecked = false));
    },

    // 全选切换
    toggleSelectAll(value) {
      this.tableData.forEach(row => (row.isChecked = value));
    }
  }
  ,
  mounted() {
    this.fetchData();
  }
}
;
</script>
