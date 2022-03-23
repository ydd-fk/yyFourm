<template>
<div id="messageBox">
  <Header></Header>
  <main>
    <p class="title">
      <span>我的消息</span>
    </p>
    <div class="box">
      <div class="left-box">
        <div class="items-box">
          <div v-for="item in userList">
            <person-item :message-user="item"
                         @getHistoryMsg="getHistoryMessageClick"
                         :is-checked="item.fromUser.id==to_user.id"/>
          </div>
        </div>
      </div>
      <div class="right-box">
        <p class="to-user-title">
          <span>{{ to_user.name }}</span>
        </p>
        <div class="chat-box">
          <div class="items-box" ref="messageBox">
            <div ref="messageInnerBox">
              <p class="tips" v-if="isLoading"><i class="el-icon-loading"></i></p>
              <p class="tips" v-if="this.messagePage.isEnd==1">已经到顶啦~</p>
              <div v-for="(item,index) in messageList">
                <history-time :time="item.time"
                              :pre-time="index==0?null:(messageList[index-1].time+'')"/>
                <MessageItem
                  :is-to-user="item.toUserID==user.id"
                  :txt="item.txt"
                  :other-user-icon="to_user.icon"
                  :user-icon="user.icon"
                ></MessageItem>
              </div>
            </div>


          </div>

        </div>
        <div class="send-box">
          <textarea v-model="messageTxt" />
          <div class="send-box-bottom">
            <el-button type="primary" size="small" @click="sendClick">发送</el-button>
          </div>
        </div>
      </div>
    </div>

  </main>
</div>
</template>

<script>
import Header from "@/components/common/Header";
import HeaderImage from "@/components/HeaderImage";
import PersonItem from "@/components/PersonItem";
import MessageItem from "@/components/MessageItem";
import messageApi from "@/api/message";
import HistoryTime from "@/components/HistoryTime";
export default {
name: "MyMessage",
  components: {HistoryTime, MessageItem, PersonItem, HeaderImage, Header},
  data(){
    return{
      messageList:[],
      userList:[],
      socket:null,
      messageTxt:"",
      userPage:{
        pageSize:15,
        pageNum:0,
        isEnd:0
      },
      messagePage:{
        limit:20,
        isEnd:0
      },
      to_user:{
        id:0,
        icon:"",
        name:""
      },
      isLoading:false
    }
  },
  async created() {
    await this.getUserListLately();
    const {user} = this.$route.params;
    if (user!=null){
      let isHaveUser = false;
      const {id,name,icon} = user;
      this.userList.forEach(item=>{
        if (item.fromUser.id==id) isHaveUser=true;

      })
      if (!isHaveUser){
        let newUser = {
          fromUser:{
            id,
            icon,
            name
          },
          txt:"",
          isRead:1,
          toUserID:this.user.id
        }
        this.userList.unshift(newUser)
      }
      await this.getHistoryMessageClick({
        to_userID: id,
        to_userIcon: icon,
        to_userName: name
      })
    }
  },
  mounted() {
    this.initSocket();
    this.$refs.messageBox.addEventListener("scroll",this.boxScrollEvent)

  },
  methods:{
    initSocket(){
      this.socket = new WebSocket(`ws://121.196.244.13:8080/wsserver/${this.user.id}`);
      this.socket.onopen = this.socketOpen;
      this.socket.onerror = this.socketError;
      this.socket.onmessage = this.socketGetMessage;
    },
    async boxScrollEvent(e){
      let beginHeight = this.$refs.messageInnerBox.offsetHeight;
    if(e.target.scrollTop==0){
      await this.getHistoryMessage();
      if (this.messagePage.isEnd==0) this.$refs.messageBox.scrollTop = beginHeight;
    }

  },
  //点击联系人
    async getHistoryMessageClick({to_userID,to_userIcon,to_userName}){
      if (this.to_user.id==to_userID) return;
      //初始化数据
      this.messageList = [];
      this.to_user={
        id:to_userID,
        icon: to_userIcon,
        name:to_userName
      };
      this.messagePage.isEnd=0;
      await this.getHistoryMessage();
      this.onTop();
      await this.isRead(to_userID);
      console.log("offsetHeight")
      console.log(this.$refs.messageInnerBox.offsetHeight)
    },
    async isRead(from_id){
      this.userList.forEach(item=>{
        if (item.fromUser.id==from_id) {
          if (item.isRead==0){
            item.isRead=1;
          }
        }
      })
      await messageApi.isRead(from_id);
    },
    //获取聊天记录
    async getHistoryMessage(){
      if (this.isLoading) return;
      if (this.messagePage.isEnd==1) return;
      this.isLoading=true;
      const {limit} = this.messagePage;
      const data = await messageApi.getHistoryMessage({
          limit,
          offset:this.messageList.length,
          to_userID:this.to_user.id
        });
      this.messageList = data.list.concat(this.messageList);
      if (data.isLastPage) this.messagePage.isEnd=1;
      this.isLoading=false;
    },
    //获取近期联系人消息
    async getUserListLately(){
      let {pageNum,pageSize,isEnd} = this.userPage;
      if (isEnd==1) return;
      this.pageNum+=1;
      let data = await messageApi.getMessageLately({pageNum:pageNum+1, pageSize})
      if (data.isLastPage) this.userPage.isEnd=1;
      this.userList = data.list;
    },
    //获取新消息时，将该联系人置顶
    newUserOnTop(message){
      const from_id = message.fromUser.id;
      const to_id = message.toUserID;
      this.userList.forEach(item=>{
        if(from_id==item.fromUser.id||to_id==item.fromUser.id) item.txt=message.txt;
      });
      let itemTemp = null;
      this.userList = this.userList.filter(item=>{
        if(from_id==item.fromUser.id||to_id==item.fromUser.id) itemTemp=item;
        return item.fromUser.id!=from_id&item.fromUser.id!=to_id;
      })
      if (itemTemp==null){
        this.userList.unshift(message)
      }else{
        itemTemp.txt = message.txt;
        this.userList.unshift(itemTemp);
      }
    },
    socketOpen(){},
    socketError(e){
      console.log("error",e)
      this.$message.error("连接服务器失败，请重试");
    },
    socketGetMessage(msg){
      let data = JSON.parse(msg.data);
      const {message} = data;
      let isOnTop = false;
      this.newUserOnTop(message);
      if (this.to_user.id==message.fromUser.id||this.to_user.id==message.toUserID) {
        const {scrollTop,scrollHeight,clientHeight} = this.$refs.messageBox;
        if (parseInt(scrollTop+clientHeight)>=scrollHeight-5&&parseInt(scrollTop+clientHeight)<=scrollHeight+5) isOnTop=true;
        this.messageList.push(message);
      };
      //如果fromID==userID 则表明是自己发送的消息已经确认送达了
      //如果fromID!=userID 则表明是接收到别的用户的信息了
      if (this.user.id==message.fromUser.id) {
        this.messageTxt="";
        this.onTop();
      }else if(this.to_user.id==message.fromUser.id){
        this.isRead(message.fromUser.id);
      }else{
        this.userList.forEach(item=>{
          if (item.fromUser.id==message.fromUser.id) item.isRead=0;
        })
      }
      if (isOnTop) this.onTop();
    },
    sendClick(){
      const {name,icon} = this.user;
      const {id} = this.to_user;
      if (id==0) return this.$message.info("请选择联系人");
      if (this.messageTxt==null||this.messageTxt=="") return this.$message.info("请输入内容");

      const message = {
        toUserID:id,
        txt:this.messageTxt,
        from_userName:name,
        from_userIcon:icon
      }
      this.socket.send(JSON.stringify(message));
    },
    onTop(){
      this.$nextTick(() => {
        this.$refs.messageBox.scrollTop = this.$refs.messageBox.scrollHeight;
      });
    }
  },
  computed:{
    user(){return this.$store.state.user},
  },
  beforeDestroy() {
  this.socket.close();
  }
}
</script>

