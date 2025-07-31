<template>
  <div class="video-grid grid-3">
    <div v-for="(camera, index) in cameras" :key="index"
         :class="['grid-item', index === 2 ? 'span-2' : '']">
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
.grid-3 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 10px;
  height: 100%;
}

.span-2 {
  grid-column: span 2;
}
</style>
