<template>
  <el-dialog
    title="查看"
    :close-on-click-modal="false"
    :lock-scroll="false"
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
          收款人姓名
        </template>
        {{dataForm.cardName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          银行卡号
        </template>
        {{dataForm.cardNumber}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          银行卡类型
        </template>
        {{dataForm.cardType}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          银行名称
        </template>
        {{dataForm.bankName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          电话号码
        </template>
        {{dataForm.phone}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          银行卡状态
        </template>
        <el-tag v-if="dataForm.cardStatus === 1" size="small" type="success">已绑定</el-tag>
        <el-tag v-else size="small" type="info">已解绑</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          绑定时间
        </template>
        {{dataForm.boundAt}}
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: 0,
        userId: '',
        nickname: '',
        cardName: '',
        cardNumber: '',
        cardType: '',
        bankTypeId: '',
        bankName: '',
        cardStatus: '',
        boundAt: ''
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
            url: `/mall/userbankcard/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.userbankcard
            }
          })
        }
      })
    }
  }
}
</script>
