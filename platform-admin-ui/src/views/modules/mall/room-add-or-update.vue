<template>
  <el-dialog
    :title="!dataForm.roomid ? '新增' : '修改'"
    :close-on-click-modal="false"
    append-to-body
    :lock-scroll="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="150px">
      <el-form-item label="roomid" prop="roomid" v-if="false">
        <el-input v-model="dataForm.roomid"></el-input>
      </el-form-item>
      <el-form-item label="房间名" prop="name">
        <el-input v-model="dataForm.name"
                  placeholder="直播间名字，最短3个汉字，最长17个汉字，1个汉字相当于2个字符"></el-input>
      </el-form-item>
      <el-form-item label="直播间背景图" prop="coverImg">
        <el-tooltip content="建议像素1080*1920，大小不超过2M">
          <el-img v-model="dataForm.coverImg">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="直播间分享图" prop="shareImg">
        <el-tooltip content="建议像素800*640，大小不超过1M">
          <el-img v-model="dataForm.shareImg">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="购物直播频道封面图" prop="feedsImg">
        <el-tooltip content="建议像素800*800，大小不超过100KB">
          <el-img v-model="dataForm.feedsImg">
          </el-img>
        </el-tooltip>
      </el-form-item>
      <el-alert
        title="直播计划开始时间（开播时间需要在当前时间的10分钟后 并且 开始时间不能在 6 个月后）"
        type="warning">
      </el-alert>
      <el-form-item label="直播计划开始时间" prop="startTime">
        <el-date-picker
          :picker-options="datePicker"
          v-model="dataForm.startTime"
          type="datetime"
          value-format="timestamp"
          placeholder="直播计划开始时间">
        </el-date-picker>
      </el-form-item>
      <el-alert
        title="直播计划结束时间（开播时间和结束时间间隔不得短于30分钟，不得超过12小时）"
        type="warning">
      </el-alert>
      <el-form-item label="直播计划结束时间" prop="endTime">
        <el-date-picker
          :picker-options="datePicker"
          v-model="dataForm.endTime"
          type="datetime"
          value-format="timestamp"
          placeholder="直播计划开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="主播昵称" prop="anchorName">
        <el-input v-model="dataForm.anchorName"
                  placeholder="主播昵称，最短2个汉字，最长15个汉字，1个汉字相当于2个字符"></el-input>
      </el-form-item>
      <el-form-item label="主播微信号" prop="anchorWechat">
        <el-input v-model="dataForm.anchorWechat"
                  placeholder="主播微信号，如果未实名认证，需要先前往“小程序直播”小程序进行实名验证"></el-input>
      </el-form-item>
      <el-form-item label="主播副号微信号" prop="subAnchorWechat" v-if="!disabled">
        <el-input v-model="dataForm.subAnchorWechat"
                  placeholder="主播副号微信号，如果未实名认证，需要先前往“小程序直播”小程序进行实名验证"></el-input>
      </el-form-item>
      <el-form-item label="主播实名认证">
        <a href="https://res.wx.qq.com/op_res/9rSix1dhHfK4rR049JL0PHJ7TpOvkuZ3mE0z7Ou_Etvjf-w1J_jVX0rZqeStLfwh">https://res.wx.qq.com/op_res/9rSix1dhHfK4rR049JL0PHJ7TpOvkuZ3mE0z7Ou_Etvjf-w1J_jVX0rZqeStLfwh</a>
      </el-form-item>
      <el-form-item label="创建者微信号" prop="createrWechat" v-if="!disabled">
        <el-input v-model="dataForm.createrWechat"
                  placeholder="创建者微信号，不传入则此直播间所有成员可见。传入则此房间仅创建者、管理员、超管、直播间主播可见"></el-input>
      </el-form-item>
      <el-form-item label="官方收录" prop="isFeedsPublic">
        <el-radio-group v-model="dataForm.isFeedsPublic">
          <el-radio :label="0">关闭</el-radio>
          <el-radio :label="1">开启</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="直播类型" prop="type" v-if="!disabled">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="0">手机直播</el-radio>
          <el-radio :label="1">推流</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="横屏竖屏" prop="screenType" v-if="!disabled">
        <el-radio-group v-model="dataForm.screenType">
          <el-radio :label="0">竖屏</el-radio>
          <el-radio :label="1">横屏</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="点赞" prop="closeLike">
        <el-radio-group v-model="dataForm.closeLike">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="货架" prop="closeGoods">
        <el-radio-group v-model="dataForm.closeGoods">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="评论" prop="closeComment">
        <el-radio-group v-model="dataForm.closeComment">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="回放" prop="closeReplay">
        <el-radio-group v-model="dataForm.closeReplay">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="分享" prop="closeShare">
        <el-radio-group v-model="dataForm.closeShare">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="客服" prop="closeKf">
        <el-radio-group v-model="dataForm.closeKf">
          <el-radio :label="0">开启</el-radio>
          <el-radio :label="1">关闭</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button @click="dataFormSubmit()">确定</el-button>
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
        roomid: '',
        name: '',
        coverImg: '',
        startTime: '',
        endTime: '',
        anchorName: '',
        anchorWechat: '',
        subAnchorWechat: '',
        createrWechat: '',
        shareImg: '',
        feedsImg: '',
        isFeedsPublic: 1,
        type: 0,
        screenType: 0,
        closeLike: 0,
        closeGoods: 0,
        closeComment: 0,
        closeReplay: 0,
        closeShare: 0,
        closeKf: 0
      },
      dataRule: {
        name: [
          {required: true, message: '房间名不能为空', trigger: 'blur'}
        ],
        coverImg: [
          {required: true, message: '直播间背景图不能为空', trigger: 'blur'}
        ],
        startTime: [
          {required: true, message: '直播计划开始时间不能为空', trigger: 'blur'}
        ],
        endTime: [
          {required: true, message: '直播计划结束时间不能为空', trigger: 'blur'}
        ],
        anchorName: [
          {required: true, message: '主播昵称不能为空', trigger: 'blur'}
        ],
        anchorWechat: [
          {required: true, message: '主播微信号不能为空', trigger: 'blur'}
        ],
        shareImg: [
          {required: true, message: '直播间分享图不能为空', trigger: 'blur'}
        ],
        feedsImg: [
          {required: true, message: '购物直播频道封面图不能为空', trigger: 'blur'}
        ],
        closeLike: [
          {required: true, message: '点赞不能为空', trigger: 'blur'}
        ],
        closeGoods: [
          {required: true, message: '货架不能为空', trigger: 'blur'}
        ],
        closeComment: [
          {required: true, message: '评论不能为空', trigger: 'blur'}
        ]
      },
      datePicker: {
        // 可选时间大于等于当前时间
        disabledDate (time) {
          return time.getTime() <= Date.now() - 3600 * 1000 * 24 || time.getTime() >= Date.now() + 3600 * 1000 * 24 * 180
        },
        firstDayOfWeek: 1
      }
    }
  },
  methods: {
    init (roomid) {
      if (roomid) {
        this.disabled = true
      }
      this.dataForm.roomid = roomid || ''
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.roomid) {
          this.$http({
            url: `/mall/room/info/${this.dataForm.roomid}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.room
              this.dataForm.startTime = data.room.startTime * 1000
              this.dataForm.endTime = data.room.endTime * 1000
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
              url: `/mall/room/${!this.dataForm.roomid ? 'save' : 'update'}`,
              method: 'post',
              data: this.dataForm,
              showError: false
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500
                })
                this.visible = false
                this.$emit('refreshDataList')
              } else {
                if (data.code === 300036) {
                  this.$alert(`<img style="width: 380px" src="${data.msg}"/>`, '主播微信号未实名认证，请扫码认证', {
                    dangerouslyUseHTMLString: true,
                    showClose: false
                  })
                } else {
                  this.$message({
                    message: 'errorCode:' + data.code + ',errorMsg:' + data.msg,
                    type: 'error',
                    duration: 5000
                  })
                }
              }
            })
          }
        })
    }
  }
}
</script>
