<template>
  <div class="mod-comment">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-select v-model="searchForm.status" clearable placeholder="状态" @change="getDataList(1)">
          <el-option
            key="0"
            label="待审核"
            value="0">
          </el-option>
          <el-option
            key="1"
            label="审核通过"
            value="1">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
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
        prop="userId"
        header-align="center"
        align="center"
        label="会员昵称">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUserDetails(scope.row.userId)">{{scope.row.nickname}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsId"
        header-align="center"
        align="center"
        label="评论对象">
        <template slot-scope="scope" v-if="scope.row.type === 0">
          <el-button type="text" size="small" @click="showGoodsDetails(scope.row.goodsId)">{{scope.row.goodsName}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsSpecifitionNameValue"
        header-align="center"
        align="center"
        label="规格属性">
      </el-table-column>
      <el-table-column
        show-tooltip-when-overflow=""
        prop="content"
        header-align="center"
        align="center"
        label="评价内容">
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" size="small" type="danger">文章</el-tag>
          <el-tag v-else-if="scope.row.type === 0" size="small" type="success">商品</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="addTime"
        header-align="center"
        align="center"
        label="评论时间">
        <template slot-scope="scope">
          <span>{{scope.row.addTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 0" @click="changeStatus(scope.row.id)" size="small" type="danger">待审核
          </el-button>
          <el-tag v-else-if="scope.row.status === 1" size="small" type="success">审核通过</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="evalLevel"
        header-align="center"
        align="center"
        label="评价级别">
        <template slot-scope="scope">
          <el-rate
            disabled
            v-model="scope.row.evalLevel">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="deliveryLevel"
        header-align="center"
        align="center"
        label="配送质量">
        <template slot-scope="scope">
          <el-rate
            disabled
            v-model="scope.row.deliveryLevel">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="goodsLevel"
        header-align="center"
        align="center"
        label="商品服务">
        <template slot-scope="scope">
          <el-rate
            disabled
            v-model="scope.row.goodsLevel">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:comment:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:comment:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
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
    <!-- 弹窗, 用户详情、商品详情 -->
    <user-detail v-if="userDetailVisible" ref="userDetail"></user-detail>
    <goods-detail v-if="goodsDetailVisible" ref="goodsDetail"></goods-detail>
  </div>
</template>

<script>
import AddOrUpdate from './comment-add-or-update'
import UserDetail from './user-add-or-update'
import GoodsDetail from './goods-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        status: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false,
      userDetailVisible: false,
      goodsDetailVisible: false
    }
  },
  components: {
    AddOrUpdate,
    UserDetail,
    GoodsDetail
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 查看会员详情
    showUserDetails (id) {
      this.userDetailVisible = true
      this.$nextTick(() => {
        this.$refs.userDetail.init(id, true)
      })
    },
    // 查看商品详情
    showGoodsDetails (id) {
      this.goodsDetailVisible = true
      this.$nextTick(() => {
        this.$refs.goodsDetail.init(id, true)
      })
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/comment/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'status': this.searchForm.status
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
    changeStatus (id) {
      this.$confirm(`确定审核通过?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/comment/changeStatus',
          method: 'post',
          data: [id]
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
          url: '/mall/comment/delete',
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
