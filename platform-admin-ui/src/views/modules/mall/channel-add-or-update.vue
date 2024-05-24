<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="dataForm.name" :disabled="disabled" placeholder="名称" maxlength="4" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="跳转链接" prop="url">
        <el-input v-model="dataForm.url" :disabled="disabled" placeholder="跳转链接" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status" :disabled="disabled" class="width200">
          <el-radio-button :label="0">隐藏</el-radio-button>
          <el-radio-button :label="1">显示</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="ICON链接" prop="iconUrl">
        <el-tooltip content="建议像素200*200，大小不超过50KB">
          <el-img v-model="dataForm.iconUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="频道类型" prop="type">
        <el-select v-model="dataForm.type" :disabled="disabled" placeholder="请选择">
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="颜色" prop="color">
        <el-color-picker v-model="dataForm.color" :disabled="disabled"></el-color-picker>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="0"
                         step-strictly></el-input-number>
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
      typeList: [],
      dataForm: {
        id: '',
        name: '',
        iconUrl: '',
        sort: '',
        type: '',
        color: '',
        status: 1,
        url: ''
      },
      dataRule: {
        name: [{
          required: true,
          message: '名称不能为空',
          trigger: 'blur'
        }],
        url: [{
          required: true,
          message: '跳转链接不能为空',
          trigger: 'blur'
        }],
        iconUrl: [{
          required: true,
          message: 'ICON链接不能为空',
          trigger: 'blur'
        }]
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
            url: `/mall/channel/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.channel
            }
          })
        }
      })
      this.getMallChannelType()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/channel/${!this.dataForm.id ? 'save' : 'update'}`,
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
    getMallChannelType () {
      this.$http({
        url: '/sys/dict/list',
        method: 'get',
        params: {
          'code': 'mall_channel_type'
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
