<template>
  <!-- 只保留大屏容器 -->
  <div class="cigarette-container">
    <div class="screen-viewport">
      <BigScreen />
    </div>
  </div>
</template>

<script>
import BigScreen from '@/views/cigaretteFront/menu/big-screen/scnHome.vue'

export default {
  name: 'CigaretteIndex',
  components: { BigScreen }
}
</script>

<style scoped>
/* 防御性样式 */
.cigarette-container {
  position: fixed;
  top: 50px;
  left: 0;
  max-height: 1060px;
  overflow: auto;
  margin: 0;
  padding: 0;
  z-index: 9999;
}

</style>
