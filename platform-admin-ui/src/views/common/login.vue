<template>
  <div class="site-wrapper site-page--login min300">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="brand-info">
          <h2 class="brand-info__text">九龄商城商户管理平台</h2>
<!--          <p class="brand-info__intro">商户管理平台</p>-->
        </div>
        <div class="login-main" v-show="isLogin">
          <h3 class="login-title">管理员登录</h3>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
                   status-icon>
            <el-form-item prop="userName">
              <el-input v-model="dataForm.userName" placeholder="帐号" autofocus></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" placeholder="验证码">
                  </el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt=""
                       style="height: 32px; width: 96px; border-radius: 4px;">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-row type="flex" justify="end">
                <el-button type="text" style="padding: 0" @click="register()">商家入驻申请>></el-button>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dataFormSubmit()" style="width: 100%">登录</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="copyright_footer" v-show="isLogin">
<!--          <el-link type="info" target="_blank"-->
<!--                   href="https://platform-wxmall.oss-cn-beijing.aliyuncs.com/copyright.jpg">软著登记号：2019SR107256-->
<!--          </el-link>-->
<!--          <p class="brand-info__intro">-->
<!--            <el-link type="info" target="_blank" href="http://www.beian.miit.gov.cn">安徽微同科技有限公司 © 2019 |-->
<!--              皖ICP备18002832号-1-->
<!--            </el-link>-->
<!--          </p>-->
        </div>

        <div class="register-main" v-show="!isLogin">
          <el-card :body-style="{ padding: '3px' }">
            <h3 class="login-title">商家入驻</h3>
            <el-form :model="registerDataForm" :rules="registerDataRule" ref="registerDataForm" label-width="140px">
              <el-row>
                <el-col :span="10">
                  <h4 class="login-title">账号信息</h4>
                  <el-form-item prop="telephone" label="手机号" label-width="100px">
                    <el-input v-model="registerDataForm.telephone" placeholder="手机号"></el-input>
                  </el-form-item>
