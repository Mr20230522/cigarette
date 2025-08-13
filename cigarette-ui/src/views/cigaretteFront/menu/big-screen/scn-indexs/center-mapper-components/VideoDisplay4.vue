<template>
  <div class="video-grid grid-4">
    <div v-for="(camera, index) in cameras" :key="index" class="grid-item">
      <video :src="camera.url" autoplay muted controls></video>
      <div class="camera-label">{{ camera.name }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    cameras: Array
  }
};
</script>

<style scoped>
.grid-4 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 10px;
  height: 100%;
}
</style>
