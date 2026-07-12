<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="任务ID" prop="taskId">
        <el-input v-model="queryParams.taskId" placeholder="请输入任务ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作类型" prop="operateType">
        <el-select v-model="queryParams.operateType" placeholder="请选择" clearable>
          <el-option label="接受" :value="1" />
          <el-option label="拒绝" :value="2" />
          <el-option label="推送成功" :value="3" />
          <el-option label="推送失败" :value="4" />
          <el-option label="超时推管理员" :value="5" />
          <el-option label="超时自动关闭" :value="6" />
          <el-option label="提交反馈" :value="7" />
          <el-option label="手动关闭" :value="8" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList">
      <el-table-column label="ID" align="center" prop="id" width="80px" />
      <el-table-column label="任务ID" align="center" prop="taskId" width="100px" />
      <el-table-column label="操作人" align="center" prop="operateUser" width="120px" />
      <el-table-column label="操作类型" align="center" prop="operateType" width="120px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.operateType === 1" type="warning">接受</el-tag>
          <el-tag v-else-if="scope.row.operateType === 2" type="danger">拒绝</el-tag>
          <el-tag v-else-if="scope.row.operateType === 3" type="success">推送成功</el-tag>
          <el-tag v-else-if="scope.row.operateType === 4" type="danger">推送失败</el-tag>
          <el-tag v-else-if="scope.row.operateType === 5" type="info">超时推管理员</el-tag>
          <el-tag v-else-if="scope.row.operateType === 6" type="info">超时自动关闭</el-tag>
          <el-tag v-else-if="scope.row.operateType === 7">提交反馈</el-tag>
          <el-tag v-else-if="scope.row.operateType === 8" type="info">手动关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作内容" align="center" prop="operateContent" min-width="200px" show-overflow-tooltip />
      <el-table-column label="操作时间" align="center" prop="createTime" min-width="160px" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listPushLog } from "@/api/cigarette/alert/pushLog";

export default {
  name: "PushLog",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      logList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        operateType: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listPushLog(this.queryParams).then(response => {
        this.logList = response.rows;
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
    }
  }
};
</script>
