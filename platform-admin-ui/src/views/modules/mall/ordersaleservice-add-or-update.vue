<template>
  <el-dialog
    title="查看"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-descriptions :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          订单编号
        </template>
        {{dataForm.orderSn}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          申请人
        </template>
        <el-tag>{{dataForm.nickname + dataForm.mobile}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          退款单编号
        </template>
        {{dataForm.saleserviceSn}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          申请退款原因
        </template>
        {{dataForm.reason}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          申请时间
        </template>
        {{dataForm.createTime}}
      </el-descriptions-item>


      <el-descriptions-item>
        <template slot="label">
          审核时间
        </template>
        {{dataForm.handleTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          退款时间
        </template>
        {{dataForm.refundTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          用户备注
        </template>
        {{dataForm.remark}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          申请退款金额
        </template>
        {{dataForm.amount}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          状态
        </template>
        <el-tag v-if="dataForm.status === 1" size="small" type="warning">已申请</el-tag>
        <el-tag v-else-if="dataForm.status === 2" size="small" type="success">已审核</el-tag>
        <el-tag v-else-if="dataForm.status === 3" size="small" type="success">已退款</el-tag>
        <el-tag v-else-if="dataForm.status === 4" size="small" type="info">已驳回</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          审核原因
        </template>
        {{dataForm.handleReason}}
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button v-if="!disabled" type="primary" @click="dataFormSubmit(1)">通过</el-button>
      <el-button v-if="!disabled" type="warning" @click="dataFormSubmit(2)">拒绝</el-button>
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
        orderSn: '',
        userId: '',
        saleserviceSn: '',
        reason: '',
        amount: '',
        status: '',
        createTime: '',
        handleTime: '',
        refundTime: '',
        handleReason: '',
        remark: ''
      },
      dataRule: {
        handleReason: [
          {required: true, message: '审核原因不能为空', trigger: 'blur'}
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
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/ordersaleservice/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.ordersaleservice
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit (type) {
      this.$http({
        url: `/mall/ordersaleservice/${type === 1 ? 'adopt' : 'refuse'}`,
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
  }
}
</script>
