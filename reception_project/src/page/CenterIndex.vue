<template>
  <div id="centerIndexBox">
    <div id="infoBox">
      <div>
        <p>注册时间：{{ formatDate(user.regTime) }}</p>
      </div>
      <div>
        <span>名称：</span><el-input v-model="nameTxt" class="nameInput" placeholder="请输入名称" size="small"></el-input>
      </div>
      <div>
        <span>性别：</span>
        <el-radio-group v-model="radio">
          <el-radio :label="1">女</el-radio>
          <el-radio :label="2">男</el-radio>
          <el-radio :label="3">保密</el-radio>
        </el-radio-group>
      </div>
      <div>
        <span>出生日期：</span>
        <el-date-picker
          v-model="birthday"
          type="date"
          placeholder="选择日期">
        </el-date-picker>
      </div>
    </div>
    <div class="btnBox">
      <el-button :loading="saveLoading" @click="saveClick" class="preservationBtn" size="small" type="primary">保存</el-button>
    </div>
  </div>

</template>

<script>
import userApi from "@/api/user"
import {SUCCESS_CODE} from "@/utils/codeUtil";
export default {
  name: "CenterIndex",
  created(){
    this.nameTxt=this.user.name;
    let {birthday} = this.user;
    if (typeof birthday=="string") this.birthday=new Date(birthday);
    if (typeof birthday=="string") this.birthday=new Date(birthday);
    else this.birthday=birthday

    this.radio=this.user.sex;

  },
  data() {
    return {
      yyAge:"",
      imageUrl: '',
      radio:1,
      birthday:'',
      nameTxt:"",
      saveLoading:false
    };
  },
  methods: {
    formatDate(date) {
      return this.$formatDate(new Date(date));
    },
    async saveClick(){
      this.saveLoading=true;
      let birthday = null;
      try {
        birthday = new Date(this.birthday.getTime()+(60*60*24*1000));
      }catch (e) {}

      const {code} = await userApi.modifyUserInfo({birthday,name:this.nameTxt,sex:this.radio})
      this.saveLoading=false;
      if (code==SUCCESS_CODE) {
        this.$message.success("保存成功");
        this.$store.commit("setBirthday",this.birthday)
        this.$store.commit("setName",this.nameTxt)
        this.$store.commit("setSex",this.radio)
      } else this.$message.error("保存失败，请稍后再试")
    }
  },
  computed:{
    user(){
      return this.$store.state.user;
    }
  },
}
</script>

<style scoped lang="less">
#centerIndexBox{
  background-color: #ffffff;
  border: 2px solid #ececec;
  width: 80%;
  margin: 0 auto;
  padding-bottom: 40px;
}
#infoBox{
  display: flex;
  flex-direction: column;
  height: 250px;
  width: 350px;
  margin: 0 auto;
  justify-content: center;
  &>div{
    margin-bottom: 25px;
  }

  .nameInput{
    width: 200px;
  }

}
.btnBox{
  display: flex;
  justify-content: center;
  .preservationBtn{
    width: 100px;
  }
}


</style>
