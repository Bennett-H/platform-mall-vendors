<template>
  <div class="mod-searchhistory">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.nickname" placeholder="会员昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.searchFrom" clearable placeholder="搜索来源" @change="getDataList(1)">
          <el-option key="1" label="微信小程序" value="1">
          </el-option>
          <el-option key="2" label="微信公众号" value="2">
          </el-option>
          <el-option key="3" label="APP" value="3">
          </el-option>
          <el-option key="4" label="H5" value="4">
          </el-option>
          <el-option key="5" label="支付宝小程序" value="5">
          </el-option>
          <el-option key="6" label="QQ小程序" value="6">
          </el-option>
          <el-option key="7" label="头条小程序" value="7">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:searchhistory:delete')" type="danger" @click="deleteHandle()"
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
        width="200"
        prop="nickname"
        header-align="center"
        align="center"
        label="会员昵称">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUserDetails(scope.row.userId)">{{scope.row.nickname}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="keyword"
        header-align="center"
        align="center"
        label="关键词">
      </el-table-column>
      <el-table-column
        prop="searchFrom"
        header-align="center"
        align="center"
        label="搜索来源">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.searchFrom === 1" size="small" type="success">微信小程序</el-tag>
          <el-tag v-else-if="scope.row.searchFrom === 2" size="small">微信公众号</el-tag>
          <el-tag v-else-if="scope.row.searchFrom === 3" size="small" type="info">APP</el-tag>
          <el-tag v-else-if="scope.row.searchFrom === 4" size="small" type="danger">H5</el-tag>
          <el-tag v-else-if="scope.row.searchFrom === 5" size="small" type="info">支付宝小程序</el-tag>
          <el-tag v-else-if="scope.row.searchFrom === 6" size="small" type="info">QQ小程序</el-tag>
          <el-tag v-else-if="scope.row.searchForm === 7" size="small" type="warning">头条小程序</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="addTime"
        header-align="center"
        align="center"
        label="搜索时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:searchhistory:delete')" type="text" size="small"
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
    <user-detail v-if="userDetailVisible" ref="userDetail"></user-detail>
  </div>
</template>

<script>
import UserDetail from './user-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        nickname: '',
        searchFrom: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      userDetailVisible: false
    }
  },
  components: {
    UserDetail
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
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/searchhistory/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'nickname': this.searchForm.nickname,
          'searchFrom': this.searchForm.searchFrom
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
          url: '/mall/searchhistory/delete',
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
