<template>
  <div class="mod-report">
    <el-row :gutter="20">
      <el-col :span="24" v-if="isAuth('mall:order:allReport')">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <el-date-picker
              style="float: right;width: 160px"
              v-model="endTime"
              type="date"
              @change="initAllShopsSalesCount"
              value-format="yyyy-MM-dd"
              placeholder="选择结束日期"
              :picker-options="pickerEndOptions">
            </el-date-picker>
            <el-date-picker
              style="float: right;width: 160px"
              v-model="startTime"
              type="date"
              @change="initAllShopsSalesCount"
              value-format="yyyy-MM-dd"
              placeholder="选择开始日期"
              :picker-options="pickerStartOptions">
            </el-date-picker>
          </div>
          <div id="initAllShopsSalesSumChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="24" v-if="isAuth('mall:order:report')">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <el-select v-model="shopsId" clearable filterable @change="initShopsGoodsSalesCount" placeholder="店铺名称"
                       class="width185">
              <el-option
                v-for="shops in shopsList"
                :key="shops.id"
                :label="shops.name"
                :value="shops.id">
              </el-option>
            </el-select>
            <b>({{details}})</b>
            <el-date-picker
              style="float: right;width: 160px"
              v-model="endTime"
              type="date"
              @change="initShopsGoodsSalesCount"
              value-format="yyyy-MM-dd"
              placeholder="选择结束日期"
              :picker-options="pickerEndOptions">
            </el-date-picker>
            <el-date-picker
              style="float: right;width: 160px"
              v-model="startTime"
              type="date"
              @change="initShopsGoodsSalesCount"
              value-format="yyyy-MM-dd"
              placeholder="选择开始日期"
              :picker-options="pickerStartOptions">
            </el-date-picker>
          </div>
          <div id="initShopsGoodsSalesCountChart" class="chart-box"></div>
          <div id="initShopsGoodsSalesSumChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {isAuth} from '../../../utils'

export default {
  data () {
    return {
      pickerStartOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now() - 3600 * 1000 * 24
        },
        firstDayOfWeek: 1
      },
      pickerEndOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now() - 3600 * 1000 * 24
        },
        firstDayOfWeek: 1
      },
      shopsId: '',
      startTime: '',
      endTime: '',
      details: '',
      shopsGoodsSalesCountChart: null,
      shopsGoodsSalesSumChart: null,
      allShopsSalesSumChart: null,
      shopsList: []
    }
  },
  activated () {
    this.$http({
      url: '/mall/shops/queryMyShop',
      method: 'get'
    }).then(({data}) => {
      if (data && data.code === 0) {
        this.shopsList = data.list
      }
    })
    if (this.shopsGoodsSalesCountChart) {
      this.shopsGoodsSalesCountChart.resize()
    }
    if (this.shopsGoodsSalesSumChart) {
      this.shopsGoodsSalesSumChart.resize()
    }
    if (this.allShopsSalesSumChart) {
      this.allShopsSalesSumChart.resize()
    }
  },
  created () {
    this.initShopsGoodsSalesCount()
    this.initAllShopsSalesCount()
  },
  methods: {
    // 所有分店销售统计
    initAllShopsSalesCount () {
      if (isAuth('mall:order:allReport')) {
        this.$http({
          url: `/mall/order/allShopsGoodsSalesCount`,
          method: 'get',
          params: {
            startTime: this.startTime || '',
            endTime: this.endTime || ''
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            let list = data.list
            let xData = []
            let yData = []
            let priceSum = 0
            for (let item of list) {
              xData.push(item.NAME)
              yData.push(item.PRICE_SUM)
              priceSum += item.PRICE_SUM
            }
            let option = {
              color: ['#409EFF'],
              title: {
                text: '所有店铺销售统计  合计：' + priceSum + '元'
              },
              tooltip: {
                trigger: 'axis',
                // 坐标轴指示器，坐标轴触发有效
                axisPointer: {
                  // 默认为直线，可选为：'line' | 'shadow'
                  type: 'shadow'
                }
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
              },
              xAxis: [
                {
                  type: 'category',
                  data: xData,
                  axisTick: {
                    alignWithLabel: true
                  }
                }
              ],
              yAxis: [
                {
                  type: 'value'
                }
              ],
              series: [
                {
                  name: '销售额(元)',
                  type: 'bar',
                  barWidth: '60%',
                  data: yData
                }
              ]
            }
            this.allShopsSalesSumChart = this.$echarts.init(document.getElementById('initAllShopsSalesSumChart'))
            this.allShopsSalesSumChart.setOption(option)
            window.addEventListener('resize', () => {
              this.allShopsSalesSumChart.resize()
            })
            if (list.length === 0) {
              this.$message({
                message: '暂无店铺销售统计数据',
                type: 'success',
                duration: 1500
              })
            }
          }
        })
      }
    },
    // 分店商品销售统计
    initShopsGoodsSalesCount () {
      if (isAuth('mall:order:report')) {
        this.$http({
          url: `/mall/order/shopsGoodsSalesCount`,
          method: 'get',
          params: {
            shopsId: this.shopsId,
            startTime: this.startTime || '',
            endTime: this.endTime || ''
          }
        }).then(({data}) => {
          if (data && data.code === 0) {
            let list = data.list
            let xData = []
            let yData = []
            let y2Data = []
            let priceSum = 0
            for (let item of list) {
              xData.push(item.GOODS_NAME)
              yData.push(item.GOODS_COUNT)
              y2Data.push(item.PRICE_SUM)
              priceSum += item.PRICE_SUM
            }
            this.details = data.details
            this.shopsId = data.shopsId
            let option = {
              color: ['#997B71'],
              title: {
                text: '商品销售量'
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow'
                }
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
              },
              xAxis: [
                {
                  type: 'category',
                  data: xData,
                  axisTick: {
                    alignWithLabel: true
                  }
                }
              ],
              yAxis: [
                {
                  type: 'value'
                }
              ],
              series: [
                {
                  name: '销量',
                  type: 'bar',
                  barWidth: '60%',
                  data: yData
                }
              ]
            }
            this.shopsGoodsSalesCountChart = this.$echarts.init(document.getElementById('initShopsGoodsSalesCountChart'))
            this.shopsGoodsSalesCountChart.setOption(option)
            this.shopsGoodsSalesSumChart = this.$echarts.init(document.getElementById('initShopsGoodsSalesSumChart'))
            let option2 = {
              color: ['#409EFF'],
              title: {
                text: '商品销售额  合计：' + priceSum + '元'
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow'
                }
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
              },
              xAxis: [
                {
                  type: 'category',
                  data: xData,
                  axisTick: {
                    alignWithLabel: true
                  }
                }
              ],
              yAxis: [
                {
                  type: 'value'
                }
              ],
              series: [
                {
                  name: '销售额(元)',
                  type: 'bar',
                  barWidth: '60%',
                  data: y2Data
                }
              ]
            }
            this.shopsGoodsSalesSumChart.setOption(option2)
            window.addEventListener('resize', () => {
              this.shopsGoodsSalesCountChart.resize()
              this.shopsGoodsSalesSumChart.resize()
            })
            if (list.length === 0) {
              this.$message({
                message: '暂无商品销售数据',
                type: 'success',
                duration: 1500
              })
            }
          }
        })
      }
    }
  }
}
</script>

<style lang="scss">
.chart-box {
  min-height: 400px;
}
</style>
