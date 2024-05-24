<template>
  <div class="mod-room">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="房间名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.anchorName" placeholder="主播昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.liveStatus" placeholder="直播状态" @change="getDataList(1)" clearable>
          <el-option
            key="101"
            label="直播中"
            value="101">
          </el-option>
          <el-option
            key="102"
            label="未开始"
            value="102">
          </el-option>
          <el-option
            key="103"
            label="已结束"
            value="103">
          </el-option>
          <el-option
            key="104"
            label="禁播"
            value="104">
          </el-option>
          <el-option
            key="105"
            label="暂停中"
            value="105">
          </el-option>
          <el-option
            key="106"
            label="异常"
            value="106">
          </el-option>
          <el-option
            key="107"
            label="已过期"
            value="107">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:room:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:room:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
        <el-button v-if="isAuth('mall:room:getLiveInfo')" type="warning" @click="getLiveInfo()">
          同步直播间
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="房间名">
      </el-table-column>
      <el-table-column
        prop="coverImg"
        header-align="center"
        align="center"
        label="封面图片">
        <template slot-scope="scope">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.coverImg)" :src="scope.row.coverImg"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="shareImg"
        header-align="center"
        align="center"
        label="分享图">
        <template slot-scope="scope">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.shareImg)" :src="scope.row.shareImg"/>
        </template>
      </el-table-column>
      <el-table-column
        width="160"
        prop="startTime"
        header-align="center"
        align="center"
        label="直播计划开始时间">
        <template slot-scope="scope">
          {{transDate(scope.row.startTime, 'yyyy-MM-dd hh:mm:ss')}}
        </template>
      </el-table-column>
      <el-table-column
        width="160"
        prop="endTime"
        header-align="center"
        align="center"
        label="直播计划结束时间">
        <template slot-scope="scope">
          {{transDate(scope.row.endTime, 'yyyy-MM-dd hh:mm:ss')}}
        </template>
      </el-table-column>
      <el-table-column
        prop="anchorName"
        header-align="center"
        align="center"
        label="主播昵称">
      </el-table-column>
      <el-table-column
        prop="liveStatus"
        header-align="center"
        align="center"
        label="直播状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.liveStatus === 101" size="small" type="success">直播中</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 102" size="small" type="info">未开始</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 103" size="small">已结束</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 104" size="danger" type="info">禁播</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 105" size="danger" type="warning">暂停中</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 106" size="danger" type="warning">异常</el-tag>
          <el-tag v-else-if="scope.row.liveStatus === 107" size="small" type="warning">已过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('mall:room:getPushUrl')&&scope.row.liveStatus === 102"
                     @click="getPushUrl(scope.row.roomid)">推流地址
          </el-button>
          <el-button type="text" size="small" v-if="isAuth('mall:room:getSharedCode')"
                     @click="getSharedCode(scope.row.roomid)">分享二维码
          </el-button>
          <el-button type="text" size="small" v-if="isAuth('mall:room:update')&&scope.row.liveStatus === 102"
                     @click="addOrUpdateHandle(scope.row.roomid)">修改
          </el-button>
          <el-button type="text" size="small" v-if="isAuth('mall:room:addgoods')&&scope.row.liveStatus === 102"
                     @click="showAddGoods(scope.row.roomid)">导入商品
          </el-button>
          <el-button type="text" size="small" @click="getGoodsDataList(scope.row.roomid)">商品
          </el-button>
          <el-button type="text" size="small" @click="getGoodsMediaList(scope.row.roomid)">回放
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog
      title="直播间商品"
      :close-on-click-modal="false"
      :visible.sync="visibleGoods">
      <el-table
        :data="goodsData"
        stripe
        style="width: 100%">
        <el-table-column
          prop="name"
          label="商品名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="url"
          label="商品url"
          width="150">
        </el-table-column>
        <el-table-column
          prop="coverImg"
          header-align="center"
          align="center"
          label="封面图片">
          <template slot-scope="scope">
            <img style="height: 50%;width: 50%" @click="openImg(scope.row.coverImg)" :src="scope.row.coverImg"/>
          </template>
        </el-table-column>
        <el-table-column
          prop="price"
          label="商品价格（元）"
          width="150">
          <template slot-scope="scope">
            {{scope.row.price / 100}}
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visibleGoods = false">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="visibleMedis">
      <el-table
        :data="mediaData"
        stripe
        style="width: 100%">
        <el-table-column
          prop="mediaUrl"
          label="回放视频">
          <template slot-scope="scope">
            <el-link target="_blank" :href="scope.row.mediaUrl" type="info">{{scope.row.mediaUrl}}</el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="expireTime"
          label="过期时间">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visibleMedis = false">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="导入商品"
      :close-on-click-modal="false"
      :visible.sync="visibleAddGoods">
      <el-form @keyup.enter.native="submitAddGoods()"
               label-width="80px">
        <el-form-item label="选择商品" prop="priceType">
          <el-select v-model="goodsIds" multiple clearable filterable style="width: 500px;">
            <el-option
              v-for="item in goodsList"
              :key="item.goodsId"
              :label="item.name"
              :value="item.goodsId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visibleAddGoods = false">取消</el-button>
        <el-button type="primary" @click="submitAddGoods()">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="visibleUrl">
      <el-alert
        :closable="false"
        :title="url"
        type="success">
      </el-alert>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visibleUrl = false">取消</el-button>
      </span>
    </el-dialog>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './room-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        anchorName: '',
        liveStatus: '',
        name: ''
      },
      url: '',
      visibleGoods: false,
      visibleMedis: false,
      visibleAddGoods: false,
      visibleUrl: false,
      dataList: [],
      goodsData: [],
      mediaData: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      roomId: '',
      goodsList: [],
      goodsIds: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  deactivated () {
    this.$notify.closeAll()
  },
  created () {
    this.$notify({
      title: '主播扫码开播',
      dangerouslyUseHTMLString: true,
      message: '<image style="width: 280px; height: 280px;" src="https://platform-wxmall.oss-cn-beijing.aliyuncs.com/live.jpg"></image>',
      duration: 0
    })
  },
  methods: {
    // 同步直播间
    getLiveInfo () {
      this.$http({
        url: '/mall/room/getLiveInfo',
        method: 'get',
        params: {
          'page': 0,
          'limit': 100
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500
          })
          this.getDataList()
        }
      })
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/room/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'anchorName': this.searchForm.anchorName,
          'liveStatus': this.searchForm.liveStatus,
          'name': this.searchForm.name
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.records
          this.totalPage = data.page.total
        } else {
          this.dataList = []
          this.totalPage = 0
        }
      })
    },
    getGoodsMediaList (roomid) {
      this.visibleMedis = true
      this.$http({
        url: '/mall/roommedia/queryAll',
        method: 'get',
        params: {
          'roomid': roomid
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.mediaData = data.list
        } else {
          this.mediaData = []
        }
      })
    },
    getSharedCode (roomid) {
      this.$http({
        url: `/mall/room/getSharedCode/${roomid}`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.visibleUrl = true
          this.url = data.sharedCode
        }
      })
    },
    getPushUrl (roomid) {
      this.$http({
        url: `/mall/room/getPushUrl/${roomid}`,
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.visibleUrl = true
          this.url = data.pushUrl
        }
      })
    },
    submitAddGoods () {
      this.$http({
        url: '/mall/room/addgoods',
        method: 'post',
        data: {
          roomId: this.roomId,
          goodsIds: this.goodsIds
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500
          })
          this.visibleAddGoods = false
          this.$emit('refreshDataList')
        }
      })
    },
    showAddGoods (roomId) {
      this.visibleAddGoods = true
      this.roomId = roomId
      this.goodsIds = []
      this.$http({
        url: '/mall/roomallgoods/queryAll',
        method: 'get',
        params: {
          'auditStatus': 2
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.goodsList = data.list
        } else {
          this.goodsList = []
        }
      })
    },
    // 获取数据列表
    getGoodsDataList (roomid) {
      this.visibleGoods = true
      this.$http({
        url: '/mall/roomgoods/queryAll',
        method: 'get',
        params: {
          'roomid': roomid
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.goodsData = data.list
        } else {
          this.goodsData = []
        }
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 查看详情
    showDetails (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, true)
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (roomid) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(roomid)
      })
    },
    // 删除
    deleteHandle () {
      let ids = this.dataListSelections.map(item => {
        return item.roomid
      })
      this.$confirm('确定对所选项进行[删除]操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/room/delete',
          method: 'post',
          data: ids
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.getDataList()
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>
