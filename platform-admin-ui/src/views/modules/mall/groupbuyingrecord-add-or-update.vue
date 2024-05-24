<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="商品">{{dataForm.goodsName}}</el-descriptions-item>
      <el-descriptions-item label="商品规格">{{dataForm.goodsDetail}}</el-descriptions-item>
      <el-descriptions-item label="用户">{{dataForm.nickname}}
        <el-img class="width120" v-model="dataForm.headImgUrl" :disabled="disabled">
        </el-img>
      </el-descriptions-item>
      <el-descriptions-item label="订单编号">{{dataForm.orderSn}}</el-descriptions-item>
      <el-descriptions-item label="参加人数">{{dataForm.joinNumber}}</el-descriptions-item>
      <el-descriptions-item label="成团人数">{{dataForm.groupNumber}}</el-descriptions-item>
      <el-descriptions-item label="到期时间">
        <el-tag
          type="danger">
          {{ dataForm.expireTime }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        <el-tag
          type="success"
          effect="dark">
          {{ dataForm.createTime }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="修改时间" v-if="dataForm.updateTime">
        <el-tag
          type="warning"
          effect="dark">
          {{ dataForm.updateTime }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="结束时间" v-if="dataForm.endTime">
        <el-tag
          type="info"
          effect="dark">
          {{ dataForm.endTime }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="拼团价格">
        <el-tag
          type="danger">
          {{ dataForm.price }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag v-if="dataForm.status === 0" size="small" type="info">拼团失败</el-tag>
        <el-tag v-if="dataForm.status === 1" size="small" type="warning">拼团中</el-tag>
        <el-tag v-if="dataForm.status === 2" size="small" type="success">拼团成功</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="是否团长">
        <el-switch
          v-model="dataForm.isLeader===1"
          disabled>
        </el-switch>
      </el-descriptions-item>
      <el-descriptions-item label="团长" v-if="dataForm.isLeader!==1">
        {{dataForm.leaderNickname}}
      </el-descriptions-item>
    </el-descriptions>
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
        id: 0,
        goodsId: '',
        goodsDetail: '',
        userId: '',
        isLeader: '',
        leaderNickname: '',
        orderSn: '',
        expireTime: '',
        joinNumber: '',
        number: '',
        price: '',
        status: '',
        createTime: '',
        updateTime: '',
        endTime: ''
      },
      dataRule: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
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
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/groupbuyingrecord/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.groupbuyingrecord
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
              url: `/mall/groupbuyingrecord/${!this.dataForm.id ? 'save' : 'update'}`,
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
