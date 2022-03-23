<template>
<div class="person-item"
     :style="{'background-color': (isChecked==true ? '#e4e5e6':'#ffffff')}"
     @click="personClick">
  <div class="delBtn">
    <span>
      <icon name="chacha_noback" :w="10" :h="10" color="black"/>
    </span>
  </div>
  <header-image :size="40" :url="messageUser.fromUser.icon"/>
  <div class="info">
    <span class="user-name">{{messageUser.fromUser.name}}</span>
    <span class="new-message">{{messageUser.txt}}</span>
    <div class="messageRead" v-if="!isRead()">
    </div>
  </div>
</div>
</template>

<script>
import HeaderImage from "@/components/HeaderImage";
export default {
  name: "PersonItem",
  props:{
    messageUser:Object,
    isChecked:Boolean
  },
  components: {HeaderImage},
  methods:{
    personClick(){
      const {id,icon,name} = this.messageUser.fromUser;
      this.$emit("getHistoryMsg",{to_userID:id,to_userIcon:icon,to_userName:name})
    },
    isRead(){
      const {toUserID,isRead} = this.messageUser;
      if (toUserID!=this.user.id) return true;
      if (isRead==1) return true;
      return false;
    },
  },
  computed:{
    user(){
      return this.$store.state.user;
    }
  }
}
</script>

<style lang="less" scoped>
.person-item{
  position: relative;
  padding-left: 10px;
  height: 78px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid #dedede;
  .info{
    padding-left: 3px;
    display: flex;
    flex: 1;
    flex-direction: column;
    .user-name{
      font-size: 14px;
    }
    .new-message{
      font-size: 12px;
      color: #999999;
      margin: 5px 0;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      max-width: 160px;
    }
  }
  &:hover{
    background-color: #e4e5e6;
    cursor: pointer;
  }
  &:hover .delBtn{
    opacity:1;
  }
}
.delBtn{
  opacity: 0;
  position: absolute;
  background-color: rgba(173,173,173,0.65);
  width: 25px;
  height: 25px;
  box-sizing: border-box;
  padding: 0 0 0 10px;
  border-radius: 0px 0px 0px 25px;
  top: 0;
  right: 0;
}
.messageRead{
  position: absolute;
  border-radius: 50%;
  background-color: #53a5ff;
  width: 10px;
  height: 10px;
  right: 15px;
  bottom: 8px;
}
</style>
