<template>
    <el-tabs v-model="activeName" @tab-click="handleClick" :stretch="true">
      <el-tab-pane label="关键图片" name="keyPicture">
        <div style="width: 100%;">
          <keyPicture></keyPicture>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关键视频" name="keyVideo">
        <keyVideo></keyVideo>
      </el-tab-pane>
      <el-tab-pane label="片段视频" name="timeVideo">
        <timeVideo></timeVideo>
      </el-tab-pane>
    </el-tabs>
  </template>
  
  <script>
  import keyPicture from './keyPicture/index.vue';
  import keyVideo from './keyVideo/index.vue';
  import timeVideo from './timeVideo/index.vue';
  export default {
    name: "multimediaResource",
  
    components: {keyPicture,keyVideo,timeVideo},
    data() {
      return {
        activeName: 'keyPicture'
      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
  </script>