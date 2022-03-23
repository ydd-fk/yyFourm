<template>
<div class="itemBox">
  <div class="itemTopBar">
    <router-link :to="{path:'/other',query:{id:posts.user.id}}">
      <HeaderImage :size="50" :url="posts.user.icon"/>
    </router-link>

    <router-link :to="{path:'/other',query:{id:posts.user.id}}">
      <span class="itemName">{{ posts.user.name }}</span>
    </router-link>
  </div>
  <h4 class="itemTitle">
    <span @click="postsItemClick">{{ posts.title }}</span>
  </h4>
  <p class="itemDetail">{{ posts.detail }}</p>
  <div class="ItemImageBox">
    <el-image
      v-for="item in posts.icons"
      class="itemImage"
      :src="baseURL+'/img/'+item.url"
      :key="item.id"
      fit="cover"
      :preview-src-list="[
        baseURL+'/img/'+item.url
      ]"></el-image>
  </div>
  <div class="itemBtm">
    <span class="itemTime">{{this.$formatDate(new Date(posts.time))}}</span>
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

</div>
</template>

<script>
import config from "@/http/config";
import HeaderImage from "@/components/HeaderImage";
export default {
  name: "PostsItem",
  props:{
    posts:Object
  },
  data(){
    return {
      svgW:18,

    }
  },
  methods:{
    postsItemClick(){
      this.$router.push({path:"/detail",query:{postsID:this.posts.id}})
    }
  },
  computed:{
    baseURL(){
      return config.baseURL
    }
  },
  components:{
    HeaderImage
  }
}
</script>

<style scoped lang="less">
.itemBox{
  border-bottom: 1px solid #d9d9d9;
  padding: 5px 10px;
  .itemName{
    font-size: 0.8em;
  }
  .itemDetail,.itemTitle{
    margin: 8px 0 5px 0;
  }
  span:hover{
    color: #4d73de;
    cursor: pointer;
  }
  .ItemImageBox{
    .itemImage{
      margin-right: 10px;
      width: 150px;
      height: 150px;
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
}
a{
  text-decoration: none;
  color: #404040;
}

</style>
