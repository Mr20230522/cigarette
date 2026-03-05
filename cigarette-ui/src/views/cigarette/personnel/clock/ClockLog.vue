<template>
  <div>
    <el-row :gutter="20">
      <!-- 地区数据 -->
      <el-col :span="4" :xs="24">
        <div>
          <el-tree :data="treeData" node-key="nodeKey" default-expand-all :props="defaultProps"
                   @node-click="handleNodeClick">
          </el-tree>
        </div>
      </el-col>
      <!-- 摄像头数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="检测人员id" prop="staffId">
            <el-input v-model="queryParams.staffId" placeholder="请输入检测人员id" clearable
                      @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="值班日期" prop="dutyDate">
            <el-date-picker clearable v-model="queryParams.dutyDate" type="date" value-format="yyyy-MM-dd"
                            placeholder="请选择值班日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker clearable v-model="queryParams.startTime" type="date" value-format="yyyy-MM-dd"
                            placeholder="请选择开始时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker clearable v-model="queryParams.endTime" type="date" value-format="yyyy-MM-dd"
                            placeholder="请选择结束时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="出勤有误" prop="flag">
            <el-input v-model="queryParams.flag" placeholder="请输入出勤有误" clearable @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                       v-hasPermi="['personnel:clockLog:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                       v-hasPermi="['personnel:clockLog:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                       v-hasPermi="['personnel:clockLog:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                       v-hasPermi="['personnel:clockLog:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
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
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                         v-hasPermi="['personnel:clockLog:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         v-hasPermi="['personnel:clockLog:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改出勤记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body style="margin-top: 100px;">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="搜索用户">
          <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入用户名称、邮箱或电话号码" clearable
                    suffix-icon="el-icon-search"></el-input>
          <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: auto;">
            <el-card class="user-list">
              <el-row v-for="(user, index) in filteredUsers" :key="index" class="user-info"
                      :class="{ 'bg-color': index % 2 === 1, 'selected': user === selectedUser }">
                <el-col :span="24">
                  <span @click="selectUser(user)" class="label" style="cursor:pointer;">用户名称:{{ user.username
                    }}&nbsp;&nbsp;电话号码:{{ user.phonenumber }}&nbsp;&nbsp;邮箱:{{ user.email }}
                  </span>
                </el-col>
              </el-row>
            </el-card>
          </el-scrollbar>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="监测人员" prop="leader">
              <el-input :disabled="true" v-model="form.leader" placeholder="请在上方搜索人员" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input :disabled="true" v-model="form.phone" placeholder="请在上方搜索人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检测点" prop="detectionId">
          <el-select v-model="form.detectionId" placeholder="请选择所管理的监测点" filterable
                     @change="handleDetectionChange">
            <el-option v-for="item in detectionOptions" :key="item.detectionId"
                       :label="getDetectionName(item.detectionId)" :value="item.detectionId"></el-option>
          </el-select>
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker clearable v-model="form.startTime" type="date" value-format="yyyy-MM-dd"
                              placeholder="请选择开始时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker clearable v-model="form.endTime" type="date" value-format="yyyy-MM-dd"
                              placeholder="请选择结束时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="值班日期" prop="dutyDate">
              <el-date-picker clearable v-model="form.dutyDate" type="date" value-format="yyyy-MM-dd"
                              placeholder="请选择值班日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出勤有误" prop="flag">
              <el-select v-model="form.flag" placeholder="请输入出勤有误">
                <el-option v-for="dict in dict.type.tob_clock_correct" :key="dict.value" :label="dict.label"
                           :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/cigarette/personnel/clockLog";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { listUser } from "@/api/system/user";
import { listStaff, getStaffByUserId } from "@/api/cigarette/personnel/staff"; // 导入 getStaffByUserId

export default {
  name: "ClockLog",
  dicts: ['tob_clock_correct'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      logList: [],
      title: "",
      open: false,
      searchInput: '',
      userIdList: [],
      filteredUsers: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData: [],
      selectedUser: null,
      districts: [],
      detections: [],
      detectionOptions: [],
      staffList: [], // 添加 staffList

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

      form: {},

      rules: {
        staffId: [
          { required: true, message: "检测人员id不能为空", trigger: "blur" }
        ],
        dutyDate: [
          { required: true, message: "值班日期不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        flag: [
          { required: true, message: "出勤有误不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions();
    this.loadDistrictOptions();
    this.getUserList();
  },
  mounted() {
    this.filteredUsers = [];
  },
  methods: {
    // ==================== 修改：保存 userId ====================
    getUserList() {
      this.loading = true;
      listUser({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 关键：保存 userId，用于后面查 staffId
        this.userIdList = response.rows.map(user => {
          return {
            userId: user.userId,        // 新增：保存 userId
            username: user.nickName,
            phonenumber: user.phonenumber,
            email: user.email,
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      }).finally(() => {
        this.loading = false;
      });

      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    loadDistrictOptions() {
      listDistrict().then(response => {
        this.districtOptions = response.data.map(item => ({
          districtId: item.districtId,
          districtName: item.districtName
        }));
      }).catch(error => {
        console.error("Failed to load district options:", error);
      });
    },

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

    handleDetectionChange(newValue) {
      const selectedDetection = this.detectionOptions.find(item => item.detectionId === newValue);
      if (selectedDetection) {
        this.form.districtId = selectedDetection.districtId;
      }
    },

    getDistrictName(districtId) {
      const district = this.districtOptions.find(item => item.districtId === districtId);
      return district ? district.districtName : '未知地区';
    },

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

    buildTreeData() {
      const districtMap = new Map();

      this.districts.forEach(district => {
        districtMap.set(district.districtId, {
          ...district,
          label: district.districtName,
          nodeKey: `district-${district.districtId}`,
          children: []
        });
      });

      this.detections.forEach(detection => {
        const district = districtMap.get(detection.districtId);
        if (district) {
          district.children.push({
            label: detection.detectionName,
            nodeKey: `detection-${detection.detectionId}`,
            type: 'detection',
            ...detection
          });
        }
      });

      this.treeData = [];
      districtMap.forEach((node, districtId) => {
        if (node.parentId === null || node.parentId === 0) {
          this.treeData.push(node);
        } else {
          const parentNode = districtMap.get(node.parentId);
          if (parentNode) {
            parentNode.children.push(node);
          }
        }
      });
    },

    handleNodeClick(data, node) {
      const detectionIds = [];

      const collectDetectionIds = (node) => {
        if (node.type === 'detection') {
          detectionIds.push(node.detectionId);
        }
        if (node.children && node.children.length) {
          node.children.forEach(child => collectDetectionIds(child));
        }
      };

      collectDetectionIds(data);

      if (detectionIds.length > 1) {
        this.logList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listLog(this.queryParams);
          this.logList.push(...response.rows);
        });
      } else {
        this.queryParams.detectionId = detectionIds[0];
        this.getList();
      }
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.commutingId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        commutingId: null,
        staffId: null,
        dutyDate: null,
        startTime: null,
        endTime: null,
        dutyType: '1',
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        flag: null
      };
      this.resetForm("form");
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加出勤记录";
    },

    handleUpdate(row) {
      this.reset();
      const commutingId = row.commutingId || this.ids
      getLog(commutingId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改出勤记录";
      });
    },

    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.commutingId != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLog(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    handleDelete(row) {
      const commutingIds = row.commutingId || this.ids;
      this.$modal.confirm('是否确认删除出勤记录编号为"' + commutingIds + '"的数据项？').then(function () {
        return delLog(commutingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },

    handleExport() {
      this.download('cigarette/personnel/clockLog/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    },

    filterUsers() {
      const searchInput = this.searchInput.toLowerCase().trim();
      if (!searchInput) {
        this.filteredUsers = [];
        return;
      }
      this.filteredUsers = this.userIdList.filter(user => {
        return (
          user.username.toLowerCase().includes(searchInput) ||
          user.email.toString().includes(searchInput) ||
          user.phonenumber.toString().includes(searchInput)
        );
      }).slice(0, 10);
    },

    // ==================== 修改：异步查询 staffId ====================
    async selectUser(user) {
      this.selectedUser = user;

      try {
        // 关键：根据 userId 查询 staff 信息
        const res = await getStaffByUserId(user.userId);

        if (res.data) {
          // 成功获取 staff 信息，设置 staffId
          this.$set(this.form, "staffId", res.data.staffId);
          this.$set(this.form, "email", user.email);
          this.$set(this.form, "leader", user.username);
          this.$set(this.form, "phone", user.phonenumber);
        } else {
          // 未找到 staff 信息
          this.$modal.msgError("该用户未绑定工作人员信息，无法选择");
          return;
        }
      } catch (error) {
        this.$modal.msgError("查询工作人员信息失败");
        console.error(error);
        return;
      }

      // 清空搜索输入框和搜索结果
      this.searchInput = '';
      this.filteredUsers = [];
    },
  }
};
</script>