<!--                  <el-form-item prop="smscode" label="验证码" :disabled="registerDataForm.start" label-width="90px">-->
<!--                    <el-input v-model="registerDataForm.smscode" placeholder="验证码" style="width: 230px"></el-input>-->
<!--                    <div v-if="registerDataForm.start" style="float: right;">-->
<!--                      <countdown :start="registerDataForm.start" :value="registerDataForm.time"-->
<!--                                 @on-finish="registerDataForm.start = false"/>-->
<!--                      秒后重发-->
<!--                    </div>-->
<!--                    <el-button v-else type="primary" @click="handleCode()">获取验证码</el-button>-->
<!--                  </el-form-item>-->
                  <el-form-item prop="password" label="密码" label-width="90px">
                    <el-input type="password" v-model="registerDataForm.password" placeholder="密码"></el-input>
                  </el-form-item>
                  <el-form-item prop="confPassword" label="确认密码" label-width="90px">
                    <el-input type="password" v-model="registerDataForm.confPassword" placeholder="确认密码"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="14">
                  <h4 class="login-title">店铺和个人信息</h4>
                  <el-form-item prop="name" label="店铺名称">
                    <el-input v-model="registerDataForm.name" placeholder="店铺名称"></el-input>
                  </el-form-item>
                  <el-form-item prop="applyer" label="法人">
                    <el-input v-model="registerDataForm.applyer" placeholder="法人"></el-input>
                  </el-form-item>
                  <el-form-item prop="idCard" label="身份证号">
                    <el-input v-model="registerDataForm.idCard" placeholder="身份证号"></el-input>
                  </el-form-item>
                  <el-form-item prop="idNoStaTime" label="身份证有效期开始">
                    <el-date-picker
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                      v-model="registerDataForm.idNoStaTime"
                      type="date">
                    </el-date-picker>
                  </el-form-item>
                  <el-form-item prop="idNoEndTime" label="身份证有效期截止">
                    <el-date-picker
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                      v-model="registerDataForm.idNoEndTime"
                      type="date">
                    </el-date-picker>
                  </el-form-item>

                  <el-form-item label="身份证件正面" prop="idCardFront">
                    <el-upload
                      :action="uploadUrl"
                      :before-upload="beforeUploadHandle"
                      :on-success="successIdCardFrontHandle"
                      :on-error="handleError"
                      list-type="picture">
                      <el-input v-model="registerDataForm.idCardFront" style="width: 200px" :disabled="true"
                                placeholder="身份证件正面">
                        <el-button v-if="registerDataForm.idCardFront" slot="append" icon="el-icon-view"
                                   @click.stop="openImg(registerDataForm.idCardFront)"></el-button>
                      </el-input>
                      <el-button size="small" plain>点击上传</el-button>
                      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                    </el-upload>
                  </el-form-item>
                  <el-form-item label="身份证件反面" prop="idCardReverse">
                    <el-upload
                      :action="uploadUrl"
                      :before-upload="beforeUploadHandle"
                      :on-success="successIdCardReverseHandle"
                      :on-error="handleError"
                      list-type="picture">
                      <el-input v-model="registerDataForm.idCardReverse" style="width: 200px" :disabled="true"
                                placeholder="身份证件反面">
                        <el-button v-if="registerDataForm.idCardReverse" slot="append" icon="el-icon-view"
                                   @click.stop="openImg(registerDataForm.idCardReverse)"></el-button>
                      </el-input>
                      <el-button size="small" plain>点击上传</el-button>
                    </el-upload>
                  </el-form-item>
                  <el-form-item prop="companyName" label="公司名称">
                    <el-input v-model="registerDataForm.companyName" placeholder="公司名称"></el-input>
                  </el-form-item>
                  <el-form-item prop="companyLicense" label="营业执照">
                    <el-upload
                      :action="uploadUrl"
                      :before-upload="beforeUploadHandle"
                      :on-success="successLicenseHandle"
                      :on-error="handleError"
                      list-type="picture">
                      <el-input v-model="registerDataForm.companyLicense" style="width: 200px" :disabled="true"
                                placeholder="营业执照">
                        <el-button v-if="registerDataForm.companyLicense" slot="append" icon="el-icon-view"
                                   @click.stop="openImg(registerDataForm.companyLicense)"></el-button>
                      </el-input>
                      <el-button size="small" plain>点击上传</el-button>
                    </el-upload>
                  </el-form-item>
                </el-col>

              </el-row>
              <el-row>
                <el-checkbox v-model="canRegister" text-color="#ffffff">我确认已阅读并了解
                  <el-button @click="dialogVisible=true" type="text">商家入驻条例</el-button>
                </el-checkbox>
              </el-row>
              <el-row>
              </el-row>
              <el-row type="flex" justify="center">
                <el-button type="warning" style="width: 40%" @click="disableRegister()">取消</el-button>
                <el-button type="primary" style="width: 40%" :disabled="!canRegister" @click="registerDataFormSubmit()">
                  确认
                </el-button>
              </el-row>
            </el-form>

            <el-dialog
              title="温馨提示"
              :visible.sync="dialogVisible"
              width="30%">
              <h3 style="color: red">商家入驻须知</h3>
              <span>此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍此处是商城介绍。</span>
              <span slot="footer" class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
              </span>
            </el-dialog>
          </el-card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import {getUUID} from '@/utils'
import {Encrypt} from '@/utils/AESUtils'
import {isMobile} from '@/utils/validate'

