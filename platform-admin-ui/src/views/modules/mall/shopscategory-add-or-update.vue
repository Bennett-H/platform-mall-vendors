<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="分类名称" prop="name">
        <el-input v-model="dataForm.name" :disabled="disabled" placeholder="分类名称" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="所属店铺" prop="shopsId">
        <el-select v-model="dataForm.shopsId" :disabled="disabled" clearable filterable placeholder="请选择">
          <el-option
            v-for="shop in shops"
            :key="shop.id"
            :label="shop.name"
            :value="shop.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status" :disabled="disabled" class="width200">
          <el-radio-button :label="0">隐藏</el-radio-button>
          <el-radio-button :label="1">显示</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="0"
                         step-strictly></el-input-number>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-tooltip content="建议像素50*50，大小不超过10KB">
          <el-img v-model="dataForm.icon" :disabled="disabled">
          </el-img>
        </el-tooltip>
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
        shopsId: '',
        status: 1,
        sort: '',
        icon: ''
      },
      dataRule: {
        name: [
          {
            required: true,
            message: '名称不能为空',
            trigger: 'blur'
          }
        ],
        status: [
          {
            required: true,
            message: '状态不能为空',
            trigger: 'blur'
          }
        ],
        shopsId: [
          {
            required: true,
            message: '所属店铺不能为空',
            trigger: 'blur'
          }
        ]
      },
      shops: []
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
            url: `/mall/shopscategory/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.shopscategory
            }
          })
        }
        this.$http({
          url: `/mall/shops/queryMyShop`,
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.shops = data.list
          }
        })
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/shopscategory/${!this.dataForm.id ? 'save' : 'update'}`,
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
