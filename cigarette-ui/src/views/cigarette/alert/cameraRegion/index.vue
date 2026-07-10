<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="卡口ID" prop="cameraId">
        <el-input v-model="queryParams.cameraId" placeholder="请输入卡口ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="地域名称" prop="locationName">
        <el-input v-model="queryParams.locationName" placeholder="请输入地域名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
        v-hasPermi="['cigarette:alert:cameraRegion:add']">新增</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
        v-hasPermi="['cigarette:alert:cameraRegion:remove']">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80px" />
      <el-table-column label="卡口ID" align="center" prop="cameraId" width="100px" />
      <el-table-column label="地域ID" align="center" prop="locationId" width="100px" />
      <el-table-column label="地域名称" align="center" prop="locationName" width="150px" />
      <el-table-column label="是否生效" align="center" prop="isActive" width="100px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isActive === 1 ? 'success' : 'danger'">
            {{ scope.row.isActive === 1 ? '生效' : '失效' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="160px" />
      <el-table-column label="操作" fixed="right" align="center" width="150px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cigarette:alert:cameraRegion:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cigarette:alert:cameraRegion:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="卡口ID" prop="cameraId">
          <el-input-number v-model="form.cameraId" placeholder="请输入卡口ID" :min="1" />
        </el-form-item>
        <el-form-item label="地域ID" prop="locationId">
          <el-input-number v-model="form.locationId" placeholder="请输入地域ID" :min="1" />
        </el-form-item>
        <el-form-item label="地域名称" prop="locationName">
          <el-input v-model="form.locationName" placeholder="请输入地域名称" />
        </el-form-item>
        <el-form-item label="是否生效" prop="isActive">
          <el-radio-group v-model="form.isActive">
            <el-radio :label="1">生效</el-radio>
            <el-radio :label="0">失效</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="群Webhook" prop="groupWebhookUrl">
          <el-input v-model="form.groupWebhookUrl" placeholder="企微群机器人Webhook地址（可选）" />
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
import { listCameraRegion, getCameraRegion, addCameraRegion, updateCameraRegion, delCameraRegion } from "@/api/cigarette/alert/cameraRegion";

export default {
  name: "CameraRegion",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      regionList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cameraId: null,
        locationName: null
      },
      form: {},
      rules: {
        cameraId: [{ required: true, message: "卡口ID不能为空", trigger: "blur" }],
        locationId: [{ required: true, message: "地域ID不能为空", trigger: "blur" }],
        locationName: [{ required: true, message: "地域名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCameraRegion(this.queryParams).then(response => {
        this.regionList = response.rows;
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
      this.title = "新增卡口地域映射";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCameraRegion(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡口地域映射";
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delCameraRegion(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCameraRegion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCameraRegion(this.form).then(response => {
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
      this.form = { cameraId: null, locationId: null, locationName: null, groupWebhookUrl: null, isActive: 1 };
      this.resetForm("form");
    }
  }
};
</script>
