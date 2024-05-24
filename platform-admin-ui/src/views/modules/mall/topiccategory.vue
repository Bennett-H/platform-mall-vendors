<template>
  <div class="mod-topiccategory">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="searchForm.title" placeholder="文章类别" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.typeValue" placeholder="类别类型" clearable @change="getDataList()">
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <el-button v-if="isAuth('mall:topiccategory:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('mall:topiccategory:delete')" type="danger" @click="deleteHandle()"
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
        label="文章类别">
      </el-table-column>
      <el-table-column
        prop="picUrl"
        header-align="center"
        align="center"
        label="图片">
        <template slot-scope="scope">
          <img style="height: 30%;width: 30%" v-if="scope.row.picUrl" @click="openImg(scope.row.picUrl)"
               :src="scope.row.picUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="iconUrl"
        header-align="center"
        align="center"
        label="icon">
        <template slot-scope="scope">
          <img style="height: 30%;width: 30%" v-if="scope.row.iconUrl" @click="openImg(scope.row.iconUrl)"
               :src="scope.row.iconUrl"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="typeName"
        header-align="center"
        align="center"
        label="类别类型">
      </el-table-column>
      <el-table-column
        prop="appUrl"
        header-align="center"
        align="center"
        label="小程序内链接">
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
          <el-button v-if="isAuth('mall:topiccategory:info')" type="text" size="small"
                     @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:topiccategory:update')" type="text" size="small"
                     @click="addOrUpdateHandle(scope.row.id)">修改
          </el-button>
          <el-button v-if="isAuth('mall:topiccategory:delete')" type="text" size="small"
                     @click="deleteHandle(scope.row.id)">删除
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
import AddOrUpdate from './topiccategory-add-or-update'
import {openImg} from '../../../utils'

export default {
  data () {
    return {
      searchForm: {
        title: '',
        typeValue: ''
      },
      dataList: [],
      typeList: [],
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
    this.getCategoryType()
  },
  methods: {
    openImg,
    // 获取数据列表
    getDataList (page) {
      if (page) {
        this.pageIndex = page
      }
      this.$http({
        url: '/mall/topiccategory/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'title': this.searchForm.title,
          'typeValue': this.searchForm.typeValue
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
          url: '/mall/topiccategory/delete',
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
    },
    getCategoryType () {
      this.$http({
        url: '/sys/dict/list',
        method: 'get',
        params: {
          'code': 'topic_big_category'
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.typeList = data.page.records
        } else {
          this.typeList = []
        }
      })
    }
  }
}
</script>
