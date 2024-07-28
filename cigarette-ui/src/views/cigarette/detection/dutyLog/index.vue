<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="监测人员id" prop="inspectorId">
        <el-input
          v-model="queryParams.inspectorId"
          placeholder="请输入监测人员id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执勤有误" prop="flag">
        <el-select v-model="queryParams.flag" placeholder="请选择执勤有误" clearable>
          <el-option
            v-for="dict in dict.type.tob_clock_correct"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      
      
      <el-form-item label="行为" prop="behavior">
        <el-input
          v-model="queryParams.behavior"
          placeholder="请输入行为"
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
          v-hasPermi="['detection:dutyLog:add']"
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
          v-hasPermi="['detection:dutyLog:edit']"
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
          v-hasPermi="['detection:dutyLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['detection:dutyLog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dutyLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="执勤ID" align="center" prop="dutyId" />
      <el-table-column label="监测人员id" align="center" prop="inspectorId" />
      <el-table-column label="执勤时间" align="center" prop="dutyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dutyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="执勤有误" align="center" prop="flag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_clock_correct" :value="scope.row.flag"/>
        </template>
      </el-table-column>
      <el-table-column label="图片id" align="center" prop="keyPictureId" />
      <el-table-column label="视频id" align="center" prop="videoId" />
      <el-table-column label="行为" align="center" prop="behavior" />
      <el-table-column label="是否工作" align="center" prop="workFlag" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['detection:dutyLog:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['detection:dutyLog:remove']"
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

    <!-- 添加或修改执勤记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="监测人员id" prop="inspectorId">
          <el-input v-model="form.inspectorId" placeholder="请输入监测人员id" />
        </el-form-item>
        <el-form-item label="执勤时间" prop="dutyTime">
          <el-date-picker clearable
            v-model="form.dutyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择执勤时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="执勤有误" prop="flag">
          <el-select v-model="form.flag" placeholder="请选择执勤有误">
            <el-option
              v-for="dict in dict.type.tob_clock_correct"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片id" prop="keyPictureId">
          <el-input v-model="form.keyPictureId" placeholder="请输入图片id" />
        </el-form-item>
        <el-form-item label="视频id" prop="videoId">
          <el-input v-model="form.videoId" placeholder="请输入视频id" />
        </el-form-item>
        <el-form-item label="行为" prop="behavior">
          <el-input v-model="form.behavior" placeholder="请输入行为" />
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
import { listDutyLog, getDutyLog, delDutyLog, addDutyLog, updateDutyLog } from "@/api/cigarette/detection/dutyLog";

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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectorId: null,
        dutyTime: null,
        flag: null,
        keyPictureId: null,
        videoId: null,
        behavior: null,
        workFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        inspectorId: [
          { required: true, message: "监测人员id不能为空", trigger: "blur" }
        ],
        dutyTime: [
          { required: true, message: "执勤时间不能为空", trigger: "blur" }
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
        flag: [
          { required: true, message: "执勤有误不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询执勤记录列表 */
    getList() {
      this.loading = true;
      listDutyLog(this.queryParams).then(response => {
        this.dutyLogList = response.rows;
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
        dutyId: null,
        inspectorId: null,
        dutyTime: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        flag: null,
        keyPictureId: null,
        videoId: null,
        behavior: null,
        workFlag: null
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
      this.ids = selection.map(item => item.dutyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const dutyIds = row.dutyId || this.ids;
      this.$modal.confirm('是否确认删除执勤记录编号为"' + dutyIds + '"的数据项？').then(function() {
        return delDutyLog(dutyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/detection/dutyLog/export', {
        ...this.queryParams
      }, `dutyLog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
