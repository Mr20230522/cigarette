<template>
  <div class="app-container">
    <!-- 页面顶部居中的标题 -->
    <div class="page-title">
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="24">
          <h1>已录入人员</h1>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索框 -->
    <el-row :gutter="20" class="search-row">
      <el-col :span="6">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable></el-input>
      </el-col>
      <el-col :span="6">
        <el-select v-model="queryParams.gender" placeholder="选择性别">
          <el-option label="男" value="M"></el-option>
          <el-option label="女" value="F"></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="getList">搜索</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
      <el-table-column label="驾驶人ID" align="center" prop="suspectId" />
      <el-table-column label="身份证" align="center" prop="identityCard" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="性别" align="center" prop="gender">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.gender" :value="scope.row.gender" /> <!-- 性别字段 -->
        </template>
      </el-table-column>
      <el-table-column label="电话" align="center" prop="phone" />
      <el-table-column label="驾驶证号" align="center" prop="suspectLicenseNumber" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="出现次数" align="center" prop="occurrenceNumber" />
      <el-table-column label="违法状态" align="center" prop="illegalStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.illegal_status" :value="scope.row.illegalStatus" /> <!-- 违法状态字段 -->
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="涉案数" align="center" prop="caseInvolved" />
      <el-table-column label="车员图片" align="center" prop="picture" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.picture" :width="50" :height="50"/> <!-- 图片预览 -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList" />
  </div>
</template>

<script>
import {
  listPerson, // 引入相应的 API 方法
} from "@/api/cigarette/personnel/person";

export default {
  name: "Person",
  dicts: ['gender', 'illegal_status'], // 相关字典
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      // 人员表格数据
      personList: [],
      title: "",
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        age: null,
        gender: null,
        identityCard: null,
        phone: null,
        address: null,
        // Additional fields for querying
        suspectLicenseNumber: null,
        occurrenceNumber: null,
        illegalStatus: null,
        caseInvolved: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询人员列表 */
    getList() {
      this.loading = true;
      listPerson(this.queryParams).then(response => {
        this.personList = response.rows; // 人员列表
        this.total = response.total;
        this.loading = false;
      });
    },
  }
};
</script>

<style>
.bg-color {
  background-color: #f0f0f0;
}

.page-title h1 {
  font-size: 24px;
  margin: 20px 0;
  color: #333;
}

.selected {
  background-color: #d0e8f2;
  /* 天蓝色背景 */
}

.search-row {
  margin-bottom: 20px;
}
</style>
