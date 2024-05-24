<template>
  <el-dialog
    title="查看"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" ref="dataForm"
             label-width="120px">
      <el-form-item label="发送人：">
        <div v-html="dataForm.sender"></div>
      </el-form-item>
      <el-form-item label="接收人：">
        <div v-html="dataForm.receiver"></div>
      </el-form-item>
      <el-form-item label="邮件主题：">
        <div v-html="dataForm.subject"></div>
      </el-form-item>
      <el-form-item label="发送内容：">
        <div v-html="dataForm.content"></div>
      </el-form-item>
      <el-form-item label="发送时间：">
        <div v-html="dataForm.sendDate"></div>
      </el-form-item>
      <el-form-item label="发送类型：" prop="type">
        <el-tag v-if="dataForm.type === 0" size="small">系统发送邮件</el-tag>
        <el-tag v-else-if="dataForm.type === 1" size="small" type="success">用户发送邮件</el-tag>
      </el-form-item>
      <el-form-item label="发送结果：" prop="sendResult">
        <el-tag v-if="dataForm.sendResult === 1" size="small" type="danger">发送失败</el-tag>
        <el-tag v-else-if="dataForm.sendResult === 0" size="small" type="success">发送成功</el-tag>
      </el-form-item>
    </el-form>
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
      dataForm: {}
    }
  },
  methods: {
    init (id) {
      if (id) {
        this.$nextTick(() => {
          this.$http({
            url: `/sys/maillog/info/${id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.visible = true
              this.dataForm = data.maillog
            }
          })
        })
      }
    }
  }
}
</script>
