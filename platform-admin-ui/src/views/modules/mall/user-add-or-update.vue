<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看会员详情'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-descriptions>
      <el-descriptions-item>
        <template slot="label">
          用户头像
        </template>
        <el-image :src="dataForm.headImgUrl" style="width: 100px; height: 100px">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{dataForm.userName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-male"></i>
          性别
        </template>
        <el-tag v-if="dataForm.gender === 0" size="small" type="error">未知</el-tag>
        <el-tag v-else-if="dataForm.gender === 1" size="small" type="success">男</el-tag>
        <el-tag v-else-if="dataForm.gender === 2" size="small" type="danger">女</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        {{dataForm.mobile}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-sunny"></i>
          生日
        </template>
        {{dataForm.birthday}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-timer"></i>
          注册时间
        </template>
        {{dataForm.registerTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-map-location"></i>
          注册ip
        </template>
        {{dataForm.registerIp}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-alarm-clock"></i>
          最后登录时间
        </template>
        {{dataForm.lastLoginTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-map-location"></i>
          最后登录IP
        </template>
        {{dataForm.lastLoginIp}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          aliUserId
        </template>
        {{dataForm.aliUserId}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          maOpenId
        </template>
        {{dataForm.openId}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          mpOpenId
        </template>
        {{dataForm.mpOpenId}}
      </el-descriptions-item>
      <el-descriptions-item>
<!--        <template slot="label">-->
<!--          <i class="el-icon-office-building"></i>-->
<!--          qqOpenId-->
<!--        </template>-->
<!--        {{dataForm.qqOpenId}}-->
        <template slot="label">
          <i class="el-icon-office-building"></i>
          会员标签
        </template>
        <el-select multiple @change="$forceUpdate()" v-model="dataForm.userTagsList" :disabled="disabled"  placeholder="请选择" class="width120">
          <el-option
            v-for="tag in tagList"
            :key="tag.value"
            :label="tag.name"
            :value="tag.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          绑定分销用户ID
        </template>
        {{dataForm.referUserId}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-star-off"></i>
          是否关注
        </template>
        <el-tag v-if="dataForm.subscribe === 0" size="small" type="danger">否</el-tag>
        <el-tag v-else-if="dataForm.subscribe === 1" size="small" type="success">是</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          关注时间
        </template>
        {{dataForm.subscribeTime}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-coin"></i>
          余额
        </template>
        {{dataForm.balance}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-wallet"></i>
          总积分
        </template>
        {{dataForm.integral}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-bank-card"></i>
          会员等级
        </template>
        <el-select v-model="dataForm.userLevelId" :disabled="disabled" filterable placeholder="请选择" class="width120">
          <el-option
            v-for="level in levelList"
            :key="level.id"
            :label="level.name"
            :value="level.id">
          </el-option>
        </el-select>
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
        id: '',
        userName: '',
        password: '',
        gender: '',
        birthday: '',
        registerTime: '',
        lastLoginTime: '',
        lastLoginIp: '',
        userLevelId: '',
        nickname: '',
        mobile: '',
        registerIp: '',
        headImgUrl: '',
        aliUserId: '',
        mpOpenId: '',
        qqOpenId: '',
        openId: '',
        unionId: '',
        subscribe: '',
        subscribeTime: '',
        integral: '',
        balance: '',
        userTags: '',
        referUserId: '',
        userTagsList: []
      },
      levelList: [],
      tagList: []
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
            url: `/mall/user/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.user
              if (data.user.userTags) {
                this.dataForm.userTagsList = data.user.userTags.split(',')
              }
            }
          })
        }
        this.$http({
          url: '/mall/userlevel/queryAll',
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.levelList = data.list
          }
        })
        this.$http({
          url: '/sys/dict/queryByCode?code=mall_cust_tag',
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.tagList = data.list
          }
        })
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.dataForm.userTags = this.dataForm.userTagsList.join(',')
      this.$http({
        url: `/mall/user/${!this.dataForm.id ? 'save' : 'update'}`,
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
  }
}
</script>