export default {
  components: {
    countdown: () => import('@/components/countdown')
  },
  data () {
    const checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('手机号不能为空'))
      } else {
        const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error('请输入正确的手机号'))
        }
      }
    }
    const checkIDCardNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('身份证不能为空'))
      }
      let aCity = {
        11: '北京',
        12: '天津',
        13: '河北',
        14: '山西',
        15: '内蒙古',
        21: '辽宁',
        22: '吉林',
        23: '黑龙江',
        31: '上海',
        32: '江苏',
        33: '浙江',
        34: '安徽',
        35: '福建',
        36: '江西',
        37: '山东',
        41: '河南',
        42: '湖北',
        43: '湖南',
        44: '广东',
        45: '广西',
        46: '海南',
        50: '重庆',
        51: '四川',
        52: '贵州',
        53: '云南',
        54: '西藏',
        61: '陕西',
        62: '甘肃',
        63: '青海',
        64: '宁夏',
        65: '新疆',
        71: '台湾',
        81: '香港',
        82: '澳门',
        91: '国外'
      }
      let iSum = 0
      if (!/^\d{17}(\d|x)$/i.test(value)) {
        return callback(new Error('你输入的身份证长度或格式错误'))
      }
      value = value.replace(/x$/i, 'a')
      if (aCity[parseInt(value.substr(0, 2))] == null) {
        return callback(new Error('你的身份证地区非法'))
      }
      let sBirthday = value.substr(6, 4) + '-' + Number(value.substr(10, 2)) + '-' + Number(value.substr(12, 2))
      let d = new Date(sBirthday.replace(/-/g, '/'))
      if (sBirthday !== (d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate())) {
        return callback(new Error('身份证上的出生日期非法'))
      }
      for (var i = 17; i >= 0; i--) {
        iSum += (Math.pow(2, i) % 11) * parseInt(value.charAt(17 - i), 11)
      }
      if (iSum % 11 !== 1) {
        return callback(new Error('你输入的身份证号非法'))
      }
      return callback()
    }

    return {
      dialogVisible: false,
      canRegister: false,
      isLogin: true,
      registerDataForm: {
        applyer: '',
        telephone: '',
        idCard: '',
        name: '',
        // idValidityPeriodList: [],
        idNoStaTime: '',
        idNoEndTime: '',
        smscode: null,
        time: 60,
        start: false,
        password: '',
        confPassword: '',
        idCardFront: '',
        idCardReverse: '',
        companyMame: '',
        companyLicense: ''
      },

      dataForm: {
        userName: '',
        password: '',
        uuid: '',
        captcha: ''
      },
      dataRule: {
        userName: [
          {
            required: true,
            message: '帐号不能为空',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '密码不能为空',
            trigger: 'blur'
          }
        ]
        // captcha: [
        //   {
        //     required: true,
        //     message: '验证码不能为空',
        //     trigger: 'blur'
        //   }
        // ]
      },
      captchaPath: '',

      uploadUrl: '',
      idCartPicList: [],
      registerDataRule: {
        idCard: [
          {validator: checkIDCardNumber, trigger: 'blur', required: true}
        ],
        applyer: [
          {required: true, message: '姓名不能为空', trigger: 'blur'}
        ],
        // smscode: [
        //   {required: true, message: '短信验证码不能为空', trigger: 'blur'}
        // ],
        telephone: [
          {validator: checkPhone, trigger: 'blur', required: true}
        ],
        name: [
          {required: true, message: '店铺名称不能为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'}
        ],
        confPassword: [
          {required: true, message: '确认密码不能为空', trigger: 'blur'}
        ],
        idCardFront: [
          {required: true, message: '请上传身份证正面', trigger: 'blur'}
        ],
        idCardReverse: [
          {required: true, message: '请上传身份证反面', trigger: 'blur'}
        ],
        companyMame: [
          {required: true, message: '公司名称不能为空', trigger: 'blur'}
        ],
        companyLicense: [
          {required: true, message: '营业执照不能为空', trigger: 'blur'}
        ],
        idNoStaTime: [
          {required: true, message: '有效期开始不能为空', trigger: 'blur'}
        ],
        idNoEndTime: [
          {required: true, message: '有效期截止不能为空', trigger: 'blur'}
        ]
        // idValidityPeriodList: [
        //   {validator: checkIdValidityPeriodList, trigger: 'blur', required: true}
        // ]
      }
    }
  },
  created () {
    this.uploadUrl = this.$http.BASE_URL + `/sys/oss/uploadIgnore`
    this.getCaptcha()
    // this.$notify({
    //   title: '扫码体验旗舰版',
    //   dangerouslyUseHTMLString: true,
    //   message: '<image style="width: 280px; height: 280px;" src="https://platform-wxmall.oss-cn-beijing.aliyuncs.com/gh_3ac3aedc0ea0_344.jpg"></image>',
    //   duration: 0
    // })
  },
  methods: {
    // 提交注册信息
    registerDataFormSubmit () {
      this.$refs.registerDataForm.validate((valid) => {
        if (valid) {
          if (this.registerDataForm.password !== this.registerDataForm.confPassword) {
            return this.$message.error('密码输入不一致，请重新输入')
          }
          // this.registerDataForm.idNoStaTime = this.registerDataForm.idValidityPeriodList[0]
          // this.registerDataForm.idNoEndTime = this.registerDataForm.idValidityPeriodList[1]
          this.registerDataForm.idCardValid = this.registerDataForm.idNoStaTime + '-' + this.registerDataForm.idNoEndTime
          this.$http({
            url: `/mall/shops/register`,
            method: 'post',
            data: this.registerDataForm
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message.success('申请成功，等待管理员审核')
              this.isLogin = true
            }
          })
        }
      })
    },
    // 取消用户注册
    disableRegister () {
      this.isLogin = true
      this.$refs.registerDataForm.resetFields()
    },
    // 用户注册
    register () {
      this.$router.push({path: '/register'})
    },

    // 提交表单
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: '/sys/login',
            method: 'post',
            data: {
              'userName': this.dataForm.userName,
              'password': Encrypt(this.dataForm.password),
              'uuid': this.dataForm.uuid,
              'captcha': this.dataForm.captcha
            }
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$cookie.set('token', data.token)
              this.$router.push({path: '/home'})
            } else {
              this.getCaptcha()
            }
          })
        }
      })
    },
    // 获取验证码
    getCaptcha () {
      this.dataForm.uuid = getUUID()
      this.captchaPath = this.$http.BASE_URL + `/captcha.jpg?uuid=${this.dataForm.uuid}`
    },

    async handleCode () {
      if (!this.registerDataForm.telephone) {
        return this.$message.error('请输入手机号')
      }
      if (!isMobile(this.registerDataForm.telephone)) {
        return this.$message.error('请输入正确的手机号')
      }
      try {
        this.$http({
          url: '/mall/shops/registerShopSendCode',
          method: 'post',
          data: {
            'phone': this.registerDataForm.telephone
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message.success('发送成功')
            this.registerDataForm.start = true
          }
        })
        this.start = true
      } catch (err) {
        this.$message.error(err.msg)
      }
    },
    // 上传之前
    beforeUploadHandle (file) {
      if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        this.$message.error('只支持jpg、png、gif格式的图片！')
        return false
      }
    },
    // 上传成功
    successHandle (response, file, fileList) {
      if (response && response.code === 0) {
        this.picFileList[0] = response.url
//          this.dataForm.indexItemPicUrl = response.url
        this.$message.info('操作成功！')
      } else {
        this.$message.error(response.msg)
      }
    },
    // 上传成功
    successIdCardFrontHandle (response, file, fileList) {
      if (response && response.code === 0) {
        this.registerDataForm.idCardFront = response.url
        this.$message.info('操作成功！')
      } else {
        this.$message.error(response.msg)
      }
    },
    // 上传成功
    successIdCardReverseHandle (response, file, fileList) {
      if (response && response.code === 0) {
        this.registerDataForm.idCardReverse = response.url
        this.$message.info('操作成功！')
      } else {
        this.$message.error(response.msg)
      }
    },
    // 上传成功
    successLicenseHandle (response, file, fileList) {
      if (response && response.code === 0) {
        this.registerDataForm.companyLicense = response.url
        this.$message.info('操作成功！')
      } else {
        this.$message.error(response.msg)
      }
    },
    handleError (error, file, fileList) {
      this.$message.error(error.toString())
    }

  }
}
</script>

