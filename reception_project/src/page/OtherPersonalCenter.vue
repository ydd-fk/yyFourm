<template>
<div id="outBox">
  <Header></Header>
  <div id="innerBox">
    <div class="centerTop" :style="`background-image: url('${baseURL}/img/${user.backImg}')`">
      <div class="centerTopLeft">
        <header-image :size="65" :url="user.icon"/>
        <p class="userName">{{ user.name }}</p>
        <p class="sign">{{ user.sign }}</p>
        <el-button @click="()=>{followClick(user.id);this.isFollow=1;}"
                   v-if="isFollow==0"
                   class="followBtn"
                   type="primary">关注</el-button>
        <el-button @click="()=>{cancelFollowClick(user.id);this.isFollow=0;}"
                   v-if="isFollow==1"
                   class="followBtn" type="info">取关</el-button>
        <el-button class="sayBtn" type="info" @click="sayClick">私信</el-button>
      </div>
      <div class="centerTopRight">
        <div class="numTabs">
          <div @click="getFollowClick">
            <span>关注</span>
            <span>{{ followPage.total }}</span>
          </div>
          <div @click="getFansClick">
            <span>粉丝</span>
            <span>{{ fansPage.total }}</span>
          </div>
        </div>
      </div>
    </div>
    <div>
      <div class="postsPoint">
        <p>ta的帖子</p>
      </div>
      <div v-for="item in postsList" :key="item.id">
        <other-posts-item :posts="item"></other-posts-item>
      </div>

    </div>
    <p v-if="postsPage.total==postsList.length" class="tips">
      <span>已经到底啦~</span>
    </p>
    <p v-else="postsPage.total==postsList.length" class="tips moreBtn" @click="getPosts">
      <span>加载更多</span>
    </p>
  </div>

  <el-drawer
    :title="`${user.name}的关注`"
    :visible.sync="followDrawer">
    <div>
      <div class="followItem" v-for="item in followUserList">
        <header-image :size="50" :url="item.icon" :id="item.id" :isinit="true" @fatherInit="init"/>
        <div class="userInfoBox">
          <span class="userName">{{ item.name }}</span>
          <span class="userSign">{{ item.sign }}</span>
        </div>
        <el-button v-if="follow_isFollow(item.id)"
                   type="small"
                   @click="follow_cancelFollowClick(item.id)"
                   class="followBtn">取关</el-button>

        <el-button v-else="follow_isFollow(item.id)"
                   type="small"
                   @click="follow_followClick(item.id)"
                   class="followBtn">关注</el-button>
      </div>
      <p v-if="followPage.total==followUserList.length" class="tips">
        <span>暂无更多</span>
      </p>
      <p v-else="followPage.total==followUserList.length" class="tips moreBtn"  @click="queryFollowUser">
        <span>加载更多</span>
      </p>
    </div>
  </el-drawer>

  <el-drawer
    :title="`${user.name}的粉丝`"
    :visible.sync="fansDrawer">
    <div>
      <div class="followItem" v-for="item in fansUserList">
        <header-image :id="item.id" :size="50" :url="item.icon" :isinit="true" @fatherInit="init"/>
        <div class="userInfoBox">
          <span class="userName">{{ item.name }}</span>
          <span class="userSign">{{ item.sign }}</span>
        </div>
        <el-button v-if="fans_isFollow(item.id)" type="small" @click="fans_CancelFollowClick(item.id)" class="followBtn">取关</el-button>
        <el-button v-else="fans_isFollow(item.id)" type="small" @click="fans_followClick(item.id)" class="followBtn">关注</el-button>
      </div>
      <p v-if="fansPage.total==fansUserList.length" class="tips">
        <span>暂无更多</span>
      </p>
      <p v-else="fansPage.total==fansUserList.length" class="tips moreBtn"  @click="queryFansUser">
        <span>加载更多</span>
      </p>
    </div>
  </el-drawer>

</div>
</template>

<script>
import Header from "@/components/common/Header";
import OtherPostsItem from "@/components/OtherPostsItem";
import HeaderImage from "@/components/HeaderImage";
import userApi from "@/api/user";
import postsApi from "@/api/posts";
import config from "@/http/config";
import {SUCCESS_CODE} from "@/utils/codeUtil";

