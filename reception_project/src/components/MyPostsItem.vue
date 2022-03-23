<template>
<div class="itemBox">
  <h4 class="itemTitle"><span class="postsTitle" @click="titleClick">{{ posts.title }}</span></h4>
  <p class="itemDetail">{{ posts.detail }}</p>
  <div class="ItemImageBox" v-for="item in posts.icons">
    <el-image
      class="itemImage"
      :src="baseURL+'/img/'+item.url"
      fit="cover"
      :preview-src-list="[
        baseURL+'/img/'+item.url,
      ]"></el-image>
  </div>
  <div class="itemBtm">
    <span class="itemTime">{{ dataFormat(posts.time) }}</span>
    <div class="svgBox">
    <span>
      <icon name="pinglun" :w="svgW" :h="svgW" color="black"></icon>{{ posts.comments }}
    </span>
      <span>
      <icon name="shoucang" :w="svgW" :h="svgW" color="black"></icon>{{ posts.collections }}
    </span>
      <span>
      <icon name="dianzan" :w="svgW" :h="svgW" color="black"></icon>{{ posts.likes }}
    </span>
    </div>
  </div>
  <div class="operationBtn">
      <span class="alertBtn" @click="modifyBtnClick">编辑</span>
      <span class="delBtn" @click="$parent.delPosts(posts.id)">删除</span>

  </div>
</div>
</template>

<script>
import config from "@/http/config";
export default {
  name: "PostsItem",
  props:{
    posts:Object
  },
  data(){
    return {
      svgW:18
    }
  },
  methods:{
    dataFormat(date){
      return this.$formatDate(new Date(date))
    },
    modifyBtnClick(){
      console.log("=========modifyBtnClick============")
      console.log(this.posts)//name: 'AddPosts'
      this.$router.push({ name: 'AlertPosts', params: {posts:this.posts}})
    },
    titleClick(){
      this.$router.push({path:'/detail',query:{postsID:this.posts.id}})
    }
  },
  computed:{
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/title";
a{
  text-decoration: none;
}
.itemBox{
  position: relative;
  border-bottom: 1px solid #d9d9d9;
  padding: 2px 10px;
  .itemName{
    font-size: 0.8em;
  }
  .itemDetail,.itemTitle{
    margin: 8px 0 5px 0;
  }
  .ItemImageBox{
    display: inline-block;
    margin-right: 15px;
    .itemImage{
      width: 100px;
      height: 100px;
    }
  }
  .itemBtm{
    display: flex;
    *{
      font-size: 0.8em;
      color: gray;
    }
    .svgBox{
      flex: 1;
      text-align: end;
      span{
        margin-right: 10px;
      }

  }

  }
  .operationBtn{
    display: none;
    position: absolute;
    top: 5px;
    right: 10px;
    font-weight: bold;
    font-size: 0.9em;
    .alertBtn{
      color: #06ac3f;
      margin-right: 6px;
    }
    .delBtn{
      color: #f55c5c;
    }
    span:hover{
      opacity: 0.7;
      cursor: pointer;
    }
  }
}
.itemBox:hover .operationBtn{
  display: block;
}

</style>
