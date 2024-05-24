<template>
  <div class="mod-banner">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.title" placeholder="标题" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:banner:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:banner:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="mediaType"
        header-align="center"
        align="center"
        label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.mediaType === 0" size="small" type="danger">图片</el-tag>
          <el-tag v-else-if="scope.row.mediaType === 1" size="small" type="success">链接</el-tag>
          <el-tag v-else-if="scope.row.mediaType === 2" size="small" type="success">文本</el-tag>
          <el-tag v-else-if="scope.row.mediaType === 3" size="small" type="success">视频</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="分区">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 'index'" size="small" type="success">首页分区</el-tag>
          <el-tag v-else-if="scope.row.type === 'pedigree'" size="small" type="warn">族谱分区</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="title"
        header-align="center"
        align="center"
        show-tooltip-when-overflow
        label="标题">
      </el-table-column>
      <el-table-column
        prop="imageUrl"
        header-align="center"
        align="center"
        label="图片">
        <template slot-scope="scope">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.imageUrl)" :src="scope.row.imageUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="link"
        header-align="center"
        align="center"
        label="小程序页面">
      </el-table-column>
      <el-table-column
        prop="videoUrl"
        header-align="center"
        align="center"
        label="视频链接">
        <template slot-scope="scope">
          <video v-if="scope.row.mediaType === 3" :src="scope.row.videoUrl"
                 style="width: 300px;height: 200px"
                 controls="controls">您的浏览器不支持视频播放
          </video>
        </template>
      </el-table-column>
      <el-table-column
        show-tooltip-when-overflow
        prop="content"
        header-align="center"
        align="center"
        label="文本内容">
        <template slot-scope="scope">
          <div v-html="scope.row.content"></div>
        </template>
      </el-table-column>
      <el-table-column
        prop="endTime"
        header-align="center"
        align="center"
        label="结束时间">
        <template slot-scope="scope">
          <span>{{scope.row.endTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="enabled"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.enabled === 0" size="small" type="danger">禁用</el-tag>
          <el-tag v-else-if="scope.row.enabled === 1" size="small" type="success">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:banner:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:banner:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('mall:banner:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './banner-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        title: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/banner/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'title': this.searchForm.title
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.records
          this.totalPage = data.page.total
        } else {
          this.dataList = []
          this.totalPage = 0
        }
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 查看详情
    showDetails (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, true)
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle (id) {
      let ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('确定对所选项进行[删除]操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/banner/delete',
          method: 'post',
          data: ids
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.getDataList()
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>
