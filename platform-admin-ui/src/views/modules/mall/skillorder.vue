<template>
  <div class="mod-order">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.nickname" placeholder="会员昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.orderSn" placeholder="订单编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.consignee" placeholder="收货人" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.mobile" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.fromType" clearable placeholder="下单来源" @change="getDataList(1)">
          <el-option
            key="1"
            label="微信小程序"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="微信公众号"
            value="2">
          </el-option>
          <el-option
            key="3"
            label="APP"
            value="3">
          </el-option>
          <el-option
            key="4"
            label="H5"
            value="4">
          </el-option>
          <el-option
            key="5"
            label="支付宝小程序"
            value="5">
          </el-option>
          <el-option
            key="6"
            label="QQ小程序"
            value="6">
          </el-option>
          <el-option
            key="7"
            label="头条小程序"
            value="7">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.payStatus" clearable placeholder="付款状态" @change="getDataList(1)">
          <el-option
            key="1"
            label="未付款"
            value="1">
          </el-option>
          <el-option
            key="3"
            label="已付款"
            value="3">
          </el-option>
          <el-option
            key="4"
            label="退款"
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
        prop="orderSn"
        header-align="center"
        align="center"
        label="订单编号">
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
        width="120px"
        prop="orderType"
        header-align="center"
        align="center"
        label="订单类型">
        <template slot-scope="scope">
          <el-tag type="warning">秒杀订单</el-tag>
        </template>
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
        label="实际支付金额">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:order:modPrice') && scope.row.orderStatus === 0" size="mini"
                     @click="modPrice(scope.row.id,scope.row.actualPrice)">{{scope.row.actualPrice}}
          </el-button>
          <el-tag v-else size="small" type="danger">{{scope.row.actualPrice}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="180px"
        prop="orderStatus"
        header-align="center"
        align="center"
        label="订单状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 0" size="small" effect="dark">待付款</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 100" size="small" effect="dark" type="warning">已过期</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 101" size="small" effect="dark" type="warning">已取消</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 102" size="small" effect="dark" type="warning">已删除</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 201" size="small" effect="dark" type="success">待发货</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 300" size="small" effect="dark" type="success">已发货</el-tag>
          <el-button v-if="isAuth('mall:order:confirmReceive') && scope.row.orderStatus === 300" type="primary"
                     size="mini" @click="confirmReceive(scope.row.id)">确认收货
          </el-button>
          <el-tag v-else-if="scope.row.orderStatus === 301" size="small" effect="dark" type="success">确认收货</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 400" size="small" effect="dark" type="danger">申请退款</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 401" size="small" effect="dark" type="danger">退款</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 402" size="small" effect="dark" type="danger">售后退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="shippingStatus"
        header-align="center"
        align="center"
        label="发货状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shippingStatus === 1" type="info">未发货</el-tag>
          <el-button
            v-if="isAuth('mall:order:sendGoods') && scope.row.shippingStatus === 1 && scope.row.payStatus === 3 && scope.row.orderStatus === 201"
            type="primary"
            size="mini" @click="sendGoods(scope.row.id)">发货
          </el-button>
          <el-tag v-else-if="scope.row.shippingStatus === 2" size="small" type="warning">已发货</el-tag>
          <el-tag v-else-if="scope.row.shippingStatus === 3" size="small" type="success">已收货</el-tag>
          <el-tag v-else-if="scope.row.shippingStatus === 4" size="small" type="danger">退货</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="consignee"
        header-align="center"
        align="center"
        label="收货人">
      </el-table-column>
      <el-table-column
        show-tooltip-when-overflow
        prop="address"
        header-align="center"
        align="center"
        label="详细地址">
        <template slot-scope="scope">
          {{scope.row.province + scope.row.city + scope.row.district + scope.row.address}}
        </template>
      </el-table-column>
      <el-table-column
        prop="mobile"
        header-align="center"
        align="center"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="shippingName"
        header-align="center"
        align="center"
        label="快递公司">
      </el-table-column>
      <el-table-column
        prop="shippingNo"
        header-align="center"
        align="center"
        label="快递单号">
      </el-table-column>
      <el-table-column
        prop="integralMoney"
        header-align="center"
        align="center"
        label="积分抵扣金额">
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
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:order:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
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
    <el-dialog
      title="快递信息"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false">
      <el-form inline :rules="dataRule" :model="order" ref="order" label-width="80px">
        <el-form-item label="快递公司" prop="shippingId">
          <el-select v-model="order.shippingId" filterable placeholder="请选择">
            <el-option
              v-for="shipping in shippings"
              :key="shipping.id"
              :label="shipping.name"
              :value="shipping.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="快递单号" prop="shippingNo">
          <el-input v-model="order.shippingNo" placeholder="快递单号"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </el-dialog>

    <el-dialog
      title="修改价格"
      width="400px"
      :visible.sync="modPriceVisible"
      :close-on-click-modal="false">
      <el-form inline :rules="dataRule" :model="order" ref="order" label-width="80px">
        <el-form-item label="价格" prop="actualPrice">
          <el-input-number :min="0" :precision="2" :controls="false" v-model="order.actualPrice"
                           placeholder="价格"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="modPriceVisible = false">取消</el-button>
          <el-button type="primary" @click="modPricePost()">确定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './order-add-or-update'
import UserDetail from './user-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        nickname: '',
        orderSn: '',
        fromType: '',
        payStatus: '',
        consignee: '',
        mobile: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false,
      userDetailVisible: false,
      order: {
        id: '',
        shippingId: '',
        shippingNo: ''
      },
      dialogVisible: false,
      modPriceVisible: false,
      shippings: [],
      dataRule: {
        shippingId: [
          {
            required: true,
            message: '快递公司不能为空',
            trigger: 'blur'
          }
        ],
        shippingNo: [
          {
            required: true,
            message: '快递单号不能为空',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  components: {
    AddOrUpdate,
    UserDetail
  },
  activated () {
    this.getDataList()
    this.$http({
      url: '/mall/shipping/queryAll',
      method: 'get',
      params: {
        'status': 1
      }
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.shippings = data.list
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
        url: '/mall/order/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'nickname': this.searchForm.nickname,
          'orderSn': this.searchForm.orderSn,
          'fromType': this.searchForm.fromType,
          'payStatus': this.searchForm.payStatus,
          'consignee': this.searchForm.consignee,
          'mobile': this.searchForm.mobile,
          'orderType': 3
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
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 发货
    sendGoods (id) {
      this.dialogVisible = true
      this.order.id = id
    },
    // 发货提交
    dataFormSubmit () {
      this.$refs['order']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: '/mall/order/sendGoods',
              method: 'post',
              data: this.order
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500
                })
                this.dialogVisible = false
                this.getDataList()
              }
            })
          }
        })
    },
    modPrice (id, actualPrice) {
      this.modPriceVisible = true
      this.order.id = id
      this.order.actualPrice = actualPrice
    },
    // 修改价格
    modPricePost () {
      this.$http({
        url: '/mall/order/modPrice',
        method: 'post',
        data: {
          id: this.order.id,
          actualPrice: this.order.actualPrice
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
        }
      })
    },
    // 确认收货
    confirmReceive (id) {
      this.$http({
        url: '/mall/order/confirmReceive',
        method: 'get',
        params: {
          id: id
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
          url: '/mall/order/delete',
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
