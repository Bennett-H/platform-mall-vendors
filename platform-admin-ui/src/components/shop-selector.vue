<template>
  <el-select style="width: 240px" v-model="selectedShopId" size="small" v-loading="dataListLoading"
             @change="selectShop" filterable>
    <el-option v-for="item in shopList" :key="item.id" :label="item.name"
               :value="item.id"></el-option>
  </el-select>
</template>
<script>

export default {
  data () {
    return {
      shopList: [],
      selectedShopId: null,
      dataListLoading: false
    }
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: '/mall/shops/queryMyShop',
        method: 'get'
      }).then(async ({data}) => {
        if (data && data.code === 0) {
          this.shopList = data.list
          if (!this.shopList.length) {
            this.$message.info('管理的店铺为空，请先添加')
          } else {
            this.selectedShopId = await this.currentShop()
            if (!this.selectedShopId) {
              this.selectedShopId = this.shopList[0].id
              this.selectShop(this.selectedShopId)
            }
          }
        }
        this.dataListLoading = false
      })
    },
    selectShop (shopId) {
      this.$http({
        url: `/mall/shops/selectShop?shopId=${shopId}`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log('updateShopsId', data)
          this.$store.commit('user/updateShopsId', shopId)
        }
      })
    },
    currentShop () {
      return new Promise((resolve) => {
        this.$http({
          url: '/mall/shops/currentShop',
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$store.commit('user/updateShopsId', data.currentShop)
            resolve(data.currentShop)
          }
        })
      })
    }
  }
}
</script>
