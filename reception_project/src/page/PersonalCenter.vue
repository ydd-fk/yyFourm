<template>
  <div id="outBox">
    <Header></Header>
    <div id="innerBox">
      <div class="centerTop" :style="`background-image:url(${baseURL}/img/${user.backImg})`">
        <form class="bgFile">
          <input @change="fileChange" type="file"/>
              <p>更改背景</p>
        </form>
        <div class="centerTopLeft">
          <div class="headerFile">
            <el-tooltip content="点击更换头像" placement="right" effect="light">
              <input type="file" @change="iconFileChange"/>
            </el-tooltip>
            <header-image :size="65" :url="user.icon"/>
          </div>

          <p class="userName">{{ user.name }}</p>
          <el-input @change="signTxtChange" v-model="signTxt" class="tabInput" size="small" placeholder="快来编写你的个性签名吧~"/>
        </div>
        <div class="centerTopRight">
          <div class="numTabs">
            <div @click="queryFollowsBtnClick">
              <span>关注</span>
              <span>{{ followsPage.total }}</span>
            </div>
            <div @click="queryFansBtnClick">
              <span>粉丝</span>
              <span>{{ fansPage.total }}</span>
            </div>
          </div>

        </div>
      </div>


      <el-tabs style="margin: 0 10px" v-model="activeName">
        <el-tab-pane label="个人信息" name="first">
          <center-index></center-index>
        </el-tab-pane>
        <el-tab-pane label="我的收藏" name="second">
          <Collection></Collection>
        </el-tab-pane>
        <el-tab-pane label="我的帖子" name="five">
          <my-posts></my-posts>
        </el-tab-pane>
        <el-tab-pane label="修改密码" name="four">
          <reset-password></reset-password>
        </el-tab-pane>
      </el-tabs>
    </div>
<!--    关注-->
    <el-drawer
      :title="user.name+'的关注'"
      :visible.sync="followsDrawer"
    >
      <div>
        <div class="followItem" v-for="(item,index) in followsList">
          <header-image :size="50" :src="item.icon" :id="item.id"/>
          <div class="userInfoBox">
            <span class="userName">{{item.name}}</span>
            <span class="userSign">{{item.sign}}</span>
          </div>
          <el-button @click="cancelFollowBtnClick(index)" v-if="item.isFriend==1&&item.isCancelFollow==0" size="small" class="followBtn">互相关注</el-button>
          <el-button @click="cancelFollowBtnClick(index)" type="success" v-if="item.isFriend==0&&item.isCancelFollow==0" size="small" class="followBtn">已关注</el-button>
          <el-button @click="followBtnClick(index)" type="primary" v-if="item.isFriend==0&&item.isCancelFollow==1" size="small" class="followBtn">关注</el-button>
          <el-button @click="followBtnClick(index)" type="danger" v-if="item.isFriend==1&&item.isCancelFollow==1" size="small" class="followBtn">回关</el-button>
        </div>
        <p class="tips" v-if="this.followsPage.isAll==1">暂无更多</p>
        <p class="tips" v-else="this.followsPage.isAll==1"><span style="color: #4d73de;cursor: pointer" @click="getFollows">加载更多</span></p>
      </div>
    </el-drawer>
<!--    粉丝-->
    <el-drawer
      :title="this.user.name+'的粉丝'"
      :visible.sync="fansDrawer">
      <div>
        <div class="followItem" v-for="(item,index) in fansList">
          <header-image :size="50" :src="item.icon" :id="item.id"/>
          <div class="userInfoBox">
            <span class="userName">{{ item.name }}</span>
            <span class="userSign">{{ item.sign }}</span>
          </div>
          <el-button @click="cancelFansBtnClick(index)" v-if="item.isFriend==1" size="small" class="followBtn">互相关注</el-button>
          <el-button @click="fansBtnClick(index)"  v-if="item.isFriend==0" type="danger" size="small" class="followBtn">回关</el-button>

        </div>
        <p class="tips" v-if="this.fansPage.isAll==1">暂无更多</p>
        <p class="tips" v-else="this.fansPage.isAll==1"><span style="color: #4d73de" @click="getFans">加载更多</span></p>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import Header from "@/components/common/Header";
import CenterIndex from "@/page/CenterIndex";
import Collection from "@/page/Collection";
import MyPosts from "@/page/MyPosts";
import ResetPassword from "@/page/ResetPassword";
import HeaderImage from "@/components/HeaderImage";
import userApi from "@/api/user";
import {SUCCESS_CODE} from "@/utils/codeUtil";
import config from "@/http/config";

