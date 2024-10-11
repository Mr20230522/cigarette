<template>
  <div class="notice-list-container">
    <!-- 通知列表 -->
    <el-table :data="noticeList" @row-click="handleNoticeClick" style="width: 100%">
      <el-table-column prop="noticeTitle" label="公告标题" />
      <el-table-column prop="noticeType" label="公告类型" :formatter="formatNoticeType" width="100" />
      <el-table-column prop="createTime" sortable label="发布时间" width="180" />
    </el-table>

    <!-- 通知详情对话框 -->
    <el-dialog :title="noticeDetail.noticeTitle" :visible.sync="showNoticeDetail" width="50%">
      <div v-html="noticeDetail.noticeContent"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotice, getNotice } from "@/api/system/notice";

export default {
  data() {
    return {
      noticeList: [], // 通知列表
      showNoticeDetail: false, // 是否显示通知详情对话框
      noticeDetail: {} // 通知详情
    };
  },
  created() {
    this.fetchNoticeList();
  },
  methods: {
    fetchNoticeList() {
      listNotice().then(response => {
        this.noticeList = response.rows.filter(notice => notice.status === '0');
      });
    },
    handleNoticeClick(row) {
      getNotice(row.noticeId).then(response => {
        this.noticeDetail = response.data;
        this.showNoticeDetail = true;
      });
    },
    formatNoticeType(row, column, value) {
      const typeMap = {
        '1': '公告',
        '2': '通知',
      };
      return typeMap[value] || '未知类型';
    }
  }
}
</script>

<style scoped>
.notice-list-container {
  padding: 20px;
}
</style>