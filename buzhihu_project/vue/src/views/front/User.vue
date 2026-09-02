<template>
  <div class="main-content" style="width: 60%">
    <div class="card" style="padding: 20px; display: flex; align-items: center; grid-gap: 20px; margin-bottom: 10px">
      <img :src="info.user?.avatar || defaultAvatar" alt="" style="width: 80px; height: 80px; border-radius: 50%">
      <div style="flex: 1">
        <div style="font-size: 20px; font-weight: bold; display: flex; align-items: center; grid-gap: 12px">
          {{ info.user?.name || info.user?.username }}
          <el-button v-if="canFollow" :type="following ? 'info' : 'primary'" size="mini" plain @click="toggleFollow">{{ following ? '已关注' : '+ 关注' }}</el-button>
        </div>
        <div style="color: #888; margin-top: 5px">{{ info.user?.info || '这个人很懒，什么都没写' }}</div>
      </div>
      <div style="display: flex; grid-gap: 30px; text-align: center">
        <div><div style="font-size: 18px; font-weight: bold">{{ info.blogCount || 0 }}</div><div style="color: #888">博客</div></div>
        <div><div style="font-size: 18px; font-weight: bold">{{ info.followers || 0 }}</div><div style="color: #888">粉丝</div></div>
        <div><div style="font-size: 18px; font-weight: bold">{{ info.following || 0 }}</div><div style="color: #888">关注</div></div>
      </div>
    </div>
    <blog-list :user-id="userId"/>
    <Footer/>
  </div>
</template>

<script>
import Footer from "@/components/Footer";
import BlogList from "@/components/BlogList";

export default {
  name: "User",
  components: {Footer, BlogList},
  data() {
    return {
      userId: Number(this.$route.query.userId),
      info: {},
      following: false,
      myUser: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  computed: {
    canFollow() {
      return this.myUser.id && this.myUser.id !== this.userId
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/user/publicInfo/' + this.userId).then(res => {
        if (res.code === '200') {
          this.info = res.data || {}
          this.following = !!this.info.isFollowing
        } else if (res.code === '5004') {
          this.$message.error('该用户不存在')
        }
      })
    },
    toggleFollow() {
      this.$request.post('/follow/set', {followId: this.userId}).then(res => {
        if (res.code === '200') {
          this.following = !this.following
          this.info.followers = (this.info.followers || 0) + (this.following ? 1 : -1)
          this.$message.success(this.following ? '关注成功' : '已取消关注')
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