export default {
name: "PersonalCenter",
  components: {HeaderImage, ResetPassword, MyPosts, Collection, CenterIndex, Header},
  created() {
    this.getTotalInfo();
    this.signTxt = this.user.sign
  },
  data(){
  return {
    signTxt:"",
    activeName:"first",
    test:"",
    drawerTitle:"",
    drawer:false,
    followsList:[],
    fansList:[],
    followsPage:{
      total:0,
      pageSize:0,
      isAll:0,
      pageNum:20
    },
    fansPage:{
      total:0,
      pageSize:0,
      isAll:0,
      pageNum:20
    },
    followsDrawer:false,
    fansDrawer:false

  }
  },
  methods:{
    signTxtChange(){
      this.$store.dispatch('setSign',this.signTxt);
    },
    async getTotalInfo(){
      const {fansTotal,followTotal} = await userApi.getTotal(this.user.id)
      this.followsPage.total=followTotal;
      this.fansPage.total=fansTotal;
    },
    async getFollows() {
      let {pageSize,isAll} = this.followsPage;
      if (isAll==1) return;
      this.followsPage.pageNum+=1;
      if (this.followsPage.pageNum==1) this.followsList=[]
      const data = await userApi.findFollowsUser(this.user.id,this.followsPage.pageNum,pageSize);
      this.followsList = this.followsList.concat(data.list);
      this.followsPage.total=data.total;
      this.followsList.forEach((item)=>{
        item.isCancelFollow = 0;
      })
      if (this.followsPage.total==this.followsList.length) this.followsPage.isAll=1;
    },
    async getFans(){
      let {pageSize,isAll} = this.fansPage;
      if (isAll==1) return;
      this.fansPage.pageNum+=1;
      if (this.fansPage.pageNum==1) this.fansList=[];
      const data = await userApi.findFansUser(this.user.id,this.fansPage.pageNum,pageSize);
      this.fansList = this.fansList.concat(data.list);
      this.fansPage.total=data.total;
      if (this.fansList.length==this.fansPage.total) this.fansPage.isAll=1;

    },
    queryFollowsBtnClick(){
      this.followsPage.pageNum=0;
      this.followsDrawer = true;
      this.followsPage.isAll=0;
      this.getFollows();
    },
    queryFansBtnClick(){
      this.fansPage.pageNum=0;
      this.fansDrawer = true;
      this.fansPage.isAll=0;
      this.getFans();
    },
    async cancelFollowBtnClick(index){
      const {code} = await userApi.cancelFollowUser(this.followsList[index].id);
      if (code==SUCCESS_CODE) {
        this.$message.success("取关成功")
        this.followsList[index].isCancelFollow=1;
        this.followsPage.total-=1;
        this.$set(this.followsList,index,this.followsList[index])
      }else this.$message.error("取关失败，稍后请重试")
    },
    async followBtnClick(index){
      const {code} = await userApi.followUser(this.followsList[index].id);
      if (code==SUCCESS_CODE) {
        this.$message.success("关注成功")
        this.followsList[index].isCancelFollow=0;
        this.followsPage.total+=1;
        this.$set(this.followsList,index,this.followsList[index])
      }else this.$message.error("关注失败，稍后请重试")
    },
    async fansBtnClick(index){
      const {code} = await userApi.followUser(this.fansList[index].id);
      if (code==SUCCESS_CODE) {
        this.$message.success("关注成功")
        this.fansList[index].isFriend=1;
        this.followsPage.total+=1;
        this.$set(this.fansList,index,this.fansList[index])
      }else this.$message.error("关注失败，稍后请重试")
    },
    async cancelFansBtnClick(index){
      const {code} = await userApi.cancelFollowUser(this.fansList[index].id);
      if (code==SUCCESS_CODE) {
        this.$message.success("取关成功")
        this.fansList[index].isFriend=0;
        this.followsPage.total-=1;
        this.$set(this.fansList,index,this.fansList[index])
      }else this.$message.error("取关失败，稍后请重试")
    },
    async fileChange(e){
      let file = e.target.files[0];
      let formData = new FormData();
      formData.append("image",file);
      const data = await userApi.modifyBackImg(formData)
      if (data.code==SUCCESS_CODE) {
        this.$store.commit("setBackImg",data.fileName);
        this.$message.success("上传成功")
      } else this.$message.success("上传背景图失败")
    },
    async iconFileChange(e){
      let file = e.target.files[0];
      let formData = new FormData();
      formData.append("image",file);
      const data = await userApi.modifyIcon(formData)
      if (data.code==SUCCESS_CODE) {
        this.$store.commit("setIcon",data.fileName);
        this.$message.success("上传成功")
      } else this.$message.success("上传背景图失败")
    }
  },
  computed:{
    user(){
      return this.$store.state.user
    },
    baseURL(){
      return config.baseURL;
    }
  }
}
</script>

<style scoped lang="less">
@import "../style/center";
@import "../style/drawer";
#innerBox{
  .centerTop{
    //background-repeat: no-repeat;
    //background-position: center;
    .bgFile{
      top: 15px;
      right: 15px;
      position: absolute;
      width: 80px;
      height: 30px;
      background-color: rgba(0,0,0,0.45);
      border-radius: 4px;
      border: none;
      color: #fff;
      overflow: hidden;
      input{
        width: 100px;
        height: 100px;
        position: absolute;
        opacity: 0;
        top: 0;
        left: 0;
      }
      p{
        width: 100%;
        height: 100%;
        text-align: center;
        font-size: 0.8em;
        line-height: 30px;
      }
    }
    .bgFile:hover{
      cursor: pointer;
      background-color: rgba(0,0,0,0.5);
      color: #f6f6f6;
    }
  }
  .numTabs div:hover{
    cursor: pointer;
  }
}
.tips{
  text-align: center;
  margin: 15px;
  font-size: 0.75em;
  color: #727272;
}
.headerFile{
  position: relative;
  input{
    position: absolute;
    width: 65px;
    height: 65px;
    border-radius: 50%;
    opacity: 0;
    z-index: 100;
    cursor: pointer;
  }
}
</style>
