<template>
  <div>
    <div class="asideTopBox" v-if="user.id==0||user.id==''">
      <div class="noLoginBox" >
        <p>您暂未登录</p>
        <router-link to="/login">
          立即登录
        </router-link>
      </div>
    </div>
    <div v-if="user.id!=0&&user.id!=''" class="asideTopBox">
      <div class="top">
        <HeaderImage :size="65" :url="user.icon" :id="user.id"/>
        <p>{{user.name}}</p>
      </div>

      <el-divider>我的关注</el-divider>
      <div v-if="followsUserList.length>0" class="followBox">
        <div v-for="item in followsUserList" class="followItem">
          <header-image :size="50" :url="item.icon" :id="item.id"/>
          <p>{{ item.name }}</p>
        </div>
      </div>
      <p v-else="followsUserList.length>0" class="followTips">暂无关注，快去关注更多的人吧~</p>
    </div>

    <div class="asideHotBox">
      <el-divider>精华帖子</el-divider>
      <div class="hotBox">
        <div class="hotItem" v-for="item in postsList" @click="postsClick(item.id)">
          <span class="title">{{ item.title }}</span>
          <span class="hots">
                  <icon name="dianzan" w="18" h="18" color="black"></icon>{{ item.likes }}
          </span>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import config from "@/http/config";
import userApi from "@/api/user";
import HeaderImage from "@/components/HeaderImage";
import postsApi from "@/api/posts";
export default {
  name: "Aside",
  components: {HeaderImage},
  created(){
    setTimeout(this.getFollowsUserList,1000)
    this.getFollowsUserList();
    this.getBestLikesPostsList();
  },
  data(){
    return {
      followsUserList:[],
      postsList:[]
    }
  },
  methods:{
    async getFollowsUserList(){
      const id = this.user.id;
      if(id==0||id=="") return;
      const data = await userApi.findFollowsUser(this.user.id,1,6);
      this.followsUserList = data.list;
    },
    async getBestLikesPostsList(){
      const data = await postsApi.getBestLikes(6);
      this.postsList = data.list;
    },
    async postsClick(id){
      try {
        await this.$router.push({path:'/detail',query:{
            postsID:id
          }})
      }catch (e){}

      if (this.$route.path=='/detail'){
        this.$emit('init');

      }
    }
  },
  computed:{
    user(){
      return this.$store.state.user;
    },
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style lang="less">
.asideTopBox{
  background-color: white;
  margin-bottom: 20px;
  .top{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 160px;
    p{
      padding-top: 16px;
      font-weight: bold;
    }
  }
  .followBox{
    margin-top: 20px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    .followItem{
      text-align: center;
      width: 33.333%;
      margin-bottom: 18px;
    }

  }
  .followTips{
    color: #727272;
    font-size: 0.8em;
    padding: 15px;
  }
  .noLoginBox{
    flex-direction: column;
    height: 200px;
    display: flex;
    justify-content: center;
    align-items: center;
    p{
      margin-bottom: 5px;
    }
    a{
      color: #4d73de;
      font-size: 0.9em;
    }
  }
}

.asideHotBox{
  padding: 25px;
  background-color: white;
  .hotBox{
    margin-top: 20px;
    .hotItem{
      cursor: pointer;
      border-bottom: 1px solid #d5d5d5;
      height: 34px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .title{
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        max-width: 220px;
      }
      .hots{
        font-size: 0.8em;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
}
</style>
