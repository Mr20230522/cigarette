<template>
  <el-container style="height: 690px; border: 1px solid #eee">
    <el-aside width="360px" style="background-color: rgb(238, 241, 246); overflow-y: auto;">
      <!-- 固定部分：图片区域 -->
      <!--      <el-divider></el-divider>-->
      <div class="image-container" style="padding: 0">

        <!-- 头像图片 -->
        <img :src="'/'+this.imgd[0].picUrl" style="max-width: 100%;height: 210px">
        <!-- 分割线 -->

      </div>

      <!-- 可滚动部分：菜单和表格 -->
      <div class="scrollable-content" style="padding: 0">
        <!-- 原始菜单内容保持不变 -->
        <el-menu>

          <!-- 其他导航项省略，保持原样 -->
          <!-- 可复制原来的 el-submenu 内容 -->

          <!-- 表格区域 -->
          <div class="table-container">
            <!-- 使用 el-table 渲染表格 -->
            <el-table :data="imgData" border style="width: 100%" :show-header="false">
              <el-table-column prop="key" label="" width="110"></el-table-column>
              <el-table-column prop="value" label=""></el-table-column>
              <el-table-column prop="degreeofSuspicion" label=""></el-table-column>
            </el-table>
          </div>
        </el-menu>
      </div>
    </el-aside>
    <el-container>
      <el-main style="padding: 0">
        <div v-if="selectedRows.length > 0" class="batch-actions">
          <span>已选 {{ selectedRows.length }} 条</span>
          <el-button @click="handleExport" size="mini">导出</el-button>
          <el-button @click="handleDelete" size="mini">删除</el-button>
          <el-button @click="toggleSelectAll" size="mini">取消选择</el-button>
          <el-button @click="clearSelection" size="mini" type="danger">取消选择</el-button>
        </div>
        <el-table :data="tableData" ref="dataTable" height="663">
          <div class="table-container" ref="tableContainer" :class="{ 'animate-scroll': animate }">

            <el-table-column label="选择" width="50" fixed>
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

