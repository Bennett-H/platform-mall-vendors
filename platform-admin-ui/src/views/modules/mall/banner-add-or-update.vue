<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="类型" prop="mediaType">
        <el-radio-group v-model="dataForm.mediaType" :disabled="disabled">
          <el-radio-button :label="0">图片</el-radio-button>
          <el-radio-button :label="1">链接</el-radio-button>
          <el-radio-button :label="2">文本</el-radio-button>
          <el-radio-button :label="3">视频</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="分区" prop="type">
        <el-select v-model="dataForm.type" :disabled="disabled" placeholder="请选择">
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="enabled">
        <el-radio-group v-model="dataForm.enabled" :disabled="disabled">
          <el-radio-button :label="0">禁用</el-radio-button>
          <el-radio-button :label="1">正常</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片链接" prop="imageUrl">
        <el-tooltip content="建议像素390*220，大小不超过200KB">
          <el-img v-model="dataForm.imageUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="dataForm.title" :disabled="disabled" placeholder="" maxlength="128"
                  class="width200"></el-input>
      </el-form-item>
      <el-form-item label="小程序页面" prop="link" v-if="dataForm.mediaType === 1">
        <el-input v-model="dataForm.link" :disabled="disabled" placeholder="小程序页面"></el-input>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker type="datetime" :picker-options="datePicker" v-model="dataForm.endTime" :disabled="disabled"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="视频链接" prop="videoUrl" v-if="dataForm.mediaType === 3">
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
      <el-form-item label="文本内容" prop="content" v-if="dataForm.mediaType === 2">
        <ueditor v-model="dataForm.content" :disabled="disabled" placeholder="文本内容"></ueditor>
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
      url: '',
      disabled: false,
      visible: false,
      videoFlag: false,
      videoUploadPercent: 0,
      typeList: [],
      dataForm: {
        id: '',
        mediaType: 0,
        title: '',
        imageUrl: '',
        link: '',
        videoUrl: '',
        content: '',
        endTime: '',
        enabled: 1
      },
      dataRule: {
        title: [{
          required: true,
          message: '标题不能为空',
          trigger: 'blur'
        }],
        imageUrl: [{
          required: true,
          message: '图片不能为空',
          trigger: 'blur'
        }],
        endTime: [{
          required: true,
          message: '结束时间不能为空',
          trigger: 'blur'
        }]
      },
      datePicker: this.picker()
    }
  },
  methods: {
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
    picker () {
      return {
        // 可选时间大于等于当前时间
        disabledDate (time) {
          return time.getTime() < Date.now()
        }
      }
    },
    init (id, disabled) {
      this.url = this.$http.BASE_URL + `/sys/oss/upload?token=${this.$cookie.get('token')}`
      this.disabled = disabled
      this.dataForm = {
        id: id || '',
        mediaType: 0,
        title: '',
        imageUrl: '',
        link: '',
        videoUrl: '',
        content: '',
        endTime: '',
        enabled: 1
      }
      this.visible = true
      this.getMallBannerType()
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/banner/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.banner
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
              url: `/mall/banner/${!this.dataForm.id ? 'save' : 'update'}`,
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
    },
    getMallBannerType () {
      this.$http({
        url: '/sys/dict/list',
        method: 'get',
        params: {
          'code': 'mall_banner_type'
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.typeList = data.page.records
        } else {
          this.typeList = []
        }
      })
    }
  }
}
</script>
