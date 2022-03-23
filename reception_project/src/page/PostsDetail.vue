<template>
  <el-container>
    <el-header>
      <Header></Header>
    </el-header>
    <el-container>
      <el-main>
        <div ref="main">
          <div ref="postsInfoBox">
            <p class="title">{{ posts.title }}</p>
            <div class="infoBox">
              <header-image :size="50" :url="posts.user.icon" :id="posts.user.id"/>
              <div class="infoCenter">
                <span class="userName">张三</span>
                <span class="data">{{ formatDate(posts.time) }}</span>
              </div>
              <el-button v-if="isFollowUser==0" @click="followUser" class="btn" type="primary">关注</el-button>
              <el-button v-if="isFollowUser==1" @click="cancelFollowUser" class="btn" type="info">已关注</el-button>
            </div>
            <div class="postsMain">
              <p class="posts">
                {{posts.detail}}
              </p>

              <div class="demo-image__placeholder">
                <div v-for="item in posts.icons" class="block">
                  <el-image :src="baseURL+'/img/'+item.url"></el-image>
                </div>
              </div>
            </div>
            <!--        图标-->
            <div class="svgBox">
          <span @click="likeClick">
            <icon v-if="isLikePosts==0" name="dianzan" w="30" h="30"></icon>
            <icon v-else="isLikePosts==0" name="dianzan_off" w="30" h="30"></icon>
            <span>{{ posts.likes }}</span>
          </span>
              <span @click="followClick">
            <icon v-if="isFollowPosts==0" name="shoucang" w="30" h="30"></icon>
            <icon v-else="isFollowPosts==0" name="shoucang_off" w="30" h="30"></icon>
            <span>{{ posts.collections }}</span>
          </span>
              <!--          <span>-->
              <!--            <icon name="jubao" w="30" h="30"></icon>-->
              <!--            <span>举报</span>-->
              <!--          </span>-->

            </div>
            <div style="margin: 20px">
          </div>

          <el-divider content-position="left">全部评论</el-divider>
          <p style="margin: 10px;text-align: end" v-if="commentList.length>0">
            共&nbsp;<span style="color: #ff0000">{{ commentList.length }}</span>&nbsp;条评论
          </p>
          <p style="color: #727272;text-align: center;margin: 50px;" v-if="commentList.length==0">
            暂无评论，快来抢沙发吧~
          </p>
        </div>
        <div>
          <div v-for="(item,index) in commentList" :key="item.id">
            <div class="replyInfoBox">
              <header-image :size="50" :url="item.userIcon" :id="item.userID"/>
              <div class="replyInfo">
                <span class="userName">{{item.userName}}</span>
                <span class="data">{{ formatDate(item.time) }}</span>
              </div>
            </div>
            <p class="reply">{{ item.content }}</p>
            <div class="operationBox">
              <div>
                <span v-if="commentLikeIsShow(item.id)"  @click="cancelLikeCommentClick(index)">
                  <icon name="dianzan_off" w="20" h="20" color="black"></icon>{{ item.likes }}
              </span>
                <span v-else="commentLikeIsShow(item.id)" @click="likeCommentClick(index)">
                  <icon name="dianzan" w="20" h="20" color="black"></icon>{{ item.likes }}
              </span>
                <span v-if="item.userID==user.id" @click="delCommentClick(index)" class="delBtn">删除</span>
                <span @click="replyClick(index)">回复</span>
