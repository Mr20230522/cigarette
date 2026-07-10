<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="车牌" prop="plate">
        <el-input v-model="queryParams.plate" placeholder="请输入车牌" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待处理" :value="0" />
          <el-option label="已接受" :value="1" />
          <el-option label="处理中(已反馈)" :value="2" />
          <el-option label="已关闭" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
        v-hasPermi="['cigarette:alert:task:add']">新增</el-button>
      <el-button type="warning" plain icon="el-icon-s-promotion" size="mini" :disabled="single" @click="handlePushBatch"
        v-hasPermi="['cigarette:alert:task:push']">推送选中</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
        v-hasPermi="['cigarette:alert:task:remove']">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务编号" align="center" prop="taskNo" min-width="160px" show-overflow-tooltip />
      <el-table-column label="车牌" align="center" prop="plate" min-width="100px" />
      <el-table-column label="卡口名称" align="center" prop="cameraName" min-width="150px" show-overflow-tooltip />
      <el-table-column label="预警原因" align="center" prop="reason" min-width="120px" show-overflow-tooltip />
      <el-table-column label="任务状态" align="center" prop="status" min-width="120px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="danger">待处理</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">已接受</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">处理中(已反馈)</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="success">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="接受人" align="center" prop="acceptUser" min-width="100px" />
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="160px" />
      <el-table-column label="操作" fixed="right" align="center" width="200px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleAccept(scope.row)"
            v-if="scope.row.status === 0" v-hasPermi="['cigarette:alert:task:accept']">接受</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleReject(scope.row)"
            v-if="scope.row.status === 0" v-hasPermi="['cigarette:alert:task:reject']">拒绝</el-button>
          <el-button size="mini" type="text" icon="el-icon-circle-check" @click="handleClose(scope.row)"
            v-if="scope.row.status !== 3" v-hasPermi="['cigarette:alert:task:close']">关闭</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-promotion" @click="handlePush(scope.row)"
            v-if="scope.row.status !== 3" v-hasPermi="['cigarette:alert:task:push']">推送</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cigarette:alert:task:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="关联数据ID" prop="originalId">
          <el-input v-model="form.originalId" placeholder="请输入trafficdata的Id" />
        </el-form-item>
        <el-form-item label="预警原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入预警原因" />
        </el-form-item>
        <el-form-item label="地域ID" prop="locationId">
          <el-input-number v-model="form.locationId" placeholder="请输入地域ID" :min="1" />
        </el-form-item>
        <el-form-item label="车牌" prop="plate">
          <el-input v-model="form.plate" placeholder="请输入车牌" />
        </el-form-item>
        <el-form-item label="卡口名称" prop="cameraName">
          <el-input v-model="form.cameraName" placeholder="请输入卡口名称" />
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
import { listTask, getTask, addTask, delTask, acceptTask, rejectTask, closeTask, pushTask } from "@/api/cigarette/alert/task";

export default {
  name: "AlertTask",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      taskList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        plate: null,
        status: null,
        reason: null
      },
      form: {},
      rules: {
        originalId: [{ required: true, message: "关联数据ID不能为空", trigger: "blur" }],
        reason: [{ required: true, message: "预警原因不能为空", trigger: "blur" }],
        locationId: [{ required: true, message: "地域ID不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增任务";
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delTask(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleAccept(row) {
      this.$modal.confirm('确认接受任务【' + row.taskNo + '】？').then(function() {
        return acceptTask(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已接受");
      }).catch(() => {});
    },
    handleReject(row) {
      this.$modal.confirm('确认拒绝任务【' + row.taskNo + '】？').then(function() {
        return rejectTask(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已拒绝");
      }).catch(() => {});
    },
    handleClose(row) {
      this.$modal.confirm('确认关闭任务【' + row.taskNo + '】？').then(function() {
        return closeTask(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已关闭");
      }).catch(() => {});
    },
    handlePush(row) {
      this.$modal.confirm('确认重新推送任务【' + row.taskNo + '】？将重新推送给第一个绑定人员。').then(function() {
        return pushTask(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已重新推送");
      }).catch(() => {});
    },
    handlePushBatch() {
      const task = this.taskList.find(t => t.id === this.ids[0]);
      const taskNo = task ? task.taskNo : '';
      this.$modal.confirm('确认推送选中任务【' + taskNo + '】？将重新推送给第一个绑定人员。').then(() => {
        return pushTask(this.ids[0]);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已重新推送");
      }).catch(() => {});
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            addTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        originalId: null,
        reason: null,
        locationId: null,
        plate: null,
        cameraName: null
      };
      this.resetForm("form");
    }
  }
};
</script>
