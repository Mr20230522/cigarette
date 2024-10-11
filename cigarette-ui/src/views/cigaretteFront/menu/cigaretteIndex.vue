<template>
  <div>
  <div class="header">

  </div>
  <div class="hero">
    <!-- <h1>腾讯公司</h1> -->
  </div>
  <ul class="tabs">
    <li class="tab highlight" @click="selectTab('intro')">简介</li>
    <li class="tab" @click="selectTab('vision')">愿景及使命</li>
    <li class="tab" @click="selectTab('history')">发展历程</li>

    <li class="tab" @click="selectTab('team')">管理团队</li>

  </ul>
  <div class="content">
    <h2>{{ currentContent.title }}</h2>
    <p v-for="(line, index) in currentContent.description" :key="index">{{ line }}</p>
  </div>
    <FeedBack/>
</div>
</template>

<script>
import FeedBack from "@/components/FeedBack/feedback.vue";

export default {
  name: 'TencentProfile',
  components: {FeedBack},
  data() {
    return {
      tabs: [
        { id: 'intro', title: '简介', description: ['简介'] },
        { id: 'vision', title: '愿景及使命', description: [] },
        { id: 'history', title: '发展历程', description: [] },
        { id: 'business', title: '业务架构', description: [] },
        { id: 'team', title: '管理团队', description: [] },
        { id: 'culture', title: '企业文化', description: [] },
        { id: 'location', title: '办公地点', description: [] }
      ],
      currentTab: 'intro'
    };
  },
  methods: {
    selectTab(tabId) {
      this.currentTab = tabId;
    }
  },
  computed: {
    currentContent() {
      const tab = this.tabs.find(tab => tab.id === this.currentTab);
      return tab || {};
    }
  }
};
</script>

<style scoped>
/* 将原有的CSS样式粘贴到这里 */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  color: #333;
}
.header {
  background-color: #fff;
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.nav {
  display: flex;
  gap: 20px;
}
.hero {
  background-image: url('../../../assets/images/tu.png');
  background-size: cover;
  color: white;
  height: 800px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}
.tabs {
  display: flex;
  justify-content: center;
  background-color: #f2f2f2;
  padding: 10px 0;
  margin: 0;
  list-style: none;
  border-bottom: 2px solid #007bff;
}
.tab {
  margin: 0 15px;
  cursor: pointer;
}
.content {
  padding: 20px;
  background-color: #f9f9f9;
}
.highlight {
  color: #007bff;
}
</style>
