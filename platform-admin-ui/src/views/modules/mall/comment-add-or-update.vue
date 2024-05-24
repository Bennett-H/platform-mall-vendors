<template>
  <el-dialog
    title="查看"
    :close-on-click-modal="false"
    :lock-scroll="false"
    :visible.sync="visible">
    <el-descriptions :column="2" border>
      <el-descriptions-item>
        <template slot="label">
          评价内容
        </template>
        {{dataForm.content}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          评价图片
        </template>
        <img v-show="dataForm.commentPictureEntityList.length>0"
             v-for="commentPicture in dataForm.commentPictureEntityList" :key="commentPicture.id" class="comment-img"
             @click="openImg(commentPicture.picUrl)" :src="commentPicture.picUrl"/>
        <el-alert v-show="dataForm.commentPictureEntityList.length===0"
                  title="没有图片..." :closable="false"
                  type="error">
        </el-alert>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          评价级别
        </template>
        <el-rate
          disabled
          v-model="dataForm.evalLevel">
        </el-rate>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          配送质量
        </template>
        <el-rate
          disabled
          v-model="dataForm.deliveryLevel">
        </el-rate>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          商品服务
        </template>
        <el-rate
          disabled
          v-model="dataForm.goodsLevel">
        </el-rate>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          类型
        </template>
        <el-tag v-if="dataForm.type === 1" size="small" type="danger">文章</el-tag>
        <el-tag v-else-if="dataForm.type === 0" size="small" type="success">商品</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          状态
        </template>
        <el-tag v-if="dataForm.status === 1" size="small" type="success">审核通过</el-tag>
        <el-tag v-else-if="dataForm.status === 0" size="small" type="danger">待审核</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          评论时间
        </template>
        {{dataForm.addTime}}
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: '',
        userId: '',
        goodsId: '',
        goodsSpecifitionNameValue: '',
        content: '',
        type: '',
        addTime: '',
        status: '',
        commentPictureEntityList: []
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || ''
      this.visible = true
      this.$nextTick(() => {
        if (this.dataForm.id) {
          this.$http({
            url: `/mall/comment/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = data.comment
            }
          })
        }
      })
    }
  }
}
</script>
<style>
.comment-img {
  width: 100px;
  height: 100px;
}
</style>