<style lang="less" scoped>
@import "../style/tips";
@leftBoxW:260px;
@rightBoxW:640px;
@borderColor:#dedede;
@sendColor:#f4f5f7;
#messageBox{
  background-color: #ececec;
}
main{
  padding-top: 58px;
  height: 100vh;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  flex-direction: column;
  .title{
    height: 58px;
    width: @leftBoxW+@rightBoxW;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
    font-weight: bold;
    color: #656565;
    span{
      background-color: #ffffff;
      height: 42px;
      line-height: 42px;
      width: 100%;
      text-align: center;
      border-radius: 5px;
    }
  }
  .box{
    flex: 1;
    width: 100%;
    float: left;
    display: flex;
    justify-content: center;
    width: @leftBoxW+@rightBoxW;
    margin-bottom: 15px;
    background-color: #ffffff;
    border-radius: 5px;
    .left-box{
      border-right: 1px solid @borderColor;
      position: relative;
      width: @leftBoxW;
      height: 100%;
      .items-box{
        position: absolute;
        top: 0;
        bottom: 0;
        right: 0;
        left: 0;
        overflow: auto;
      }
    }
    .right-box{
      width: @rightBoxW;
      height: 100%;
      display: flex;
      flex-direction: column;
      .to-user-title{
        text-align: center;
        height: 36px;
        line-height: 36px;
        border-bottom: 1px solid @borderColor;
        font-size: 14px;
      }
      .chat-box{
        background-color: #f4f5f7;
        position: relative;
        flex: 1;
        .items-box{
          padding: 0px 0 10px 0;
          position: absolute;
          overflow: auto;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
        }
      }
      .send-box{
        background-color: @sendColor;
        box-sizing: border-box;
        border-top: 1px solid @borderColor;
        flex-direction: column;
        display: flex;
        height: 162px;
        textarea{
          box-sizing: border-box;
          padding: 5px;
          background-color: @sendColor;
          font-size: 16px;
          border: none;
          box-sizing: border-box;
          resize: none;
          width: 100%;
          height: 100px;
          display: block;
          margin: 5px auto;
          outline-width: 0;
        }
        .send-box-bottom{
          flex: 1;
          text-align: end;
          button{
            width: 80px;
            margin: 10px;
          }
        }
      }
    }
  }

}
::-webkit-scrollbar {
  width:8px;
}
::-webkit-scrollbar-thumb {
  border-radius:10px;
  background:rgba(0,0,0,0.1);
  -webkit-box-shadow:#727272;
}
::-webkit-scrollbar-thumb:window-inactive {
  background: inset006pxrgba(0,0,0,0.5);
}
</style>
