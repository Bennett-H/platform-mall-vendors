<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    append-to-body
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="150px">
      <el-form-item label="商品图片" prop="coverImgUrl">
        <el-tooltip content="建议像素小于300*300，大小不超过500KB">
          <el-img v-model="dataForm.coverImgUrl" :disabled="dataForm.auditStatus === 2 || disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="商品名" prop="name">
        <el-input v-model="dataForm.name" :disabled="dataForm.auditStatus === 2 || disabled"
                  placeholder="商品名称，最长14个汉字，1个汉字相当于2个字符" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="商品url" prop="url">
        <el-input v-model="dataForm.url" :disabled="dataForm.auditStatus === 2 || disabled"
                  placeholder="pages/index/index" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="价格类型" prop="priceType">
        <el-radio-group v-model="dataForm.priceType" :disabled="disabled">
          <el-radio-button :label="1">一口价</el-radio-button>
          <el-radio-button :label="2">价格区间</el-radio-button>
          <el-radio-button :label="3">折扣价</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="dataForm.priceType === 2 ?'商品价格':'原价'" prop="price">
        <el-input-number v-model="dataForm.price" :disabled="disabled" controls-position="right" :min="0"
                         :precision="2"></el-input-number>
      </el-form-item>
      <el-form-item v-if="dataForm.priceType !== 1" :label="dataForm.priceType === 2 ?'商品价格':'现价'" prop="price2">
        <el-input-number v-model="dataForm.price2" :disabled="disabled" controls-position="right" :min="0"
                         :precision="2"></el-input-number>
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
        goodsId: '',
        coverImgUrl: '',
        name: '',
        priceType: 1,
        price: '',
        price2: '',
        url: ''
      },
      dataRule: {
        name: [
          {required: true, message: '商品名不能为空', trigger: 'blur'}
        ],
        coverImgUrl: [
          {required: true, message: '商品图片不能为空', trigger: 'blur'}
        ],
        priceType: [
          {required: true, message: '价格类型不能为空', trigger: 'blur'}
        ],
        price: [
          {required: true, message: '商品价格不能为空', trigger: 'blur'}
        ],
        price2: [
          {required: true, message: '商品价格不能为空', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '商品url不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init (id, disabled) {
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.dataForm.auditStatus = ''
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/roomallgoods/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.roomallgoods
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
              url: `/mall/roomallgoods/${!this.dataForm.id ? 'save' : 'update'}`,
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
