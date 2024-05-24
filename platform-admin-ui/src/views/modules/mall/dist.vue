<template>
  <div class="mod-dist">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="searchForm.nickname" placeholder="会员昵称(只查询两级)" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.isAudit" clearable placeholder="审核状态" @change="getDataList()">
          <el-option
            key="1"
            label="已审核"
            value="1">
          </el-option>
          <el-option
            key="0"
            label="待审核"
            value="0">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.code" placeholder="邀请码" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      style="width: 100%;">
      <table-tree-column
        prop="nickname"
        header-align="center"
        treeKey="id"
        parentKey="superiorId"
        width="220"
        label="会员昵称">
      </table-tree-column>
      <el-table-column
        prop="superiorNickname"
        header-align="center"
        align="center"
        width="220"
        label="上级分销">
      </el-table-column>
      <el-table-column
        prop="isAudit"
        header-align="center"
        align="center"
        label="审核状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isAudit" size="small" type="success">已审核</el-tag>
          <template v-else>
            <el-tag size="small" type="danger">待审核</el-tag>
            <el-button v-if="isAuth('mall:dist:confirmAudit')" type="primary" size="mini"
                       @click="confirmAudit(scope.row.id)">
              审核通过
            </el-button>
          </template>
        </template>
      </el-table-column>
      <el-table-column
        prop="joinTime"
        header-align="center"
        align="center"
        label="加入时间">
      </el-table-column>
      <el-table-column
        prop="amountAvailable"
        header-align="center"
        align="center"
        label="待取佣金">
      </el-table-column>
      <el-table-column
        prop="amountWithdrawn"
        header-align="center"
        align="center"
        label="已取佣金">
      </el-table-column>
      <el-table-column
        prop="amountTotal"
        header-align="center"
        align="center"
        label="累计佣金">
      </el-table-column>
      <el-table-column
        prop="invitationCode"
        header-align="center"
        align="center"
        label="邀请码">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('mall:dist:info')" type="text" size="small" @click="showDetails(scope.row.id)">查看
          </el-button>
          <el-button v-if="isAuth('mall:dist:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './dist-add-or-update'
import TableTreeColumn from '@/components/table-tree-column'

export default {
  data () {
    return {
      searchForm: {
        nickname: null,
        isAudit: '',
        code: ''
      },
      dataList: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate,
    TableTreeColumn
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.$http({
        url: '/mall/dist/queryAll',
        method: 'get',
        params: {
          'nickname': this.searchForm.nickname,
          'isAudit': this.searchForm.isAudit,
          'code': this.searchForm.code
        }
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = this.treeDataTranslate(data.list, 'id', 'superiorId', 'childrens')
        } else {
          this.dataList = []
        }
      })
    },
    // 查看详情
    showDetails (id) {
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
          url: '/mall/dist/delete',
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
    // 分销商申请审核通过
    confirmAudit (id) {
      this.$http({
        url: `/mall/dist/confirmAudit/${id}`,
        method: 'post'
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
    }
  }
}
</script>
