<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="车牌号" prop="plate">
        <el-input v-model="queryParams.plate" placeholder="请输入车牌号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
        v-hasPermi="['cigarette:alert:caseHistory:add']">新增</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="caseList">
      <el-table-column label="ID" align="center" prop="id" width="80px" />
      <el-table-column label="车牌号" align="center" prop="hisCarPlate" min-width="120px" />
      <el-table-column label="车型" align="center" prop="hisCarType" min-width="120px" />
      <el-table-column label="品牌" align="center" prop="hisCarBrand" min-width="120px" />
      <el-table-column label="操作" fixed="right" align="center" width="120px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cigarette:alert:caseHistory:edit']">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="hisCarPlate">
          <el-input v-model="form.hisCarPlate" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车型" prop="hisCarType">
          <el-input v-model="form.hisCarType" placeholder="请输入车型" />
        </el-form-item>
        <el-form-item label="品牌" prop="hisCarBrand">
          <el-input v-model="form.hisCarBrand" placeholder="请输入品牌" />
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
import { listCaseHistory, getCaseHistory, addCaseHistory, updateCaseHistory } from "@/api/cigarette/alert/caseHistory";

export default {
  name: "CaseHistory",
  data() {
    return {
      loading: true,
      showSearch: true,
      caseList: [],
      title: "",
      open: false,
      queryParams: {
        plate: null
      },
      form: {},
      rules: {
        hisCarPlate: [{ required: true, message: "车牌号不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCaseHistory(this.queryParams).then(response => {
        this.caseList = response.data || [];
        this.loading = false;
      });
    },
    handleQuery() {
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增历史案件";
    },
    handleUpdate(row) {
      this.reset();
      getCaseHistory(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改历史案件";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCaseHistory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCaseHistory(this.form).then(response => {
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
      this.form = { hisCarPlate: null, hisCarType: null, hisCarBrand: null };
      this.resetForm("form");
    }
  }
};
</script>
