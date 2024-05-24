<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="收款人姓名" prop="cardName">
        <el-input v-model="dataForm.cardName" :disabled="disabled" placeholder="收款人姓名"></el-input>
      </el-form-item>
      <el-form-item label="银行卡号" prop="cardNumber">
        <el-input v-model="dataForm.cardNumber" :disabled="disabled" placeholder="银行卡号"></el-input>
      </el-form-item>
      <el-form-item label="卡类型" prop="cardType">
        <el-select v-model="dataForm.cardType" :disabled="disabled" clearable filterable placeholder="请选择">
          <el-option
            v-for="cardType in cardTypeList"
            :key="cardType"
            :label="cardType"
            :value="cardType">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="银行" prop="bankTypeId">
        <el-select v-model="dataForm.bankTypeId" :disabled="disabled" clearable filterable placeholder="请选择">
          <el-option
            v-for="bank in bankTypeList"
            :key="bank.id"
            :label="bank.bankName"
            :value="bank.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号码" prop="phone">
        <el-input v-model="dataForm.phone" :disabled="disabled" placeholder="手机号码"></el-input>
      </el-form-item>
<!--      <el-form-item label="银行卡状态" prop="cardStatus">-->
<!--        <el-select v-model="dataForm.cardStatus" clearable :disabled="disabled" placeholder="银行卡状态">-->
<!--          <el-option-->
<!--            key="2"-->
<!--            label="已解绑"-->
<!--            value="2">-->
<!--          </el-option>-->
<!--          <el-option-->
<!--            key="1"-->
<!--            label="已绑定"-->
<!--            value="1">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="绑定时间" prop="boundAt">-->
<!--        <el-date-picker type="datetime" v-model="dataForm.boundAt" value-format="yyyy-MM-dd HH:mm:ss" :disabled="disabled" placeholder="绑定时间"></el-date-picker>-->
<!--      </el-form-item>-->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button v-if="!disabled" type="primary" @click="dataFormSubmit()">确定</el-button>
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
        shopsId: '',
        cardName: '',
        cardNumber: '',
        cardType: '',
        phone: '',
        bankTypeId: '',
        cardStatus: '1',
        boundAt: ''
      },
      bankTypeList: [],
      cardTypeList: [
        '储蓄卡',
        '信用卡',
        '准贷记卡',
        '预付费卡'
      ],
      dataRule: {
        cardName: [
          {required: true, message: '收款人姓名不能为空', trigger: 'blur'}
        ],
        cardNumber: [
          {required: true, message: '银行卡号不能为空', trigger: 'blur'}
        ],
        cardType: [
          {required: true, message: '银行卡类型不能为空', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '电话不能为空', trigger: 'blur'}
        ],
        bankTypeId: [
          {required: true, message: '银行不能为空', trigger: 'blur'}
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
            url: `/mall/shopsbankcard/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.shopsbankcard
              this.dataForm.cardStatus = this.dataForm.cardStatus + ''
            }
          })
        }
        this.$http({
          url: `/mall/shopsbankcard/bankTypeList`,
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.bankTypeList = data.list
          }
        })
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/shopsbankcard/${!this.dataForm.id ? 'save' : 'update'}`,
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
