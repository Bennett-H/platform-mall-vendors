<template>
  <div class="mod-user">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
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
        <el-select v-model="searchForm.userTags" clearable placeholder="商城会员" class="width120">
          <el-option
            v-for="tag in tagList"
            :key="tag.value"
            :label="tag.name"
            :value="tag.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
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
          <el-tag v-if="scope.row.gender === 0" size="small" type="error">未知</el-tag>
          <el-tag v-else-if="scope.row.gender === 1" size="small" type="success">男</el-tag>
          <el-tag v-else-if="scope.row.gender === 2" size="small" type="danger">女</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="mobile"
        header-align="center"
        align="center"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="birthday"
        header-align="center"
        align="center"
        label="生日">
        <template slot-scope="scope">
          <span>{{scope.row.birthday}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="headImgUrl"
        header-align="center"
        align="center"
        label="用户头像">
        <template slot-scope="scope" v-if="scope.row.headImgUrl">
          <img style="height: 50%;width: 50%" @click="openImg(scope.row.headImgUrl)" :src="scope.row.headImgUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="registerTime"
        header-align="center"
        align="center"
        label="注册时间">
        <template slot-scope="scope">
          <span>{{scope.row.registerTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="lastLoginTime"
        header-align="center"
        align="center"
        label="最后登录时间">
        <template slot-scope="scope">
          <span>{{scope.row.lastLoginTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="180px"
        prop="memberTag"
        header-align="center"
        align="center"
        label="商城会员">
        <template slot-scope="scope">
          <div v-if="scope.row.userTags!=null">
            <span  v-for="(item,index) in scope.row.userTags.split(',')">
              <el-tag v-if="item === '1'" size="small" type="success">商城会员</el-tag>
              <el-tag v-if="item === '2'" size="small" type="warning">待审核会员</el-tag>
              <el-button v-if="item === '2'" type="primary" size="mini"  @click="confirmMember(scope.row.id)" >审核通过</el-button>
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="userLevelName"
        header-align="center"
        align="center"
        label="会员等级">
      </el-table-column>
      <el-table-column
        prop="registerIp"
        header-align="center"
        align="center"
        label="注册ip">
      </el-table-column>
      <el-table-column
        prop="subscribe"
        header-align="center"
        align="center"
        label="是否关注">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.subscribe === 0" size="small" type="danger">否</el-tag>
          <el-tag v-else-if="scope.row.subscribe === 1" size="small" type="success">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="subscribeTime"
        header-align="center"
        align="center"
        label="关注时间">
        <template slot-scope="scope">
          <span>{{scope.row.subscribeTime}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="integral"
        header-align="center"
        align="center"
        label="积分">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('mall:accountlog:list')"
                     @click="showIntegralLog(scope.row.id)">{{scope.row.integral}}
          </el-button>
          <el-button type="text" size="small" v-else>{{scope.row.integral}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="balance"
        header-align="center"
        align="center"
        label="余额">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="isAuth('mall:accountlog:list')"
                     @click="showAccountLog(scope.row.id)">{{scope.row.balance}}
          </el-button>
          <el-button type="text" size="small" v-else>{{scope.row.balance}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:user:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:user:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
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
    <accountlog v-if="accountlogVisible" ref="accountlog" @refreshDataList="getDataList"></accountlog>
    <integrallog v-if="integrallogVisible" ref="integrallog" @refreshDataList="getDataList"></integrallog>
  </div>
</template>

<script>
import Accountlog from './accountlog'
import Integrallog from './integrallog'
import AddOrUpdate from './user-add-or-update'

export default {
  data () {
    return {
      searchForm: {
        userName: '',
        gender: '',
        mobile: '',
        userTags: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false,
      integrallogVisible: false,
      accountlogVisible: false,
      tagList: []
    }
  },
  components: {
    Accountlog,
    Integrallog,
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    showIntegralLog (userId) {
      this.integrallogVisible = true
      this.$nextTick(() => {
        this.$refs.integrallog.init(userId)
      })
    },
    showAccountLog (userId) {
      this.accountlogVisible = true
      this.$nextTick(() => {
        this.$refs.accountlog.init(userId)
      })
    },
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/sys/dict/queryByCode?code=mall_cust_tag',
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.tagList = data.list
        }
      })
      this.$http({
        url: '/mall/user/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'userName': this.searchForm.userName,
          'gender': this.searchForm.gender,
          'mobile': this.searchForm.mobile,
          'userTags': this.searchForm.userTags
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
    confirmMember (id) {
      this.$confirm('是否确认审核通过？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: '/mall/user/confirmMember',
          method: 'post',
          data: {id}
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '审核通过',
              type: 'success'
            })
            this.getDataList()
          } else {
            this.$message({
              message: data.msg,
              type: 'error'
            })
          }
        })
      }).catch(() => {
      })
    },
    // 查看详情
    showDetails (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, true)
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    }
  }
}
</script>
