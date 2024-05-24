<template>
  <div>
    <el-row :gutter="20" tag="center">
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <b>商城LOGO</b>
          <el-tooltip content="建议像素150*150，大小不超过100KB">
            <el-img :disabled="!editable" v-model="dataForm.LOGO_URL" @change="updateConfig('LOGO_URL')">
            </el-img>
          </el-tooltip>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>商城名称</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('PROJECT_NAME')">
            <el-form-item prop="PROJECT_NAME">
              <el-input v-model="dataForm.PROJECT_NAME" :disabled="!editable" placeholder="微同商城商业版" class="width60">
                <el-button v-if="editable" type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('PROJECT_NAME')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>客服电话</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="updateConfig('TELEPHONE')">
            <el-form-item prop="TELEPHONE">
              <el-input v-model="dataForm.TELEPHONE" :disabled="!editable" placeholder="客服电话">
                <el-button v-if="editable" type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('TELEPHONE')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>库存预警</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('STOCK_WARNING')">
            <el-form-item prop="STOCK_WARNING">
              <el-input-number v-model="dataForm.STOCK_WARNING" :disabled="!editable"
                               :min="1" step-strictly @change="updateConfig('STOCK_WARNING')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>开启余额支付</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
            <el-form-item prop="RECHARGE_STATUS">
              <el-switch
                active-value="1"
                inactive-value="2"
                @change="updateConfig('RECHARGE_STATUS')"
                :disabled="!editable"
                v-model="dataForm.RECHARGE_STATUS"
                active-text="开"
                inactive-text="关">
              </el-switch>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>开启分销功能</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
            <el-form-item prop="DISTRIBUTION_STATUS">
              <el-switch
                active-value="1"
                inactive-value="2"
                @change="updateConfig('DISTRIBUTION_STATUS')"
                :disabled="!editable"
                v-model="dataForm.DISTRIBUTION_STATUS"
                active-text="开"
                inactive-text="关">
              </el-switch>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>开启分销商申请审核</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
            <el-form-item prop="DISTRIBUTION_AUDIT">
              <el-switch
                active-value="1"
                inactive-value="2"
                @change="updateConfig('DISTRIBUTION_AUDIT')"
                :disabled="!editable"
                v-model="dataForm.DISTRIBUTION_AUDIT"
                active-text="开"
                inactive-text="关">
              </el-switch>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>开启提现审核</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
            <el-form-item prop="WITHDRAW_AUDIT">
              <el-switch
                active-value="1"
                inactive-value="2"
                @change="updateConfig('WITHDRAW_AUDIT')"
                :disabled="!editable"
                v-model="dataForm.WITHDRAW_AUDIT"
                active-text="开"
                inactive-text="关">
              </el-switch>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>运费设置(单位：元)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('SHIPPING_FEE')">
            <el-form-item prop="SHIPPING_FEE">
              <el-input-number v-model="dataForm.SHIPPING_FEE" :disabled="!editable"
                               :min="0" :precision="2" @change="updateConfig('SHIPPING_FEE')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>免运费门槛(单位：元)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('SHIPPING_FEE_FREE')">
            <el-form-item prop="SHIPPING_FEE_FREE">
              <el-input-number v-model="dataForm.SHIPPING_FEE_FREE" :disabled="!editable"
                               :min="0" :precision="2" @change="updateConfig('SHIPPING_FEE_FREE')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>下单有效时间(单位：分钟)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('ORDER_EXPIRE')">
            <el-form-item prop="ORDER_EXPIRE">
              <el-input-number v-model="dataForm.ORDER_EXPIRE" :disabled="!editable"
                               :min="5" step-strictly @change="updateConfig('ORDER_EXPIRE')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>确认收货后可申请退款时间(单位：天)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('ALLOW_REFUND_TIME')">
            <el-form-item prop="ALLOW_REFUND_TIME">
              <el-input-number v-model="dataForm.ALLOW_REFUND_TIME" :disabled="!editable"
                               :min="1" step-strictly @change="updateConfig('ALLOW_REFUND_TIME')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>物流收货后自动确认时间(单位：天)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('AUTO_CONFIRM_TIME')">
            <el-form-item prop="AUTO_CONFIRM_TIME">
              <el-input-number v-model="dataForm.AUTO_CONFIRM_TIME" :disabled="!editable"
                               :min="1" step-strictly @change="updateConfig('AUTO_CONFIRM_TIME')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>一级分销提成比例(0.1即为10%)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('COMMISSION_TYPE_DIST_1')">
            <el-form-item prop="COMMISSION_TYPE_DIST_1">
              <el-input-number v-model="dataForm.COMMISSION_TYPE_DIST_1" :disabled="!editable"
                               :min="0" :max="1" :step="0.01" :precision="2"
                               @change="updateConfig('COMMISSION_TYPE_DIST_1')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>二级分销提成比例(0.1即为10%)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('COMMISSION_TYPE_DIST_2')">
            <el-form-item prop="COMMISSION_TYPE_DIST_2">
              <el-input-number v-model="dataForm.COMMISSION_TYPE_DIST_2" :disabled="!editable"
                               :min="0" :max="1" :step="0.01" :precision="2"
                               @change="updateConfig('COMMISSION_TYPE_DIST_2')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>平台服务费(0.1即为10%)</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('COMMISSION_TYPE_PLATFORM')">
            <el-form-item prop="COMMISSION_TYPE_PROMO_2">
              <el-input-number v-model="dataForm.COMMISSION_TYPE_PLATFORM" :disabled="!editable"
                               :min="0" :max="1" :step="0.01" :precision="2"
                               @change="updateConfig('COMMISSION_TYPE_PLATFORM')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
