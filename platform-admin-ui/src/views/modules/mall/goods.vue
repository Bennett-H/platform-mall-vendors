<template>
  <div class="mod-goods">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="商品名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.goodsSn" placeholder="商品编码" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.isOnSale" placeholder="上架" @change="getDataList(1)" clearable>
          <el-option
            key="0"
            label="下架"
            value="0">
          </el-option>
          <el-option
            key="1"
            label="上架"
            value="1">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:goods:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:goods:delete')" type="danger" @click="deleteHandle()"
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
        width="150"
        prop="name"
        header-align="center"
        align="center"
        label="商品名称">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="skuHandle(scope.row.id, scope.row.isGroup)">{{scope.row.name}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="shopsName"
        header-align="center"
        align="center"
        label="门店名称">
      </el-table-column>
      <el-table-column
        prop="goodsSn"
        header-align="center"
        align="center"
        label="商品编码">
      </el-table-column>
      <el-table-column
        width="130"
        show-tooltip-when-overflow
        prop="goodsSn"
        header-align="center"
        align="center"
        label="小程序内链接">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-clipboard:copy="'/pages/goods/goods?id=' + scope.row.id"
                     v-clipboard:success="copySuccess">
            /pages/goods/goods?id={{scope.row.id}}(点击复制)
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="categoryName"
        header-align="center"
        align="center"
        label="商品类型">
      </el-table-column>
      <el-table-column
        prop="brandName"
        header-align="center"
        align="center"
        label="品牌">
      </el-table-column>
      <el-table-column
        prop="isOnSale"
        header-align="center"
        align="center"
        label="是否上架">
        <template slot-scope="scope">
          <div v-if="isAuth('mall:goods:changeSale')">
            <el-button v-if="scope.row.isOnSale === 0" @click="changeSale(scope.row.id, scope.row.isOnSale)"
                       size="small"
                       type="danger">否
            </el-button>
            <el-button v-else-if="scope.row.isOnSale === 1" @click="changeSale(scope.row.id, scope.row.isOnSale)"
                       size="small" type="success">是
            </el-button>
          </div>
          <div v-else>
            <el-tag v-if="scope.row.isOnSale === 0" size="small"
                    type="danger">否
            </el-tag>
            <el-tag v-else-if="scope.row.isOnSale === 1" size="small"
                    type="success">是
            </el-tag>
          </div>
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="goodsNumber"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="商品库存">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="sales"
        header-align="center"
        align="center"
        label="销量">
      </el-table-column>
      <el-table-column
        prop="isHot"
        header-align="center"
        align="center"
        label="热销">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isHot === 0" size="small" type="danger">否</el-tag>
          <el-tag v-else-if="scope.row.isHot === 1" size="small" type="success">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="isNew"
        header-align="center"
        align="center"
        label="新品">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isNew === 0" size="small" type="danger">否</el-tag>
          <el-tag v-else-if="scope.row.isNew === 1" size="small" type="success">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="isCoupon"
        header-align="center"
        align="center"
        label="实体券">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isCoupon === 0" size="small" type="danger">否</el-tag>
          <el-tag v-else-if="scope.row.isCoupon === 1" size="small" type="success">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="listPicUrl"
        header-align="center"
        align="center"
        label="列表图">
        <template slot-scope="scope">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.listPicUrl)" :src="scope.row.listPicUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="marketPrice"
        header-align="center"
        align="center"
        label="市场价">
      </el-table-column>
      <el-table-column
        prop="retailPrice"
        header-align="center"
        align="center"
        label="零售价格">
      </el-table-column>
      <el-table-column
        prop="minSell"
        header-align="center"
        align="center"
        label="起售数量">
      </el-table-column>
      <el-table-column
        prop="sort"
        header-align="center"
        align="center"
        label="排序">
      </el-table-column>
      <el-table-column
        show-tooltip-when-overflow
        prop="promotionDesc"
        header-align="center"
        align="center"
        show-tooltip-when-overflow
        label="推广描述">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:goods:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:goods:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('mall:goods:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">
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
    <goodssku-add-or-update v-if="goodsskuAddOrUpdateVisible" ref="goodsskuAddOrUpdate"
                            @refreshDataList="getDataList"></goodssku-add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './goods-add-or-update'
import GoodsskuAddOrUpdate from './goodssku-add-or-update.vue'

export default {
  data () {
    return {
      searchForm: {
        name: '',
        goodsSn: '',
        isOnSale: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false,
      goodsskuAddOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate,
    GoodsskuAddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    copySuccess () {
      this.$message({
        message: '复制成功',
        type: 'success'
      })
    },
    changeSale (id, isOnSale) {
      this.$confirm(`确定${isOnSale === 1 ? '下架' : '上架'}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/goods/changeSale',
          method: 'post',
          data: [id]
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.getDataList(1)
          }
        })
      }).catch(() => {
      })
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/goods/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'name': this.searchForm.name,
          'goodsSn': this.searchForm.goodsSn,
          'isOnSale': this.searchForm.isOnSale
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
      if (id) {
        this.$confirm('修改操作会使商品重新进入审核状态, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.addOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.addOrUpdate.init(id)
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      } else {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      }
    },
    // sku设置
    skuHandle (id, isGroup) {
      this.$confirm('修改操作会使商品重新进入审核状态, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.goodsskuAddOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.goodsskuAddOrUpdate.init(id, isGroup)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
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
          url: '/mall/goods/delete',
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
