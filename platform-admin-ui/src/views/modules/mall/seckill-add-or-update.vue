<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="秒杀商品" prop="goodsId">
        <el-select v-model="dataForm.goodsId" :disabled="disabled" clearable filterable placeholder="请选择">
          <el-option
            v-for="goods in goodsList"
            :key="goods.id"
            :label="goods.name"
            :value="goods.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="秒杀名称" prop="name">
        <el-input v-model="dataForm.name" :disabled="disabled" placeholder="秒杀名称" maxlength="40" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="图片链接" prop="listPicUrl">
        <el-tooltip content="建议像素200*200，大小不超过200KB">
          <el-img v-model="dataForm.listPicUrl" :disabled="disabled">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="秒杀金额" prop="price">
        <el-input-number v-model="dataForm.price" :disabled="disabled" controls-position="right" :min="0"
                         :precision="2"></el-input-number>
      </el-form-item>
      <el-form-item label="秒杀库存" prop="stock">
        <el-input-number v-model="dataForm.stock" :disabled="disabled" controls-position="right" :min="0"
                         step-strictly></el-input-number>
      </el-form-item>
      <el-form-item label="秒杀开启时间" prop="startTime">
        <el-date-picker type="datetime" v-model="dataForm.startTime" :disabled="disabled"
                        :picker-options="datePicker" class="width200"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="秒杀开启时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="秒杀结束时间" prop="endTime">
        <el-date-picker type="datetime" v-model="dataForm.endTime" :disabled="disabled"
                        :picker-options="datePickerEnd" class="width200"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="秒杀结束时间"></el-date-picker>
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
        goodsId: '',
        name: '',
        listPicUrl: '',
        price: '',
        stock: '',
        startTime: '',
        endTime: '',
        createTime: '',
        version: ''
      },
      dataRule: {
        goodsId: [
          {
            required: true,
            message: '秒杀商品不能为空',
            trigger: 'blur'
          }
        ],
        name: [
          {
            required: true,
            message: '秒杀名称不能为空',
            trigger: 'blur'
          }
        ],
        listPicUrl: [
          {
            required: true,
            message: '图片链接不能为空',
            trigger: 'blur'
          }
        ],
        price: [
          {
            required: true,
            message: '秒杀名称不能为空',
            trigger: 'blur'
          }
        ],
        stock: [
          {
            required: true,
            message: '秒杀库存不能为空',
            trigger: 'blur'
          }
        ],
        startTime: [
          {
            required: true,
            message: '秒杀开启时间不能为空',
            trigger: 'blur'
          }
        ],
        endTime: [
          {
            required: true,
            message: '秒杀结束时间不能为空',
            trigger: 'blur'
          }
        ]
      },
      goodsList: [],
      datePicker: {
        disabledDate: (time) => {
          return time.getTime() <= Date.now() - 3600 * 1000 * 24
        },
        firstDayOfWeek: 1
      },
      datePickerEnd: {
        disabledDate: (time) => {
          let date = new Date()
          if (this.dataForm.startTime) {
            date = new Date(this.dataForm.startTime)
          }
          return time.getTime() <= Date.now() - 3600 * 1000 * 24 || time.getTime() <= date
        },
        firstDayOfWeek: 1
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
            url: `/mall/seckill/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.seckill
            }
          })
        }
        this.$http({
          url: `/mall/goods/queryAll`,
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.goodsList = data.list
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
              url: `/mall/seckill/${!this.dataForm.id ? 'save' : 'update'}`,
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