<style lang="scss">
body {
  height: auto;
  background: url(~@/assets/img/login_bg.jpg) no-repeat center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.el-row {
  &:last-child {
    margin-top: 20px;
  }
}

.site-wrapper.site-page--login {
  text-align: center;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;

  .site-content__wrapper {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 0;
    margin: 0;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: transparent;
  }

  .brand-info {
    margin: 15% 100px 0;
    color: #fff;
  }

  .brand-info__text {
    margin: 0 0 22px 0;
    font-size: 28px;
    font-weight: 400;
    text-transform: uppercase;
  }

  .copyright_footer{
    padding: 80px;
  }

  .brand-info__intro {
    margin: 10px 0;
    font-size: 16px;
    line-height: 1.58;
  }

  .login-main {
    border: 0px solid;
    border-radius: 20px;
    margin: 0 auto;
    max-width: 400px;
    min-width: 300px;
    top: 0;
    right: 0;
    padding: 10px 50px;
    color: #fff;
    background: rgba(109, 109, 109, 0.23);
  }

  .register-main {
    border: 0px solid;
    border-radius: 20px;
    margin: 0 auto;
    max-width: 1200px;
    min-width: 600px;
    top: 0;
    right: 0;
    padding: 10px 60px 10px;
    color: #fff;
    background: rgba(109, 109, 109, 0.23);
  }

  .login-title {
    font-size: 16px;
  }

  .login-captcha {
    overflow: hidden;

    > img {
      width: 100%;
      cursor: pointer;
    }

  }

  .login-btn-submit {
    width: 100%;
    margin-top: 38px;
  }

}
</style>
