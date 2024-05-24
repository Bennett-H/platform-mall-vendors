<template>
  <el-dialog
      title="推送消息"
      :close-on-click-modal="false"
      :visible.sync="visible">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>本消息将发送给所有用户，消息预览：</span>
      </div>
      <div class="margin-top-xs">
        <el-input type="textarea" disabled autosize v-model="msgReview" placeholder="模版"></el-input>
      </div>
    </el-card>
    <span slot="footer" class="dialog-footer">
      <el-button @click="send" type="success"
                 :disabled="sending">{{sending ? '发送中...' : '发送'}}</el-button>
      <el-button @click="visible=false">关闭</el-button>
    </span>
  </el-dialog>
</template>
<script>
import {mapState} from 'vuex'

export default {
  name: 'template-msg-task',
  data () {
    return {
      visible: false,
      sending: false,
      msgTemplate: {}
    }
  },
  computed: mapState({
    msgReview () {
      if (!this.msgTemplate.data) {
        return ''
      }
      let content = this.msgTemplate.content
      this.msgTemplate.data.forEach(item => {
        content = content.replace('{{' + item.name + '.DATA}}', item.value)
      })
      return content
    }
  }),
  methods: {
    init (msgTemplate) {
      if (!msgTemplate || !msgTemplate.templateId) {
        this.$message.error('消息模板无效')
        return
      }
      if (!msgTemplate.data || !(msgTemplate.data instanceof Array)) {
        this.$message.error('请现配置此模板填充数据')
        return
      }
      this.msgTemplate = msgTemplate
      this.visible = true
    },
    send () {
      if (this.sending) {
        return
      }
      this.sending = true
      this.$http({
        url: '/manage/msgTemplate/sendMsgBatch',
        method: 'post',
        data: {
          templateId: this.msgTemplate.templateId,
          url: this.msgTemplate.url,
          miniprogram: this.msgTemplate.miniprogram,
          data: this.msgTemplate.data
        }
      }).then(({data}) => {
        this.sending = false
        if (data && data.code === 0) {
          this.$message.success('消息将在后台发送')
          this.visible = false
        }
      })
    }
  }
}
</script>
