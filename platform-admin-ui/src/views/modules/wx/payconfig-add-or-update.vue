<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="140px">
      <el-form-item label="微信支付商户号" prop="mchId">
        <el-input v-model="dataForm.mchId" :disabled="dataForm.id!==''" placeholder="微信支付商户号"></el-input>
      </el-form-item>
      <el-form-item label="微信支付商户密钥" prop="mchKey">
        <el-input v-model="dataForm.mchKey" :disabled="disabled" placeholder="微信支付商户密钥"></el-input>
      </el-form-item>
      <el-form-item label="子商户公众账号ID" prop="subAppId">
        <el-input v-model="dataForm.subAppId" :disabled="disabled" placeholder="服务商模式下的子商户公众账号ID，普通模式请不要配置"></el-input>
      </el-form-item>
      <el-form-item label="子商户号" prop="subMchId">
        <el-input v-model="dataForm.subMchId" :disabled="disabled" placeholder="服务商模式下的子商户号，普通模式请不要配置"></el-input>
      </el-form-item>
      <el-form-item label="微信小程序appId" prop="appId">
        <el-input v-model="dataForm.appId" :disabled="disabled" placeholder="微信小程序appId"></el-input>
      </el-form-item>
      <el-form-item label="p12证书" prop="keyPath">
        <el-upload
          drag
          auto-upload
          :multiple="false"
          :action="url"
          :limit="1"
          :before-upload="beforeUploadHandle"
          :on-success="successHandle"
          :file-list="fileList">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
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
        appId: '',
        mchId: '',
        mchKey: '',
        subAppId: '',
        subMchId: '',
        keyPath: ''
      },
      fileList: [],
      url: '',
      dataRule: {
        mchId: [
          {required: true, message: '微信支付商户号不能为空', trigger: 'blur'}
        ],
        mchKey: [
          {required: true, message: '微信支付商户密钥不能为空', trigger: 'blur'}
        ],
        appId: [
          {required: true, message: '微信小程序appId不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    // 弹窗关闭时
    closeHandle () {
      this.fileList = []
    },
    // 上传之前
    beforeUploadHandle (file) {
      if (file.type !== 'application/x-pkcs12') {
        this.$message.error('只支持p12格式的文件！')
        return false
      }
    },
    // 上传成功
    successHandle (response, file, fileList) {
      this.fileList = fileList
      if (response && response.code === 0) {
        this.dataForm.keyPath = response.url
      } else {
        this.$message.error(response.msg)
      }
    },
    init (id, disabled) {
      this.url = this.$http.BASE_URL + `/sys/oss/upload?token=${this.$cookie.get('token')}`
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.fileList = []
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/wx/payconfig/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.payconfig
              this.fileList = [{name: data.payconfig.keyPath, url: data.payconfig.keyPath}]
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
              url: `/wx/payconfig/${!this.dataForm.id ? 'save' : 'update'}`,
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
