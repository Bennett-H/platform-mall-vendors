<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : !disabled ? '修改' : '查看商品详情'"
    :close-on-click-modal="false"
    :lock-scroll="false"
    width="70%"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             inline label-width="120px">
      <el-tabs tab-position="left" v-model="activeName" type="border-card">
        <el-tab-pane label="通用信息" name="first">
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="dataForm.name" :disabled="disabled" placeholder="商品名称" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="商品编码" prop="goodsSn">
            <el-input v-model="dataForm.goodsSn" :disabled="true" placeholder="商品编码" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="商品类型" prop="categoryId">
            <el-select v-model="dataForm.categoryId" :disabled="disabled" clearable filterable placeholder="请选择">
              <el-option
                v-for="category in categorys"
                :key="category.id"
                :label="category.name"
                :value="category.id">
              </el-option>
            </el-select>
          </el-form-item>
<!--          <el-form-item label="商家商品类型" prop="shopsCategoryId">-->
<!--            <el-select v-model="dataForm.shopsCategoryId" :disabled="disabled" clearable filterable placeholder="请选择">-->
<!--              <el-option-->
<!--                v-for="category in shopsCategorys"-->
<!--                :key="category.id"-->
<!--                :label="category.name"-->
<!--                :value="category.id">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
          <el-form-item label="商品库存" prop="goodsNumber">
            <el-input-number v-model="dataForm.goodsNumber" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="品牌" prop="brandId">
            <el-select v-model="dataForm.brandId" :disabled="disabled" clearable filterable placeholder="请选择">
              <el-option
                v-for="brand in brands"
                :key="brand.id"
                :label="brand.name"
                :value="brand.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="销量" prop="sales">
            <el-input-number v-model="dataForm.sales" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="是否热销" prop="isHot">
            <el-dict code="IS_NOT" :disabled="disabled" v-model="dataForm.isHot"></el-dict>
          </el-form-item>
          <el-form-item label="是否新商品" prop="isNew">
            <el-dict code="IS_NOT" :disabled="disabled" v-model="dataForm.isNew"></el-dict>
          </el-form-item>
          <el-form-item label="是否实体券" prop="isCoupon">
            <el-dict code="IS_NOT" :disabled="disabled" v-model="dataForm.isCoupon"></el-dict>
          </el-form-item>
