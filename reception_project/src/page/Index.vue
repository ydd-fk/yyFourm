<template>
    <el-container id="container">
      <el-header>
        <Header></Header>
      </el-header>
      <el-container>
        <el-main>
          <ul class="navBox">
            <li>最新</li>
          </ul>
          <div v-for="item in postsList">
              <PostsItem :key="item.id" :posts="item"></PostsItem>
          </div>

          <div class="bottomBox" v-if="postsList.length==page.total">
            已经到底啦~
          </div>
          <div class="bottomBox" v-else="postsList.length==page.total">
            <span class="manyBtn" @click="getPostsList">查看更多</span>
          </div>
        </el-main>

        <el-aside width="350px">
          <Aside></Aside>
        </el-aside>
      </el-container>
    </el-container>
</template>

<script>
import Header from "../components/common/Header";
import Aside from "@/components/common/Aside";
import PostsItem from "../components/PostsItem";
import posts from "@/api/posts";
export default {
  name: "Index",
  components: {PostsItem, Header, Aside},
  created() {
    this.getPostsList();
  },
  data(){
    return {
      page:{
        pageNum:0,
        pageSize:10,
        total:-1
      },
      postsList:[]
    }
  },
  methods: {
    async getPostsList(){
      this.page.pageNum+=1;
      let {pageNum,pageSize} = this.page;
      const data = await posts.getNewList({pageNum,pageSize});
      this.postsList = this.postsList.concat(data.list);
      this.page.total = data.total;
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/myLayout";


.el-main{
  .navBox{
    display: flex;
    list-style: none;
    height: 45px;
    border-bottom: 1px solid #d9d9d9;
    li:first-child{
      margin-left: 50px;
    }
    li{
      width: 50px;
      margin: 0 15px 0 0;
      text-align: center;
      line-height: 45px;
      box-sizing: border-box;
      border-bottom: 4px solid #4d73de;
    }
  }
}
.bottomBox{
  margin: 20px;
  text-align: center;
  color: #727272;
  .manyBtn:hover{
    color: #4d73de;
    cursor: pointer;
  }
}

</style>
