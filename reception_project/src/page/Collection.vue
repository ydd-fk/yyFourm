<template>
<div>
  <div v-for="item in postsList">
    <collection-post-item :key="item.id" :posts="item"/>
  </div>
  <p v-if="pageInfo.total==0" class="tips">暂无收藏</p>
  <div v-if="this.pageInfo.total>0">
    <p v-if="postsList.length>=pageInfo.total" class="tips">已经到底啦</p>
    <p v-else="postsList.length>=pageInfo.total" class="tips" style="color: #4d73de;cursor: pointer" @click="getPostsList">查看更多</p>
  </div>
</div>
</template>

<script>
import postsApi from '@/api/posts'
import CollectionPostItem from "@/components/CollectionPostItem";
import {SUCCESS_CODE} from "@/utils/codeUtil";

export default {
  name: "Collection",
  components: {CollectionPostItem},
  created() {
    this.getPostsList();
  },
  data(){
    return {
      svgW:18,
      pageInfo:{
        total:-1,
        pageNum:0,
        pageSize:10
      },
      postsList:[]
    }
  },
  methods:{
    async getPostsList(){
      let {pageNum,pageSize,total} = this.pageInfo;
      if (this.postsList.length==total) return;
      this.pageInfo.pageNum+=1;
      const data = await postsApi.getFollowsPosts({userID:this.user.id,pageNum:(pageNum+1),pageSize});
      this.pageInfo.total=data.total;
      data.list.forEach(item=>{
        item.isCancelFollow = 0;
      })
      this.postsList=this.postsList.concat(data.list);
      console.log(this.postsList)
    },
    async cancelBtnClick(id){
      const {code} = await postsApi.cancelFollow(id);
      if (code==SUCCESS_CODE){
        this.$message.success("已取消收藏")
        this.postsList.forEach(item=>{
          if (item.id==id) item.isCancelFollow=1;
        })
      }else this.$message.error("取消收藏失败，请稍后重试")
    },
    async btnClick(id){
      const {code} = await postsApi.follow(id);
      if (code==SUCCESS_CODE){
        this.$message.success("收藏成功")
        this.postsList.forEach(item=>{
          if (item.id==id) item.isCancelFollow=0;
        })
      }else this.$message.error("收藏失败，请稍后重试")
    }
  },
  computed:{
    user(){
      return this.$store.state.user;
    }
  }
}
</script>

<style scoped>
.tips{
  text-align: center;
  color: #727272;
  font-size: 0.8em;
  margin: 30px;
}
</style>
