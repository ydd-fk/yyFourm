<template>
<div class="itemBox">
  <h4 class="itemTitle" @click="postsItemClick(posts.id)">{{ posts.title }}</h4>
  <p class="itemDetail">{{ posts.detail }}</p>
  <div class="ItemImageBox">
    <div v-for="item in posts.icons" class="imgItem">
      <el-image
                class="itemImage"
                :src="baseURL+'/img/'+item.url"
                fit="cover"
                :preview-src-list="[
        baseURL+'/img/'+item.url,
      ]"></el-image>
    </div>

  </div>
  <div class="itemBtm">
    <span class="itemTime">{{ formatDate(posts.time) }}</span>
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
export default {
  name: "OtherPostsItem",
  props:{
    posts:Object
  },
  data(){
    return {
      svgW:18
    }
  },
  methods:{
    postsItemClick(id){
      this.$router.push({path:"/detail",query:{postsID:id}})
    },
    formatDate(date){
      return this.$formatDate(new Date(date))
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
.itemBox{
  border-bottom: 1px solid #d9d9d9;
  padding: 5px 10px;
  .imgItem{
    display: inline-block;
    margin-right: 15px;
  }
  .itemName{
    font-size: 0.8em;
  }
  .itemDetail,.itemTitle{
    margin: 8px 0 5px 0;
  }
  .itemTitle:hover{
    color: #4d73de;
    cursor: pointer;
  }
  .ItemImageBox{
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
}
a{
  text-decoration: none;
  color: #404040;
}

</style>
