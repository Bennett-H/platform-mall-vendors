<template>
  <div class="mod-application">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.phone" placeholder="电话号码" clearable></el-input>
      </el-form-item>
<!--      <el-form-item>-->
<!--        <el-input v-model="searchForm.wechatNo" placeholder="微信号" clearable></el-input>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-select v-model="searchForm.status" clearable placeholder="审核状态" @change="getDataList()">
          <el-option
            v-for="item in applicationStatus"
            :key="item.value"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('pedigree:application:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="phone"
        header-align="center"
        align="center"
        label="电话">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="wechatNo"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="微信号">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="wechatName"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="微信昵称">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="province"
        header-align="center"
        align="center"
        label="省份">
      </el-table-column>
      <el-table-column
        prop="city"
        header-align="center"
        align="center"
        label="城市">
      </el-table-column>
      <el-table-column
        prop="region"
        header-align="center"
        align="center"
        label="地区">
      </el-table-column>
      <el-table-column
        prop="identity"
        header-align="center"
        align="center"
        label="身份">
      </el-table-column>
      <el-table-column
        prop="job"
        header-align="center"
        align="center"
        label="职业">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="申请状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="success">已通过</el-tag>
          <el-tag v-else-if="scope.row.status === -1" size="small" type="danger">已拒绝</el-tag>
          <el-tag v-else size="small" type="info">申请中</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('pedigree:application:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id, '1')">通过
          </el-button>
          <el-button v-if="isAuth('pedigree:application:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id, '-1')">拒绝
          </el-button>
          <el-button v-if="isAuth('pedigree:application:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
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
  </div>
</template>

<script>
import {isAuth} from '../../../utils'

export default {
  data () {
    return {
      searchForm: {
        name: '',
        phone: '',
        wechatNo: '',
        status: ''
      },
      applicationStatus: [],
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: []
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    isAuth,
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/pedigree/application/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'name': this.searchForm.name,
          'phone': this.searchForm.phone,
          'wechatNo': this.searchForm.wechatNo,
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
      this.$http({
        url: `/sys/dict/list`,
        method: 'get',
        params: {
          'code': 'application_status'
        }
      }).then(({data}) => {
        console.log(data)
        if (data && data.code === 0) {
          this.applicationStatus = data.page.records
        }
      })
    },
    // 修改
    addOrUpdateHandle (id, status) {
      this.$http({
        url: `/pedigree/application/update`,
        method: 'post',
        data: {
          id: id,
          status: status
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
          url: '/pedigree/application/delete',
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