<!--                <span class="reportBtn">举报</span>-->
              </div>
            </div>
            <div class="replyBox" v-if="item.replyList.length!=0">
              <div v-for="(replyItem,replyIndex) in item.replyList">
                <p class="reply">
                  <span>
                    <span class="personTxt" @click="routeClick(replyItem.fromUserID)">{{ replyItem.fromUserName }}</span>
                    {{ replyItem.toUserName==null?'':'回复' }}
                    <span  class="personTxt"
                           v-if="replyItem.toUserID!=0"
                          @click="routeClick(replyItem.toUserID)">
                      {{replyItem.toUserName}}
                    </span>&nbsp;:
                  </span>
                  {{ replyItem.content }}
                </p>
                <div class="operationBox">
                  <div>
                    <span v-if="commentReplyLikeIsShow(replyItem.id)" @click="cancelLikeCommentReplyClick(index,replyIndex)">
                        <icon name="dianzan_off" w="20" h="20" color="black"></icon>{{replyItem.likes}}
                    </span>
                    <span v-else="commentReplyLikeIsShow(replyItem.id)" @click="likeCommentReplyClick(index,replyIndex)">
                        <icon name="dianzan" w="20" h="20" color="black"></icon>{{replyItem.likes}}
                    </span>
                    <span v-if="replyItem.fromUserID==user.id" @click="delCommentReplyClick(index,replyIndex)" class="delBtn">删除</span>
                    <span @click="replyItemClick(index,replyItem)">回复</span>
<!--                    <span>举报</span>-->
                  </div>
                </div>
              </div>
            </div>
<!--      none block    回复框-->
            <div class="replyInputBox" :style="{'display':(item.replyFlag==true?'block':'none')}">
              <el-input
                        :rows="4"
                        class="replyInput"
                        type="textarea"
                        v-model="item.replyTxt"
                        resize="none"
                        :placeholder="item.placeholder">
              </el-input>
              <el-button size="small" class="publishBtn" @click="commentList[index].replyFlag=false">收起</el-button>
              <el-button size="small" type="primary" class="publishBtn" @click="sendClick(index)">发表</el-button>
            </div>

            <div style="margin: 10px">
              <el-divider></el-divider>
            </div>

          </div>


        </div>
          <p class="tips" v-if="commentLoading">加载评论中......</p>
          <p class="tips" v-if="commentList.length!=0&&commentList.length==commentPage.total">没有更多评论</p>
<!--          底部-->
        <el-input :rows="6"
                  class="endInput"
                  type="textarea"
                  v-model="comment"
                  resize="none"
                  placeholder="快来说一句吧">
        </el-input>
        <div style="text-align: center;margin-bottom: 40px">
          <el-button size="small" class="publishBtn" @click="comment=''">重置</el-button>
          <el-button size="small" type="primary" class="publishBtn" @click="sendCommentClick">发表</el-button>
        </div>
        </div>
      </el-main>
      <el-aside width="350px">
        <Aside @init="init"></Aside>
      </el-aside>
    </el-container>
  </el-container>
</template>

