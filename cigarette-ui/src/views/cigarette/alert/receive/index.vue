<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="车牌" prop="plate">
        <el-input v-model="queryParams.plate" placeholder="请输入车牌" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="卡口" prop="cameraName">
        <el-input v-model="queryParams.cameraName" placeholder="请输入卡口名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="处理状态" prop="processStatus">
        <el-select v-model="queryParams.processStatus" placeholder="请选择" clearable>
          <el-option label="未处理" value="0" />
          <el-option label="处理中" value="1" />
          <el-option label="已处理" value="2" />
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

    <el-table v-loading="loading" :data="receiveList">
      <el-table-column label="接收编号" align="center" prop="receiveNo" min-width="160px" show-overflow-tooltip />
      <el-table-column label="原始数据ID" align="center" prop="originalId" width="120px" />
      <el-table-column label="车牌" align="center" prop="plate" width="110px" />
      <el-table-column label="卡口名称" align="center" prop="cameraName" min-width="150px" show-overflow-tooltip />
      <el-table-column label="预警原因" align="center" prop="reason" min-width="120px" show-overflow-tooltip />
      <el-table-column label="处理状态" align="center" prop="processStatus" width="100px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.processStatus === '0'" type="warning">未处理</el-tag>
          <el-tag v-else-if="scope.row.processStatus === '1'" type="primary">处理中</el-tag>
          <el-tag v-else-if="scope.row.processStatus === '2'" type="success">已处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="160px" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listReceive } from "@/api/cigarette/alert/receive";

export default {
  name: "AlertReceive",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      receiveList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plate: null,
        cameraName: null,
        processStatus: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listReceive(this.queryParams).then(response => {
        this.receiveList = response.rows;
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
