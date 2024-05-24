<template>
  <div class="mod-integralgoods">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="商品名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.goodsSn" placeholder="商品编码" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.isOnSale" placeholder="上架" @change="getDataList(1)">
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
        <el-button v-if="isAuth('mall:integralgoods:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:integralgoods:delete')" type="danger" @click="deleteHandle()"
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
        prop="name"
        header-align="center"
        align="center"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="goodsSn"
        header-align="center"
        align="center"
        label="商品编码">
      </el-table-column>
      <el-table-column
        prop="goodsNumber"
        header-align="center"
        align="center"
        label="商品库存">
      </el-table-column>
      <el-table-column
        prop="isOnSale"
        header-align="center"
        align="center"
        label="是否上架">
        <template slot-scope="scope">
          <div v-if="isAuth('mall:integralgoods:changeSale')">
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
        prop="keywords"
        header-align="center"
        align="center"
        label="关键词">
      </el-table-column>
      <el-table-column
        prop="goodsBrief"
        header-align="center"
        align="center"
        label="简明介绍">
      </el-table-column>
      <el-table-column
        prop="retailPrice"
        header-align="center"
        align="center"
        label="积分价格">
      </el-table-column>
      <el-table-column
        prop="sort"
        header-align="center"
        align="center"
        label="排序">
      </el-table-column>
      <el-table-column
        prop="sales"
        header-align="center"
        align="center"
        label="销量">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="添加时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:integralgoods:info')" type="text" size="small"
                     @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:integralgoods:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('mall:integralgoods:delete')" type="text" size="small"
                     @click="deleteHandle(scope.row.id)">删除
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
import AddOrUpdate from './integralgoods-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        name: '',
        goodsSn: '',
        isOnSale: '1'
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
    changeSale (id, isOnSale) {
      this.$confirm(`确定${isOnSale === 1 ? '下架' : '上架'}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/integralgoods/changeSale',
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
        url: '/mall/integralgoods/list',
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
          url: '/mall/integralgoods/delete',
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
