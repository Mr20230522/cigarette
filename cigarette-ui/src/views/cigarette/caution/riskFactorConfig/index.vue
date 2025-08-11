<template>
  <div class="app-container">
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="factorName" label="因子名称" />
      <el-table-column prop="factorValue" label="因子值" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="updatedAt" label="更新时间" />
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="form" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="因子名称" prop="factorName">
          <el-input v-model="formData.factorName" placeholder="请输入因子名称" />
        </el-form-item>
        <el-form-item label="因子值" prop="factorValue">
          <el-input-number v-model="formData.factorValue" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="formData.description" placeholder="请输入描述" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, add, edit, del } from "@/api/cigarette/caution/factorConfig";

export default {
  name: "FactorConfig",
  data() {
    return {
      loading: false,
      tableData: [],
      dialogVisible: false,
      dialogTitle: "",
      isEdit: false,
      formData: {
        factorName: "",
        factorValue: null,
        description: ""
      },
      rules: {
        factorName: [
          { required: true, message: "请输入因子名称", trigger: "blur" }
        ],
        factorValue: [
          { required: true, message: "请输入因子值", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.fetchList();
  },
  methods: {
    async fetchList() {
      this.loading = true;
      const res = await getList();
      this.tableData = res.rows || [];
      this.loading = false;
      console.log("因子配置列表数据:", this.tableData);
    },
    handleAdd() {
      this.isEdit = false;
      this.dialogTitle = "新增因子配置";
      this.formData = { factorName: "", factorValue: null, description: "" };
      this.dialogVisible = true;
    },
    handleEdit(row) {
      this.isEdit = true;
      this.dialogTitle = "编辑因子配置";
      this.formData = { ...row };
      this.dialogVisible = true;
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return;
        if (this.isEdit) {
          await edit(this.formData);
        } else {
          await add(this.formData);
        }
        this.dialogVisible = false;
        this.fetchList();
      });
    },
    async handleDelete(id) {
      this.$confirm("是否确认删除该记录？", "提示", { type: "warning" })
        .then(async () => {
          await del(id);
          this.fetchList();
          this.$message.success("删除成功");
        })
        .catch(() => {});
    }
  }
};
</script>