<!--      <el-col :span="12" :md="6" :xl="4" class="config_card">-->
<!--        <el-card>-->
<!--          <div slot="header" class="clearfix">-->
<!--            <h4>一级推广提成比例(0.1即为10%)</h4>-->
<!--          </div>-->
<!--          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"-->
<!--                   @keyup.enter.native="updateConfig('COMMISSION_TYPE_PROMO_1')">-->
<!--            <el-form-item prop="COMMISSION_TYPE_PROMO_1">-->
<!--              <el-input-number v-model="dataForm.COMMISSION_TYPE_PROMO_1" :disabled="!editable"-->
<!--                               :min="0" :max="1" :step="0.01" :precision="2"-->
<!--                               @change="updateConfig('COMMISSION_TYPE_PROMO_1')"/>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--      <el-col :span="12" :md="6" :xl="4" class="config_card">-->
<!--        <el-card>-->
<!--          <div slot="header" class="clearfix">-->
<!--            <h4>二级推广提成比例(0.1即为10%)</h4>-->
<!--          </div>-->
<!--          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"-->
<!--                   @keyup.enter.native="updateConfig('COMMISSION_TYPE_PROMO_2')">-->
<!--            <el-form-item prop="COMMISSION_TYPE_PROMO_2">-->
<!--              <el-input-number v-model="dataForm.COMMISSION_TYPE_PROMO_2" :disabled="!editable"-->
<!--                               :min="0" :max="1" :step="0.01" :precision="2"-->
<!--                               @change="updateConfig('COMMISSION_TYPE_PROMO_2')"/>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-card>-->
<!--      </el-col>-->
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>单次最低提现额度</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('WITHDRAW_SINGLE_LOWEST')">
            <el-form-item prop="WITHDRAW_SINGLE_LOWEST">
              <el-input-number v-model="dataForm.WITHDRAW_SINGLE_LOWEST" :disabled="!editable"
                               :min="1" :precision="2" @change="updateConfig('WITHDRAW_SINGLE_LOWEST')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>单次最高提现额度</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('WITHDRAW_SINGLE_HIGHEST')">
            <el-form-item prop="WITHDRAW_SINGLE_HIGHEST">
              <el-input-number v-model="dataForm.WITHDRAW_SINGLE_HIGHEST" :disabled="!editable"
                               :min="1" :precision="2" @change="updateConfig('WITHDRAW_SINGLE_HIGHEST')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>当日最高提现额度</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('WITHDRAW_DAY_HIGHEST')">
            <el-form-item prop="WITHDRAW_DAY_HIGHEST">
              <el-input-number v-model="dataForm.WITHDRAW_DAY_HIGHEST" :disabled="!editable"
                               :min="1" :precision="2" @change="updateConfig('WITHDRAW_DAY_HIGHEST')"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>快递鸟kdnBusinessId
              <el-link type="success" href="http://www.kdniao.com/reg" target="_blank">注册地址</el-link>
            </h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('KDN_BUSINESS_ID')">
            <el-form-item prop="KDN_BUSINESS_ID">
              <el-input v-model="dataForm.KDN_BUSINESS_ID" v-if="editable" :disabled="!editable"
                        placeholder="kdnBusinessId">
                <el-button v-if="editable" type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('KDN_BUSINESS_ID')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>快递鸟kdnAppKey</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('KDN_APP_KEY')">
            <el-form-item prop="KDN_APP_KEY">
              <el-input v-model="dataForm.KDN_APP_KEY" v-if="editable" :disabled="!editable" placeholder="kdnAppKey">
                <el-button v-if="editable" type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('KDN_APP_KEY')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>腾讯位置KEY
              <el-link type="success" href="https://lbs.qq.com/dev/console/register" target="_blank">注册地址</el-link>
            </h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="updateConfig('QQ_MAP_KEY')">
            <el-form-item prop="QQ_MAP_KEY">
              <el-input v-model="dataForm.QQ_MAP_KEY" v-if="editable" :disabled="!editable" placeholder="腾讯位置KEY">
                <el-button type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('QQ_MAP_KEY')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12" :md="6" :xl="4" class="config_card">
        <el-card>
          <div slot="header" class="clearfix">
            <h4>核销地址</h4>
          </div>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
                   @keyup.enter.native="updateConfig('CHECK_COUPON_PAGE')">
            <el-form-item prop="KDN_APP_KEY">
              <el-input v-model="dataForm.CHECK_COUPON_PAGE" v-if="editable" :disabled="!editable" placeholder="核销地址">
                <el-button v-if="editable" type="primary" slot="append" icon="el-icon-check"
                           @click="updateConfig('CHECK_COUPON_PAGE')"></el-button>
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="24">
        <el-card>
          <el-collapse accordion>
            <el-collapse-item title="用户协议" name="1">
              <ueditor v-model="dataForm.USER_TREATY" :disabled="!editable" placeholder="用户协议"></ueditor>
              <el-button v-if="editable" type="success" icon="el-icon-check" @click="updateConfig('USER_TREATY')">更新
              </el-button>
            </el-collapse-item>
            <el-collapse-item title="隐私协议" name="2">
              <ueditor v-model="dataForm.PRIVACY_TREATY" :disabled="!editable" placeholder="隐私协议"></ueditor>
              <el-button v-if="editable" type="success" icon="el-icon-check" @click="updateConfig('PRIVACY_TREATY')">
                更新
              </el-button>
            </el-collapse-item>
            <el-collapse-item title="分销协议" name="4">
              <ueditor v-model="dataForm.PROMO_TREATY" :disabled="!editable" placeholder="分销协议"></ueditor>
              <el-button v-if="editable" type="success" icon="el-icon-check" @click="updateConfig('PROMO_TREATY')">
                更新
              </el-button>
            </el-collapse-item>
            <el-collapse-item title="族谱介绍" name="3">
              <ueditor v-model="dataForm.PEDIGREE_INFO" :disabled="!editable" placeholder="族谱信息"></ueditor>
              <el-button v-if="editable" type="success" icon="el-icon-check" @click="updateConfig('PEDIGREE_INFO')">
                更新
              </el-button>
            </el-collapse-item>
            <el-collapse-item title="提现协议" name="5">
              <ueditor v-model="dataForm.CASH_TREATY" :disabled="!editable" placeholder="提现协议"></ueditor>
              <el-button v-if="editable" type="success" icon="el-icon-check" @click="updateConfig('CASH_TREATY')">
                更新
              </el-button>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data () {
    let validateTelephone = (rule, value, callback) => {
      if (!/^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/.test(value)) {
        callback(new Error('请输入正确的手机或固话'))
      } else {
        callback()
      }
    }
    return {
      editable: this.isAuth('sys:config:saveConfigValue'),
      dataForm: {
        COMMISSION_TYPE_DIST_1: '',
        COMMISSION_TYPE_DIST_2: '',
        COMMISSION_TYPE_PROMO_1: '',
        COMMISSION_TYPE_PROMO_2: '',
        WITHDRAW_SINGLE_LOWEST: '',
        WITHDRAW_SINGLE_HIGHEST: '',
        WITHDRAW_DAY_HIGHEST: '',
        STOCK_WARNING: '',
        KDN_BUSINESS_ID: '',
        KDN_APP_KEY: '',
        RECHARGE_STATUS: '',
        DISTRIBUTION_STATUS: '',
        DISTRIBUTION_AUDIT: '',
        WITHDRAW_AUDIT: '',
        PROJECT_NAME: '',
        SHIPPING_FEE: '',
        SHIPPING_FEE_FREE: '',
        ORDER_EXPIRE: '',
        ALLOW_REFUND_TIME: '',
        LOGO_URL: '',
        QQ_MAP_KEY: '',
        USER_TREATY: '',
        PRIVACY_TREATY: '',
        PEDIGREE_INFO: '',
        TELEPHONE: ''
      },
      dataRule: {
        TELEPHONE: [{
          required: true,
          validator: validateTelephone,
          trigger: 'blur'
        }]
      }
    }
  },
  activated () {
    this.getData()
  },
  methods: {
    getData () {
      this.$http({
        url: '/sys/config/queryKeyValues',
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          let list = data.data
          list.forEach(item => {
            this.dataForm[item.PARAM_KEY] = item.PARAM_VALUE
          })
        }
      })
    },
    updateConfig (key) {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // this.$confirm('确定此次更新操作?', '提示', {
          //   confirmButtonText: '确定',
          //   cancelButtonText: '取消',
          //   type: 'warning'
          // }).then(() => {
          this.$http({
            url: '/sys/config/saveConfigValue',
            method: 'post',
            data: {
              paramKey: key,
              paramValue: this.dataForm[key]
            }
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '更新成功',
                type: 'success',
                duration: 1500
              })
            }
          })
          // }).catch(() => {
          //   this.getData()
          // })
        }
      })
    }
  }
}
</script>
<style>
.config_card {
  height: 200px;
}
</style>
