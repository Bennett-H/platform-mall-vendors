<template>
  <div class="mod-coupon">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.title" placeholder="优惠券标题" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.couponSn" placeholder="优惠券编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.couponType" clearable placeholder="优惠券类型" @change="getDataList(1)">
          <el-option
            key="1"
            label="代金券"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="折扣"
            value="2">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.status" clearable placeholder="状态" @change="getDataList(1)">
          <el-option
            key="1"
            label="正常"
            value="1">
          </el-option>
          <el-option
            key="2"
            label="过期"
            value="2">
          </el-option>
          <el-option
            key="3"
            label="禁用"
            value="3">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:coupon:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:coupon:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
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
        prop="title"
        header-align="center"
        align="center"
        label="标题">
      </el-table-column>
      <el-table-column
        prop="couponSn"
        header-align="center"
        align="center"
        label="编号">
      </el-table-column>
      <el-table-column
        prop="couponType"
        header-align="center"
        align="center"
        label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.couponType === 1" size="small" type="danger">代金券</el-tag>
          <el-tag v-else-if="scope.row.couponType === 2" size="small" type="success">折扣</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="minPrice"
        header-align="center"
        align="center"
        label="最低消费金额">
      </el-table-column>
      <el-table-column
        prop="subPrice"
        header-align="center"
        align="center"
        label="优惠">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.couponType === 1" size="small" type="danger">{{scope.row.subPrice}}元</el-tag>
          <el-tag v-else-if="scope.row.couponType === 2" size="small" type="success">{{scope.row.discount}}折</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        width="170"
        show-tooltip-when-overflow
        prop="beginTime"
        header-align="center"
        align="center"
        label="有效期">
        <template slot-scope="scope">
          {{scope.row.beginTime}} / {{scope.row.endTime}}
        </template>
      </el-table-column>
      <el-table-column
        width="170"
        show-tooltip-when-overflow
        prop="beginGetTime"
        header-align="center"
        align="center"
        label="领取时间">
        <template slot-scope="scope">
          {{scope.row.beginGetTime}} / {{scope.row.endGetTime}}
        </template>
      </el-table-column>
      <el-table-column
        prop="totalCount"
        header-align="center"
        align="center"
        label="优惠券数量">
      </el-table-column>
      <el-table-column
        prop="sendCount"
        header-align="center"
        align="center"
        label="已发数量">
      </el-table-column>
      <el-table-column
        prop="limitType"
        header-align="center"
        align="center"
        label="使用类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.limitType === 0" size="small" type="danger">全场通用券</el-tag>
          <el-tag v-else-if="scope.row.limitType === 1" size="small" type="success">指定商品</el-tag>
          <el-tag v-else-if="scope.row.limitType === 2" size="small" type="success">指定品牌</el-tag>
          <el-tag v-else-if="scope.row.limitType === 3" size="small" type="success">新人券</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="limitUser"
        header-align="center"
        align="center"
        label="每人限领数量">
      </el-table-column>
      <el-table-column
        width="180"
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 3" size="small" type="danger">禁用</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small" type="info">过期</el-tag>
          <el-tag v-else-if="scope.row.status === 1" size="small" type="success">正常</el-tag>
          <el-button v-if="isAuth('mall:coupon:sendUser') && scope.row.status === 1" type="text"
                     size="mini" @click="sendUser(scope.row.id)">发放
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="sort"
        header-align="center"
        align="center"
        label="排序">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:coupon:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:coupon:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('mall:coupon:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">
            删除
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
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <el-dialog
      title="发放优惠券"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false">
      <el-form :inline="true" :model="searchForm" @keyup.enter.native="getUserList(1)">
        <el-form-item>
          <el-input v-model="searchForm.userName" placeholder="用户昵称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-dict code="SEX" v-model="searchForm.gender"></el-dict>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchForm.mobile" placeholder="手机号" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getUserList(1)">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table
        @selection-change="handleSelectionChange"
        :data="mallUserList"
        style="width: 100%">
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          prop="userName"
          header-align="center"
          align="center"
          label="用户昵称">
        </el-table-column>
        <el-table-column
          prop="gender"
          header-align="center"
          align="center"
          label="性别">
          <template slot-scope="scope">
            <span>{{transDict('SEX', scope.row.gender)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="mobile"
          header-align="center"
          align="center"
          label="手机号">
        </el-table-column>
        <el-table-column
          prop="headImgUrl"
          header-align="center"
          align="center"
          label="用户头像">
          <template slot-scope="scope" v-if="scope.row.headImgUrl">
            <img style="height: 50px;width: 50px" @click="openImg(scope.row.headImgUrl)" :src="scope.row.headImgUrl"/>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandleUser"
        @current-change="currentChangeHandleUser"
        :current-page="pageIndexUser"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSizeUser"
        :total="totalPageUser"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './coupon-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        userName: '',
        gender: '',
        mobile: '',
        title: '',
        couponSn: '',
        couponType: '',
        status: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      pageIndexUser: 1,
      pageSizeUser: 10,
      totalPageUser: 0,
      dataListSelections: [],
      addOrUpdateVisible: false,
      dataRule: {
        userIds: [
          {
            required: true,
            message: '发放用户不能为空',
            trigger: 'blur'
          }
        ]
      },
      userCoupon: {
        id: '',
        userIds: []
      },
      dialogVisible: false,
      mallUserList: []
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 给会员发放优惠券
    sendUser (id) {
      this.dialogVisible = true
      this.getUserList()
      this.userCoupon = {
        id: id,
        userIds: []
      }
    },
    handleSelectionChange (val) {
      this.userCoupon.userIds = val.map(item => {
        return item.id
      })
    },
    getUserList (page) {
      if (page) {
        this.pageIndexUser = page
      }
      this.$http({
        url: '/mall/user/list',
        method: 'get',
        params: {
          'page': this.pageIndexUser,
          'limit': this.pageSizeUser,
          'userName': this.searchForm.userName,
          'gender': this.searchForm.gender,
          'mobile': this.searchForm.mobile
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.mallUserList = data.page.records
          this.totalPageUser = data.page.total
        } else {
          this.mallUserList = []
          this.totalPageUser = 0
        }
      })
    },
    sizeChangeHandleUser (val) {
      this.pageSizeUser = val
      this.pageIndexUser = 1
      this.getUserList()
    },
    currentChangeHandleUser (val) {
      this.pageIndexUser = val
      this.getUserList()
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/coupon/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'title': this.searchForm.title,
          'couponSn': this.searchForm.couponSn,
          'couponType': this.searchForm.couponType,
          'status': this.searchForm.status
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
    // 下发优惠券
    dataFormSubmit () {
      if (this.userCoupon.userIds.length === 0) {
        this.$message({
          message: '请选择会',
          type: 'error',
          duration: 1500
        })
      } else {
        this.$http({
          url: '/mall/coupon/sendUser',
          method: 'post',
          data: this.userCoupon
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
            this.dialogVisible = false
            this.getDataList()
          }
        })
      }
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle (id) {
      let ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('确定对所选项进行[删除]操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/coupon/delete',
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
