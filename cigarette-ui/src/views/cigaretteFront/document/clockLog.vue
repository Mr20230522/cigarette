<template>
  <div class="app-container">
    <div class="page-title">
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="24">
          <h1>出勤信息</h1>
        </el-col>
      </el-row>
    </div>
    <el-row :gutter="20">
      <!-- 摄像头数据 -->
      <el-col >
        <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
          <el-table-column label="出勤ID" align="center" prop="commutingId" />
          <el-table-column label="检测人员id" align="center" prop="staffId" />
          <el-table-column label="检测点" align="center" prop="detectionId">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="值班日期" align="center" prop="dutyDate" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dutyDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="startTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="值班类型" align="center" prop="dutyType" />
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="出勤有误" align="center" prop="flag">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_clock_correct" :value="scope.row.flag" />
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改出勤记录对话框 -->
  </div>
</template>

<script>
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/cigarette/personnel/clockLog";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { listUser, } from "@/api/system/user";
import { listStaff } from "@/api/cigarette/personnel/staff"; // 导入工作人员列表接口

export default {
  name: "ClockLog",

  dicts: ['tob_clock_correct'],
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
      // 出勤记录表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData: [],
      // 存储搜索信息
      searchInput: '',
      // 存储用户信息
      userIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredUsers: [],
      // 存储所选用户信息
      selectedUser: null,
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        staffId: null,
        dutyDate: null,
        startTime: null,
        endTime: null,
        dutyType: null,
        flag: null
      },
      // 表单参数
      form: {},
      // 表单校验
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
    this.getUserList();
  },
  mounted() {
    this.filteredUsers = [];
  },
  methods: {
    getUserList() {
      this.loading = true;
      listUser({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.userIdList = response.rows.map(user => {
          return {
            username: user.nickName,
            phonenumber: user.phonenumber,
            email: user.email,
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      })
        .finally(() => {
          this.loading = false;
        });

      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 加载检测点选项
    loadDetectionOptions() {
      listDetection().then(response => {
        this.detectionOptions = response.rows.map(item => ({
          detectionId: item.detectionId,
          districtId: item.districtId,
          detectionName: item.detectionName
        }));
      }).catch(error => {
        console.error("Failed to load detection options:", error);
      });
    },
    // 根据 detectionId 获取监测点名字
    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知监测点';
    },
    async getList() {
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listLog(this.queryParams).then(response => {
          this.logList = response.rows;
          this.total = response.total;
        });

        this.buildTreeData();
        this.loading = false;
      } catch (error) {
        console.error('Failed to fetch data:', error);
        this.loading = false;
      }
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


