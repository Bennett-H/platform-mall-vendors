<template>
  <div class="mod-payfacetoface">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-select v-model="searchForm.shopsId" clearable filterable placeholder="所属店铺" class="width185"
                   @change="getDataList(1)">
          <el-option
            v-for="shops in shopsList"
            :key="shops.id"
            :label="shops.name"
            :value="shops.id">
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
        width="120px"
        prop="fromType"
        header-align="center"
        align="center"
        label="下单来源">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fromType === 1" size="small" type="success">微信小程序</el-tag>
          <el-tag v-else-if="scope.row.fromType === 2" size="small" type="success">微信公众号</el-tag>
          <el-tag v-else-if="scope.row.fromType === 3" size="small" type="warning">APP</el-tag>
          <el-tag v-else-if="scope.row.fromType === 4" size="small" type="warning">H5</el-tag>
          <el-tag v-else-if="scope.row.fromType === 5" size="small" type="warning">支付宝小程序</el-tag>
          <el-tag v-else-if="scope.row.fromType === 6" size="small" type="warning">QQ小程序</el-tag>
          <el-tag v-else-if="scope.row.fromType === 7" size="small" type="warning">头条小程序</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="orderSn"
        header-align="center"
        align="center"
        label="订单编号">
      </el-table-column>
      <el-table-column
        prop="payStatus"
        header-align="center"
        align="center"
        label="付款状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.payStatus === 1" size="small" type="info">未付款</el-tag>
          <el-tag v-else-if="scope.row.payStatus === 2" size="small" type="warning">付款中</el-tag>
          <el-tag v-else-if="scope.row.payStatus === 3" size="small" type="success">已付款</el-tag>
          <el-tag v-else-if="scope.row.payStatus === 4" size="small" type="danger">退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="actualPrice"
        header-align="center"
        align="center"
        label="实际支付的金额">
      </el-table-column>
      <el-table-column
        prop="addTime"
        header-align="center"
        align="center"
        label="下单时间">
      </el-table-column>
      <el-table-column
        prop="payTime"
        header-align="center"
        align="center"
        label="付款时间">
      </el-table-column>
      <el-table-column
        prop="shopsName"
        header-align="center"
        align="center"
        label="所属店铺">
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
        name: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      userDetailVisible: false,
      shopsList: []
    }
  },
  components: {
    UserDetail
  },
  activated () {
    this.getDataList()
    this.$http({
      url: '/mall/shops/queryMyShop',
      method: 'get'
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.shopsList = data.list
      }
    })
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
        url: '/mall/payfacetoface/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'name': this.searchForm.name,
          'shopsId': this.searchForm.shopsId
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
    }
  }
}
</script>
