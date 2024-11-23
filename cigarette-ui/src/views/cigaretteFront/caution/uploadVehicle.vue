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
    <!-- 卡片展示车辆信息 -->
    <el-row gutter="20" type="flex" justify="center" align="top">
      <el-col :span="8" v-for="(vehicle, index) in vehicleList" :key="vehicle.carId">
        <el-card :body-style="{ padding: '0px' }" class="vehicle-card">

          <img :src="vehicle.picture" alt="车辆图片" class="image" />

          <div style="padding: 14px;">
            <div class="card-title">
              <span>{{ vehicle.licensePlate }}</span>
              <span class="vehicle-info">车辆编号：{{ vehicle.carId }}</span>
            </div>
            <div class="vehicle-details">
              <p>车牌类型：<dict-tag :options="dict.type.tob_licenseplatetype" :value="vehicle.licensePlateType" /></p>
              <p>车型：<dict-tag :options="dict.type.tob_vehicle_type" :value="vehicle.carTypeId" /></p>
              <p>车身颜色：{{ vehicle.carColor }}</p>
              <p>出现次数：{{ vehicle.occurrenceNumber }}</p>
              <p>涉案数：{{ vehicle.casesInvolved }}</p>
              <p>车主姓名：{{ vehicle.name }}</p>
              <p>购置日期：<span>{{ parseTime(vehicle.vehiclePurchaseDate, '{y}-{m}-{d}') }}</span></p>
              <p>违法状态：<dict-tag :options="dict.type.tob_illegal_status" :value="vehicle.illegalStatus" /></p>
              <p>状态：<dict-tag :options="dict.type.sys_normal_disable" :value="vehicle.status" /></p>
              <p>备注：{{ vehicle.remark }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {listVehicle} from "@/api/cigarette/vehicle/vehicle";
import {listUser, getUser} from "@/api/system/user";

export default {
  name: "Vehicle",
  dicts: ["tob_illegal_status", "sys_normal_disable", "tob_licenseplatetype", "tob_vehicle_type"],
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
      // 车辆卡片数据
      vehicleList: [],
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
      listVehicle(this.queryParams).then((response) => {
        this.vehicleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    parseTime(time, format) {
      // 时间格式化函数，请根据项目全局引入方式调整
      const date = new Date(time);
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
      };
      return format.replace(/{(y|m|d)}/g, (result, key) => {
        const value = formatObj[key];
        return value.toString().padStart(2, "0");
      });
    },
  },
};
</script>

<style>
.page-title h1 {
  font-size: 24px;
  margin: 20px 0;
  color: #333;
}

.vehicle-card {
  margin-bottom: 20px;
}

.image {
  width: 100%;
  height: auto;
  display: block;
}

.vehicle-info {
  font-size: 14px;
  color: #666;
}

.vehicle-details {
  margin-top: 10px;
}
</style>
