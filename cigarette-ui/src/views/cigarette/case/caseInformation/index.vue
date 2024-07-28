<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="案件代码" prop="caseCode">
        <el-input
          v-model="queryParams.caseCode"
          placeholder="请输入案件代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="案件名称" prop="caseName">
        <el-input
          v-model="queryParams.caseName"
          placeholder="请输入案件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="立案时间" prop="caseStartDate">
        <el-date-picker clearable
          v-model="queryParams.caseStartDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择立案时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结案时间" prop="caseEndDate">
        <el-date-picker clearable
          v-model="queryParams.caseEndDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结案时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="案值" prop="caseValue">
        <el-input
          v-model="queryParams.caseValue"
          placeholder="请输入案值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="案发地点" prop="incidentLocation">
        <el-input
          v-model="queryParams.incidentLocation"
          placeholder="请输入案发地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="违法人员id" prop="illegalId">
        <el-input
          v-model="queryParams.illegalId"
          placeholder="请输入违法人员id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="违法车辆id" prop="behaviorId">
        <el-input
          v-model="queryParams.behaviorId"
          placeholder="请输入违法车辆id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主要案由" prop="mainCharge">
        <el-input
          v-model="queryParams.mainCharge"
          placeholder="请输入主要案由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="次要案由" prop="secondaryCharge">
        <el-input
          v-model="queryParams.secondaryCharge"
          placeholder="请输入次要案由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="录入日期" prop="entryDate">
        <el-date-picker clearable
          v-model="queryParams.entryDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择录入日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="案件人id" prop="caseUserId">
        <el-input
          v-model="queryParams.caseUserId"
          placeholder="请输入案件人id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['case:caseInformation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['case:caseInformation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['case:caseInformation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['case:caseInformation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="caseInformationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="案件ID" align="center" prop="caseId" />
      <el-table-column label="案件代码" align="center" prop="caseCode" />
      <el-table-column label="案件名称" align="center" prop="caseName" />
      <el-table-column label="立案时间" align="center" prop="caseStartDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.caseStartDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结案时间" align="center" prop="caseEndDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.caseEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="案值" align="center" prop="caseValue" />
      <el-table-column label="案发地点" align="center" prop="incidentLocation" />
      <el-table-column label="违法人员id" align="center" prop="illegalId" />
      <el-table-column label="违法车辆id" align="center" prop="behaviorId" />
      <el-table-column label="案件来源ID" align="center" prop="caseSourceId" />
      <el-table-column label="案件类型" align="center" prop="caseType" />
      <el-table-column label="案件状态" align="center" prop="caseStatus" />
      <el-table-column label="主要案由" align="center" prop="mainCharge" />
      <el-table-column label="次要案由" align="center" prop="secondaryCharge" />
      <el-table-column label="案情描述" align="center" prop="mainCaseDetails" />
      <el-table-column label="录入日期" align="center" prop="entryDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="案件人id" align="center" prop="caseUserId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['case:caseInformation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['case:caseInformation:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改案件信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="案件代码" prop="caseCode">
          <el-input v-model="form.caseCode" placeholder="请输入案件代码" />
        </el-form-item>
        <el-form-item label="案件名称" prop="caseName">
          <el-input v-model="form.caseName" placeholder="请输入案件名称" />
        </el-form-item>
        <el-form-item label="立案时间" prop="caseStartDate">
          <el-date-picker clearable
            v-model="form.caseStartDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择立案时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结案时间" prop="caseEndDate">
          <el-date-picker clearable
            v-model="form.caseEndDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结案时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="案值" prop="caseValue">
          <el-input v-model="form.caseValue" placeholder="请输入案值" />
        </el-form-item>
        <el-form-item label="案发地点" prop="incidentLocation">
          <el-input v-model="form.incidentLocation" placeholder="请输入案发地点" />
        </el-form-item>
        <el-form-item label="违法人员id" prop="illegalId">
          <el-input v-model="form.illegalId" placeholder="请输入违法人员id" />
        </el-form-item>
        <el-form-item label="违法车辆id" prop="behaviorId">
          <el-input v-model="form.behaviorId" placeholder="请输入违法车辆id" />
        </el-form-item>
        <el-form-item label="主要案由" prop="mainCharge">
          <el-input v-model="form.mainCharge" placeholder="请输入主要案由" />
        </el-form-item>
        <el-form-item label="次要案由" prop="secondaryCharge">
          <el-input v-model="form.secondaryCharge" placeholder="请输入次要案由" />
        </el-form-item>
        <el-form-item label="案情描述" prop="mainCaseDetails">
          <el-input v-model="form.mainCaseDetails" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="录入日期" prop="entryDate">
          <el-date-picker clearable
            v-model="form.entryDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择录入日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="案件人id" prop="caseUserId">
          <el-input v-model="form.caseUserId" placeholder="请输入案件人id" />
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
import { listCaseInformation, getCaseInformation, delCaseInformation, addCaseInformation, updateCaseInformation } from "@/api/cigarette/case/caseInformation";

export default {
  name: "CaseInformation",
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
      // 案件信息表格数据
      caseInformationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseCode: null,
        caseName: null,
        caseStartDate: null,
        caseEndDate: null,
        caseValue: null,
        incidentLocation: null,
        illegalId: null,
        behaviorId: null,
        caseSourceId: null,
        caseType: null,
        caseStatus: null,
        mainCharge: null,
        secondaryCharge: null,
        mainCaseDetails: null,
        entryDate: null,
        caseUserId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        caseCode: [
          { required: true, message: "案件代码不能为空", trigger: "blur" }
        ],
        caseName: [
          { required: true, message: "案件名称不能为空", trigger: "blur" }
        ],
        caseStartDate: [
          { required: true, message: "立案时间不能为空", trigger: "blur" }
        ],
        caseEndDate: [
          { required: true, message: "结案时间不能为空", trigger: "blur" }
        ],
        caseValue: [
          { required: true, message: "案值不能为空", trigger: "blur" }
        ],
        incidentLocation: [
          { required: true, message: "案发地点不能为空", trigger: "blur" }
        ],
        illegalId: [
          { required: true, message: "违法人员id不能为空", trigger: "blur" }
        ],
        behaviorId: [
          { required: true, message: "违法车辆id不能为空", trigger: "blur" }
        ],
        caseSourceId: [
          { required: true, message: "案件来源ID不能为空", trigger: "change" }
        ],
        caseType: [
          { required: true, message: "案件类型不能为空", trigger: "change" }
        ],
        caseStatus: [
          { required: true, message: "案件状态不能为空", trigger: "change" }
        ],
        mainCharge: [
          { required: true, message: "主要案由不能为空", trigger: "blur" }
        ],
        secondaryCharge: [
          { required: true, message: "次要案由不能为空", trigger: "blur" }
        ],
        mainCaseDetails: [
          { required: true, message: "案情描述不能为空", trigger: "blur" }
        ],
        entryDate: [
          { required: true, message: "录入日期不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询案件信息列表 */
    getList() {
      this.loading = true;
      listCaseInformation(this.queryParams).then(response => {
        this.caseInformationList = response.rows;
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
        caseId: null,
        caseCode: null,
        caseName: null,
        caseStartDate: null,
        caseEndDate: null,
        caseValue: null,
        incidentLocation: null,
        illegalId: null,
        behaviorId: null,
        caseSourceId: null,
        caseType: null,
        caseStatus: null,
        mainCharge: null,
        secondaryCharge: null,
        mainCaseDetails: null,
        entryDate: null,
        delFlag: null,
        remark: null,
        caseUserId: null
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
      this.ids = selection.map(item => item.caseId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加案件信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const caseId = row.caseId || this.ids
      getCaseInformation(caseId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改案件信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.caseId != null) {
            updateCaseInformation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCaseInformation(this.form).then(response => {
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
      const caseIds = row.caseId || this.ids;
      this.$modal.confirm('是否确认删除案件信息编号为"' + caseIds + '"的数据项？').then(function() {
        return delCaseInformation(caseIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/case/caseInformation/export', {
        ...this.queryParams
      }, `caseInformation_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