<script>
import Header from "@/components/common/Header";
import Aside from "@/components/common/Aside";
import postsApi from "@/api/posts";
import userApi from "@/api/user";
import commentApi from "@/api/comment";
import HeaderImage from "@/components/HeaderImage";
import config from "@/http/config";
import {SUCCESS_CODE,DEFAULT_CODE} from "@/utils/codeUtil";
import comment from "@/api/comment";
export default {
  name: "layout",
  created() {
    this.init();
  },
  mounted(){
    window.addEventListener('scroll', this.scroll,false);
  },
  destroyed() {
    window.removeEventListener("scroll", this.scroll);
  },
  data() {
    return {
      posts: {
        user: {}
      },
      commentList: [],
      commentPage: {
        total: -1,
        pageNum: 0,
        pageSize: 20
      },
      isFollowUser: 0,
      isLikePosts: 0,
      isFollowPosts: 0,
      comment: "",
      commentLikesArr:[],
      commentReplyLikesArr:[],
      commentOnceLoading:false,
      commentLoading:false
    }
  },
  methods: {
    routeClick(userID){
      this.$router.push({path:"/other",query:{id:userID}})
    },
    init(){
      console.log("init......")
      const postsID = this.$route.query.postsID;
      console.log(postsID)
      this.posts.id = postsID;
      if (postsID != null && postsID != '') this.getPosts(postsID);
      if (this.user.id!=0 && this.user.id!='') this.getCommentLike(postsID);
      this.getCommentList();
    },
    async getPosts() {
      const {posts, isFollowUser, isLikePosts, isFollowPosts} = await postsApi.getPostsByID(this.posts.id)
      this.posts = posts;
      this.isFollowUser = isFollowUser;
      this.isLikePosts = isLikePosts;
      this.isFollowPosts = isFollowPosts;

    },
    async getCommentList() {
      if(this.total==this.commentList.length) return;
      this.commentLoading=true;
      this.pageNum+=1;
      const {pageNum, pageSize} = this.commentPage;
      const {list, total} = await commentApi.getCommentsList(this.posts.id, pageNum, pageSize);
      list.forEach((item) => {
        //初始化 是否收起回复框、回复框文本、回复人ID、被回复人ID
        item.replyFlag = false;
        item.replyTxt = "";
        item.replyToUserID = 0;
        item.replyFromUserID = 0;
        item.replyToUserName = null;
        item.replyFromUserName = null;
        item.replyLikes = 0;

      })
      this.commentList = list;
      this.commentPage.total = total;
      this.commentLoading=false;
    },
    async likeClick() {
      console.log("点赞")
      const id = this.posts.id;
      if (this.isLikePosts == 0) {
        const {code} = await postsApi.like(id);
        console.log(code)
        if (code == SUCCESS_CODE) {
          this.isLikePosts = 1;
          this.posts.likes += 1;
        }
      } else if (this.isLikePosts == 1) {
        const {code} = await postsApi.cancelLike(id);
        if (code == SUCCESS_CODE) {
          this.isLikePosts = 0;
          this.posts.likes -= 1;
        }
      }
    },
    async followClick() {
      const id = this.posts.id;
      if (this.isFollowPosts == 0) {
        const {code} = await postsApi.follow(id);
        if (code == SUCCESS_CODE) {
          this.isFollowPosts = 1;
          this.posts.collections += 1;
        }
      } else {
        const {code} = await postsApi.cancelFollow(id);
        if (code == SUCCESS_CODE) {
          this.isFollowPosts = 0;
          this.posts.collections -= 1;
        }
      }
    },
    replyClick(index) {
      this.changeInputPlaceholder(index, this.commentList[index].userName)
      this.commentList[index].replyToUserID = 0;
      this.commentList[index].replyToUserName = null;
      this.commentList[index].replyTxt = "";
    },
    replyItemClick(index, replyObj) {
      this.changeInputPlaceholder(index, replyObj.fromUserName);
      this.commentList[index].replyToUserID = replyObj.fromUserID;
      this.commentList[index].replyToUserName = replyObj.fromUserName;
      this.commentList[index].replyTxt ="";
    },
    changeInputPlaceholder(commentList_index, replyUserName) {
      this.commentList[commentList_index].replyFlag = true;
      this.commentList[commentList_index].placeholder = "回复 " + replyUserName + " :";
      this.$set(this.commentList, commentList_index, this.commentList[commentList_index])
    },
    formatDate(date) {
      return this.$formatDate(new Date(date));
    },
    async sendClick(index) {
      const {id, replyTxt, replyToUserID,replyToUserName} = this.commentList[index];
      const data = await commentApi.addCommentReply({
        commentID: id,
        commentReply: {
          "content": replyTxt,
          "toUserID": replyToUserID
        }
      })
      if (data.code == SUCCESS_CODE) {
        this.$message({
          message: '回复成功',
          type: 'success'
        });
        const {commentReply} = data;
        const obj = {
          content: replyTxt,
          fromUserID: this.user.id,
          fromUserName: this.user.name,
          id: commentReply.id,
          likes: 0,
          time: commentReply.time,
          toUserID: replyToUserID,
          toUserName: replyToUserName
        }
        this.commentList[index].replyList.push(obj);
        this.commentList[index].replayCount+=1;
      }else{
        this.$message.error(data.message);
      }
    },
    async sendCommentClick(){
      const data = await commentApi.addComment({
        postsID:this.posts.id,
        content:this.comment
      })
      if(data.code==SUCCESS_CODE){
        this.$message({
          message:"发表评论成功",
          type:"success"
        })
        const {comment} = data;
        const commentObj = {
          content: this.comment,
          id: comment.id,
          likes: 0,
          postsID: this.posts.id,
          replayCount: 0,
          replyFlag: false,
          replyFromUserID: 0,
          replyFromUserName: null,
          replyLikes: 0,
          replyList: [],
          replyToUserID: 0,
          replyToUserName: null,
          replyTxt: "",
          time: comment.time,
          userID: this.user.id,
          userIcon: this.user.icon,
          userName: this.user.name
        }
        this.commentList.push(commentObj)
        this.commentPage.total+=1;
        this.comment="";
      }else {
        this.$message.error("发表评论失败")
      }
    },
    //删除回复或评论
    async delCommentClick(index){
      const {id} = this.commentList[index];
      const {code} = await commentApi.delComment(id);
      if(code==SUCCESS_CODE){
        this.$message.success("删除成功");
        this.commentList = this.commentList.filter(item=>{
          return item.id!=id;
        })
      }
    },
    async delCommentReplyClick(commentIndex,replyIndex){
      const {id} = this.commentList[commentIndex].replyList[replyIndex];
      const {code} = await commentApi.delCommentReply(id);
      if(code==SUCCESS_CODE){
        this.$message.success("删除成功");
        this.commentList[commentIndex].replyList=this.commentList[commentIndex].replyList.filter(item=>{
          return item.id!=id;
        })
      }
    },
    //点赞评论类方法
    async getCommentLike(postsID){
      const {commentLikesArr,commentReplyLikesArr} = await commentApi.getCommentLikes(postsID);
      this.commentLikesArr=commentLikesArr;
      this.commentReplyLikesArr=commentReplyLikesArr
    },
    async likeCommentClick(index){
      const {id} = this.commentList[index];
      const {code} =  await commentApi.likeComment(id);
      if (code == SUCCESS_CODE) {
        this.commentList[index].likes+=1;
        this.commentLikesArr.push(id)
      }else{
        this.$message.error('点赞失败');
      }
    },
    async cancelLikeCommentClick(index){
      const {id} = this.commentList[index];
      const {code} =  await commentApi.cancelLikeComment(id);
      if (code == SUCCESS_CODE) {
        this.commentList[index].likes-=1;
        this.commentLikesArr = this.commentLikesArr.filter(item=> item!=id);
      }else{
        this.$message.error('取消点赞失败');
      }
    },
    commentLikeIsShow(commentID){
      let showFlag = false;
      this.commentLikesArr.forEach(item=>{
        if (item==commentID) showFlag=true;
      })
      return showFlag;
},
    //点赞回复类方法
    async likeCommentReplyClick(commentIndex,replyIndex){
      const {id} = this.commentList[commentIndex].replyList[replyIndex];
      const {code} = await commentApi.likeCommentReply(id);
      if(code==SUCCESS_CODE){
        this.commentList[commentIndex].replyList[replyIndex].likes+=1;
        this.commentReplyLikesArr.push(id);
      }else {
        this.$message.error("点赞回复失败")
      }
    },
    async cancelLikeCommentReplyClick(commentIndex,replyIndex){
      const {id} = this.commentList[commentIndex].replyList[replyIndex];
      const {code} = await commentApi.cancelLikeCommentReply(id);
      if(code==SUCCESS_CODE){
        this.commentList[commentIndex].replyList[replyIndex].likes-=1;
        this.commentReplyLikesArr = this.commentReplyLikesArr.filter(item=>item!=id);
      }else {
        this.$message.error("取消点赞回复失败")
      }
    },
    commentReplyLikeIsShow(replyID){
      let showFlag = false;
      this.commentReplyLikesArr.forEach(item=>{
        if (item==replyID) showFlag=true;
      })
      return showFlag;
    },
    scroll() {
      let scroll = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;//页面滚动高度
      let windowHeight=window.innerHeight;//窗口高度
      let card_sectionTop=this.$refs.main.offsetTop;//card_section距离顶部的偏移高度（card_section为你的照片或div元素ID）
      let card_sectionHeight=this.$refs.main.offsetHeight;//card_section的高度
      let postsBox_sectionTop = this.$refs.postsInfoBox.offsetTop;
      let postsBox_sectionHeight=this.$refs.postsInfoBox.offsetHeight;
      //触底刷新评论
      if(card_sectionTop+card_sectionHeight<scroll+windowHeight){
        //这里开始的你请求操作或者是显示数据
        console.log("触底")
      }
      //第一次加载评论
      if((postsBox_sectionTop+postsBox_sectionHeight<scroll+windowHeight)&&!this.commentOnceLoading){
        console.log("加载评论")
        this.getCommentList()
        this.commentOnceLoading=true;
      }
    },
    async followUser(){
      const {code} = await userApi.followUser(this.posts.user.id);
      if (code==SUCCESS_CODE){
        this.$message.success("关注成功")
        this.isFollowUser=1;
      }else{
        this.$message.error("关注用户失败，请稍后再试")
      }
    },
    async cancelFollowUser(){
      const {code} = await userApi.cancelFollowUser(this.posts.user.id);
      if (code==SUCCESS_CODE){
        this.$message.success("已取关用户")
        this.isFollowUser=0;
      }else{
        this.$message.error("取关失败，请稍后再试")
      }
    }
  },
    components: {
      HeaderImage,
      Aside,
      Header
    },
    computed: {
      baseURL() {
        return config.baseURL;
      },
      user(){
        return this.$store.state.user;
      }
    }

}
</script>

