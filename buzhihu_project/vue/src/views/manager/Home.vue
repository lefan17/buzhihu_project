<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; flex-wrap: wrap; gap: 10px; margin: 10px 0">
      <div class="card stat-card" v-for="s in stats" :key="s.label">
        <div class="stat-num" :style="{color: s.color}">{{ s.value }}</div>
        <div class="stat-label">{{ s.label }}</div>
      </div>
    </div>

    <div style="display: flex; grid-gap: 10px">
      <div style="flex: 2" class="card">
        <div style="font-size: 18px; font-weight: bold; margin-bottom: 5px">近7日新增</div>
        <svg :viewBox="'0 0 700 240'" style="width: 100%; height: 240px">
          <line v-for="i in 5" :key="'g'+i" x1="55" :y1="20+(i-1)*44" x2="680" :y2="20+(i-1)*44" stroke="#f0f0f0"/>
          <polyline :points="blogPoints" fill="none" stroke="#2a60c9" stroke-width="2"/>
          <polyline :points="userPoints" fill="none" stroke="#67c23a" stroke-width="2"/>
          <circle v-for="(p,i) in trend" :key="'b'+i" :cx="x(i)" :cy="y(p.blogCount)" r="3.5" fill="#2a60c9"/>
          <circle v-for="(p,i) in trend" :key="'u'+i" :cx="x(i)" :cy="y(p.userCount)" r="3.5" fill="#67c23a"/>
          <text v-for="(p,i) in trend" :key="'t'+i" :x="x(i)" y="228" text-anchor="middle" font-size="11" fill="#999">{{ p.date.slice(5) }}</text>
        </svg>
        <div style="display: flex; grid-gap: 20px; font-size: 13px; color: #666">
          <span><span style="display:inline-block;width:12px;height:12px;background:#2a60c9;border-radius:2px;margin-right:5px"></span>博客</span>
          <span><span style="display:inline-block;width:12px;height:12px;background:#67c23a;border-radius:2px;margin-right:5px"></span>用户</span>
        </div>
      </div>

      <div style="flex: 1" class="card">
        <div style="font-size: 18px; font-weight: bold; margin-bottom: 5px">分类文章数</div>
        <svg :viewBox="'0 0 320 ' + (20 + catBlogCount.length * 34)" style="width: 100%">
          <g v-for="(c,i) in catBlogCount" :key="i">
            <text x="8" :y="22+i*34" font-size="12" fill="#666">{{ c.name }}</text>
            <rect :x="100" :y="10+i*34" :width="barWidth(c.cnt)" height="16" rx="3" fill="#409eff"/>
            <text :x="108+barWidth(c.cnt)" :y="23+i*34" font-size="11" fill="#333">{{ c.cnt }}</text>
          </g>
          <text v-if="!catBlogCount.length" x="10" y="30" font-size="12" fill="#999">暂无数据</text>
        </svg>
      </div>
    </div>

    <div style="display: flex; grid-gap: 10px; margin: 10px 0">
      <div style="flex: 1" class="card">
        <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px">报名最多的活动</div>
        <div v-for="(a, i) in topActivitySigns" :key="i" style="display: flex; padding: 8px 0; border-bottom: 1px solid #f5f5f5">
          <span style="width: 60px; color: #666">{{ a.name }}</span>
          <span style="flex: 1; color: #333" class="line1">{{ a.cnt }} 人报名</span>
        </div>
        <div v-if="!topActivitySigns.length" style="color: #999; padding: 10px">暂无数据</div>
      </div>
      <div style="flex: 1" class="card">
        <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px">点赞最多的文章</div>
        <div v-for="(b, i) in topBlogLikes" :key="i" style="padding: 8px 0; border-bottom: 1px solid #f5f5f5">
          <div class="line1" style="color: #333">{{ b.title }}</div>
          <div style="color: #999; font-size: 12px">{{ b.cnt }} 个赞</div>
        </div>
        <div v-if="!topBlogLikes.length" style="color: #999; padding: 10px">暂无数据</div>
      </div>
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 15px; font-size: 20px; font-weight: bold">公告列表</div>
        <div>
          <el-timeline reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <el-popover
                  placement="right"
                  width="200"
                  trigger="hover"
                  :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      statData: {},
      trend: [],
      topActivitySigns: [],
      topBlogLikes: []
    }
  },
  computed: {
    stats() {
      const d = this.statData || {}
      return [
        {label: '用户总数', value: d.userCount || 0, color: '#409eff'},
        {label: '博客总数', value: d.blogCount || 0, color: '#67c23a'},
        {label: '活动总数', value: d.activityCount || 0, color: '#e6a23c'},
        {label: '评论总数', value: d.commentCount || 0, color: '#f56c6c'},
        {label: '活动报名', value: d.signCount || 0, color: '#909399'},
        {label: '点赞总数', value: d.likeCount || 0, color: '#ff6600'},
        {label: '收藏总数', value: d.collectCount || 0, color: '#8e44ad'},
        {label: '待处理举报', value: d.pendingReportCount || 0, color: '#d81e06'}

      ]
    },
    catBlogCount() {
      return this.statData.categoryBlogCount || []
    },
    maxTrend() {
      let m = 10
      this.trend.forEach(p => {
        if (p.blogCount > m) m = p.blogCount
        if (p.userCount > m) m = p.userCount
      })
      return m
    },
    maxCat() {
      let m = 1
      this.catBlogCount.forEach(c => { if (Number(c.cnt) > m) m = Number(c.cnt) })
      return m
    },
    blogPoints() {
      return this.trend.map((p, i) => this.x(i) + ',' + this.y(p.blogCount)).join(' ')
    },
    userPoints() {
      return this.trend.map((p, i) => this.x(i) + ',' + this.y(p.userCount)).join(' ')
    }
  },
  created() {
    this.load()
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
  },
  methods: {
    load() {
      this.$request.get('/dashboard/statistics').then(res => {
        this.statData = res.data || {}
        this.trend = res.data?.trend || []
        this.topActivitySigns = res.data?.topActivitySigns || []
        this.topBlogLikes = res.data?.topBlogLikes || []
      })
    },
    x(i) {
      return 60 + i * 100
    },
    y(v) {
      return 196 - (v / this.maxTrend) * 150
    },
    barWidth(c) {
      return Math.max(2, (Number(c) / this.maxCat) * 180)
    }
  }
}
</script>

<style scoped>
.stat-card {
  width: 120px;
  text-align: center;
  padding: 15px 10px;
}
.stat-num {
  font-size: 26px;
  font-weight: bold;
}
.stat-label {
  color: #888;
  margin-top: 5px;
  font-size: 13px;
}
</style>
