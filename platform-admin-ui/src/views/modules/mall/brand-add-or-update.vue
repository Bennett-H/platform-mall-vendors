<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="品牌名称" prop="name">
        <el-input v-model="dataForm.name" :disabled="disabled" placeholder="品牌名称" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="显示" prop="isShow">
        <el-radio-group v-model="dataForm.isShow" :disabled="disabled" class="width200">
          <el-radio-button :label="0">否</el-radio-button>
          <el-radio-button :label="1">是</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="品牌大图" prop="listPicUrl">
        <el-tooltip content="建议像素750*420，大小不超过50KB">
          <el-img v-model="dataForm.listPicUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="品牌小图" prop="appListPicUrl">
        <el-tooltip content="建议像素750*420，大小不超过50KB">
          <el-img v-model="dataForm.appListPicUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="底价" prop="floorPrice">
        <el-input-number v-model="dataForm.floorPrice" :disabled="disabled" controls-position="right" :min="0"
                         :precision="2"></el-input-number>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="0"
                         step-strictly></el-input-number>
      </el-form-item>
      <el-form-item label="品牌描述" prop="simpleDesc">
        <el-input type="textarea" :rows="5" v-model="dataForm.simpleDesc" :disabled="disabled" class="width200"
                  placeholder="品牌描述"></el-input>
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
        name: '',
        listPicUrl: '',
        appListPicUrl: '',
        simpleDesc: '',
        sort: '',
        isShow: 1,
        floorPrice: ''
      },
      dataRule: {
        name: [{
          required: true,
          message: '名称不能为空',
          trigger: 'blur'
        }],
        listPicUrl: [{
          required: true,
          message: '品牌大图不能为空',
          trigger: 'blur'
        }],
        floorPrice: [{
          required: true,
          message: '底价不能为空',
          trigger: 'blur'
        }],
        isShow: [{
          required: true,
          message: '显示不能为空',
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
            url: `/mall/brand/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.brand
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
              url: `/mall/brand/${!this.dataForm.id ? 'save' : 'update'}`,
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
