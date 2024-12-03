<template>
  <div class="app-container">

    <el-card shadow="never" class="box-card">
      <el-row>
        <el-col :span="16">
          <el-button icon="el-icon-top" size="mini" @click="setCardsPerRow(3)">每行3个</el-button>
          <el-button icon="el-icon-top" size="mini" @click="setCardsPerRow(4)">每行4个</el-button>
          <!-- <el-button type="primary" icon="el-icon-bottom" size="mini" @click="sumGradeDesc()">降序排序</el-button>
          <el-button icon="el-icon-top" size="mini" @click="sumGradeAsc()">升序排序</el-button> -->
        </el-col>
        <el-col :span="8">
          <span>
            <el-input v-model="search" size="mini" placeholder="输入车牌搜索" />
          </span>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never" v-loading="loading" style="margin-top: 20px;">
      <el-row>
        <el-col :span="colSpan"
          v-for="(vehicle, carId) in vehicleList.filter(data => !search || data.licensePlate.toLowerCase().includes(search.toLowerCase()))"
          :key="carId">
          <el-card :body-style="{ padding: '0px' }" style="margin-right: 40px;margin-bottom: 30px;">
            <el-row>
              <el-col style="width: 60%;">
                <image-preview :src="vehicle.picture" class="image" />
              </el-col>
              <el-col style="width: 40%;">
                <div style="padding: 14px;">
                  <el-row style="margin-bottom: 20px;">
                    <span>{{ vehicle.licensePlate }}</span>
                  </el-row>
                  <el-row style="margin-bottom: 20px;">
                    <span style=" font-size: 13px;    color: #999;">{{ vehicle.name }}</span>
                  </el-row>
                  <el-row style="margin-bottom: 20px;">
                    <dict-tag :options="dict.type.tob_licenseplatetype" :value="vehicle.licensePlateType"
                      style=" font-size: 13px;    color: #999" />
                  </el-row>
                  <el-row style="margin-bottom: 20px;">
                    <dict-tag :options="dict.type.tob_vehicle_type" :value="vehicle.carTypeId"
                      style=" font-size: 13px;    color: #999;" />
                  </el-row>
                  <el-row style="margin-bottom: 20px;">
                    <span style=" font-size: 13px;    color: #999; ">{{ vehicle.carColor }}</span>
                  </el-row>
                  <el-row style="margin-bottom: 20px;">
                    <dict-tag :options="dict.type.tob_illegal_status" :value="vehicle.illegalStatus"
                      style=" font-size: 13px;    color: #999" />
                  </el-row>
                </div>

              </el-col>
            </el-row>
            <el-row style="padding: 0 15px 15px 15px;">
              <div class="bottom clearfix">
                <time class="time">{{ parseTime(vehicle.createTime, '{y}-{m}-{d}') }}</time>
                <el-button type="text" class="button" @click="readDetails(vehicle.carId)">详情</el-button>
              </div>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 详细信息 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <el-card :body-style="{ padding: '0px' }" style="margin-right: 40px;margin-bottom: 30px;">
        <el-row>
          <el-col style="width: 60%;">
            <image-preview :src="viewForm.picture" class="image" />
          </el-col>
          <el-col style="width: 40%;">

            <div style="padding: 14px;">

              <el-descriptions :title=viewForm.licensePlate column="2">
                <el-descriptions-item label="车辆编号">{{ viewForm.carId }}</el-descriptions-item>
                <el-descriptions-item label="驾驶员编号">{{ viewForm.carOwnerId }}</el-descriptions-item>

                <el-descriptions-item label="姓名">{{ viewForm.name }}</el-descriptions-item>
                <el-descriptions-item label="电话号码">{{ viewForm.phone }}</el-descriptions-item>
                
                <el-descriptions-item label="车牌类型">
                  <dict-tag :options="dict.type.tob_licenseplatetype" :value="viewForm.licensePlateType" />
                </el-descriptions-item>
                <el-descriptions-item label="车型">
                  <dict-tag :options="dict.type.tob_vehicle_type" :value="viewForm.carTypeId" />
                </el-descriptions-item>
                
                <el-descriptions-item label="车身颜色">{{ viewForm.carColor }}</el-descriptions-item>
                <el-descriptions-item label="状态"><dict-tag :options="dict.type.tob_illegal_status" :value="viewForm.illegalStatus"/>
                </el-descriptions-item>
              
                
                <el-descriptions-item label="出现次数">{{ viewForm.occurrenceNumber }}</el-descriptions-item>
                <el-descriptions-item label="涉案数">{{ viewForm.casesInvolved }}</el-descriptions-item>
                <el-descriptions-item label="车辆购置日期">{{ viewForm.vehiclePurchaseDate }}</el-descriptions-item>
                <el-descriptions-item label="记录时间">{{ parseTime(viewForm.createTime, '{y}-{m}-{d}') }}</el-descriptions-item>
              

               </el-descriptions>
              
            </div>

          </el-col>
        </el-row>
      </el-card>

    </el-dialog>




    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import {
  listVehicle,
  getVehicle
} from "@/api/cigarette/vehicle/vehicle";
export default {
  name: "Vehicle",
  dicts: ['tob_illegal_status', 'sys_normal_disable', 'tob_licenseplatetype', 'tob_vehicle_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 车辆表格数据
      vehicleList: [],
      viewForm: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      cardsPerRow: 3, // 每行显示的卡片数量
      search: '',
    };
  },
  created() {
    this.getList();
  },
  computed: {
    colSpan() {
      // 计算每个卡片占据的栅格数
      return 24 / this.cardsPerRow;
    }
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
    setCardsPerRow(num) {
      this.cardsPerRow = num;
    },
    readDetails(carid) {
      getVehicle(carid).then(response => {
        this.viewForm = response.data;
        this.open = true;
        this.title = "车辆详情";
      });

    }
  }
};
</script>

<style>
.time {
  font-size: 13px;
  color: #999;
  float: left;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;

}

.image {
  width: 100%;

  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
