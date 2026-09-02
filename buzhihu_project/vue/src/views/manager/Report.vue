<template>
  <div>
    <div class="search">
      <el-select v-model="status" placeholder="处理状态" style="width: 160px" clearable>
        <el-option label="待处理" :value="0"></el-option>
        <el-option label="已处理" :value="1"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip>
        <el-table-column prop="id" label="ID" width="70" align="center"></el-table-column>
        <el-table-column prop="module" label="模块" width="80" align="center"></el-table-column>
        <el-table-column prop="fid" label="内容ID" width="80" align="center"></el-table-column>
        <el-table-column prop="reason" label="举报原因" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reporterName" label="举报人" width="100"></el-table-column>
        <el-table-column prop="time" label="举报时间" width="160"></el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template v-slot="scope">
            <el-tag :type="scope.row.status === 0 ? 'danger' : 'success'">{{ scope.row.status === 0 ? '待处理' : '已处理' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleResult" label="处理结果" width="100" align="center"></el-table-column>
        <el-table-column prop="handleTime" label="处理时间" width="160"></el-table-column>
        <el-table-column label="操作" align="center" width="240">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="view(scope.row)">查看内容</el-button>
            <template v-if="scope.row.status === 0">
              <el-button size="mini" type="info" plain @click="handle(scope.row, 'IGNORE')">忽略</el-button>
              <el-button size="mini" type="danger" plain @click="handle(scope.row, 'DELETE')">删除内容</el-button>
              <el-button size="mini" type="warning" plain @click="handle(scope.row, 'BAN')">封禁作者</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Report",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/report/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    view(row) {
      if (row.module === '博客') {
        window.open('/front/blogDetail?blogId=' + row.fid)
      } else if (row.module === '活动') {
        window.open('/front/activityDetail?activityId=' + row.fid)
      } else {
        this.$message.info('评论请到评论列表查看')
      }
    },
    handle(row, action) {
      const tips = {IGNORE: '确定忽略该举报吗？', DELETE: '确定删除该内容吗？此操作不可恢复！', BAN: '确定封禁该内容作者吗？'}[action]
      this.$confirm(tips, '提示', {type: 'warning'}).then(() => {
        this.$request.post('/report/handle', null, {params: {id: row.id, action: action}}).then(res => {
          if (res.code === '200') {
            this.$message.success('处理成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
