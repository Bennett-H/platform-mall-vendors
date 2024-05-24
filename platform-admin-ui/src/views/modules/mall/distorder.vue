<template>
  <div class="mod-distorder">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.nickname" placeholder="会员昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.orderSn" placeholder="订单编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.type" placeholder="订单类型" @change="getDataList(1)" clearable>
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.auditStatus" placeholder="审核状态" @change="getDataList(1)" clearable>
          <el-option v-for="item in auditStatus" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:distorder:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
        <el-button v-if="isAuth('mall:distorder:delete')" type="primary" @click="exportHandle()">导出未提现数据
        </el-button>
        <el-tooltip placement="top">
          <div slot="content">操作前请查询过滤出审核成功的提现订单</div>
          <el-button v-if="isAuth('mall:distorder:delete')"
                     :disabled="!(searchForm.auditStatus==1&&searchForm.type==3&&dataListSelections.length > 0)"
                     type="success" @click="confirmCash('CASH_PASSED')">批量设置已提现
          </el-button>
        </el-tooltip>
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
        prop="nickname"
        header-align="center"
        align="center"
        label="会员昵称">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="showUserDetails(scope.row.userId)">{{ scope.row.nickname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="buyerNickname"
        header-align="center"
        align="center"
        label="购买者">
      </el-table-column>
      <el-table-column
        prop="orderSn"
        header-align="center"
        align="center"
        label="订单编号">
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="订单类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === item.value" v-for="item in orderTypes" size="small" :type="item.type"
                  :key="item.value">
            {{ item.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="auditStatus"
        header-align="center"
        align="center"
        width="150"
        label="订单状态">
        <template slot-scope="scope">
          <template v-if="scope.row.type === 3">
            <template v-if="scope.row.auditStatus === 1">
              <el-tag size="small" type="success">审核通过</el-tag>
              <div>{{ scope.row.auditDesc }}</div>
            </template>
            <template v-else-if="scope.row.auditStatus === 2">
              <el-tag size="small" type="danger">审核不通过</el-tag>
              <div>{{ scope.row.auditDesc }}</div>
            </template>
            <template v-else-if="scope.row.auditStatus === 3">
              <el-tag size="small" type="success">审核通过已提现</el-tag>
              <div>{{ scope.row.paymentNo }}</div>
            </template>
            <template v-else>
              <el-tag size="small" type="warning">待审核</el-tag>
              <!--              <el-button v-if="isAuth('mall:distorder:confirmAudit')" type="primary" size="mini"-->
              <!--                         @click="confirmAudit(scope.row.id)">审核通过-->
              <!--              </el-button>-->
            </template>
          </template>
          <template v-else>
            <el-tag v-if="scope.row.incomeTime === null" size="small" type="info">待结算</el-tag>
            <el-tag v-else size="small" type="success">已结算</el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column
        prop="income"
        header-align="center"
        align="center"
        label="结算收益">
      </el-table-column>
      <el-table-column
        prop="incomeTime"
        header-align="center"
        align="center"
        label="结算时间">
      </el-table-column>
      <el-table-column
        prop="commissionType"
        header-align="center"
        align="center"
        label="提成类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.commissionType === item.value" v-for="item in commissionTypes" size="small"
                  :type="item.type"
                  :key="item.value">
            {{ item.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="commission"
        header-align="center"
        align="center"
        label="提成比例">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="200"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:distorder:info')" type="text" size="small"
                     @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:distorder:delete')" type="text" size="small"
                     @click="deleteHandle(scope.row.id)">删除
          </el-button>

          <template v-if="isAuth('mall:distorder:confirmAudit')&&scope.row.type===3&&scope.row.auditStatus===0">
            <el-button type="text" size="small" @click="confirmAudit(scope.row.id,'AUDIT_PASSED')">审核通过
            </el-button>
            <el-button type="text" size="small" @click="confirmAudit(scope.row.id,'AUDIT_NOT_PASSED')">审核不通过
            </el-button>
          </template>
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
    <add-or-update v-if="addOrUpdateVisible" :orderTypes="orderTypes" :commissionTypes="commissionTypes"
                   ref="addOrUpdate"
                   @refreshDataList="getDataList"></add-or-update>
    <user-detail v-if="userDetailVisible" ref="userDetail"></user-detail>
  </div>
</template>

<script>
import AddOrUpdate from './distorder-add-or-update'
import UserDetail from './user-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        nickname: null,
        orderSn: null,
        type: '',
        auditStatus: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      userDetailVisible: false,
      addOrUpdateVisible: false,
      orderTypes: [
        {label: '代理提成', value: 1, type: 'info'},
        {label: '推广提成', value: 2, type: 'success'},
        {label: '佣金提现', value: 3, type: 'warning'}
      ],
      auditStatus: [
        {label: '未审核', value: 0, type: 'info'},
        {label: '审核通过', value: 1, type: 'success'},
        {label: '审核不通过', value: 2, type: 'warning'},
        {label: '审核通过已提现', value: 3, type: 'success'}
      ],
      commissionTypes: [
        {label: '非提成', value: null, type: 'warning'},
        {label: '一级分销', value: 1, type: 'info'},
        {label: '二级分销', value: 2, type: 'info'},
        {label: '一级推广', value: 3, type: 'success'},
        {label: '二级推广', value: 4, type: 'success'}
      ]
    }
  },
  components: {
    AddOrUpdate,
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
        url: '/mall/distorder/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'nickname': this.searchForm.nickname,
          'orderSn': this.searchForm.orderSn,
          'type': this.searchForm.type,
          'auditStatus': this.searchForm.auditStatus
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
          url: '/mall/distorder/delete',
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
    exportHandle () {
      let url = this.$http.BASE_URL + `/mall/distorder/exportMyOrderExcel?token=${this.$cookie.get('token')}`
      window.open(url)
    },
    importHandle () {

    },
    // 提现审核通过
    confirmAudit (id, auditStatus) {
      let ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      let type = auditStatus === 'AUDIT_PASSED' ? 'success' : 'warning'
      this.$prompt(`确定对操作的订单进行提现？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请添加审核说明',
        type: type
      }).then(({value}) => {
        this.$http({
          url: '/mall/distorder/confirmAudit',
          method: 'post',
          data: {ids: ids, auditStatus: auditStatus, auditDesc: value}
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '审核成功',
              type: 'success',
              duration: 1500
            })
            this.getDataList()
          }
        })
      }).catch(() => {
      })
    },
    confirmCash (id, auditStatus) {
      let ids = this.dataListSelections.map(item => {
        return item.id
      })
      let type = auditStatus === 'CASH_PASSED' ? 'success' : 'warning'
      this.$prompt(`确定对操作的订单进行提现？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请添加提现说明或提现订单号',
        type: type
      }).then(({value}) => {
        this.$http({
          url: '/mall/distorder/confirmCash',
          method: 'post',
          data: {ids: ids, auditStatus: auditStatus, auditDesc: value}
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '提现成功',
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