<style scoped lang="less">
@import "../style/myLayout";
.personTxt{
  color: #939393;
  cursor: pointer;
  &:hover{
    color: #4d73de;
  }
}
.btn{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 58px;
  height: 26px;
}
.title{
  font-size: 20px;
  margin: 15px;
}
.infoBox{
  margin: 0 0 10px 10px;
  display: flex;
  align-items: end;
  .infoCenter{
    display: flex;
    flex-direction: column;
    margin: 0 15px;
  }
}
.userName{
  color: #404040;
}
.data{
  font-size: 13px;
  color: #969696;
}

.postsMain{
  .posts{
    margin: 28px 10px 10px 10px;
  }
  .block{
    text-align: center;
    .el-image{
      width: 70%;
    }
  }
}
.replyInfoBox{
  display: flex;
  align-items: end;
  margin: 5px 10px;
  .replyInfo{
    margin: 0 10px;
    display: flex;
    flex-direction: column;
  }
}
.reply{
  margin: 10px;
}
.operationBox{
  display: flex;
  justify-content: end;
  font-size: 0.8em;
  padding-right: 10px;
  span{
    cursor: pointer;
    color: #aaaaaa;
  }
  span:hover{
    color: #000000;
  }
}
.replyBox{
  padding: 10px;
  width: 90%;
  margin: 10px auto;
  background-color: #f5f5f5;
  border-radius: 10px;
}
.svgBox{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px;
  &>span{
    display: flex;
    flex-direction: column;
    margin: 50px;
    justify-content: center;
    align-items: center;
  }
  &>span:hover{
    cursor:pointer;
  }
}
.endInput{
  z-index: 100;
  bottom: 0;
  width: 100%;
  margin: 40px 0;
  resize: none;
}

.replyInputBox{
  margin: 0 auto;
  width: 60%;
  text-align: end;

  .replyInput{
    width: 100%;
  }
}
.publishBtn{
  width: 62px;
  height: 30px;
  margin-top: 5px;
}
.tips{
  font-size: 0.72em;
  color: #727272;
  text-align: center;
}
</style>
