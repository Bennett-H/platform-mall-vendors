<template>
  <div>
    <el-row :gutter="20">
<!--      <el-card shadow="hover">-->
<!--        <div slot="header" class="clearfix">-->
<!--          <b>小程序访问趋势数据</b>-->
<!--        </div>-->
<!--        <el-col :span="24" v-if="dailySummary.refDate">-->
<!--          <el-card shadow="hover">-->
<!--            <el-descriptions :title="'概况趋势('+dailySummary.refDate+')'" :column="3" border>-->
<!--              <template slot="extra">-->
<!--                <el-date-picker-->
<!--                  style="float: right;width: 140px"-->
<!--                  v-model="value1"-->
<!--                  type="date"-->
<!--                  @change="changeValue1"-->
<!--                  value-format="yyyy-MM-dd"-->
<!--                  placeholder="选择日期"-->
<!--                  :picker-options="pickerOptions">-->
<!--                </el-date-picker>-->
<!--              </template>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  累计访问人数-->
<!--                </template>-->
<!--                {{dailySummary.visitTotal}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  转发次数-->
<!--                </template>-->
<!--                {{dailySummary.sharePv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  转发人数-->
<!--                </template>-->
<!--                {{dailySummary.shareUv}}-->
<!--              </el-descriptions-item>-->
<!--            </el-descriptions>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="8" v-if="dailyVisit.refDate" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <el-descriptions :title="'日访问趋势('+dailyVisit.refDate+')'" :column="2" border>-->
<!--              <template slot="extra">-->
<!--                <el-date-picker-->
<!--                  style="float: right;width: 140px"-->
<!--                  v-model="value2"-->
<!--                  type="date"-->
<!--                  @change="changeValue2"-->
<!--                  value-format="yyyy-MM-dd"-->
<!--                  placeholder="选择日期"-->
<!--                  :picker-options="pickerOptions">-->
<!--                </el-date-picker>-->
<!--              </template>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  打开次数-->
<!--                </template>-->
<!--                {{dailyVisit.sessionCnt}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问次数-->
<!--                </template>-->
<!--                {{dailyVisit.visitPv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问人数-->
<!--                </template>-->
<!--                {{dailyVisit.visitUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  新用户数-->
<!--                </template>-->
<!--                {{dailyVisit.visitUvNew}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  人均停留时长-->
<!--                </template>-->
<!--                {{dailyVisit.stayTimeUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  次均停留时长-->
<!--                </template>-->
<!--                {{dailyVisit.stayTimeSession}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  平均访问深度-->
<!--                </template>-->
<!--                {{dailyVisit.visitDepth}}-->
<!--              </el-descriptions-item>-->
<!--            </el-descriptions>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="8" v-if="weeklyVisit.refDate" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <el-descriptions :title="'周访问趋势('+weeklyVisit.refDate+')'" :column="2" border>-->
<!--              <template slot="extra">-->
<!--                <el-date-picker-->
<!--                  style="float: right;width: 160px"-->
<!--                  v-model="value3"-->
<!--                  type="week"-->
<!--                  format="yyyy 第 WW 周"-->
<!--                  @change="changeValue3"-->
<!--                  value-format="yyyy-MM-dd"-->
<!--                  placeholder="选择周"-->
<!--                  :picker-options="pickerWeekOptions">-->
<!--                </el-date-picker>-->
<!--              </template>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  打开次数-->
<!--                </template>-->
<!--                {{weeklyVisit.sessionCnt}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问次数-->
<!--                </template>-->
<!--                {{weeklyVisit.visitPv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问人数-->
<!--                </template>-->
<!--                {{weeklyVisit.visitUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  新用户数-->
<!--                </template>-->
<!--                {{weeklyVisit.visitUvNew}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  人均停留时长-->
<!--                </template>-->
<!--                {{weeklyVisit.stayTimeUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  次均停留时长-->
<!--                </template>-->
<!--                {{weeklyVisit.stayTimeSession}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  平均访问深度-->
<!--                </template>-->
<!--                {{weeklyVisit.visitDepth}}-->
<!--              </el-descriptions-item>-->
<!--            </el-descriptions>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="8" v-if="monthlyVisit.refDate" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <el-descriptions :title="'月访问趋势('+monthlyVisit.refDate+')'" :column="2" border>-->
<!--              <template slot="extra">-->
<!--                <el-date-picker-->
<!--                  style="float: right;width: 160px"-->
<!--                  v-model="value4"-->
<!--                  type="month"-->
<!--                  format="yyyy-MM"-->
<!--                  @change="changeValue4"-->
<!--                  value-format="yyyy-MM-dd"-->
<!--                  placeholder="选择月"-->
<!--                  :picker-options="pickerMonthOptions">-->
<!--                </el-date-picker>-->
<!--              </template>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  打开次数-->
<!--                </template>-->
<!--                {{monthlyVisit.sessionCnt}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问次数-->
<!--                </template>-->
<!--                {{monthlyVisit.visitPv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  访问人数-->
<!--                </template>-->
<!--                {{monthlyVisit.visitUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  新用户数-->
<!--                </template>-->
<!--                {{monthlyVisit.visitUvNew}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  人均停留时长-->
<!--                </template>-->
<!--                {{monthlyVisit.stayTimeUv}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  次均停留时长-->
<!--                </template>-->
<!--                {{monthlyVisit.stayTimeSession}}-->
<!--              </el-descriptions-item>-->
<!--              <el-descriptions-item>-->
<!--                <template slot="label">-->
<!--                  平均访问深度-->
<!--                </template>-->
<!--                {{monthlyVisit.visitDepth}}-->
<!--              </el-descriptions-item>-->
<!--            </el-descriptions>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--      </el-card>-->
<!--      <el-card shadow="hover">-->
<!--        <div slot="header" class="clearfix">-->
<!--          <b>小程序访问分布数据<b v-if="visitDistribution.refDate">({{visitDistribution.refDate}})</b></b>-->
<!--          <el-date-picker-->
<!--            style="float: right;width: 140px"-->
<!--            v-model="value5"-->
<!--            type="date"-->
<!--            @change="changeValue5"-->
<!--            value-format="yyyy-MM-dd"-->
<!--            placeholder="选择日期"-->
<!--            :picker-options="pickerOptions">-->
<!--          </el-date-picker>-->
<!--        </div>-->
<!--        <el-col :span="12" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <div id="initVisitDistributionCntChart" class="chart-box"></div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="12" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <div id="initVisitDistributionUvChart" class="chart-box"></div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="12" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <div id="initVisitDistributionDeepChart" class="chart-box"></div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--        <el-col :span="12" :xs="24">-->
<!--          <el-card shadow="hover">-->
<!--            <div id="initVisitDistributionStayTimeChart" class="chart-box"></div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--      </el-card>-->
<!--      <el-card shadow="hover">-->
<!--        <div slot="header" class="clearfix">-->
<!--          <b>小程序访问页面排行(<b>{{visitPageList.date}}</b>)</b>-->
<!--          <el-date-picker-->
<!--            style="float: right;width: 140px"-->
<!--            v-model="value6"-->
<!--            type="date"-->
<!--            @change="changeValue6"-->
<!--            value-format="yyyy-MM-dd"-->
<!--            placeholder="选择日期"-->
<!--            :picker-options="pickerOptions">-->
<!--          </el-date-picker>-->
<!--        </div>-->
<!--        <el-table-->
<!--          :data="visitPageList.list"-->
<!--          height="400"-->
<!--          border-->
<!--          style="width: 100%">-->
<!--          <el-table-column-->
<!--            type="index"-->
<!--            :index="indexMethod">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pagePath"-->
<!--            label="页面路径"-->
<!--            width="300">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pageVisitPv"-->
<!--            label="访问次数">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pageVisitUv"-->
<!--            label="访问人数">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pageStayTimePv"-->
<!--            label="次均停留时长">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="entryPagePv"-->
<!--            label="进入页次数">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="exitPagePv"-->
<!--            label="退出页次数">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pageSharePv"-->
<!--            label="转发次数">-->
<!--          </el-table-column>-->
<!--          <el-table-column-->
<!--            prop="pageShareUv"-->
<!--            label="转发人数">-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </el-card>-->
      <el-card shadow="hover">
        <div id="initFootprintCountChart" class="chart-box"></div>
      </el-card>
      <el-card shadow="hover" v-if="isAuth('sys:monitor:server')">
        <div slot="header" class="clearfix">
          <b>服务器监控</b>
        </div>
        <el-col :span="6" :xs="24">
          <el-card shadow="hover">
            <div id="cpuChart" class="chart-box"></div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="24">
          <el-card shadow="hover">
            <div id="memoryChart" class="chart-box"></div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="24">
          <el-card shadow="hover">
            <div id="diskChart" class="chart-box"></div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="24">
          <el-card shadow="hover">
            <b>磁盘状态</b>
            <el-card shadow="hover" v-for="item in monitorData.diskInfos" :key="item.uuid">
              <b>{{item.volume}}</b>
              <el-progress :text-inside="true" :stroke-width="15" :percentage="item.usePercent"></el-progress>
              <label>{{item.used}}可用，共{{item.size}}</label>
            </el-card>
          </el-card>
        </el-col>
        <el-col :span="12" v-if="monitorData.sysInfo" :xs="24">
          <el-card shadow="hover">
            <el-descriptions title="服务器信息" :column="2" border>
              <el-descriptions-item>
                <template slot="label">
                  服务器名称
                </template>
                {{monitorData.sysInfo.name}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  操作系统
                </template>
                {{monitorData.sysInfo.osName}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  服务器IP
                </template>
                {{monitorData.sysInfo.ip}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  系统架构
                </template>
                {{monitorData.sysInfo.osArch}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  内核
                </template>
                {{monitorData.cupInfo.logicalProcessorCount}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  逻辑处理器
                </template>
                {{monitorData.cupInfo.physicalProcessorCount}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  处理器
                </template>
                {{monitorData.centralProcessor.name}}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12" v-if="monitorData.jvmInfo" :xs="24">
          <el-card shadow="hover">
            <el-descriptions title="Java虚拟机信息" :column="2" border>
              <el-descriptions-item>
                <template slot="label">
                  Java名称
                </template>
                {{monitorData.jvmInfo.jdkName}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  Java版本
                </template>
                {{monitorData.jvmInfo.jdkVersion}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  最大内存
                </template>
                {{monitorData.jvmInfo.maxMemory}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  已使用内存
                </template>
                {{monitorData.jvmInfo.usedMemory}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  可用内存
                </template>
                {{monitorData.jvmInfo.freeMemory}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  内存使用率
                </template>
                {{monitorData.jvmInfo.usePercent * 100}}%
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  启动时间
                </template>
                {{transDate(monitorData.jvmInfo.startTime / 1000, 'yyyy-MM-dd hh:mm:ss')}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  安装路径
                </template>
                {{monitorData.jvmInfo.jdkHome}}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="24" v-if="monitorData.processList">
          <el-card shadow="hover">
            <div slot="header">
              <b>系统前10进程监控</b>
            </div>
            <el-table
              :data="monitorData.processList"
              stripe
              border
              style="width: 100%">
              <el-table-column
                prop="pid"
                label="pid">
              </el-table-column>
              <el-table-column
                prop="name"
                label="name">
              </el-table-column>
              <el-table-column
                prop="cpu"
                label="cpu">
                <template slot-scope="scope">
                  {{scope.row.cpu}}%
                </template>
              </el-table-column>
              <el-table-column
                prop="threadCount"
                label="threadCount">
              </el-table-column>
              <el-table-column
                prop="virtualSize"
                label="virtualSize">
              </el-table-column>
              <el-table-column
                prop="residentSetSize"
                label="residentSetSize">
              </el-table-column>
              <el-table-column
                prop="kernelTime"
                label="kernelTime">
              </el-table-column>
              <el-table-column
                prop="userTime"
                label="userTime">
              </el-table-column>
              <el-table-column
                prop="startTime"
                label="startTime">
                <template slot-scope="scope">
                  {{transDate(scope.row.startTime / 1000, 'yyyy-MM-dd hh:mm:ss')}}
                </template>
              </el-table-column>
              <el-table-column
                prop="upTime"
                label="upTime">
              </el-table-column>
              <el-table-column
                prop="bytesRead"
                label="bytesRead">
              </el-table-column>
              <el-table-column
                prop="bytesWritten"
                label="bytesWritten">
              </el-table-column>
              <el-table-column
                prop="openFiles"
                label="openFiles">
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-card>
    </el-row>
  </div>
</template>

<script>
export default {
  data () {
    return {
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() > Date.now() - 3600 * 1000 * 24
        },
        shortcuts: [{
          text: '昨天',
          onClick (picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '前天',
          onClick (picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 2)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick (picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }],
        firstDayOfWeek: 1
      },
      pickerWeekOptions: {
        disabledDate (time) {
          let now = new Date()
          let nowYear = now.getFullYear()
          let nowMonth = now.getMonth()
          let nowDay = now.getDate()
          let day = now.getDay() || 7
          return time.getTime() > new Date(nowYear, nowMonth, nowDay + 1 - day).getTime() - 1
        },
        firstDayOfWeek: 1
      },
      pickerMonthOptions: {
        disabledDate (time) {
          let now = new Date()
          let nowYear = now.getFullYear()
          let nowMonth = now.getMonth()
          return time.getTime() > new Date(nowYear, nowMonth, 1).getTime() - 1
        }
      },
      value1: '',
      value2: '',
      value3: '',
      value4: '',
      value5: '',
      value6: '',
      dailySummary: [],
      dailyVisit: [],
      weeklyVisit: [],
      monthlyVisit: [],
      visitPageList: [],
      visitDistribution: {
        refDate: '',
        list: {
          access_source_visit_uv: {},
          access_staytime_info: {},
          access_source_session_cnt: {},
          access_depth_info: {}
        }
      },
      footprintCountChart: null,
      visitDistributionCntChart: null,
      visitDistributionStayTimeChart: null,
      visitDistributionDeepChart: null,
      monitorVisible: false,
      monitorData: [],
      cpuChart: null,
      memoryChart: null,
      diskChart: null
    }
  },
  computed: {
    shopsId: {
      get () {
        return this.$store.state.user.shopsId
      }
    }
  },
  watch: {
    shopsId (newValue, oldValue) {
      this.initFootprintCount()
    }
  },
  created () {
    if (this.$cookie.get('token')) {
      this.initFootprintCount()
      // this.getDailySummaryTrend()
      // this.getDailyVisitTrend()
      // this.getWeeklyVisitTrend()
      // this.getMonthlyVisitTrend()
      // this.getVisitDistribution()
      // this.getVisitPage()
      // if (this.isAuth('sys:monitor:server')) {
      //   this.monitor()
      // }
    }
  },
  activated () {
    // 由于给echart添加了resize事件, 在组件激活时需要重新resize绘画一次, 否则出现空白bug
    if (this.footprintCountChart) {
      this.footprintCountChart.resize()
    }
    if (this.visitDistributionCntChart) {
      this.visitDistributionCntChart.resize()
    }
    if (this.visitDistributionStayTimeChart) {
      this.visitDistributionStayTimeChart.resize()
    }
    if (this.visitDistributionDeepChart) {
      this.visitDistributionDeepChart.resize()
    }
    if (this.isAuth('sys:monitor:server')) {
      if (this.cpuChart) {
        this.cpuChart.resize()
      }
      if (this.memoryChart) {
        this.memoryChart.resize()
      }
      if (this.diskChart) {
        this.diskChart.resize()
      }
    }
  },
  methods: {
    indexMethod (index) {
      return index + 1
    },
    changeValue1 () {
      this.getDailySummaryTrend()
    },
    changeValue2 () {
      this.getDailyVisitTrend()
    },
    changeValue3 () {
      this.getWeeklyVisitTrend()
    },
    changeValue4 () {
      this.getMonthlyVisitTrend()
    },
    changeValue5 () {
      this.getVisitDistribution()
    },
    changeValue6 () {
      this.getVisitPage()
    },
    getDailySummaryTrend () {
      this.$http({
        url: '/wx/maanalysis/getDailySummaryTrend',
        method: 'get',
        params: {
          date: this.value1
        }
      }).then(({data}) => {
        if (data && data.code === 0 && data.list.length > 0) {
          this.dailySummary = data.list[0]
        } else {
          this.dailySummary = [{
            'refDate': '',
            'visitTotal': '',
            'sharePv': '',
            'shareUv': ''
          }]
        }
      })
    },
    getDailyVisitTrend () {
      this.$http({
        url: '/wx/maanalysis/getDailyVisitTrend',
        method: 'get',
        params: {
          date: this.value2
        }
      }).then(({data}) => {
        if (data && data.code === 0 && data.list.length > 0) {
          this.dailyVisit = data.list[0]
        } else {
          this.dailyVisit = [{
            'refDate': '',
            'sessionCnt': '',
            'visitPv': '',
            'visitUv': '',
            'visitUvNew': '',
            'stayTimeUv': '',
            'stayTimeSession': '',
            'visitDepth': ''
          }]
        }
      })
    },
    getWeeklyVisitTrend () {
      this.$http({
        url: '/wx/maanalysis/getWeeklyVisitTrend',
        method: 'get',
        params: {
          date: this.value3
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          if (data && data.code === 0 && data.list.length > 0) {
            this.weeklyVisit = data.list[0]
          } else {
            this.weeklyVisit = [{
              'refDate': '',
              'sessionCnt': '',
              'visitPv': '',
              'visitUv': '',
              'visitUvNew': '',
              'stayTimeUv': '',
              'stayTimeSession': '',
              'visitDepth': ''
            }]
          }
        }
      })
    },
    getMonthlyVisitTrend () {
      this.$http({
        url: '/wx/maanalysis/getMonthlyVisitTrend',
        method: 'get',
        params: {
          date: this.value4
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          if (data && data.code === 0 && data.list.length > 0) {
            this.monthlyVisit = data.list[0]
          } else {
            this.monthlyVisit = [{
              'refDate': '',
              'sessionCnt': '',
              'visitPv': '',
              'visitUv': '',
              'visitUvNew': '',
              'stayTimeUv': '',
              'stayTimeSession': '',
              'visitDepth': ''
            }]
          }
        }
      })
    },
    getVisitDistribution () {
      this.$http({
        url: '/wx/maanalysis/getVisitDistribution',
        method: 'get',
        params: {
          date: this.value5
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.visitDistribution = data.data

          // 访问来源分布
          let visitDistributionCntChartDataX = []
          let visitDistributionCntChartDataY = []
          for (let key in this.visitDistribution.list.access_source_session_cnt) {
            let element = this.visitDistribution.list.access_source_session_cnt[key]
            visitDistributionCntChartDataX.push(this.translateScene(key))
            visitDistributionCntChartDataY.push({
              value: element,
              name: this.translateScene(key)
            })
          }
          let cntOption = {
            title: {
              text: '访问来源分布（PV）',
              left: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 10,
              data: visitDistributionCntChartDataX
            },
            toolbox: {
              show: true,
              feature: {
                mark: {show: true},
                dataView: {
                  show: true,
                  readOnly: true
                },
                magicType: {
                  show: true,
                  type: ['pie', 'funnel']
                },
                saveAsImage: {show: true}
              }
            },
            series: [{
              name: '访问来源(PV)',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: visitDistributionCntChartDataY
            }]
          }
          this.visitDistributionCntChart = this.$echarts.init(document.getElementById('initVisitDistributionCntChart'))
          this.visitDistributionCntChart.setOption(cntOption)
          window.addEventListener('resize', () => {
            this.visitDistributionCntChart.resize()
          })

          // 访问时长分布UV
          let visitDistributionUvChartDataX = []
          let visitDistributionUvChartDataY = []
          for (let key in this.visitDistribution.list.access_source_visit_uv) {
            let element = this.visitDistribution.list.access_source_visit_uv[key]
            visitDistributionUvChartDataX.push(this.translateScene(key))
            visitDistributionUvChartDataY.push({
              value: element,
              name: this.translateScene(key)
            })
          }
          let uvOption = {
            title: {
              text: '访问来源分布（UV）',
              left: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 10,
              data: visitDistributionUvChartDataX
            },
            toolbox: {
              show: true,
              feature: {
                mark: {show: true},
                dataView: {
                  show: true,
                  readOnly: true
                },
                magicType: {
                  show: true,
                  type: ['pie', 'funnel']
                },
                saveAsImage: {show: true}
              }
            },
            series: [{
              name: '访问来源(UV)',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: visitDistributionUvChartDataY
            }]
          }
          this.visitDistributionUvChart = this.$echarts.init(document.getElementById('initVisitDistributionUvChart'))
          this.visitDistributionUvChart.setOption(uvOption)
          window.addEventListener('resize', () => {
            this.visitDistributionUvChart.resize()
          })

          // 访问深度的分布
          let visitDistributionDeepChartDataX = []
          let visitDistributionDeepChartDataY = []
          for (let key in this.visitDistribution.list.access_depth_info) {
            let element = this.visitDistribution.list.access_depth_info[key]
            visitDistributionDeepChartDataX.push(this.translateDeep(key))
            visitDistributionDeepChartDataY.push(element)
          }
          let deepOption = {
            color: ['#11C26D'],
            title: {
              text: '访问深度',
              left: 'center'
            },
            toolbox: {
              show: true,
              feature: {
                mark: {show: true},
                dataView: {
                  show: true,
                  readOnly: true
                },
                magicType: {
                  show: true,
                  type: ['pie', 'funnel']
                },
                saveAsImage: {show: true}
              }
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
            xAxis: [{
              type: 'category',
              data: visitDistributionDeepChartDataX,
              axisTick: {
                alignWithLabel: true
              }
            }],
            series: [{
              name: '访问深度(PV)',
              type: 'bar',
              barWidth: '60%',
              data: visitDistributionDeepChartDataY
            }],
            yAxis: {
              type: 'value'
            }
          }
          this.visitDistributionDeepChart = this.$echarts.init(document.getElementById('initVisitDistributionDeepChart'))
          this.visitDistributionDeepChart.setOption(deepOption)
          window.addEventListener('resize', () => {
            this.visitDistributionDeepChart.resize()
          })

          // 访问时长分布
          let visitDistributionStayTimeChartDataX = []
          let visitDistributionStayTimeChartDataY = []
          for (let key in this.visitDistribution.list.access_staytime_info) {
            let element = this.visitDistribution.list.access_staytime_info[key]
            visitDistributionStayTimeChartDataX.push(this.translateStayTime(key))
            visitDistributionStayTimeChartDataY.push(element)
          }
          let stayTimeOption = {
            color: ['#3E8EF7'],
            title: {
              text: '访问时长',
              left: 'center'
            },
            toolbox: {
              show: true,
              feature: {
                mark: {show: true},
                dataView: {
                  show: true,
                  readOnly: true
                },
                magicType: {
                  show: true,
                  type: ['pie', 'funnel']
                },
                saveAsImage: {show: true}
              }
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
            xAxis: [{
              type: 'category',
              data: visitDistributionStayTimeChartDataX,
              axisTick: {
                alignWithLabel: true
              }
            }],
            series: [{
              name: '访问时长(PV)',
              type: 'bar',
              barWidth: '60%',
              data: visitDistributionStayTimeChartDataY
            }],
            yAxis: {
              type: 'value'
            }
          }
          this.visitDistributionStayTimeChart = this.$echarts.init(document.getElementById('initVisitDistributionStayTimeChart'))
          this.visitDistributionStayTimeChart.setOption(stayTimeOption)
          window.addEventListener('resize', () => {
            this.visitDistributionStayTimeChart.resize()
          })
        }
      })
    },
    getVisitPage () {
      this.$http({
        url: '/wx/maanalysis/getVisitPage',
        method: 'get',
        params: {
          date: this.value6
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.visitPageList = data
        }
      })
    },
    monitor () {
      this.$http({
        url: '/sys/monitor/server',
        method: 'post'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.monitorVisible = true
          this.monitorData = data
          let option = {
            series: [
              {
                type: 'gauge',
                axisLine: {
                  lineStyle: {
                    width: 30,
                    color: [
                      [0.3, '#67e0e3'],
                      [0.7, '#37a2da'],
                      [1, '#fd666d']
                    ]
                  }
                },
                detail: {
                  valueAnimation: true,
                  formatter: '{value}%'
                },
                data: [{
                  value: Math.round(data.cupInfo.usePercent * 100),
                  name: 'CPU'
                }]
              }
            ]
          }
          this.cpuChart = this.$echarts.init(document.getElementById('cpuChart'))
          this.cpuChart.setOption(option)
          window.addEventListener('resize', () => {
            this.cpuChart.resize()
          })
          option = {
            series: [
              {
                type: 'gauge',
                axisLine: {
                  lineStyle: {
                    width: 30,
                    color: [
                      [0.3, '#67e0e3'],
                      [0.7, '#37a2da'],
                      [1, '#fd666d']
                    ]
                  }
                },
                detail: {
                  valueAnimation: true,
                  formatter: '{value}%'
                },
                data: [{
                  value: Math.round(data.memoryInfo.usePercent * 100),
                  name: '内存'
                }]
              }
            ]
          }
          this.memoryChart = this.$echarts.init(document.getElementById('memoryChart'))
          this.memoryChart.setOption(option)
          window.addEventListener('resize', () => {
            this.memoryChart.resize()
          })
          option = {
            series: [
              {
                type: 'gauge',
                axisLine: {
                  lineStyle: {
                    width: 30,
                    color: [
                      [0.3, '#67e0e3'],
                      [0.7, '#37a2da'],
                      [1, '#fd666d']
                    ]
                  }
                },
                detail: {
                  valueAnimation: true,
                  formatter: '{value}%'
                },
                data: [{
                  value: Math.round(data.diskUsePercent),
                  name: '磁盘'
                }]
              }
            ]
          }
          this.diskChart = this.$echarts.init(document.getElementById('diskChart'))
          this.diskChart.setOption(option)
          window.addEventListener('resize', () => {
            this.diskChart.resize()
          })
        }
      })
    },
    // 柱状图
    initFootprintCount () {
      if (!this.shopsId) {
        return
      }
      this.$http({
        url: `/mall/footprint/footprintCount`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          let list = data.list
          let xData = []
          let yData = []
          for (let item of list) {
            xData.push(item.goodsName)
            yData.push(item.goodsSum)
          }
          let option = {
            title: {
              text: '商品浏览量',
              subtext: '统计'
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
                name: '点击量（次）',
                type: 'bar',
                barWidth: '60%',
                data: yData
              }
            ]
          }
          this.footprintCountChart = this.$echarts.init(document.getElementById('initFootprintCountChart'))
          this.footprintCountChart.setOption(option)
          window.addEventListener('resize', () => {
            this.footprintCountChart.resize()
          })
        }
      })
    },
    translateScene (sceneId) {
      let result = ''
      sceneId = Number(sceneId)
      switch (sceneId) {
        case 1:
          result = '小程序历史列表'
          break
        case 2:
          result = '搜索'
          break
        case 3:
          result = '会话'
          break
        case 4:
          result = '扫一扫二维码'
          break
        case 5:
          result = '公众号主页'
          break
        case 6:
          result = '聊天顶部'
          break
        case 7:
          result = '系统桌面'
          break
        case 8:
          result = '小程序主页'
          break
        case 9:
          result = '附近的小程序'
          break
        case 11:
          result = '模板消息'
          break
        case 13:
          result = '公众号菜单'
          break
        case 14:
          result = 'APP分享'
          break
        case 15:
          result = '支付完成页'
          break
        case 16:
          result = '长按识别二维码'
          break
        case 17:
          result = '相册选取二维码'
          break
        case 18:
          result = '公众号文章'
          break
        case 19:
          result = '钱包'
          break
        case 20:
          result = '卡包'
          break
        case 21:
          result = '小程序内卡券'
          break
        case 22:
          result = '其他小程序'
          break
        case 23:
          result = '其他小程序返回'
          break
        case 24:
          result = '卡券适用门店列表'
          break
        case 25:
          result = '搜索框快捷入口'
          break
        case 26:
          result = '小程序客服消息'
          break
        case 27:
          result = '公众号下发'
          break
        case 29:
          result = '任务栏-最近使用'
          break
        case 30:
          result = '长按小程序菜单圆点'
          break
        case 31:
          result = '连wifi成功页'
          break
        case 32:
          result = '城市服务'
          break
        case 33:
          result = '微信广告'
          break
        case 34:
          result = '其他移动应用'
          break
        case 35:
          result = '发现入口-我的小程序'
          break
        case 36:
          result = '任务栏-我的小程序'
          break
        default:
          result = '其他'
      }
      return result
    },
    translateStayTime (key) {
      let result = ''
      key = Number(key)
      switch (key) {
        case 1:
          result = '0-2s'
          break
        case 2:
          result = '3-5s'
          break
        case 3:
          result = '6-10s'
          break
        case 4:
          result = '11-20s'
          break
        case 5:
          result = '20-30s'
          break
        case 6:
          result = '30-50s'
          break
        case 7:
          result = '50-100s'
          break
        case 8:
          result = '>100s'
          break
        default:
          result = '其他'
      }
      return result
    },
    translateDeep (key) {
      let result = ''
      key = Number(key)
      switch (key) {
        case 1:
          result = '1 页'
          break
        case 2:
          result = '2 页'
          break
        case 3:
          result = '3 页'
          break
        case 4:
          result = '4 页'
          break
        case 5:
          result = '5 页'
          break
        case 6:
          result = '6-10 页'
          break
        case 7:
          result = '>10 页'
          break
        default:
          result = '其他'
      }
      return result
    }
  }
}
</script>

<style lang="scss">
.chart-box {
  min-height: 400px;
}
</style>
