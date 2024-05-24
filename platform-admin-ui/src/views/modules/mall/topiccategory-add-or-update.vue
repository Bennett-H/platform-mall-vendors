<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="文章类别" prop="title">
        <el-input v-model="dataForm.title" :disabled="disabled" placeholder="文章类别名称"></el-input>
      </el-form-item>
      <el-form-item label="图片链接" prop="picUrl">
        <el-tooltip content="建议像素430*240，大小不超过100KB">
          <el-img v-model="dataForm.picUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="icon链接" prop="iconUrl">
        <el-tooltip content="建议像素430*240，大小不超过100KB">
          <el-img v-model="dataForm.iconUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="类别类型" prop="type">
        <el-select v-model="dataForm.type" placeholder="请选择" :disabled="disabled">
          <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="小程序内链接" prop="appUrl">
        <el-input v-model="dataForm.appUrl" :disabled="disabled" placeholder="小程序内链接"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number :min="0" v-model="dataForm.sort" :disabled="disabled" placeholder="排序"></el-input-number>
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
        id: 0,
        title: '',
        picUrl: '',
        iconUrl: '',
        appUrl: '',
        type: '',
        sort: 0
      },
      dataRule: {
        title: [
          {
            required: true,
            message: '文章类别名称不能为空',
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
            url: `/mall/topiccategory/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.topiccategory
            }
          })
        }
      })
      this.getTopicCategoryType()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/topiccategory/${!this.dataForm.id ? 'save' : 'update'}`,
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
    getTopicCategoryType () {
      this.$http({
        url: '/sys/dict/list',
        method: 'get',
        params: {
          'code': 'topic_big_category'
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
