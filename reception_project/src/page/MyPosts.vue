<template>
<div>
  <div v-for="item in postsList">
    <my-posts-item :posts="item" :key="item.id"/>
  </div>
  <p v-if="pageInfo.total==0" class="tips">暂无收藏</p>
  <div v-if="this.pageInfo.total>0">
    <p v-if="postsList.length>=pageInfo.total" class="tips">已经到底啦</p>
    <p v-else="postsList.length>=pageInfo.total" class="tips" style="color: #4d73de;cursor: pointer" @click="getPostsList">查看更多</p>
  </div>
</div>
</template>

<script>
import MyPostsItem from "@/components/MyPostsItem";
import {SUCCESS_CODE} from "@/utils/codeUtil";
import postsApi from '@/api/posts'
export default {
name: "MyPosts",
  created() {
  this.getPostsList();
  },
  data(){
    return{
      pageInfo:{
        total:-1,
        pageNum:0,
        pageSize:10
      },
      postsList:[],
    }
  },
  methods:{
    async getPostsList(){
      let {pageNum,pageSize,total} = this.pageInfo;
      if (this.postsList.length==total) return;
      this.pageInfo.pageNum+=1;
      const data = await postsApi.getUsersPosts({userID:this.user.id,pageNum:(pageNum+1),pageSize});
      this.pageInfo.total=data.total;
      data.list.forEach(item=>{
        item.isCancelFollow = 0;
      })
      this.postsList=this.postsList.concat(data.list);
      console.log(this.postsList)
    },
    delPosts(id){
      this.$confirm('此操作将永久删除该贴子, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const {code} = await postsApi.removePosts(id);
        if (code==SUCCESS_CODE) {
          this.$message.success("删除成功")
          this.postsList = this.postsList.filter(item=>item.id!=id)
        }
        else this.$message.error("删除失败，请稍后重试")
      });

    }
  },
  computed:{
    user(){
      return this.$store.state.user;
    }
  },
  components: {MyPostsItem}
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
