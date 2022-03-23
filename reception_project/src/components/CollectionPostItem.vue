<template>
<div class="itemBox">
  <h4 class="itemTitle">
    <router-link :to="{path:'/detail',query:{postsID:posts.id}}">
      {{ posts.title }}
    </router-link>
  </h4>
  <p class="itemDetail">{{ posts.detail }}</p>
  <div class="ItemImageBox" v-for="item in posts.icons">
    <el-image
      :key="item.id"
      class="itemImage"
      :src="baseURL+'/img/'+item.url"
      fit="cover"
      :preview-src-list="[
        baseURL+'/img/'+item.url,
      ]"></el-image>
  </div>
  <div class="itemBtm">
    <div class="svgBox">
    <span>
      <icon name="pinglun" :w="svgW" :h="svgW" color="black"></icon>{{posts.comments}}
    </span>
      <span>
      <icon name="shoucang" :w="svgW" :h="svgW" color="black"></icon>{{posts.collections}}
    </span>
      <span>
      <icon name="dianzan" :w="svgW" :h="svgW" color="black"></icon>{{posts.likes}}
    </span>
    </div>
  </div>
  <span v-if="posts.isCancelFollow==0" class="cancelBtn" @click="$parent.cancelBtnClick(posts.id)">
    取消收藏
  </span>
  <span v-if="posts.isCancelFollow==1" class="cancelBtn" @click="$parent.btnClick(posts.id)">
    收藏
  </span>
</div>
</template>

<script>
import config from "@/http/config"
export default {
  name: "CollectionPostsItem",
  props:{
    posts:Object
  },
  data(){
    return {
      svgW:18,
    }
  },
  methods:{

  },
  computed:{
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style scoped lang="less">
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
  .cancelBtn{
    display: none;
    font-weight: bold;
    color: #727272;
    position: absolute;
    top: 40%;
    right: 15px;
  }
  .cancelBtn:hover{
    color: #c7c7c7;
    cursor: pointer;
  }
}
.itemBox:hover .cancelBtn{
  display: block;
}
a{
  text-decoration: none;
  color: #000000;
}
a:hover{
  color: #4d73de;
}


</style>
