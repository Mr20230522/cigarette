<template>
  <div class="app-container">
    <div class="page-title">
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="24">
          <h1>执勤信息</h1>
        </el-col>
      </el-row>
    </div>
    <el-row :gutter="20">
      <!-- 摄像头数据 -->
      <el-col >
        <el-table v-loading="loading" :data="dutyLogList" @selection-change="handleSelectionChange">
          <el-table-column label="执勤ID" align="center" prop="dutyId" />
          <el-table-column label="监测人员id" align="center" prop="staffId" />
          <el-table-column label="执勤时间" align="center" prop="dutyTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dutyTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="执勤有误" align="center" prop="flag">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_clock_correct" :value="scope.row.flag" />
            </template>
          </el-table-column>
          <el-table-column label="监测点" align="center" prop="detectionId">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="图片id" align="center" prop="keyPictureId" />
          <el-table-column label="视频id" align="center" prop="videoId" />
          <el-table-column label="行为" align="center" prop="behavior" />
          <el-table-column label="是否工作" align="center" prop="workFlag" />
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120px" >
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['detection:dutyLog:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['detection:dutyLog:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listDutyLog, getDutyLog, delDutyLog, addDutyLog, updateDutyLog } from "@/api/cigarette/detection/dutyLog";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { listUser, } from "@/api/system/user";


export default {
  name: "DutyLog",
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
      // 执勤记录表格数据
      dutyLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 搜索人
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
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        staffId: null,
        dutyTime: null,
        flag: null,
        keyPictureId: null,
        videoId: null,
        behavior: null,
        workFlag: null
      },
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
    this.getUserList();
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
            staffId:user.userId,
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      })
        .finally(() => {
          this.loading = false;
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

        listDutyLog(this.queryParams).then(response => {
          this.dutyLogList = response.rows;
          this.total = response.total;
        });

        this.buildTreeData();
        this.loading = false;
      } catch (error) {
        console.error('Failed to fetch data:', error);
        this.loading = false;
      }
    },
     /** 新增按钮操作 */
     handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加执勤记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const dutyId = row.dutyId || this.ids
      getDutyLog(dutyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改执勤记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.dutyId != null) {
            updateDutyLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDutyLog(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
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