export default {
  name: "OtherPersonalCenter",
  components: {HeaderImage, Header,OtherPostsItem},
  created() {
    this.init();
  },
  data(){
    return{
      followDrawer: false,
      fansDrawer: false,
      user:{

      },
      postsList:[],
      postsPage:{
        total:-1,
        pageNum:0,
        pageSize:15
      },
      followPage:{
        total: 0,
        pageNum:0,
        pageSize: 20
      },
      fansPage:{
        total: 0,
        pageNum:0,
        pageSize: 20
      },
      followUserList:[],
      fansUserList:[],
      follow_follows:[],
      fans_follows:[],
      isFollow:0
    }
  },
  methods:{
    init(){
      console.log("=====init=====")
      this.followDrawer=false;
      this.fansDrawer=false;
      this.user={};
      this.postsList=[];
      this.postsPage={
        total:-1,
        pageNum:0,
        pageSize:15
      };
      this.followPage={
        total: 0,
        pageNum:0,
        pageSize: 20
      };
      this.fansPage={
        total: 0,
        pageNum:0,
        pageSize: 20
      };
      this.followUserList=[];
      this.fansUserList=[];
      this.follow_follows=[];
      this.fans_follows=[];
      this.isFollow=0;
      let {id} = this.$route.query;
      if (id==null) return this.$router.replace("/")
      if (id==this.$store.state.user.id) return this.$router.replace("/center")
      this.user.id=id;
      this.getUser();
      this.getTotal();
      this.getPosts();
    },
    async getUser(){
      const {isFollow,user} = await userApi.getOtherUser(this.user.id);
      this.user = user;
      this.isFollow = isFollow;
    },
    async getTotal(){
      const {fansTotal,followTotal} = await userApi.getTotal(this.user.id);
      console.log("fansTotal=="+fansTotal)
      console.log("followTotal=="+followTotal)
      this.followPage.total=followTotal;
      this.fansPage.total=fansTotal;
    },
    async getPosts(){
      console.log("getPosts")
      let {pageNum,pageSize} = this.postsPage;
      if (this.postsList.length==this.postsPage.total) return;
      this.pageNum+=1;
      const {total,list} = await postsApi.getUsersPosts({userID:this.user.id,pageNum:pageNum+1,pageSize});
      this.postsPage.total=total;
      this.postsList = this.postsList.concat(list);
    },
    fans_followClick(id){
      this.fans_follows.push(id)
      this.followClick(id)
    },
    fans_CancelFollowClick(id){
      this.fans_follows=this.fans_follows.filter(item=>item!=id)
      this.cancelFollowClick(id)
    },
    follow_followClick(id){
      this.follow_follows.push(id)
      this.followClick(id)
    },
    follow_cancelFollowClick(id){
      this.follow_follows=this.follow_follows.filter(item=>item!=id)
      this.cancelFollowClick(id)
    },
    async followClick(id){
      const {code} = await userApi.followUser(id);
      if (code==SUCCESS_CODE) {
        this.$message.success("关注成功");
      } else this.$message.error("关注失败")
    },
    async cancelFollowClick(id){
      const {code} = await userApi.cancelFollowUser(id);
      if (code==SUCCESS_CODE) {
        this.$message.success("取关成功");
      } else this.$message.error("取关失败")
    },
    getFollowClick(){
      this.followDrawer=true;
      this.followUserList=[];
      this.queryFollowUser();
    },
    getFansClick(){
      this.fansDrawer=true;
      this.fansUserList=[];
      this.queryFansUser();
    },
    async queryFollowUser(){
      let {pageNum,pageSize,total} = this.followPage;
      if (this.followUserList==total) return;
      this.pageNum+=1;
      const {follows,userFollows} = await userApi.findOtherFollowsUser(this.user.id, pageNum + 1, pageSize);
      this.followUserList=this.followUserList.concat(follows.list);
      this.followPage.total=follows.total;
      console.log("===============")
      console.log(userFollows)
      this.follow_follows=userFollows;
    },
    async queryFansUser(){
      let {pageNum,pageSize,total} = this.fansPage;
      if (this.fansUserList==total) return;
      this.pageNum+=1;
      const {fans,userFollows} = await userApi.findOtherFansUser(this.user.id, pageNum + 1, pageSize);
      this.fansUserList=this.fansUserList.concat(fans.list);
      this.fansPage.total=fans.total;
      console.log("===============")
      console.log(userFollows)
      this.fans_follows=userFollows;
    },
    follow_isFollow(id){
      let flag=false;
      this.follow_follows.forEach(item=>{
        if (item==id) flag=true;
      })
      return flag;
    },
    fans_isFollow(id){
      let flag=false;
      this.fans_follows.forEach(item=>{
        if (item==id) flag=true;
      })
      return flag;
    },
    sayClick(){
      const {id,icon,name} = this.user;
      this.$router.push({
        name:"Message",
        params:{
          user:{
            id,icon,name
          }}
      })
    }
  },
  computed:{
    baseURL(){
      return config.baseURL;
    },
  }
}
</script>

<style scoped lang="less">
@import "../style/center";
@import "../style/drawer";
@import "../style/tips";
#outBox{
  #innerBox{
    .sign{
      font-size: 13px;
      color: #2d2d2d;
    }
    .postsPoint{
      height: 50px;
      border-bottom: 1px solid #e3e3e3;
      display: flex;
      align-items: center;
      padding-left: 5px;
    }
    .followBtn{
      top: 160px;
      left: 100px;
      position: absolute;
      padding: 0;
      width: 60px;
      height: 28px;
      font-size: 0.8em;
    }
    .sayBtn{
      top: 160px;
      left: 160px;
      position: absolute;
      padding: 0;
      width: 60px;
      height: 28px;
      font-size: 0.8em;
    }
  }
  .numTabs div:hover{
    cursor: pointer;
  }
}
</style>