<!--          <el-form-item label="市场价" prop="marketPrice">-->
<!--            <el-input-number v-model="dataForm.marketPrice" :disabled="disabled" controls-position="right" :min="0.01"-->
<!--                             :precision="2"></el-input-number>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="零售价格" prop="retailPrice">-->
<!--            <el-input-number v-model="dataForm.retailPrice" :disabled="disabled" controls-position="right" :min="0.01"-->
<!--                             :precision="2"></el-input-number>-->
<!--          </el-form-item>-->
          <el-form-item label="起售数量" prop="minSell">
            <el-input-number v-model="dataForm.minSell" :disabled="disabled" controls-position="right" :min="1"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="关键词" prop="keywords">
            <el-input v-model="dataForm.keywords" :disabled="disabled" placeholder="商品检索关键词" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="简明介绍" prop="goodsBrief">
            <el-input v-model="dataForm.goodsBrief" :disabled="disabled" placeholder="商品一句话描述" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="推广标签" prop="promotionTag">
            <el-input v-model="dataForm.promotionTag" :disabled="disabled" placeholder="推广标签" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="推广描述" prop="promotionDesc">
            <el-input v-model="dataForm.promotionDesc" :disabled="disabled" placeholder="推广描述" class="width200"></el-input>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="dataForm.sort" :disabled="disabled" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-form-item>
          <el-form-item label="列表图" prop="listPicUrl">
            <el-tooltip content="建议像素200*200，大小不超过200KB">
              <el-img v-model="dataForm.listPicUrl" :disabled="disabled">
              </el-img>
            </el-tooltip>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品参数">
          <div v-for="(item,index) in dataForm.goodsAttributeEntityList">
            <el-row :gutter="20" tag="ul">
              <el-col :span="4">
                <el-tooltip content="属性" placement="top">
                  <el-select v-model="item.attributeId" :disabled="disabled" clearable filterable placeholder="属性">
                    <el-option v-for="attribute in attributes"
                               :value="attribute.id"
                               :label="attribute.name"
                               :key="attribute.id">
                    </el-option>
                  </el-select>
                </el-tooltip>
              </el-col>
              <el-col :span="9">
                <el-tooltip content="值" placement="top">
                  <el-input type="text" :disabled="disabled" v-model="item.value" placeholder="值"/>
                </el-tooltip>
              </el-col>
              <el-col :span="3" style="min-width: 140px;">
                <el-tooltip content="排序" placement="top">
                  <el-input-number :disabled="disabled" controls-position="right" :min="0" step-strictly
                                   v-model="item.orderSort" placeholder="排序" class="width120"/>
                </el-tooltip>
              </el-col>
              <el-col :span="6" style="min-width: 95px;">
                <el-button type="warning" :disabled="disabled" icon="el-icon-plus" v-if="index === 0"
                           @click="addAttrRow"
                           circle></el-button>
                <el-button type="danger" :disabled="disabled" icon="el-icon-delete" @click="delAttrRow(index)"
                           circle></el-button>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        <el-tab-pane label="商品规格">
          <el-alert type="error" :closable="false">保存后在商品列表点击商品名称配置</el-alert>
          <div v-for="(item,index) in dataForm.specificationEntityList">
            <el-row :gutter="20" tag="ul">
              <el-col :span="10">
                <el-tooltip content="名称" placement="top">
                  <el-input type="text" :disabled="disabled" v-model="item.name" placeholder="名称"/>
                </el-tooltip>
              </el-col>
              <el-col :span="3" style="min-width: 140px;">
                <el-tooltip content="排序" placement="top">
                  <el-input-number v-model="item.sort" :disabled="disabled" controls-position="right" :min="0"
                                   step-strictly placeholder="排序" class="width120"></el-input-number>
                </el-tooltip>
              </el-col>
              <el-col :span="6" style="min-width: 95px;">
                <el-button type="warning" :disabled="disabled" icon="el-icon-plus" v-if="index === 0" @click="addSpeRow"
                           circle></el-button>
                <el-button type="danger" :disabled="disabled" icon="el-icon-delete" @click="delSpeRow(index)"
                           circle></el-button>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
