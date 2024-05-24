<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px" inline>
      <el-tabs tab-position="left" v-model="activeName">
        <el-tab-pane label="通用信息" name="first">
          <el-form-item label="名称" prop="name">
            <el-input v-model="dataForm.name" :disabled="disabled" placeholder="名称" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="商品编码" prop="goodsSn">
            <el-input v-model="dataForm.goodsSn" :disabled="disabled" placeholder="商品编码" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="商品库存" prop="goodsNumber">
            <el-input-number v-model="dataForm.goodsNumber" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="积分价格" prop="retailPrice">
            <el-input-number v-model="dataForm.retailPrice" :disabled="disabled" controls-position="right" :min="1"
                             :precision="0"></el-input-number>
          </el-form-item>
          <el-form-item label="关键词" prop="keywords">
            <el-input v-model="dataForm.keywords" :disabled="disabled" placeholder="商品检索关键词" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="简明介绍" prop="goodsBrief">
            <el-input v-model="dataForm.goodsBrief" :disabled="disabled" placeholder="商品一句话描述" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="销量" prop="sales">
            <el-input-number v-model="dataForm.sales" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="详细描述">
          <el-row>
            <el-form-item label="视频上传">
              <!-- action必选参数, 上传的地址 -->
              <el-upload class="avatar-uploader el-upload-text" :action="url" :show-file-list="false"
                         :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo"
                         :on-progress="uploadVideoProcess">
                <video v-if="dataForm.videoUrl && !videoFlag" :src="dataForm.videoUrl"
                       style="width: 400px;height: 300px"
                       controls="controls">您的浏览器不支持视频播放
                </video>
                <i v-else-if="!dataForm.videoUrl && !videoFlag"
                   class="el-icon-plus avatar-uploader-icon"></i>
                <el-progress v-if="videoFlag" type="circle" :percentage="videoUploadPercent"
                             style="margin-top:30px;"></el-progress>
              </el-upload>
              <p class="text">请保证视频格式正确，且不超过10M</p>
            </el-form-item>
          </el-row>
          <el-form-item label="列表图" prop="listPicUrl">
            <el-tooltip content="建议像素200*200，大小不超过200KB">
              <el-img v-model="dataForm.listPicUrl" :disabled="disabled">
              </el-img>
            </el-tooltip>
          </el-form-item>
          <ueditor v-model="dataForm.goodsDesc" :disabled="disabled" placeholder="商品描述"></ueditor>
        </el-tab-pane>
      </el-tabs>
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
      url: '',
      videoFlag: false,
      videoUploadPercent: 0,
      activeName: 'first',
      disabled: false,
      visible: false,
      dataForm: {
        id: 0,
        name: '',
        goodsSn: '',
        goodsNumber: '',
        isOnSale: '',
        isDelete: '',
        listPicUrl: '',
        keywords: '',
        goodsBrief: '',
        retailPrice: '',
        goodsDesc: '',
        sort: '',
        sales: '',
        createUserId: '',
        createTime: '',
        createUserOrgNo: '',
        videoUrl: ''
      },
      dataRule: {
        name: [{
          required: true,
          message: '名称不能为空',
          trigger: 'blur'
        }],
        goodsSn: [{
          required: true,
          message: '商品编码不能为空',
          trigger: 'blur'
        }],
        goodsNumber: [{
          required: true,
          message: '商品库存不能为空',
          trigger: 'blur'
        }],
        listPicUrl: [{
          required: true,
          message: '列表图不能为空',
          trigger: 'blur'
        }],
        retailPrice: [{
          required: true,
          message: '价格不能为空',
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    init (id, disabled) {
      this.url = this.$http.BASE_URL + `/sys/oss/upload?token=${this.$cookie.get('token')}`
      this.activeName = 'first'
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/integralgoods/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.integralgoods
            }
          })
        } else {
          this.dataForm = {
            id: 0,
            name: '',
            goodsSn: '',
            goodsNumber: '',
            isOnSale: '',
            isDelete: '',
            listPicUrl: '',
            keywords: '',
            goodsBrief: '',
            retailPrice: '',
            goodsDesc: '',
            sort: '',
            sales: '',
            createUserId: '',
            createTime: '',
            createUserOrgNo: '',
            videoUrl: ''
          }
        }
      })
    },
    beforeUploadVideo (file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (['video/mp4', 'video/ogg', 'video/x-flv', 'video/x-msvideo', 'video/x-ms-wmv', 'video/rmvb'].indexOf(file.type) === -1) {
        this.$message.error('请上传正确的视频格式')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传视频大小不能超过10MB哦!')
        return false
      }
    },
    uploadVideoProcess (event, file, fileList) {
      this.videoFlag = true
      this.videoUploadPercent = parseInt(file.percentage.toFixed(0))
    },
    // 获取上传地址
    handleVideoSuccess (response, file) {
      this.videoFlag = false
      this.videoUploadPercent = 0
      if (response && response.code === 0) {
        this.dataForm.videoUrl = response.url
      } else {
        this.$message.error('视频上传失败，请重新上传！')
      }
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/integralgoods/${!this.dataForm.id ? 'save' : 'update'}`,
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
