<template>
  <div class="app-container">
    <!-- 页面顶部居中的标题 -->
    <div class="page-title">
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="24">
          <h1>已录入车辆</h1>
        </el-col>
      </el-row>
    </div>
    <el-table v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange">
      <el-table-column label="车辆编号" align="center" prop="carId" />
      <el-table-column label="车牌编号" align="center" prop="licensePlate" min-width="120px" />
      <el-table-column label="车牌类型" align="center" prop="licensePlateType" min-width="160px">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_licenseplatetype" :value="scope.row.licensePlateType" />
        </template>
      </el-table-column>
      <el-table-column label="车型" align="center" prop="carTypeId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_vehicle_type" :value="scope.row.carTypeId" />
        </template>
      </el-table-column>
      <el-table-column label="车身颜色" align="center" prop="carColor" />
      <el-table-column label="出现次数" align="center" prop="occurrenceNumber" />
      <el-table-column label="涉案数" align="center" prop="casesInvolved" />
      <el-table-column label="车主id" align="center" prop="carOwnerId" />
      <el-table-column label="车主姓名" align="center" prop="name" />
      <el-table-column label="车辆购置日期" align="center" prop="vehiclePurchaseDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.vehiclePurchaseDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="违法状态" align="center" prop="illegalStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_illegal_status" :value="scope.row.illegalStatus" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="车辆图片" align="center" prop="picture" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.picture" :width="50" :height="50"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList" />
  </div>
</template>

<script>
import {
  listVehicle,
} from "@/api/cigarette/vehicle/vehicle";
import {
  listUser,
  getUser
} from "@/api/system/user";
export default {
  name: "Vehicle",
  dicts: ['tob_illegal_status', 'sys_normal_disable', 'tob_licenseplatetype','tob_vehicle_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 车辆表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licensePlate: null,
        licensePlateType: null,
        carTypeId: null,
        carColor: null,
        occurrenceNumber: null,
        casesInvolved: null,
        carOwnerId: null,
        vehiclePurchaseDate: null,
        illegalStatus: null,
        status: null,
        picture: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车辆列表 */
    getList() {
      this.loading = true;
      listVehicle(this.queryParams).then(response => {
        this.vehicleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
};
</script>

<style>
.bg-color {
  background-color: #f0f0f0;
}

.page-title h1 {
  font-size: 24px;
  margin: 20px 0;
  color: #333;
}

.selected {
  background-color: #d0e8f2;
  /* 天蓝色背景 */
}
</style>
