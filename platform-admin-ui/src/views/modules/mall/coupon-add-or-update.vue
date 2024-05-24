<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    append-to-body
    :visible.sync="visible">
    <el-form inline :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="优惠券标题" prop="title">
        <el-input v-model="dataForm.title" :disabled="disabled" placeholder="优惠券标题" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="优惠券编号" prop="couponSn">
        <el-input v-model="dataForm.couponSn" :disabled="disabled" placeholder="优惠券编号" class="width200"></el-input>
      </el-form-item>
      <el-form-item label="最低消费金额" prop="minPrice">
        <el-input-number :min="1" :precision="2" v-model="dataForm.minPrice" :disabled="disabled"
                         placeholder="最低消费金额"></el-input-number>
      </el-form-item>
      <el-form-item label="优惠券类型" prop="couponType">
        <el-radio-group v-model="dataForm.couponType" :disabled="disabled" class="width200">
          <el-radio-button :label="1">代金券</el-radio-button>
          <el-radio-button :label="2">折扣</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="优惠金额" prop="subPrice" v-if="dataForm.couponType === 1">
        <el-input v-model="dataForm.subPrice" :disabled="disabled" placeholder="优惠金额" class="width200">
          <template slot="append">元</template>
        </el-input>
      </el-form-item>
      <el-form-item label="折扣率" prop="discount" v-if="dataForm.couponType === 2">
        <el-input-number :max="9.9" :min="0" :step="0.01" :precision="2" v-model="dataForm.discount" class="width200"
                         :disabled="disabled" placeholder="折扣率"></el-input-number>
      </el-form-item>
      <el-form-item label="有效期开始时间" prop="beginTime">
        <el-date-picker type="datetime" v-model="dataForm.beginTime" :disabled="disabled" class="width200"
                        :picker-options="datePicker"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="有效期开始时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="有效期结束时间" prop="endTime">
        <el-date-picker type="datetime" v-model="dataForm.endTime" :disabled="disabled" class="width200"
                        :picker-options="datePicker"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="有效期结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="开始领取时间" prop="beginGetTime">
        <el-date-picker type="datetime" v-model="dataForm.beginGetTime" :disabled="disabled" class="width200"
                        :picker-options="datePicker"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="开始领取时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束领取时间" prop="endGetTime">
        <el-date-picker type="datetime" v-model="dataForm.endGetTime" :disabled="disabled" class="width200"
                        :picker-options="datePicker"
                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="结束领取时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="优惠券数量" prop="totalCount">
        <el-input-number :min="1" v-model="dataForm.totalCount" :disabled="disabled" class="width200"
                         placeholder="优惠券数量"></el-input-number>
      </el-form-item>
      <el-form-item label="已发放数量" prop="sendCount">
        <el-input-number :min="0" v-model="dataForm.sendCount" :disabled="disabled" class="width200"
                         placeholder="已发放数量"></el-input-number>
      </el-form-item>
      <el-form-item label="使用类型" prop="limitType">
        <el-radio-group v-model="dataForm.limitType" :disabled="disabled">
          <el-radio-button :label="0">通用券</el-radio-button>
          <el-radio-button :label="1">指定商品</el-radio-button>
          <el-radio-button :label="2">指定品牌</el-radio-button>
          <el-radio-button :label="3">新人券</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="指定商品" prop="goodsIds" v-if="dataForm.limitType === 1">
        <el-select v-model="dataForm.goodsIds" :disabled="disabled" clearable multiple filterable placeholder="请选择">
          <el-option
            v-for="goods in goodsList"
            :key="goods.id"
            :label="goods.name"
            :value="goods.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="指定品牌" prop="brandIds" v-if="dataForm.limitType === 2">
        <el-select v-model="dataForm.brandIds" :disabled="disabled" clearable multiple filterable placeholder="请选择">
          <el-option
            v-for="brand in brandList"
            :key="brand.id"
            :label="brand.name"
            :value="brand.id">
          </el-option>
        </el-select>
      </el-form-item>
      <br>
      <el-form-item label="每人限领数量" prop="limitUser">
        <el-input-number :min="1" v-model="dataForm.limitUser" :disabled="disabled" class="width200"
                         placeholder="每人限领数量"></el-input-number>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status" :disabled="disabled">
          <el-radio-button :label="1">正常</el-radio-button>
          <el-radio-button :label="2">过期</el-radio-button>
          <el-radio-button :label="3">禁用</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="1" class="width200"
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
    let validateMoney = (rule, value, callback) => {
      if (!/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value)) {
        callback(new Error('格式有误'))
      } else {
        if (value < 1) {
          callback(new Error('最少优惠1元'))
        }
        callback()
      }
    }
    let validateDiscount = (rule, value, callback) => {
      if (!/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value)) {
        callback(new Error('格式有误'))
      } else {
        if (value > 9.9) {
          callback(new Error('最大折扣为9.9'))
        }
        callback()
      }
    }
    return {
      disabled: false,
      visible: false,
      dataForm: {
        id: 0,
        title: '',
        couponSn: '',
        couponType: 1,
        minPrice: 1,
        subPrice: '',
        discount: '',
        beginTime: '',
        endTime: '',
        beginGetTime: '',
        endGetTime: '',
        totalCount: 1,
        sendCount: 0,
        limitType: 0,
        limitUser: 1,
        status: 1,
        sort: 1,
        goodsIds: [],
        brandIds: []
      },
      dataRule: {
        title: [{
          required: true,
          message: '优惠券标题不能为空',
          trigger: 'blur'
        }],
        couponSn: [{
          required: true,
          message: '优惠券编号不能为空',
          trigger: 'blur'
        }],
        subPrice: [{
          validator: validateMoney,
          trigger: 'blur'
        }],
        discount: [{
          validator: validateDiscount,
          trigger: 'blur'
        }],
        minPrice: [{
          required: true,
          message: '最低消费金额不能为空',
          trigger: 'blur'
        }],
        beginTime: [{
          required: true,
          message: '有效期开始时间不能为空',
          trigger: 'blur'
        }],
        endTime: [{
          required: true,
          message: '有效期结束时间不能为空',
          trigger: 'blur'
        }],
        beginGetTime: [{
          required: true,
          message: '开始领取时间不能为空',
          trigger: 'blur'
        }],
        endGetTime: [{
          required: true,
          message: '结束领取时间不能为空',
          trigger: 'blur'
        }],
        totalCount: [{
          required: true,
          message: '优惠券发放数量不能为空',
          trigger: 'blur'
        }],
        limitUser: [{
          required: true,
          message: '每人限领数量不能为空',
          trigger: 'blur'
        }]
      },
      goodsList: [],
      brandList: [],
      datePicker: this.picker()
    }
  },
  methods: {
    picker () {
      return {
        // 可选时间大于等于当前时间
        disabledDate (time) {
          return time.getTime() < Date.now()
        }
      }
    },
    init (id, disabled) {
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/coupon/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.coupon
            }
          })
        }
      })
      this.$http({
        url: '/mall/goods/queryAll',
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.goodsList = data.list
        }
      })
      this.$http({
        url: '/mall/brand/queryAll',
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.brandList = data.list
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/coupon/${!this.dataForm.id ? 'save' : 'update'}`,
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
