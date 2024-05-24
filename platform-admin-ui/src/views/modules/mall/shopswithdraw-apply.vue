<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '审核' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="店铺名称" prop="shopsName">
        <el-input v-model="shopsEntity.name" readOnly placeholder="店铺名称"></el-input>
      </el-form-item>
      <el-form-item label="店铺管理员" prop="realName">
        <el-input v-model="userEntity.realName" readOnly placeholder="店铺管理员"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="userEntity.mobile" readOnly placeholder="店铺手机号"></el-input>
      </el-form-item>
      <el-form-item label="申请金额" prop="applyMoney">
        <el-input v-model="dataForm.applyMoney" readOnly placeholder="申请金额"></el-input>
      </el-form-item>
      <el-form-item label="银行卡号" prop="applyMoney">
        <el-select v-model="dataForm.shopsBankCardId" :disabled="disabled" clearable filterable placeholder="请选择" @change="setShopBankCardInfo" >
          <el-option
            v-for="shopsBankCard in shopsBankCardList"
            :key="shopsBankCard.id"
            :label="shopsBankCard.cardNumber + '(' + shopsBankCard.cardName + ')'"
            :value="shopsBankCard.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-table
        ref="multipleTable"
        :data="canWithdrawList"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="orderSn"
          header-align="center"
          align="center"
          label="订单编号">
        </el-table-column>
        <el-table-column
          width="200"
          prop="nickname"
          header-align="center"
          align="center"
          label="会员昵称">
        </el-table-column>
        <el-table-column
          width="120px"
          prop="orderType"
          header-align="center"
          align="center"
          label="订单类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderType === 1" size="small" type="info">商城订单</el-tag>
            <el-tag v-else-if="scope.row.orderType === 2" size="small" type="success">店铺订单</el-tag>
            <el-tag v-else-if="scope.row.orderType === 3" size="small" type="warning">秒杀订单</el-tag>
            <el-tag v-else-if="scope.row.orderType === 4" size="small" type="warning">积分订单</el-tag>
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
      </el-table>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      disabled: false,
      visible: false,
      dataForm: {
        shopsId: '',
        userId: '',
        applyType: '',
        applyMoney: '',
        applyAccount: '',
        applyStatus: 1,
        commission: '',
        shopsBankCardId: '',
        orderIds: []
      },
      canWithdrawList: [],
      shopsBankCardList: [],
      shopsEntity: {},
      userEntity: {},
      dataRule: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    applyWithdrawInit (id) {
      this.visible = true
      this.$http({
        url: `/mall/shopswithdraw/withdrawDetail`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.canWithdrawList = data.data.canWithdrawList
          this.shopsEntity = data.data.shopsEntity
          this.userEntity = data.data.userEntity
          this.dataForm.shopsId = this.shopsEntity.id
          this.dataForm.userId = this.userEntity.userId
        }
      })
      this.$http({
        url: `/mall/shopsbankcard/queryAll`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.shopsBankCardList = data.list
        }
      })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
      console.log(JSON.stringify(val))
      this.dataForm.applyMoney = 0
      this.dataForm.orderIds = []
      for (var i = 0; i < val.length; i++) {
        this.dataForm.applyMoney = this.dataForm.applyMoney + val[i].actualPrice
        this.dataForm.orderIds.push(val[i].id)
      }
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
//            if (!this.dataForm.applyMoney || this.dataForm.applyMoney < 1) {
//              return this.$message({
//                message: '申请金额必须大于1元',
//                type: 'error',
//                duration: 1500
//              })
//            }

          if (valid) {
            this.$http({
              url: `/mall/shopswithdraw/withdraw`,
              method: 'post',
              data: this.dataForm
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '申请成功',
                  type: 'success',
                  duration: 1500
                })
                this.visible = false
                this.$emit('refreshDataList')
              }
            })
          }
        })
    }
  }
}
</script>
