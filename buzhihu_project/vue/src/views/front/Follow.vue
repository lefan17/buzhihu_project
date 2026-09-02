<template>
  <div class="main-content">
    <div class="card" style="padding: 15px; margin-bottom: 10px; font-size: 18px; font-weight: bold">关注动态</div>
    <blog-list type="follow"/>
    <Footer/>
  </div>
</template>

<script>
import Footer from "@/components/Footer";
import BlogList from "@/components/BlogList";

export default {
  name: "Follow",
  components: {Footer, BlogList},
  data() {
    return {}
  },
  methods: {}
}
</script>

<style scoped>
</style>
