<template>
  <el-dialog
    title="订单详情"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-card class="box-card" shadow="hover">
      <div slot="header" class="clearfix">
        <span>商品列表</span>
      </div>
      <el-table
        :data="dataForm.orderGoodsEntityList"
        border
        style="width: 100%">
        <el-table-column
          prop="goodsName"
          label="商品名称"
          header-align="center"
          align="center">
        </el-table-column>
        <el-table-column
          prop="goodsSn"
          label="SKU编码"
          header-align="center"
          align="center">
        </el-table-column>
        <el-table-column
          prop="number"
          label="商品数量"
          header-align="center"
          align="center">
        </el-table-column>
        <el-table-column
          prop="retailPrice"
          label="零售价格"
          header-align="center"
          align="center">
          <template slot-scope="scope">
            <el-tag type="danger">￥{{scope.row.retailPrice}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="goodsSpecifitionNameValue"
          label="商品规格"
          header-align="center"
          align="center">
        </el-table-column>
        <el-table-column
          prop="listPicUrl"
          label="图片"
          header-align="center"
          align="center">
          <template slot-scope="scope">
            <img style="height: 50%;width: 50%" @click="openImg(scope.row.listPicUrl)" :src="scope.row.listPicUrl"/>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <br>
    <el-descriptions title="收货人信息" :column="2" border>
      <el-descriptions-item>
        <template slot="label">
          收货人
        </template>
        {{dataForm.consignee}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          手机号
        </template>
        {{dataForm.mobile}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          收货地址
        </template>
        {{dataForm.province + dataForm.city + dataForm.district + dataForm.address}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.postscript">
        <template slot="label">
          留言
        </template>
        {{dataForm.postscript}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.postalCode">
        <template slot="label">
          邮编
        </template>
        {{dataForm.postalCode}}
      </el-descriptions-item>
    </el-descriptions>
    <br>
    <el-descriptions title="订单信息" :column="3" border>
      <el-descriptions-item v-if="dataForm.parentId">
        <template slot="label">
          父级订单ID
        </template>
        {{dataForm.parentId}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单编号
        </template>
        {{dataForm.orderSn}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单总价
        </template>
        <el-tag size="small" type="danger">{{dataForm.orderPrice}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.integralMoney">
        <template slot="label">
          积分抵扣金额
        </template>
        <el-tag size="small" type="danger">{{dataForm.integralMoney}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          实付金额
        </template>
        <el-tag size="small" type="danger">{{dataForm.actualPrice}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.prepayId">
        <template slot="label">
          prepayId
        </template>
        {{dataForm.prepayId}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.couponId">
        <template slot="label">
          使用的优惠券
        </template>
        <el-button type="text" size="small" @click="showCouponDetails(dataForm.couponId)">{{dataForm.couponTitle}}
        </el-button>
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.couponPrice">
        <template slot="label">
          优惠价格
        </template>
        <el-tag type="danger">-￥{{dataForm.couponPrice}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.shopsName">
        <template slot="label">
          店铺
        </template>
        {{dataForm.shopsName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          下单时间
        </template>
        {{dataForm.addTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          过期时间
        </template>
        {{dataForm.addTime}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.confirmTime">
        <template slot="label">
          发货时间
        </template>
        {{dataForm.confirmTime}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.payTime">
        <template slot="label">
          付款时间
        </template>
        {{dataForm.payTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单类型
        </template>
        <el-tag v-if="dataForm.orderType === 1" size="small" type="success">商城订单</el-tag>
        <el-tag v-if="dataForm.orderType === 2" size="small" type="success">店铺自提订单</el-tag>
        <el-tag v-if="dataForm.orderType === 3" size="small" type="warning">秒杀订单</el-tag>
        <el-tag v-if="dataForm.orderType === 4" size="small" type="warning">积分订单</el-tag>
        <el-tag v-if="dataForm.orderType === 5" size="small" type="warning">拼团订单</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          下单来源
        </template>
        <el-tag v-if="dataForm.fromType === 1" size="small" type="success">微信小程序</el-tag>
        <el-tag v-if="dataForm.fromType === 2" size="small" type="success">微信公众号</el-tag>
        <el-tag v-if="dataForm.fromType === 3" size="small" type="warning">APP</el-tag>
        <el-tag v-if="dataForm.fromType === 4" size="small" type="warning">H5</el-tag>
        <el-tag v-if="dataForm.fromType === 5" size="small" type="warning">支付宝小程序</el-tag>
        <el-tag v-if="dataForm.fromType === 6" size="small" type="warning">QQ小程序</el-tag>
        <el-tag v-if="dataForm.fromType === 7" size="small" type="warning">头条小程序</el-tag>
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.orderType===3">
        <template slot="label">
          秒杀商品
        </template>
        {{dataForm.goodsName}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.payType">
        <template slot="label">
          支付方式
        </template>
        <el-tag v-if="dataForm.payType === 1" size="small" type="success">微信支付</el-tag>
        <el-tag v-if="dataForm.payType === 2" size="small" type="success">余额支付</el-tag>
        <el-tag v-if="dataForm.payType === 3" size="small" type="warning">支付宝支付</el-tag>
        <el-tag v-if="dataForm.payType === 4" size="small" type="warning">积分兑换</el-tag>
        <el-tag v-if="dataForm.payType === 5" size="small" type="warning">字节跳动</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          付款状态
        </template>
        <el-tag v-if="dataForm.payStatus === 1" size="small" type="info">未付款</el-tag>
        <el-tag v-if="dataForm.payStatus === 2" size="small" type="warning">付款中</el-tag>
        <el-tag v-if="dataForm.payStatus === 3" size="small" type="success">已付款</el-tag>
        <el-tag v-if="dataForm.payStatus === 4" size="small" type="danger">退款</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          发货状态
        </template>
        <el-tag v-if="dataForm.shippingStatus === 1" type="info">未发货</el-tag>
        <el-tag v-if="dataForm.shippingStatus === 2" size="small" type="warning">已发货</el-tag>
        <el-tag v-if="dataForm.shippingStatus === 3" size="small" type="success">已收货</el-tag>
        <el-tag v-if="dataForm.shippingStatus === 4" size="small" type="danger">退货</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单状态
        </template>
        <el-tag v-if="dataForm.orderStatus === 0" size="small" effect="dark">待付款</el-tag>
        <el-tag v-if="dataForm.orderStatus === 100" size="small" effect="dark" type="warning">已过期</el-tag>
        <el-tag v-if="dataForm.orderStatus === 101" size="small" effect="dark" type="warning">已取消</el-tag>
        <el-tag v-if="dataForm.orderStatus === 102" size="small" effect="dark" type="warning">已删除</el-tag>
        <el-tag v-if="dataForm.orderStatus === 201" size="small" effect="dark" type="success">待发货</el-tag>
        <el-tag v-if="dataForm.orderStatus === 300" size="small" effect="dark" type="success">已发货</el-tag>
        <el-tag v-if="dataForm.orderStatus === 301" size="small" effect="dark" type="success">确认收货</el-tag>
        <el-tag v-if="dataForm.orderStatus === 400" size="small" effect="dark" type="danger">申请退款</el-tag>
        <el-tag v-if="dataForm.orderStatus === 401" size="small" effect="dark" type="danger">退款</el-tag>
        <el-tag v-if="dataForm.orderStatus === 402" size="small" effect="dark" type="danger">售后退款</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <br>
    <el-descriptions title="物流信息" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          快递公司名称
        </template>
        {{dataForm.shippingName}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.shippingCode">
        <template slot="label">
          快递公司CODE
        </template>
        {{dataForm.shippingCode}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          快递单号
        </template>
        {{dataForm.shippingNo}}
      </el-descriptions-item>
      <el-descriptions-item v-if="dataForm.shippingFee">
        <template slot="label">
          快递费用
        </template>
        <el-tag size="small" type="danger">{{dataForm.shippingFee}}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
    <coupon-detail v-if="couponDetailVisible" ref="couponDetail"></coupon-detail>
  </el-dialog>
</template>

<script>
import CouponDetail from './coupon-add-or-update'

export default {
  data () {
    return {
      visible: false,
      couponDetailVisible: false,
      dataForm: {
        id: '',
        orderType: '',
        orderSn: '',
        userId: '',
        orderStatus: '',
        shippingStatus: '',
        payStatus: '',
        consignee: '',
        country: '',
        province: '',
        city: '',
        district: '',
        address: '',
        mobile: '',
        postscript: '',
        shippingId: '',
        shippingName: '',
        shippingCode: '',
        shippingNo: '',
        shippingFee: '',
        prepayId: '',
        actualPrice: '',
        integralMoney: '',
        orderPrice: '',
        addTime: '',
        confirmTime: '',
        payTime: '',
        couponId: '',
        couponPrice: '',
        parentId: '',
        shopsName: '',
        goodsName: '',
        orderGoodsEntityList: []
      }
    }
  },
  components: {
    CouponDetail
  },
  methods: {
    // 查看优惠券详情
    showCouponDetails (id) {
      this.couponDetailVisible = true
      this.$nextTick(() => {
        this.$refs.couponDetail.init(id, true)
      })
    },
    init (id) {
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/order/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.order
            }
          })
        }
      })
    }
  }
}
</script>
