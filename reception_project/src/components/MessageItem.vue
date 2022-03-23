<template>
  <div class="message-item" v-if="isToUser">
    <div>
      <header-image :size="36" :url="otherUserIcon"/>
    </div>
    <div class="message-txt">
      <p class="to-user-radius">{{ txt }}</p>
    </div>
  </div>
  <div class="message-item from-user-item" v-else="isToUser">
    <div class="message-txt from-user-message">
      <p class="from-user-radius">{{ txt }}</p>
    </div>
    <div>
      <header-image :size="36" :url="userIcon"/>
    </div>
  </div>
</template>

<script>
import HeaderImage from "@/components/HeaderImage";
export default {
  props:{
    isToUser:Boolean,
    txt:String,
    otherUserIcon:String,
    userIcon:String
  },
  name: "MessageItem",
  components:{HeaderImage}
}
</script>

<style lang="less" scoped>
.message-item{
  display: flex;
  padding: 0 16px 16px;
  p{
    font-size: 14px;
    display: inline-block;
    padding: 8px 16px;
  }
}
.message-txt{
  flex: 1;
  padding: 10px 0 0 5px;
  p{
    max-width: 80%;
  }
}
.from-user-message{
  text-align: end;
  padding: 10px 5px 0 0;
}
.to-user-radius{
  background-color: #ffffff;
  border-radius: 0px 16px 16px 16px;
}
.from-user-radius{
  color: #ffffff;
  background-color: #4091ff;
  border-radius: 16px 0px 16px 16px;
}
</style>