.el-table__body-wrapper {
  overflow-y: auto !important;
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

</style>

<script>
import request from "@/utils/request";
import * as XLSX from 'xlsx'

let i = 1290;
const weights = {
  frequency_factor: 0.10,
  LPN_factor: 0.20,
  car_film_factor: 0.05,
  brand_factor: 0.05,
  car_type_factor: 0.05,
  face_factor: 0.30,
  time_factor: 0.10,
  month_factor: 0.10,
  location_factor: 0.05,
};

const riskValues = {
  high: 0.9,
  mid: 0.5,
  low: 0.3,
};

function getRiskLevel(value, category) {
  const levels = {
    car_type: {
      high: ['棚栏货车', '小型客车', 'MPV'],
      mid: ['轻型货车', '物流车'],
      low: ['小轿车', '摩托车'],
    },
    car_film: {
      high: ['深色膜'],
      mid: ['浅色膜'],
      low: ['无车膜'],
    },
    brand: {
      high: ['金杯', '江铃', '五菱', '福特', '依维柯', '福田'],
      mid: ['解放', '长安'],
      low: [],
    },
    time: {
      high: [0, 6], // 00:00 - 06:00
      mid: [18, 24],
      low: [6, 18],
    },
    month: {
      high: [9, 10, 11],
      mid: [8, 12],
      low: [6, 7],
    },
    location: {
      high: ['偏僻农村道路', '高速公路'],
      mid: ['物流集散地'],
      low: ['城市主干道'],
    },
    face: {
      high: true, // 有案底
      mid: false,
      low: null,
    },
  };

  const riskCategory = levels[category];

  if (category === 'time') {
    const hour = parseInt(value.split(':')[0]);
    if (hour >= riskCategory.high[0] && hour < riskCategory.high[1]) return 'high';
    if (hour >= riskCategory.mid[0] && hour < riskCategory.mid[1]) return 'mid';
    return 'low';
  }

  if (category === 'month') {
    if (riskCategory.high.includes(value)) return 'high';
    if (riskCategory.mid.includes(value)) return 'mid';
    return 'low';
  }

  if (category === 'face') {
    return value ? 'high' : 'mid';
  }

  if (Array.isArray(riskCategory.high) && riskCategory.high.includes(value)) return 'high';
  if (Array.isArray(riskCategory.mid) && riskCategory.mid.includes(value)) return 'mid';
  if (Array.isArray(riskCategory.low) && riskCategory.low.includes(value)) return 'low';
  return 'low';
}

// 计算并打印每项风险
function calculateDetailedRisk(data) {
  const results = [];
  let totalRisk = 0;

  const categories = {
    car_type: 'car_type_factor',
    car_film: 'car_film_factor',
    brand: 'brand_factor',
    face: 'face_factor',
    time: 'time_factor',
    month: 'month_factor',
    location: 'location_factor',
  };

  for (let [key, factorKey] of Object.entries(categories)) {
    const level = getRiskLevel(data[key], key);
    const risk = riskValues[level];
    const weight = weights[factorKey];
    const contribution = risk * weight;

    results.push({
      属性: key,
      值: data[key],
      风险等级: level,
      风险值: risk,
      权重: weight,
      贡献值: contribution,
    });

    totalRisk += contribution;
  }

  return { results, totalRisk };
}

// 示例数据
const data = {
  car_type: '小型客车',
  car_film: '深色膜',
  brand: '金杯',
  face: true,
  time: '01:00',
  month: 10,
  location: '高速公路',
};

const { results, totalRisk } = calculateDetailedRisk(data);

console.log('【各项风险详情】：');
results.forEach(item => {
  console.log(
    `${item.属性}（值: ${item.值}） → ${item.风险等级}风险，风险值 ${item.风险值} × 权重 ${item.权重} = 贡献 ${item.贡献值.toFixed(4)}`
  );
});
console.log(`\n【总风险值】：${totalRisk.toFixed(4)}`);

export default {

  data() {
    return {
      imgd: [],
      imgd2: [],
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
          {key: "抓拍时间:", value: this.imgd2.captureTime, degreeofSuspicion: 15},
          {key: "车牌号码:", value: this.imgd2.plate, degreeofSuspicion: 15},
          {key: "车牌颜色:", value: this.imgd2.plateColor, degreeofSuspicion: 15},
          {key: "车牌类型:", value: this.imgd2.plateType, degreeofSuspicion: 15},
          {key: "数据类型:", value: this.imgd2.dataType, degreeofSuspicion: 15},
          {key: "违章类型:", value: this.imgd2.violationType, degreeofSuspicion: 15},
          {key: "方向:", value: this.imgd2.direction, degreeofSuspicion: 15},
          {key: "速度(km/h):", value: this.imgd2.speed, degreeofSuspicion: 15},
          {key: "通道号:", value: this.imgd2.channelId, degreeofSuspicion: 15},
          {key: "车道号:", value: this.imgd2.laneNo, degreeofSuspicion: 15}
        ];
      }
    },
    // 导出方法
    handleExport() {
      // console.log('导出的数据为：', this.selectedRows);
      // alert(`共导出 ${this.selectedRows.length} 条记录`);
      if (!this.selectedRows || this.selectedRows.length === 0) {
        this.$message.warning('请先选择要导出的数据');
        return;
      }

      const exportData = this.selectedRows.map(row => ({
        抓拍时间: row.captureTime,
        车牌号码: row.plateNumber,
        车牌颜色: row.plateColor,
        车牌类型: row.plateType,
        数据类型: row.dataType,
        违章类型: row.violationType,
        方向: row.direction,
        速度: row.speed,
        通道号: row.channelId,
        车道号: row.laneNo
      }));

      const ws = XLSX.utils.json_to_sheet(exportData);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, '监控数据');

      const fileName = `车辆监控数据_${new Date().toISOString().slice(0, 10)}.xlsx`;
      XLSX.writeFile(wb, fileName);

      this.$message.success(`成功导出 ${this.selectedRows.length} 条记录`);
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
        const tableBody = this.$refs.dataTable.$el.querySelector('.el-table__body-wrapper');
        if (tableBody) {
          tableBody.scrollTop = tableBody.scrollHeight;
        }
      });
    }
  },
}
;
</script>
