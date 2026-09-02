<template>
  <!--  评论开始  -->
  <div class="card">
    <h2 style="margin-bottom: 20px">评论 {{ commentCount }}</h2>

    <div style="margin-bottom: 20px">
      <el-input type="textarea" placeholder="请输入评论内容" v-model="commentContent"></el-input>
      <div style="text-align: right; margin-top: 5px">
        <el-button type="primary" @click="addComment">评 论</el-button>
      </div>
    </div>

    <div>
      <div style="display: flex; grid-gap: 20px; margin-bottom: 20px" v-for="item in commentList" :key="item.id">
        <img :src="item.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
        <div style="flex: 1">
          <!--                这是第一级评论-->
          <div style="margin-bottom: 10px">
            <div style="color: #666; margin-bottom: 10px">{{ item.userName }}</div>
            <div style="color: #444; margin-bottom: 10px">{{ item.content }}</div>
            <div style="color: #888; font-size: 13px; margin-bottom: 10px"><span style="margin-right: 20px">{{
                item.time
              }}</span>
              <span style="cursor: pointer;" :class="{ 'comment-active' : item.showReplyInput }"
                    @click="handleShowReplyInput(item)"><i class="el-icon-s-comment"></i>评论</span>
              <span style="margin-left: 20px; cursor: pointer" @click="del(item.id)" v-if="item.userId === user.id"><i
                  class="el-icon-delete"></i>删除</span>
              <span style="margin-left: 20px; cursor: pointer" @click="openReport(item)">举报</span>
            </div>
            <div v-if="item.showReplyInput">
              <el-input type="textarea" placeholder="请输入回复内容" v-model="item.replyContent"></el-input>
              <div style="text-align: right; margin-top: 5px">
                <el-button type="primary" @click="addReplay(item)">回 复</el-button>
              </div>
            </div>
          </div>
          <!--                这是回复-->
          <div style="display: flex;  grid-gap: 20px; margin-bottom: 20px" v-for="sub in item.children" :key="sub.id">
            <img :src="sub.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
            <div style="flex: 1">
              <div style="color: #666; margin-bottom: 10px">{{ sub.userName }} <span style="color: #333"
                                                                                     v-if="sub.replyUser !== item.userName">回复  {{
                  sub.replyUser
                }}</span></div>
              <div style="color: #444; margin-bottom: 10px">{{ sub.content }}</div>
              <div style="color: #888; font-size: 13px; margin-bottom: 10px"><span
                  style="margin-right: 20px">{{ sub.time }}</span>
                <span style="cursor: pointer;" :class="{ 'comment-active' : sub.showReplyInput }"
                      @click="handleShowReplyInput(sub)"><i class="el-icon-s-comment"></i>评论</span>
                <span style="margin-left: 20px; cursor: pointer" @click="del(sub.id)" v-if="sub.userId === user.id"><i
                    class="el-icon-delete"></i>删除</span>
                <span style="margin-left: 20px; cursor: pointer" @click="openReport(sub)">举报</span>
              </div>
              <div v-if="sub.showReplyInput">
                <el-input type="textarea" placeholder="请输入回复内容" v-model="sub.replyContent"></el-input>
                <div style="text-align: right; margin-top: 5px">
                  <el-button type="primary" @click="addReplay(sub)">回 复</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="举报评论" :visible.sync="reportVisible" width="420px" :close-on-click-modal="false">
      <el-radio-group v-model="reportReason" style="margin-bottom: 12px; display: flex; flex-wrap: wrap">
        <el-radio label="垃圾广告" style="margin: 0 12px 6px 0"></el-radio>
        <el-radio label="违法违规" style="margin: 0 12px 6px 0"></el-radio>
        <el-radio label="色情低俗" style="margin: 0 12px 6px 0"></el-radio>
        <el-radio label="侵权抄袭" style="margin: 0 12px 6px 0"></el-radio>
        <el-radio label="其他" style="margin: 0 12px 6px 0"></el-radio>
      </el-radio-group>
      <el-input type="textarea" :rows="3" v-model="reportDetail" placeholder="补充说明（选填）"></el-input>
      <div slot="footer">
        <el-button @click="reportVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitReport">提交举报</el-button>
      </div>
    </el-dialog>
  </div>
  <!--  评论结束   -->

</template>

<script>
export default {
  name: "CommentComponent",
  props: {
    fid: null,
    module: null
  },
  data() {
    return {
      commentCount: 0,
      commentContent: '',
      commentList: [],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      reportVisible: false,
      reportReason: '',
      reportDetail: '',
      reportTarget: null
    }
  },
  created() {
    this.loadComment()
  },
  methods: {
    openReport(item) {
      if (!this.user.id) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      this.reportTarget = item
      this.reportVisible = true
    },
    submitReport() {
      if (!this.reportReason) {
        this.$message.warning('请选择举报原因')
        return
      }
      const reason = this.reportReason + (this.reportDetail ? '：' + this.reportDetail : '')
      this.$request.post('/report/add', {fid: this.reportTarget.id, module: '评论', reason: reason}).then(res => {
        if (res.code === '200') {
          this.$message.success('举报成功，感谢反馈')
          this.reportVisible = false
          this.reportReason = ''
          this.reportDetail = ''
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadComment()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleShowReplyInput(item) {
      this.$set(item, 'showReplyInput', !item.showReplyInput)
    },
    addReplay(item) {
      this.$request.post('/comment/add', {
        pid: item.id,
        rootId: item.rootId,
        content: item.replyContent,
        fid: this.fid,
        module: this.module
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          item.replyContent = ''
          this.loadComment()  // 重新加载数据
        }
      })
    },
    loadComment() {
      this.$request.get('/comment/selectForUser', {
        params: {fid: this.fid, module: this.module}
      }).then(res => {
        this.commentList = res.data || []
      })

      this.$request.get('/comment/selectCount', {
        params: {fid: this.fid, module: this.module}
      }).then(res => {
        this.commentCount = res.data || 0
      })
    },
    addComment() {
      this.$request.post('/comment/add', {
        content: this.commentContent,
        fid: this.fid,
        module: this.module
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.commentContent = ''
          this.loadComment()  // 重新加载数据
        }
      })
    },
  }
}
</script>

<style scoped>

</style>