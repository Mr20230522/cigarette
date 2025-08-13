<template>
  <div class="factor-trigger">
    <!-- 操作栏 -->
    <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>

    <!-- 表格 -->
    <el-table :data="tableData" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center"/>
      <el-table-column prop="factorId" label="因子ID" align="center"/>
      <el-table-column prop="triggerValue" label="触发值" align="center"/>
      <el-table-column prop="updatedAt" label="更新时间" align="center">
        <template slot-scope="scope">
          {{ formatTime(scope.row.updatedAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-show="total > 0"
      :current-page.sync="queryParams.pageNum"
      :page-size.sync="queryParams.pageSize"
      :total="total"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchList"
      class="custom-pagination"
    />

    <!-- 弹窗 -->
    <el-dialog :title="isEdit ? '编辑触发配置' : '新增触发配置'" :visible.sync="dialogVisible" width="500px">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="因子ID" prop="factorId">
          <el-input-number v-model="formData.factorId" :min="1" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="触发值" prop="triggerValue">
          <el-input v-model="formData.triggerValue" style="width: 100%"/>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getList, add, edit, del} from "@/api/cigarette/caution/factorTrigger";
import {parseTime} from "@/utils/ruoyi"; // RuoYi 时间工具

export default {
  name: "FactorTrigger",
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      dialogVisible: false,
      isEdit: false,
      formData: {},
      rules: {
        factorId: [{required: true, message: "请输入因子ID", trigger: "blur"}],
        triggerValue: [{required: true, message: "请输入触发值", trigger: "blur"}]
      }
    };
  },
  created() {
    this.fetchList();
  },
  methods: {
    async fetchList() {
      this.loading = true;
      const res = await getList(this.queryParams);
      this.tableData = res.rows || [];
      this.total = res.total || 0;
      this.loading = false;
    },
    handleAdd() {
      this.isEdit = false;
      this.formData = {
        factorId: null,
        triggerValue: ""
      };
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.resetFields();
      });
    },
    handleEdit(row) {
      this.isEdit = true;
      this.formData = {...row};
      this.dialogVisible = true;
    },
    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
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
      this.$confirm("确认删除该记录吗？", "提示", {type: "warning"})
        .then(async () => {
          await del(id);
          this.fetchList();
        })
        .catch(() => {
        });
    },
    formatTime(val) {
      return val ? parseTime(val) : "-";
    }
  }
};
</script>

<style>
.factor-trigger {
  padding: 20px;
}

.custom-pagination {
  display: flex;
  justify-content: flex-end; /* 右对齐分页 */
  align-items: center;
  padding: 10px 0;
  font-family: 'Arial', sans-serif;
}

.custom-pagination .el-pager li.active {
  background-color: #409EFF; /* 高亮页码 */
  color: #fff;
  border-radius: 4px;
}

.custom-pagination .el-pager li:hover {
  background-color: #66b1ff;
  color: #fff;
}

.custom-pagination .el-pager li {
  border-radius: 4px;
}

.custom-pagination .el-pagination__total {
  font-weight: 500;
  margin-right: 20px;
}

.custom-pagination .el-pagination__jump {
  margin-left: 10px;
}
</style>
