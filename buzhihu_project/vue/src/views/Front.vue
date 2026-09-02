<template>
  <div>
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>公告：{{ top }}</div>
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">欢迎来到不知乎</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
            <el-menu-item index="/front/home">首页</el-menu-item>
            <el-menu-item index="/front/follow">关注</el-menu-item>
            <el-menu-item index="/front/activity">活动中心</el-menu-item>
            <el-menu-item index="/front/person">个人中心</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div>
        <el-input style="width: 260px; margin-right: 10px" placeholder="请输入关键字搜索" v-model="title" clearable></el-input>
        <el-button type="success" @click="goSearch">搜 索</el-button>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-popover placement="bottom" width="380" trigger="click" @show="loadNotification">
            <div slot="reference" style="position: relative; cursor: pointer; margin-right: 18px; display: inline-block; vertical-align: middle">
              <i class="el-icon-bell" style="font-size: 20px; color: #333"></i>
              <span v-if="unread > 0" class="notify-badge">{{ unread > 99 ? '99+' : unread }}</span>
            </div>
            <div>
              <div style="display: flex; justify-content: space-between; align-items: center; padding-bottom: 10px; border-bottom: 1px solid #eee">
                <span style="font-weight: bold">消息通知</span>
                <span style="color: #2a60c9; cursor: pointer" @click="readAll">全部已读</span>
              </div>
              <div style="max-height: 300px; overflow: auto">
                <div v-for="item in notifyList" :key="item.id" class="notify-item" @click="goNotify(item)">
                  <img :src="item.actorAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 32px; height: 32px; border-radius: 50%">
                  <div style="flex: 1; min-width: 0">
                    <div class="line1" :class="{ 'notify-unread': item.isRead === 0 }">{{ item.content }}</div>
                    <div style="color: #999; font-size: 12px">{{ item.time }}</div>
                  </div>
                </div>
                <div v-if="!notifyList.length" style="text-align: center; color: #999; padding: 20px">暂无消息</div>
              </div>
            </div>
          </el-popover>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser"/>
    </div>
  </div>

</template>

<script>

export default {
  name: "FrontLayout",

  data() {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      title: this.$route.query.title,
      notifyList: [],
      unread: 0
    }
  },

  mounted() {
    this.loadNotice()
    if (this.user.id) {
      this.loadUnread()
    }
  },
  methods: {
    goSearch(){
      location.href =('/front/search?title=' + this.title)
    },
    loadUnread() {
      this.$request.get('/notification/unreadCount').then(res => {
        this.unread = res.data || 0
      })
    },
    loadNotification() {
      this.$request.get('/notification/selectPage', {params: {pageNum: 1, pageSize: 10}}).then(res => {
        this.notifyList = res.data?.list || []
        this.loadUnread()
      })
    },
    readAll() {
      this.$request.put('/notification/readAll').then(res => {
        if (res.code === '200') {
          this.unread = 0
          this.notifyList.forEach(item => { item.isRead = 1 })
          this.$message.success('已全部标记为已读')
        }
      })
    },
    goNotify(item) {
      if (item.isRead === 0) {
        this.$request.put('/notification/read/' + item.id)
        item.isRead = 1
        this.loadUnread()
      }
      if (item.fid) {
        if (item.module === '博客') {
          this.$router.push('/front/blogDetail?blogId=' + item.fid)
        } else if (item.module === '活动') {
          this.$router.push('/front/activityDetail?activityId=' + item.fid)
        }
      }
    },
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
@import "@/assets/css/front.css";

.notify-badge {
  position: absolute;
  top: -6px;
  right: -10px;
  min-width: 16px;
  height: 16px;
  line-height: 16px;
  border-radius: 8px;
  background-color: #f56c6c;
  color: #fff;
  font-size: 11px;
  text-align: center;
  padding: 0 4px;
}

.notify-item {
  display: flex;
  grid-gap: 10px;
  padding: 10px 5px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  align-items: flex-start;
}

.notify-item:hover {
  background-color: #f5f7fa;
}

.notify-unread {
  color: #2a60c9;
  font-weight: bold;
}
</style>