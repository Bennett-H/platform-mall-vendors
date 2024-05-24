<template>
  <div class="mod-ordersaleservice">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.orderSn" placeholder="订单编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.userName" placeholder="申请用户" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.status" placeholder="退款状态" @change="getDataList(1)" clearable>
          <el-option
            key="1"
            label="已申请"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="已审核"
            value="2">
          </el-option>
          <el-option
            key="3"
            label="已退款"
            value="3">
          </el-option>
          <el-option
            key="4"
            label="已驳回"
            value="4">
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
        prop="orderSn"
        header-align="center"
        align="center"
        label="订单编号">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showOrderDetails(scope.row.orderSn)">{{scope.row.orderSn}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="userId"
        header-align="center"
        align="center"
        label="申请用户">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUserDetails(scope.row.userId)">{{scope.row.nickname}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="saleserviceSn"
        header-align="center"
        align="center"
        label="退款单编号">
      </el-table-column>
      <el-table-column
        prop="reason"
        header-align="center"
        align="center"
        label="申请退款原因">
      </el-table-column>
      <el-table-column
        prop="amount"
        header-align="center"
        align="center"
        label="申请退款金额">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="warning">已申请</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small" type="success">已审核</el-tag>
          <el-tag v-else-if="scope.row.status === 3" size="small" type="success">已退款</el-tag>
          <el-tag v-else-if="scope.row.status === 4" size="small" type="info">已驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="申请时间">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="handleTime"
        header-align="center"
        align="center"
        label="审核时间">
        <template slot-scope="scope">
          <span>{{scope.row.handleTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="refundTime"
        header-align="center"
        align="center"
        label="退款时间">
        <template slot-scope="scope">
          <span>{{scope.row.refundTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="handleReason"
        header-align="center"
        align="center"
        label="审核原因">
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        align="center"
        label="用户备注">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:ordersaleservice:info')" type="text" size="small"
                     @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:ordersaleservice:update')&&scope.row.status===1" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">审核
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
    <user-detail v-if="userDetailVisible" ref="userDetail"></user-detail>
    <order-detail v-if="orderDetailVisible" ref="orderDetail"></order-detail>
  </div>
</template>

<script>
import OrderDetail from './order-add-or-update'
import AddOrUpdate from './ordersaleservice-add-or-update'
import UserDetail from './user-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        orderSn: '',
        userName: '',
        status: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      orderDetailVisible: false,
      addOrUpdateVisible: false,
      userDetailVisible: false
    }
  },
  components: {
    OrderDetail, AddOrUpdate, UserDetail
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
    // 查看详情
    showOrderDetails (id) {
      this.orderDetailVisible = true
      this.$nextTick(() => {
        this.$refs.orderDetail.init(id)
      })
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/ordersaleservice/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'orderSn': this.searchForm.orderSn,
          'userName': this.searchForm.userName,
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
          url: '/mall/ordersaleservice/delete',
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
