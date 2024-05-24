<template>
  <div class="mod-topic">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.parentName" placeholder="父亲姓名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.isFamous" clearable placeholder="是否名人" @change="getDataList()">
          <el-option key="1" label="是" :value=1></el-option>
          <el-option key="0" label="否" :value=0></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('pedigree:member:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('pedigree:member:delete')" type="danger" @click="deleteHandle()"
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
        prop="name"
        header-align="center"
        align="center"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="headImg"
        header-align="center"
        align="center"
        label="头像">
        <template slot-scope="scope">
          <img style="height: 30%;width: 30%" v-if="scope.row.headImg" @click="openImg(scope.row.headImg)"
               :src="scope.row.headImg"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="backImg"
        header-align="center"
        align="center"
        label="背景">
        <template slot-scope="scope">
          <img style="height: 30%;width: 30%" v-if="scope.row.backImg" @click="openImg(scope.row.backImg)"
               :src="scope.row.backImg"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="birthDate"
        header-align="center"
        align="centxer"
        show-tooltip-when-overflow
        label="出生日期">
      </el-table-column>
      <el-table-column
        prop="deathDate"
        header-align="center"
        align="center"
        label="离世日期">
      </el-table-column>
      <el-table-column
        prop="isFamous"
        header-align="center"
        align="center"
        label="名人">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isFamous === 1" size="small" type="success">是</el-tag>
          <el-tag v-else size="small" type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="parentName"
        header-align="center"
        align="center"
        label="父亲">
      </el-table-column>
      <el-table-column
        prop="layer"
        header-align="center"
        align="center"
        label="代">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('pedigree:member:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('pedigree:member:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('pedigree:member:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">
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
  </div>
</template>

<script>
import AddOrUpdate from './pedigree-add-or-update'
import {isAuth, openImg} from '../../../utils'

export default {
  data () {
    return {
      searchForm: {
        name: '',
        parentName: '',
        isFamous: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    isAuth,
    openImg,
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/pedigree/member/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'name': this.searchForm.name,
          'parentName': this.searchForm.parentName,
          'isFamous': this.searchForm.isFamous
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
          url: '/pedigree/member/delete',
          method: 'post',
          data: ids
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: data.message,
              type: data.message === '操作成功' ? 'success' : 'info',
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
