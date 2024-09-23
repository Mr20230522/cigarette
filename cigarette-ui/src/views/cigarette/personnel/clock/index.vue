<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" stretch="true">
    <el-tab-pane label="值班记录" name="ClockLog">
      <div style="width: 100%;">
        <ClockLog></ClockLog>
      </div>
    </el-tab-pane>
    <el-tab-pane label="值班配置" name="dutySchedule">
      <Schedule></Schedule>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import ClockLog from './ClockLog.vue';
import Schedule from './dutySchedule/index.vue';
export default {
  name: "clock",

  components: {ClockLog,Schedule},
  data() {
    return {
      activeName: 'ClockLog'
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>