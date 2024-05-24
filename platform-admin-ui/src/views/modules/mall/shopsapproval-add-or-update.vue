<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '审核' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="申请人" prop="applyer">
        <el-input v-model="dataForm.applyer" :disabled="disabled" readOnly placeholder="申请人"></el-input>
      </el-form-item>
      <el-form-item label="身份证" prop="idCard">
        <el-input v-model="dataForm.idCard" :disabled="disabled" readOnly placeholder="身份证"></el-input>
      </el-form-item>
      <el-form-item label="身份有效期" prop="idCardValid">
        <el-input v-model="dataForm.idCardValid" :disabled="disabled" readOnly placeholder="店铺名字"></el-input>
      </el-form-item>
      <el-form-item label="手机号码" prop="telephone">
        <el-input v-model="dataForm.telephone" :disabled="disabled" readOnly placeholder="手机号码"></el-input>
      </el-form-item>
      <el-form-item label="店铺名字" prop="name">
        <el-input v-model="dataForm.name" :disabled="disabled" readOnly placeholder="店铺名字"></el-input>
      </el-form-item>
      <el-form-item label="身份证件正面" prop="idCardFront">
        <el-img v-model="dataForm.idCardFront" disabled>
        </el-img>
      </el-form-item>
      <el-form-item label="身份证件反面" prop="idCardReverse">
        <el-img v-model="dataForm.idCardReverse" disabled>
        </el-img>
      </el-form-item>
      <el-form-item label="审核结果" prop="applyStatus">
        <el-radio-group v-model="dataForm.applyStatus" :disabled="disabled">
          <el-radio :label="1">通过</el-radio>
          <el-radio :label="0">不通过</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核说明" prop="applyResultDesc">
        <el-input v-model="dataForm.applyResultDesc" :disabled="disabled" placeholder="审核说明"></el-input>
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
        shopsSn: '',
        imgUrl: '',
        userId: '',
        workTime: '',
        longitude: '',
        latitude: '',
        details: '',
        telephone: '',
        deleteStatus: '',
        shopDesc: '',
        sort: ''
      },
      dataRule: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
//          workTime: [
//            {required: true, message: '营业时间不能为空', trigger: 'blur'}
//          ],
        shopsSn: [
          {required: true, message: '店铺编码不能为空', trigger: 'blur'}
        ]
      },
      userList: JSON.parse(sessionStorage.getItem('userList') || '[]')
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
            url: `/mall/shops/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.shops
//                if (data.shops.workTime) {
//                  this.dataForm.workTime = data.shops.workTime.split('-')
//                }
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
              url: `/mall/shops/check`,
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
