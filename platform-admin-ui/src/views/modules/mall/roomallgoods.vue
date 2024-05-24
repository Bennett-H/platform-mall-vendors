<template>
  <div class="mod-roomallgoods">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="商品名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:roomallgoods:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:roomallgoods:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
        <el-button v-if="isAuth('mall:roomallgoods:getapproved')" type="warning" @click="getapproved()">
          同步商品
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
        prop="coverImgUrl"
        header-align="center"
        align="center"
        label="商品图片">
        <template slot-scope="scope">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.coverImgUrl)" :src="scope.row.coverImgUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="商品名">
      </el-table-column>
      <el-table-column
        prop="priceType"
        header-align="center"
        align="center"
        label="价格类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.priceType === 1" size="small" type="danger">一口价</el-tag>
          <el-tag v-else-if="scope.row.priceType === 2" size="small" type="info">价格区间</el-tag>
          <el-tag v-else-if="scope.row.priceType === 3" size="small" type="success">折扣价</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="price"
        header-align="center"
        align="center"
        label="商品价格">
      </el-table-column>
      <el-table-column
        prop="price2"
        header-align="center"
        align="center"
        label="商品价格">
      </el-table-column>
      <el-table-column
        prop="url"
        header-align="center"
        align="center"
        label="商品url">
      </el-table-column>
      <el-table-column
        prop="auditStatus"
        header-align="center"
        align="center"
        label="审核状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.auditStatus === 0" size="small" type="info">未审核</el-tag>
          <el-tag v-if="scope.row.auditStatus === 1" size="small" type="warning">审核中</el-tag>
          <el-tag v-else-if="scope.row.auditStatus === 2" size="small" type="success">审核通过</el-tag>
          <el-tag v-else-if="scope.row.auditStatus === 3" size="small" type="danger">审核失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="thirdPartyTag"
        header-align="center"
        align="center"
        label="添加来源">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.thirdPartyTag === 2" size="small" type="success">系统内部</el-tag>
          <el-tag v-else size="small" type="info">公众平台</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:roomallgoods:info')" type="text" size="small"
                     @click="showDetails(scope.row.goodsId)">查看
          </el-button>
          <el-button v-if="isAuth('mall:roomallgoods:update') && scope.row.auditStatus !== 1" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.goodsId)">修改
          </el-button>
          <el-button v-if="isAuth('mall:roomallgoods:delete')" type="text" size="small"
                     @click="deleteHandle(scope.row.goodsId)">删除
          </el-button>
          <el-button v-if="isAuth('mall:roomallgoods:resetaudit') && scope.row.auditStatus === 1" type="text"
                     size="small"
                     @click="resetaudit(scope.row.goodsId, scope.row.auditId)">撤回审核
          </el-button>
          <el-button v-if="isAuth('mall:roomallgoods:resetaudit') && scope.row.auditStatus === 0" type="text"
                     size="small"
                     @click="audit(scope.row.goodsId, scope.row.auditId)">重新审核
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
import AddOrUpdate from './roomallgoods-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        name: ''
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
        url: '/mall/roomallgoods/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'name': this.searchForm.name
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
    // 同步商品信息
    getapproved () {
      this.$http({
        url: '/mall/roomallgoods/getapproved',
        method: 'get',
        params: {
          'page': 0,
          'limit': 100
        }
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
          url: '/mall/roomallgoods/delete',
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
    },
    // 重新审核
    audit (goodsId) {
      this.$confirm('确定重新审核操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/roomallgoods/audit',
          method: 'get',
          params: {
            'goodsId': goodsId
          }
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
    // 撤回审核
    resetaudit (goodsId, auditId) {
      this.$confirm('确定撤回审核操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/roomallgoods/resetaudit',
          method: 'get',
          params: {
            'goodsId': goodsId,
            'auditId': auditId
          }
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
