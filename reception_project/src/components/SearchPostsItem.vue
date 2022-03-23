<template>
<div class="itemBox">
  <div class="itemTopBar">


    <router-link :to="{path:'/other',query:{id:posts.user.id}}">
      <span class="itemName">{{ posts.user.name }}</span>
    </router-link>
  </div>
  <h4 class="itemTitle">
    <span @click="postsItemClick" v-html="highlight(posts.title)">{{ posts.title }}</span>
  </h4>
  <p class="itemDetail" v-html="highlight(posts.detail)">{{ posts.detail }}</p>
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
export default {
  name: "SearchPostsItem",
  props:{
    posts:Object,
    searchKey:String
  },
  data(){
    return {
      svgW:18
    }
  },
  methods:{
    highlight(val){
      val = val + '';
      const searchKey = this.searchKey;
      if(val.indexOf(searchKey) !== -1 && searchKey !== '' ){
        return val.replace(searchKey, `<span class="highlightTxt">${searchKey}</span>`);
      }else{
        return val;
      }
    },
    postsItemClick(){
      this.$router.push({path:"/detail",query:{postsID:this.posts.id}})
    },
    formatDate(date){
      return this.$formatDate(new Date(date));
    }
  }
}
</script>

<style scoped lang="less">
@titleHoverColor:#4d73de;
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
    color: @titleHoverColor;
    cursor: pointer;
  }
  .ItemImageBox{
    .itemImage{
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
/deep/ .highlightTxt{
  color: red;
}
</style>
