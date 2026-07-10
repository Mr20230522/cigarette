<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="地域ID" prop="locationId">
        <el-input-number v-model="queryParams.locationId" placeholder="地域ID" :min="0" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="企微用户ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="用户姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
        v-hasPermi="['cigarette:alert:regionPerson:add']">新增</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
        v-hasPermi="['cigarette:alert:regionPerson:remove']">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80px" />
      <el-table-column label="地域ID" align="center" prop="locationId" width="100px" />
      <el-table-column label="用户ID" align="center" prop="userId" width="150px" />
      <el-table-column label="用户姓名" align="center" prop="userName" width="120px" />
      <el-table-column label="推送优先级" align="center" prop="sortOrder" width="100px" />
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
            v-hasPermi="['cigarette:alert:regionPerson:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cigarette:alert:regionPerson:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="地域ID" prop="locationId">
          <el-input-number v-model="form.locationId" :min="0" placeholder="0代表管理员，>0代表普通地域" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="企业微信用户ID" />
        </el-form-item>
        <el-form-item label="用户姓名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="推送优先级" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" placeholder="值越小越优先推送" />
        </el-form-item>
        <el-form-item label="是否生效" prop="isActive">
          <el-radio-group v-model="form.isActive">
            <el-radio :label="1">生效</el-radio>
            <el-radio :label="0">失效</el-radio>
          </el-radio-group>
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
import { listRegionPerson, getRegionPerson, addRegionPerson, updateRegionPerson, delRegionPerson } from "@/api/cigarette/alert/regionPerson";

export default {
  name: "RegionPerson",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      personList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        locationId: null,
        userId: null,
        userName: null
      },
      form: {},
      rules: {
        locationId: [{ required: true, message: "地域ID不能为空", trigger: "blur" }],
        userId: [{ required: true, message: "用户ID不能为空", trigger: "blur" }],
        userName: [{ required: true, message: "用户姓名不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listRegionPerson(this.queryParams).then(response => {
        this.personList = response.rows;
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
      this.title = "新增地域人员绑定";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getRegionPerson(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改地域人员绑定";
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delRegionPerson(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRegionPerson(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRegionPerson(this.form).then(response => {
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
      this.form = { locationId: null, userId: null, userName: null, sortOrder: 0, isActive: 1 };
      this.resetForm("form");
    }
  }
};
</script>