<!--        <el-tab-pane label="拼团设置">-->
<!--          <el-form-item label="开启拼团" prop="isGroup">-->
<!--            <el-dict code="IS_NOT" :disabled="disabled" v-model="dataForm.isGroup"></el-dict>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="拼团人数">-->
<!--            <el-input-number v-model="dataForm.groupNumber" :disabled="disabled" controls-position="right" :min="0"-->
<!--                             step-strictly></el-input-number>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="拼团价格" prop="groupPrice">-->
<!--            <el-input-number v-model="dataForm.groupPrice" :disabled="disabled" controls-position="right" :min="0.01"-->
<!--                             :precision="2"></el-input-number>-->
<!--          </el-form-item>-->
<!--        </el-tab-pane>-->
        <el-tab-pane label="详细描述">
          <el-row>
            <el-form-item label="视频上传">
              <!-- action必选参数, 上传的地址 -->
              <el-upload class="avatar-uploader el-upload-text" :action="url" :show-file-list="false"
                         :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo"
                         :on-progress="uploadVideoProcess" :disabled="disabled">
                <video v-if="dataForm.videoUrl && !videoFlag" :src="dataForm.videoUrl"
                       style="width: 400px;height: 300px"
                       controls="controls">您的浏览器不支持视频播放
                </video>
                <i v-else-if="!dataForm.videoUrl && !videoFlag"
                   class="el-icon-plus avatar-uploader-icon"></i>
                <el-progress v-if="videoFlag" type="circle" :percentage="videoUploadPercent"
                             style="margin-top:30px;"></el-progress>
              </el-upload>
              <p class="text">请保证视频格式正确，且不超过10M</p>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="轮播图上传">
              <el-upload
                :disabled="disabled"
                class="upload-demo"
                :action="url"
                :on-remove="handleRemove"
                :before-upload="beforeUploadHandle"
                :on-success="successHandle"
                :file-list="dataForm.attachmentEntityList"
                list-type="picture-card">
                <el-button v-if="!disabled" size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-row>
          <ueditor v-model="dataForm.goodsDesc" :disabled="disabled" placeholder="商品描述"></ueditor>
          <el-button type="success" @click="openDetail">预览详情</el-button>
        </el-tab-pane>
      </el-tabs>
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
      url: '',
      activeName: 'first',
      disabled: false,
      visible: false,
      videoFlag: false,
      videoUploadPercent: 0,
      dataForm: {
        id: '',
        name: '',
        categoryId: '',
        goodsSn: '',
        brandId: '',
        goodsNumber: '',
        sales: 0,
        isHot: 0,
        isNew: 0,
        isLimited: 0,
        isCoupon: 0,
        isGroup: '0',
        groupNumber: 2,
        groupPrice: 0,
        primaryPicUrl: '',
        listPicUrl: '',
        keywords: '',
        goodsBrief: '',
        unitPrice: 0,
        marketPrice: 0.01,
        retailPrice: 0.01,
        counterPrice: 0,
        extraPrice: '',
        minSell: '',
        isAppExclusive: 0,
        appExclusivePrice: 0,
        goodsDesc: '',
        sort: '',
        promotionTag: '',
        promotionDesc: '',
        createUserId: '',
        createTime: '',
        createUserOrgNo: '',
        videoUrl: '',
        updateUserId: '',
        updateTime: '',
        attachmentEntityList: [],
        shopsCategoryId: '',
        specificationEntityList: [{
          'id': '',
          'goodsId': '',
          'name': '',
          'sort': ''
        }],
        goodsAttributeEntityList: [{
          'id': '',
          'goodsId': '',
          'attributeId': '',
          'value': '',
          'orderSort': ''
        }]
      },
      dataRule: {
        name: [{
          required: true,
          message: '商品名称不能为空',
          trigger: 'blur'
        }],
        // goodsSn: [{
        //   required: true,
        //   message: '商品编码不能为空',
        //   trigger: 'blur'
        // }],
        goodsNumber: [{
          required: true,
          message: '商品库存不能为空',
          trigger: 'blur'
        }],
        listPicUrl: [{
          required: true,
          message: '列表图不能为空',
          trigger: 'blur'
        }],
        categoryId: [{
          required: true,
          message: '商品类型不能为空',
          trigger: 'blur'
        }],
        // marketPrice: [{
        //   required: true,
        //   message: '市场价格不能为空',
        //   trigger: 'blur'
        // }],
        // retailPrice: [{
        //   required: true,
        //   message: '零售价格不能为空',
        //   trigger: 'blur'
        // }],
        minSell: [{
          required: true,
          message: '起售数量不能为空',
          trigger: 'blur'
        }]
      },
      categorys: [],
      shopsCategorys: [],
      brands: [],
      attributes: []
    }
  },
  methods: {
    beforeUploadVideo (file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (['video/mp4', 'video/ogg', 'video/x-flv', 'video/x-msvideo', 'video/x-ms-wmv', 'video/rmvb'].indexOf(file.type) === -1) {
        this.$message.error('请上传正确的视频格式')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传视频大小不能超过10MB哦!')
        return false
      }
    },
    uploadVideoProcess (event, file, fileList) {
      this.videoFlag = true
      this.videoUploadPercent = parseInt(file.percentage.toFixed(0))
    },
    // 获取上传地址
    handleVideoSuccess (response, file) {
      this.videoFlag = false
      this.videoUploadPercent = 0
      if (response && response.code === 0) {
        this.dataForm.videoUrl = response.url
      } else {
        this.$message.error('视频上传失败，请重新上传！')
      }
    },
    openDetail () {
      this.$alert(`<div style="width: 360px;height: 720px;overflow-y: auto;overflow-x: hidden"">${this.dataForm.goodsDesc}</div>`, this.dataForm.name, {
        dangerouslyUseHTMLString: true,
        closeOnClickModal: true,
        callback: action => {
        }
      })
    },
    init (id, disabled) {
      this.url = this.$http.BASE_URL + `/sys/oss/upload?token=${this.$cookie.get('token')}`
      this.disabled = disabled
      this.dataForm.id = id || ''
      this.visible = true
      this.activeName = 'first'
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/goods/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.goods
              if (data.goods.goodsAttributeEntityList.length > 0) {
                this.dataForm.goodsAttributeEntityList = data.goods.goodsAttributeEntityList
              } else {
                this.dataForm.goodsAttributeEntityList = [{
                  'id': '',
                  'goodsId': this.dataForm.id,
                  'attributeId': '',
                  'value': '',
                  'orderSort': ''
                }]
              }
              if (data.goods.specificationEntityList.length > 0) {
                this.dataForm.specificationEntityList = data.goods.specificationEntityList
              } else {
                this.dataForm.specificationEntityList = [{
                  'id': '',
                  'goodsId': this.dataForm.id,
                  'attributeId': '',
                  'value': ''
                }]
              }
            }
          })
        } else {
          this.dataForm = {
            id: '',
            name: '',
            categoryId: '',
            goodsSn: '',
            brandId: '',
            goodsNumber: '',
            sales: 0,
            isHot: 0,
            isNew: 0,
            isLimited: 0,
            isGroup: '0',
            groupNumber: 2,
            groupPrice: 0,
            primaryPicUrl: '',
            listPicUrl: '',
            keywords: '',
            goodsBrief: '',
            unitPrice: 0,
            marketPrice: 0,
            retailPrice: 0,
            counterPrice: 0,
            extraPrice: '',
            minSell: '',
            isAppExclusive: 0,
            appExclusivePrice: 0,
            goodsDesc: '',
            sort: '',
            promotionTag: '',
            promotionDesc: '',
            createUserId: '',
            createTime: '',
            createUserOrgNo: '',
            updateUserId: '',
            updateTime: '',
            attachmentEntityList: [],
            specificationEntityList: [{
              'id': '',
              'goodsId': '',
              'name': '',
              'sort': ''
            }],
            goodsAttributeEntityList: [{
              'id': '',
              'goodsId': '',
              'attributeId': '',
              'value': '',
              'orderSort': ''
            }]
          }
        }
      })
    },
    delAttrRow: function (index) {
      // 最后一行时禁止删除
      if (this.dataForm.goodsAttributeEntityList.length === 1) {
        return
      }
      this.dataForm.goodsAttributeEntityList.splice(index, 1)
    },
    addAttrRow: function () {
      let goodsId = ''
      if (this.dataForm) {
        goodsId = this.dataForm.id
      }
      this.dataForm.goodsAttributeEntityList.push({
        'id': '',
        'goodsId': goodsId,
        'attributeId': '',
        'value': '',
        'orderSort': ''
      })
    },
    delSpeRow: function (index) {
      // 最后一行时禁止删除
      if (this.dataForm.specificationEntityList.length === 1) {
        return
      }
      this.dataForm.specificationEntityList.splice(index, 1)
    },
    addSpeRow: function () {
      let goodsId = ''
      if (this.dataForm.goods) {
        goodsId = this.dataForm.goods.id
      }
      this.dataForm.specificationEntityList.push({
        'id': '',
        'goodsId': goodsId,
        'name': '',
        'sort': ''
      })
    },
    handleRemove (file, fileList) {
      this.dataForm.attachmentEntityList = fileList
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
        this.dataForm.attachmentEntityList.push({
          name: file.name,
          url: response.url
        })
      } else {
        this.$message.error(response.msg)
      }
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm']
        .validate((valid) => {
          if (valid) {
            this.$http({
              url: `/mall/goods/${!this.dataForm.id ? 'save' : 'update'}`,
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
  },
  created () {
    this.$http({
      url: `/mall/category/queryAll`,
      method: 'get',
      params: {
        'level': 2
      }
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.categorys = data.list
      }
    })
    this.$http({
      url: `/mall/brand/queryAll`,
      method: 'get'
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.brands = data.list
      }
    })
    this.$http({
      url: `/mall/attribute/queryAll`,
      method: 'get'
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.attributes = data.list
      }
    })
    this.$http({
      url: `/mall/shopscategory/queryAll`,
      method: 'get'
    }).then((shopsData) => {
      if (shopsData.data && shopsData.data.code === 0) {
        this.shopsCategorys = shopsData.data.list
      }
    })
  }
}
</script>
