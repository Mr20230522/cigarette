<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="检测点id" prop="detectionId">
        <el-input v-model="queryParams.detectionId" placeholder="请输入检测点id" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="值班表id" prop="dutyId">
        <el-input v-model="queryParams.dutyId" placeholder="请输入值班表id" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="工作人员id" prop="staffId">
        <el-input v-model="queryParams.staffId" placeholder="请输入工作人员id" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['detection:inspector:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['detection:inspector:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['detection:inspector:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['detection:inspector:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检测人员id" align="center" prop="inspectorId" />
      <el-table-column label="检测点" align="center" prop="detectionId" >
        <template slot-scope="scope">
          {{ getDetectionName(scope.row.detectionId) }}
        </template>
      </el-table-column>
  
      <el-table-column label="值班表id" align="center" prop="dutyId" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="工作人员" align="center" prop="staffId">
        <template slot-scope="scope">
          {{ getStaffName(scope.row.staffId) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['detection:inspector:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['detection:inspector:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改检测点人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!-- <el-form-item label="检测点id" prop="detectionId">
          <el-input v-model="form.detectionId" placeholder="请输入检测点id" />
        </el-form-item> -->
        <el-form-item label="检测点" prop="detectionId">
          <el-select v-model="form.detectionId" placeholder="请选择所管理的监测点" filterable>
            <el-option v-for="item in detectionOptions" :key="item.detectionId" :label="getDetectionName(item.detectionId)"
              :value="item.detectionId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="值班表id" prop="dutyId">
          <el-input v-model="form.dutyId" placeholder="请输入值班表id" />
        </el-form-item>
        <el-form-item label="删除标记" prop="delFlag">
          <el-select v-model="form.delFlag" placeholder="请选择删除标记">
            <el-option v-for="dict in dict.type.tob_del_flag" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.tob_dd_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 
         <el-form-item label="工作人员id" prop="staffId">
          <el-input v-model="form.staffId" placeholder="请输入工作人员id" />
        </el-form-item> -->
        <!-- 添加下拉框以选择所对应的工作人员 -->
        <el-form-item label="工作人员" prop="staffId">
          <el-select v-model="form.staffId" placeholder="请选择对应的工作人员" filterable>
            <el-option v-for="item in staffOptions" :key="item.staffId" :label="getStaffName(item.staffId)"
              :value="item.staffId"></el-option>
          </el-select>
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
import { listInspector, getInspector, delInspector, addInspector, updateInspector } from "@/api/cigarette/detection/inspector";
import { listStaff } from "@/api/cigarette/personnel/staff"; // 导入工作人员列表接口
import { listUser } from "@/api/system/user"; // 导入用户列表接口
import { listDetection } from "@/api/cigarette/detection/detection"; // 导入检测点列表接口

export default {
  name: "Inspector",
  dicts: ['tob_del_flag','tob_dd_status'],
  staffOptions: [], // 用于存储工作人员选项的数组
  userOptions: [], // 初始化用户选项    
  detectionOptions: [], // 初始化检测点选项
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
      // 检测点人员表格数据
      inspectorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        detectionId: null,
        dutyId: null,
        status: null,
        staffId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        detectionId: [
          { required: true, message: "检测点id不能为空", trigger: "blur" }
        ],
        dutyId: [
          { required: true, message: "值班表id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        delFlag: [
          { required: true, message: "删除标记不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        staffId: [
          { required: true, message: "工作人员id不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.loadStaffOptions(); // 加载工作人员选项
    this.loadUserOptions(); // 加载用户选项
    this.loadDetectionOptions(); // 加载检测点选项
  },
  methods: {
    // 加载检测点选项
    loadDetectionOptions() {
      listDetection().then(response => {
        this.detectionOptions = response.rows.map(item => ({
          detectionId: item.detectionId,
          detectionName: item.detectionName
        }));
      }).catch(error => {
        console.error("Failed to load detection options:", error);
      });
    },
    // 根据 detectionId 获取监测点名字
    getDetectionName(detectionId) {
      const detection=this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知用户';
    },
    //加载用户选项
    loadUserOptions() {
      listUser().then(response => {
        this.userOptions = response.rows.map(item => ({
          userId: item.userId,
          userName: item.userName
        }));
      }).catch(error => {
        console.error("Failed to load user options:", error);
      });
    },
    // 加载工作人员选项
    loadStaffOptions() {
      listStaff().then(response => {
        this.staffOptions = response.rows.map(item => ({
          staffId: item.staffId,
          userId: item.userId
        }));
      }).catch(error => {
        console.error("Failed to load staff options:", error);
      });
    },
    
    getStaffInfo(staffId) {
      return this.staffOptions.find(item => item.staffId === staffId) || {};
    },
    // 根据 staffId 获取用户姓名
    getStaffName(staffId) {
      const staffInfo = this.getStaffInfo(staffId);
      // 确保 staffInfo 中有 userId
      if (staffInfo.userId) {
        // 调用 getUserName 方法获取 userName
        return this.getUserName(staffInfo.userId);
      }
      return '未知工作人员';
    },
    // 通过 userId 获取 userName
    getUserName(userId) {
      // 使用 userOptions 数组来查找 userName
      const user = this.userOptions.find(item => item.userId === userId);
      return user ? user.userName : '未知用户名';
    },
    /** 查询检测点人员列表 */
    getList() {
      this.loading = true;
      listInspector(this.queryParams).then(response => {
        this.inspectorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        inspectorId: null,
        detectionId: null,
        dutyId: null,
        status: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        staffId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.inspectorId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加检测点人员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const inspectorId = row.inspectorId || this.ids
      getInspector(inspectorId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改检测点人员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inspectorId != null) {
            updateInspector(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInspector(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const inspectorIds = row.inspectorId || this.ids;
      this.$modal.confirm('是否确认删除检测点人员编号为"' + inspectorIds + '"的数据项？').then(function () {
        return delInspector(inspectorIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/detection/inspector/export', {
        ...this.queryParams
      }, `inspector_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
