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
      <el-form-item label="累计金额" prop="amountAvailable">
        <el-input v-model="shopsEntity.amountAvailable" readOnly ></el-input>
      </el-form-item>
      <el-form-item label="待取金额" prop="amountAvailable">
        <el-input v-model="shopsEntity.amountAvailable" readOnly ></el-input>
      </el-form-item>
      <el-form-item label="已取金额" prop="amountWithdrawn">
        <el-input v-model="shopsEntity.amountWithdrawn" readOnly ></el-input>
      </el-form-item>
      <el-form-item label="申请金额" prop="applyMoney">
        <el-input v-model="dataForm.applyMoney" placeholder="申请金额"></el-input>
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
    cashWithdrawInit (id) {
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
          if (!this.dataForm.applyMoney || this.dataForm.applyMoney > this.shopsEntity.amountAvailable) {
            return this.$message({
              message: '申请金额必须小于可取金额',
              type: 'error',
              duration: 1500
            })
          }

          if (valid) {
            this.$http({
              url: `/mall/shopswithdraw/cash`,
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
