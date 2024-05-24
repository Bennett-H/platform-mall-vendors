<template>
  <el-dialog
    title="SKU配置"
    :close-on-click-modal="false"
    :lock-scroll="false"
    width="100%"
    :visible.sync="visible">
    <el-card v-for="(productEntity,productIndex) in productEntityList" :key="productIndex" shadow="hover">
      <div slot="header" class="clearfix">
        <span style="float: left">图片：</span>
        <el-img style="float: left" v-model="productEntity.listPicUrl">
        </el-img>
        <el-button class="operator-btn" type="warning" icon="el-icon-plus" v-if="productIndex == 0" @click="addAttrRow"
                   circle>
        </el-button>
        <el-button class="operator-btn" type="danger" icon="el-icon-delete" @click="delAttrRow(productIndex)"
                   circle></el-button>
      </div>
      <el-row :gutter="10">
        <div v-for="(item,index) in keyValues">
          <el-col :span="3" shadow="hover" v-for="(kv,indexKv) in productEntity.keyValue" :key="indexKv">
            <template v-if="kv.key===item.key">

              <el-tooltip :content="item.key">
                <el-input v-model="kv.value" :placeholder="item.key"/>
              </el-tooltip>
            </template>
            <template v-if="kv.key==='' && indexKv ===0">
              <el-tooltip :content="item.key">
                <el-input v-model="kv.value" :placeholder="item.key"/>
              </el-tooltip>
            </template>
          </el-col>
        </div>
<!--        <el-col :span="3">-->
<!--          <el-tooltip content="SKU编码">-->
<!--            <el-input v-model="productEntity.goodsSn" placeholder="SKU编码"/>-->
<!--          </el-tooltip>-->
<!--        </el-col>-->
        <el-col :span="3">
          <el-tooltip content="库存">
            <el-input-number class="width120" v-model="productEntity.goodsNumber" controls-position="right" :min="0"
                             step-strictly></el-input-number>
          </el-tooltip>
        </el-col>
        <el-col :span="3">
          <el-tooltip content="零售价格">
            <el-input-number class="width120" v-model="productEntity.retailPrice" controls-position="right" :min="0.01"
                             :precision="2"></el-input-number>
          </el-tooltip>
        </el-col>
        <el-col :span="3">
          <el-tooltip content="市场价格">
            <el-input-number class="width120" v-model="productEntity.marketPrice" controls-position="right" :min="0.01"
                             :precision="2"></el-input-number>
          </el-tooltip>
        </el-col>
        <el-col :span="3">
          <el-tooltip content="起售数量">
            <el-input-number class="width120" v-model="productEntity.minSell" controls-position="right" :min="1">
            </el-input-number>
          </el-tooltip>
        </el-col>
        <el-col :span="3" v-if="isGroup">
          <el-tooltip content="拼团价格">
            <el-input-number class="width120" v-model="productEntity.groupPrice" controls-position="right" :min="0.01"
                             :precision="2"></el-input-number>
          </el-tooltip>
        </el-col>
      </el-row>
    </el-card>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import unionBy from 'lodash/unionBy'

export default {
  data () {
    return {
      goodsId: '',
      isGroup: false,
      visible: false,
      keyValues: [],
      // 商品的所有产品
      productEntityList: []
    }
  },
  methods: {
    init (id, isGroup) {
      this.goodsId = id
      this.isGroup = isGroup
      this.keyValues = []
      this.productEntityList = [{
        keyValue: [{
          key: '',
          value: ''
        }],
        goodsSn: '',
        goodsId: id,
        goodsNumber: '',
        retailPrice: '',
        marketPrice: '',
        minSell: '',
        groupPrice: '',
        listPicUrl: ''
      }]
      this.visible = true
      this.$nextTick(() => {
        this.$http({
          url: '/mall/specification/queryAll',
          method: 'get',
          params: {
            goodsId: this.goodsId
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            for (let i = 0; i < data.list.length; i++) {
              this.keyValues.push({
                key: data.list[i].name,
                value: ''
              })
            }
          }
        })
        this.$http({
          url: '/mall/goodssku/queryByGoodsId/' + this.goodsId,
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.productEntityList = data.list
            if (this.productEntityList.length === 0) {
              this.productEntityList = [{
                keyValue: this.keyValues,
                goodsSn: '',
                goodsId: this.goodsId,
                goodsNumber: '',
                retailPrice: '',
                marketPrice: '',
                groupPrice: '',
                minSell: ''
              }]
            }
            for (let i = 0; i < this.productEntityList.length; i++) {
              let keyValue = []
              for (let i = 0; i < this.keyValues.length; i++) {
                keyValue.push({
                  key: this.keyValues[i].key,
                  value: ''
                })
              }
              if (this.productEntityList[i].keyValue.length === 0) {
                this.productEntityList[i].keyValue = keyValue
              } else if (this.productEntityList[i].keyValue.length !== keyValue.length) {
                // 合并并去重
                this.productEntityList[i].keyValue = unionBy(this.productEntityList[i].keyValue.concat(keyValue), 'key')
              }
            }
          }
        })
      })
    },
    delAttrRow: function (index) {
      // 最后一行时禁止删除
      if (this.productEntityList.length === 1) {
        return
      }
      this.productEntityList.splice(index, 1)
    },
    addAttrRow: function () {
      let keyValueItem = []
      for (let i = 0; i < this.keyValues.length; i++) {
        keyValueItem.push({
          key: this.keyValues[i].key,
          value: ''
        })
      }
      this.productEntityList.push({
        keyValue: keyValueItem,
        goodsSn: '',
        goodsId: this.goodsId,
        goodsNumber: '',
        retailPrice: '',
        marketPrice: '',
        groupPrice: '',
        minSell: ''
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$http({
        url: `/mall/goodssku/saveGoodsProduct`,
        method: 'post',
        data: this.productEntityList
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
<style>
.operator-btn {
  float: right;
  margin: 0 10px;
}
</style>
