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
          上级分销
        </template>
        {{dataForm.superiorNickname}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          邀请码
        </template>
        <el-tag>{{dataForm.invitationCode}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          审核状态
        </template>
        <el-tag v-if="dataForm.isAudit" type="success">已审核</el-tag>
        <el-tag v-else type="danger">待审核</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          加入时间
        </template>
        {{dataForm.joinTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          待取佣金
        </template>
        {{dataForm.amountAvailable}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          已取佣金
        </template>
        {{dataForm.amountWithdrawn}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          累计佣金
        </template>
        {{dataForm.amountTotal}}
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
        superiorId: '',
        nickname: '',
        superiorNickname: '',
        isAudit: '',
        joinTime: '',
        amountAvailable: '',
        amountWithdrawn: '',
        amountTotal: '',
        invitationCode: ''
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
            url: `/mall/dist/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.dist
            }
          })
        }
      })
    }
  }
}
</script>
