<template>
  <el-dialog
    title="用户积分变动记录"
    :visible.sync="visible">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-select v-model="searchForm.type" clearable placeholder="类型"
                   class="width185">
          <el-option
            key="1"
            label="收入"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="支出"
            value="2">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.typeDetail" clearable placeholder="说明"
                   class="width185">
          <el-option
            key="1"
            label="签到奖励"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="购物奖励"
            value="2">
          </el-option>
          <el-option
            key="3"
            label="抽奖奖励"
            value="3">
          </el-option>
          <el-option
            key="4"
            label="系统发放"
            value="4">
          </el-option>
          <el-option
            key="5"
            label="兑换支出"
            value="5">
          </el-option>
          <el-option
            key="6"
            label="退款"
            value="6">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button @click="chongZhi">充值</el-button>
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
        prop="nickname"
        header-align="center"
        align="center"
        label="用户昵称">
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" size="small" type="success">收入</el-tag>
          <el-tag v-else-if="scope.row.type === 2" size="small" type="danger">支出</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="typeDetail"
        header-align="center"
        align="center"
        label="说明">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.typeDetail === 1" size="small" type="info">签到奖励</el-tag>
          <el-tag v-else-if="scope.row.typeDetail === 2" size="small" type="success">购物奖励</el-tag>
          <el-tag v-else-if="scope.row.typeDetail === 3" size="small" type="warning">抽奖奖励</el-tag>
          <el-tag v-else-if="scope.row.typeDetail === 4" size="small" type="warning">系统发放</el-tag>
          <el-tag v-else-if="scope.row.typeDetail === 5" size="small" type="warning">兑换支出</el-tag>
          <el-tag v-else-if="scope.row.typeDetail === 6" size="small" type="warning">退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="number"
        header-align="center"
        align="center"
        label="变动积分数量">
      </el-table-column>
      <el-table-column
        prop="addTime"
        header-align="center"
        align="center"
        label="添加时间">
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
  </el-dialog>
</template>

<script>

export default {
  data () {
    return {
      userId: '',
      searchForm: {
        type: '',
        typeDetail: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      visible: false
    }
  },
  methods: {
    init (userId) {
      this.userId = userId
      this.searchForm.type = ''
      this.searchForm.typeDetail = ''
      this.getDataList()
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.visible = true
      this.$http({
        url: '/mall/integrallog/list',
        method: 'get',
        params: {
          'userId': this.userId,
          'page': this.pageIndex,
          'limit': this.pageSize,
          'type': this.searchForm.type,
          'typeDetail': this.searchForm.typeDetail
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
    chongZhi () {
      this.$prompt('请输入整数', '提示', {
        closeOnClickModal: false,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'number',
        inputPattern: /^-?[1-9]\d*$/,
        inputErrorMessage: '格式不正确'
      }).then(({value}) => {
        this.$http({
          url: '/mall/user/modIntegral',
          method: 'post',
          data: {
            id: this.userId,
            integral: value
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.modPriceVisible = false
            this.getDataList()
            this.$emit('refreshDataList')
          }
        })
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
