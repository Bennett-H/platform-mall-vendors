<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '审核' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="店铺名称" prop="shopsName">
        <el-input v-model="dataForm.shopsName" readOnly placeholder="店铺名称"></el-input>
      </el-form-item>
      <el-form-item label="店铺管理员" prop="userName">
        <el-input v-model="dataForm.userName" readOnly placeholder="店铺管理员"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="dataForm.mobile" readOnly placeholder="店铺手机号"></el-input>
      </el-form-item>
      <el-form-item label="申请时间" prop="applyTime">
        <el-input v-model="dataForm.applyTime" readOnly placeholder="申请时间"></el-input>
      </el-form-item>
      <el-form-item label="申请金额" prop="applyMoney">
        <el-input v-model="dataForm.applyMoney" readOnly placeholder="申请金额"></el-input>
      </el-form-item>
      <el-form-item label="审核备注" prop="approvalRemark">
        <el-input v-model="dataForm.approvalRemark" :disabled="disabled" placeholder="审核备注"></el-input>
      </el-form-item>

      <el-table
        ref="multipleTable"
        :data="relaOrderList"
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

      <el-form-item label="申请状态" prop="applyStatus">
        <el-radio-group v-model="dataForm.applyStatus" :disabled="disabled">
          <el-radio :label="1" disabled>申请中</el-radio>
          <el-radio :label="2">通过</el-radio>
          <el-radio :label="3">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button v-if="!disabled && (dataForm.applyStatus==2 || dataForm.applyStatus == 3)" type="primary"
                 @click="dataFormSubmit()">确定</el-button>
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
        id: 0,
        userSn: '',
        userName: '',
        userId: '',
        applyType: '',
        applyTime: '',
        applyMoney: '',
        approver: '',
        approvalTime: '',
        approvalRemark: '',
        applyAccount: '',
        applyStatus: ''
      },
      relaOrderList: [],
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
    init (id, disabled) {
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/shopswithdraw/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.shopswithdraw
              this.relaOrderList = data.relaOrderList
            }
          })
        }
      })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (this.dataForm.applyStatus === 3 && !this.dataForm.approvalRemark) {
            return this.$message({
              message: '拒绝审核备注不能为空',
              type: 'error',
              duration: 1500
            })
          }

          if (valid) {
            this.$http({
              url: `/mall/shopswithdraw/${this.dataForm.applyStatus === 2 ? 'confirmWithdraw' : 'refuseWithdraw'}`,
              method: 'post',
              data: this.dataForm
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
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
