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
        <img :src="'/'+this.imgd[0].picUrl" style="max-width: 100%;">
        <!-- 分割线 -->
        <!--        <el-divider style="padding: 0;"></el-divider>-->
      </div>

      <!-- 可滚动部分：菜单和表格 -->
      <div class="scrollable-content">
        <!-- 原始菜单内容保持不变 -->
        <el-menu>

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
          <el-button @click="toggleSelectAll" size="mini">取消选择</el-button>
          <el-button @click="clearSelection" size="mini" type="danger">取消选择</el-button>
        </div>
        <el-table :data="tableData">
          <div class="table-container" ref="tableContainer" :class="{ 'animate-scroll': animate }">

            <el-table-column label="选择" width="60">
              <template slot-scope="{row}">
                <el-checkbox v-model="row.isChecked"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column type="index" label="序号" width="50">
            </el-table-column>
            <el-table-column prop="captureTime" label="抓拍时间" width="150">
            </el-table-column>
            <el-table-column prop="plate" label="车牌号码" width="120">
            </el-table-column>
            <el-table-column prop="plateColor" label="车牌颜色" width="100">
            </el-table-column>
            <el-table-column prop="plateType" label="车牌类型" width="118">
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
            <el-table-column prop="laneNo" label="车道号">
            </el-table-column>
          </div>
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

.table-container {
  transition: transform 0.5s ease-in-out;
}

.table-container.animate-scroll {
  transform: translateY(-40px);
}

</style>

<script>
import request from "@/utils/request";

let i = 1290;
export default {

  data() {
    return {
      imgd: [],
      imgd2:[],
      tableData: [],
      imgData: [],
      isChecked: false,
      maxDataCount: 14,
      timer: null,
      isFetching: false,
      animate: false
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
    },
    displayedTableData() {
      return this.tableData.slice(-this.maxDataCount); // 只显示最后 maxDataCount 条
    }
  },
  methods: {
    fetchData() {
      request({
        url: `/toVehicleMonitoring/list?lastId=${i++}`,
        method: 'get'
      }).then(res => {
        this.tableData = Array.isArray(res.data) ? res.data : [res.data];
        this.imgd = Array.isArray(res.data) ? res.data : [res.data];
        // 动态生成 imgData
        //console.log(this.tableData)
        // console.log(JSON.parse(JSON.stringify(res.data)))
        this.generateImgData();
      });
    },
    fetchNewData() {
      if (this.isFetching) return;
      this.isFetching = true;
      request({
        url: `/toVehicleMonitoring/list?lastId=${i++}`,
        method: 'get',
        timeout: 30000
      }).then(res => {
        const newData = Array.isArray(res.data) ? res.data : [res.data];
        if (newData.length > 0) {
          this.tableData.push(newData[0]);
          // if (this.tableData.length > this.maxDataCount) {
          //   this.tableData.shift();
          // }
          this.imgd = newData;
          this.imgd2 = newData[0];
          // console.log("数据")
          // console.log(this.imgd[0])
          // console.log("变量")
          // console.log(this.imgd[0].picUrl)
          //alert(this.imgd[0].picUrl)
          //console.log(this.imgd)
          this.generateImgData();
        }
      }).catch(err => {
        console.error('请求失败:', err);
      }).finally(() => {
        this.isFetching = false;
      });
    },
    generateImgData() {
      if (this.imgd) {
        this.imgData = [
          {key: "抓拍时间:", value: this.imgd2.captureTime},
          {key: "车牌号码:", value: this.imgd2.plate},
          {key: "车牌颜色:", value: this.imgd2.plateColor},
          {key: "车牌类型:", value: this.imgd2.plateType},
          {key: "数据类型:", value: this.imgd2.dataType},
          {key: "违章类型:", value: this.imgd2.violationType},
          {key: "方向:", value: this.imgd2.direction},
          {key: "速度(km/h):", value: this.imgd2.speed},
          {key: "通道号:", value: this.imgd2.channelId},
          {key: "车道号:", value: this.imgd2.laneNo}
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
  },
  mounted() {
    this.fetchData(); // 初始加载一次
    this.timer = setInterval(this.fetchNewData, 3000); // 每5秒请求一次
  },
  beforeUnmount() {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
  },
  watch: {
    tableData() {
      this.animate = true;
      this.$nextTick(() => {
        setTimeout(() => {
          this.animate = false;
        }, 500);
      });

      // 自动滚动到底部
      this.$nextTick(() => {
        const container = this.$refs.tableContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    }
  },
}
;
</script>
