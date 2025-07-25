<template>
  <div class="left_boottom_wrap ">
    <div class="swiper-container">
      <div class="swiper-wrapper">
        <one class="swiper-slide" />
        <two class="swiper-slide" />
      </div>
      <!-- 如果需要分页器 -->
<!--      <div class="swiper-pagination"></div>-->

      <!-- 如果需要导航按钮 -->
<!--      <div class="swiper-button-prev"></div>-->
<!--      <div class="swiper-button-next"></div>-->

<!--       如果需要滚动条 -->
<!--      <div class="swiper-scrollbar"></div>-->
    </div>
  </div>

</template>

<script>
import Swiper from 'swiper'
import "swiper/css/swiper.min.css"
import one from './left-bottom-components/one.vue'
import two from './left-bottom-components/two.vue'

export default {
  components: { one,two },
  data() {

  },

  mounted() {
    new Swiper ('.swiper-container', {
      // direction: 'vertical',
      loop: false,

      // 如果需要分页器
      pagination: '.swiper-pagination',

      // 如果需要前进后退按钮
      nextButton: '.swiper-button-next',
      prevButton: '.swiper-button-prev',

      // 如果需要滚动条
      scrollbar: '.swiper-scrollbar',
    })
  },
};
</script>

<style lang='scss' scoped>
.left_boottom_wrap {
  overflow: hidden;
  width: 100%;
  height: 100%;
}

.swiper-container {
  width: 100%;
  height: 100%;
}
</style>
