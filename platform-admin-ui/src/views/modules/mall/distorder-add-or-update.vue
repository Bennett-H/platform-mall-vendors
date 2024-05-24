<template>
  <el-dialog
    title="查看"
    :lock-scroll="false"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-descriptions :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          会员昵称
        </template>
        {{dataForm.nickname}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          购买者
        </template>
        {{dataForm.buyerNickname}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单编号
        </template>
        {{dataForm.orderSn}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          订单类型
        </template>
        <el-tag v-if="dataForm.type === item.value" v-for="item in orderTypes" size="small" :type="item.type"
                :key="item.value">
          {{ item.label }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          结算收益
        </template>
        {{dataForm.income}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          结算时间
        </template>
        {{dataForm.incomeTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          提成类型
        </template>
        <el-tag v-if="dataForm.commissionType === item.value" v-for="item in commissionTypes" size="small"
                :type="item.type"
                :key="item.value">
          {{ item.label }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          提成比例
        </template>
        {{dataForm.commission}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          企业付款单号
        </template>
        {{dataForm.paymentNo}}
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: ['orderTypes', 'commissionTypes'],
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        userId: '',
        subUserId: '',
        nickname: '',
        buyerNickname: '',
        orderId: '',
        orderSn: '',
        type: '',
        income: '',
        incomeTime: '',
        commissionType: '',
        commission: '',
        paymentNo: '',
        isAudit: ''
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/distorder/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.distorder
            }
          })
        }
      })
    }
  }
}
</script>
