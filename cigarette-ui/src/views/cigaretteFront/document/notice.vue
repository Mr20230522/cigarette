<template>
  <div class="notice-list-container">
    <!-- 通知列表 -->
    <el-table :data="noticeList" @row-click="handleNoticeClick" style="width: 100%">
      <el-table-column prop="noticeTitle" label="公告标题" />
      <el-table-column prop="noticeType" label="公告类型" :formatter="formatNoticeType" width="100" />
      <el-table-column prop="createTime" sortable label="发布时间" width="180" />
    </el-table>

    <!-- 通知详情对话框 -->
    <el-dialog
      :title="noticeDetail.noticeTitle"
      :visible.sync="showNoticeDetail"
      width="50%"
      :modal="false"
      class="centered-dialog">
      <!-- 弹窗内容居中 -->
      <div class="notice-content-wrapper">
        <div v-html="noticeDetail.noticeContent" class="notice-content"></div>
      </div>
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

.centered-dialog {
  display: flex; /* 使弹窗使用弹性布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
}

.centered-dialog .el-dialog {
  margin: auto; /*弹自动居中*/
}

.notice-content-wrapper {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  min-height: 200px; /* 设定最小高度，确保垂直居中时有足够的空间 */
  text-align: center; /* 将文本内容水平居中 */
}

.notice-content {
  max-width: 100%; /* 防止内容超出父容器 */
  max-height: 80vh; /* 限制内容的最大高度，确保不会超出弹窗 */
  overflow-y: auto; /* 如果内容超出高度，显示滚动条 */
}
</style>
