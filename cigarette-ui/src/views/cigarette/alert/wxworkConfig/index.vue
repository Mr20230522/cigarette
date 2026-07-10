<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="配置键" prop="configKey">
        <el-input v-model="queryParams.configKey" placeholder="请输入配置键" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="配置名称" prop="configName">
        <el-input v-model="queryParams.configName" placeholder="请输入配置名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
        v-hasPermi="['cigarette:alert:wxworkConfig:add']">新增</el-button>
      <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
        v-hasPermi="['cigarette:alert:wxworkConfig:remove']">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置键" align="center" prop="configKey" min-width="180px" show-overflow-tooltip />
      <el-table-column label="配置名称" align="center" prop="configName" min-width="150px" />
      <el-table-column label="配置值" align="center" prop="configValue" min-width="200px" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.configType === '2'">******</span>
          <span v-else>{{ scope.row.configValue }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="configType" width="80px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.configType === '1'">文本</el-tag>
          <el-tag v-else-if="scope.row.configType === '2'" type="warning">密码</el-tag>
          <el-tag v-else-if="scope.row.configType === '3'" type="info">数字</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="isActive" width="100px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isActive === 1 ? 'success' : 'danger'">
            {{ scope.row.isActive === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" min-width="150px" show-overflow-tooltip />
      <el-table-column label="操作" fixed="right" align="center" width="150px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cigarette:alert:wxworkConfig:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cigarette:alert:wxworkConfig:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="form.configKey" placeholder="如：wxwork.corpid" />
        </el-form-item>
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="form.configName" placeholder="如：企业CorpID" />
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input v-model="form.configValue" :type="form.configType === '2' ? 'password' : 'text'" placeholder="请输入配置值" show-password />
        </el-form-item>
        <el-form-item label="配置类型" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio label="1">文本</el-radio>
            <el-radio label="2">密码</el-radio>
            <el-radio label="3">数字</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否启用" prop="isActive">
          <el-radio-group v-model="form.isActive">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="备注说明" />
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
import { listWxworkConfig, getWxworkConfig, addWxworkConfig, updateWxworkConfig, delWxworkConfig } from "@/api/cigarette/alert/wxworkConfig";

export default {
  name: "WxworkConfig",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      configList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configKey: null,
        configName: null
      },
      form: {},
      rules: {
        configKey: [{ required: true, message: "配置键不能为空", trigger: "blur" }],
        configName: [{ required: true, message: "配置名称不能为空", trigger: "blur" }],
        configValue: [{ required: true, message: "配置值不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listWxworkConfig(this.queryParams).then(response => {
        this.configList = response.rows;
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
      this.title = "新增企微配置";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWxworkConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企微配置";
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delWxworkConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWxworkConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWxworkConfig(this.form).then(response => {
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
      this.form = { configKey: null, configName: null, configValue: null, configType: '1', isActive: 1, remark: null };
      this.resetForm("form");
    }
  }
};
</script>
