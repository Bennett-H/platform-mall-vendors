<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item>
        <el-alert :closable="false" type="error">每个模板类型只能存在一条记录，再次新增会覆盖原来的记录！</el-alert>
      </el-form-item>
      <el-form-item label="模板类型" prop="templateType">
        <el-radio-group v-model="dataForm.templateType" :disabled="!!dataForm.id">
          <el-radio-button :label="0">新订单提醒</el-radio-button>
          <el-radio-button :label="1">下单成功通知</el-radio-button>
          <el-radio-button :label="2">订单评价提醒</el-radio-button>
          <el-radio-button :label="3">退款</el-radio-button>
          <el-radio-button :label="4">秒杀成功通知</el-radio-button>
          <el-radio-button :label="5">订单配送通知</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="模板ID" prop="templateId">
        <el-input v-model="dataForm.templateId" :disabled="disabled" placeholder="推送模板ID"></el-input>
      </el-form-item>
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
        id: '',
        templateType: 1,
        templateId: ''
      },
      dataRule: {
        templateType: [
          {
            required: true,
            message: '模板类型不能为空',
            trigger: 'blur'
          }
        ],
        templateId: [
          {
            required: true,
            message: '模板ID不能为空',
            trigger: 'blur'
          }
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
            url: `/mall/templateconf/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.templateconf
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/templateconf/${!this.dataForm.id ? 'save' : 'update'}`,
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
