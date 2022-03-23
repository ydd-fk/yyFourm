<template>
    <el-container id="container">
      <el-header>
        <Header @searchPosts="searchBtnClick" :search-key="searchKey"></Header>
      </el-header>
      <el-container>
        <el-main>
          <div class="searchDetail">
            <p>相关帖子约{{ this.pageInfo.total }}个</p>
          </div>
          <div v-for="item in postList">
            <SearchPostsItem :posts="item" :search-key="searchKey"></SearchPostsItem>
          </div>
          <p v-if="postList.length==0" class="tips">
            <span>暂无相关帖子，换换其他的关键字试试吧</span>
          </p>
          <div v-else="postList.length==0">
            <p v-if="isEnd" class="tips">
              <span>暂无更多</span>
            </p>
            <p v-else="isEnd" class="tips moreBtn">
              <span>加载更多</span>
            </p>
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
import SearchPostsItem from "@/components/SearchPostsItem";
import postsApi from "@/api/posts";
export default {
  name: "SearchPostsItems",
  components: {SearchPostsItem, PostsItem, Header, Aside},
  created() {
    this.searchBtnClick(this.$route.query.str);
  },
  data(){
    return {
      searchKey:"",
      pageInfo:{
        total:0,
        pageNum:0,
        pageSize:15,
      },
      isEnd:false,
      postList:[]
    }
  },
  methods: {
    searchBtnClick(searchStr){
      //初始化数据
      this.searchKey=searchStr;
      this.pageInfo.total=0;
      this.pageInfo.pageNum=0;
      this.postList=[];
      this.isEnd=false;
      this.searchPosts();
    },
    async searchPosts(){
      let {pageNum,pageSize} = this.pageInfo
      if (this.isEnd) return;
      this.pageInfo.pageNum+=1;
      const data = await postsApi.searchPosts({searchKey:this.searchKey,pageNum:pageNum+1,pageSize})
      this.pageInfo.total=data.total;
      this.postList=this.postList.concat(data.list);
      if (this.postList.length>=data.total||data.list.length==0) this.isEnd=true;
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/myLayout";
@import "../style/tips";

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
  .searchDetail{
    height: 45px;
    display: flex;
    align-items: center;
    padding-left: 8px;
    font-size: 14px;
    color: #727272;
    border-bottom: 1px solid #d9d9d9;
  }
}

</style>
