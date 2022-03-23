<template>
  <div id="passwordBox">
    <div class="inputBox">
      原密码：<el-input v-model="oldPwd" type="password" class="myInput" @keyup.enter.native="inputFocus('newPwd')"></el-input>
      <br>
      新密码：<el-input v-model="newPwd" ref="newPwd" type="password" class="myInput" @keyup.enter.native="inputFocus('definePwd')"></el-input>
      <br>
      确认密码：<el-input v-model="newPwdTow" ref="definePwd" type="password" class="myInput" @keyup.enter.native="alertBtnClick"></el-input>
      <br>
      <el-button :loading="isLoading" type="primary" @click="modifyPwdClick">修改</el-button>
      <el-button @click="restClick">重置</el-button>
    </div>
  </div>

</template>

<script>
import userApi from "@/api/user";
import {SUCCESS_CODE} from "@/utils/codeUtil";

export default {
name: "ResetPassword",
  data(){
    return{
      oldPwd:"",
      newPwd:"",
      newPwdTow:"",
      isLoading:false
    }
  },
  methods:{
    inputFocus(inputName){
      console.log(11)
      this.$refs[inputName].focus();
    },
    alertBtnClick(){
      console.log("11")
    },
    async modifyPwdClick(){
      let {newPwd,newPwdTow,oldPwd} = this
      if (newPwd==""||newPwdTow==""||oldPwd=="") return this.$message.info("请填写完整")
      if (newPwd!=newPwdTow) return this.$message.info("两次密码填写不一致")

      this.isLoading = true;
      const {code} = await userApi.modifyPwd({oldPwd, newPwd})
      if (code==SUCCESS_CODE) this.$message.success("修改成功");
      else this.$message.error("修改失败，密码不正确");
      this.isLoading = false;
    },
    restClick(){
      this.oldPwd="";
      this.newPwd="";
      this.newPwdTow="";
    }
  }
}
</script>

<style scoped lang="less">
#passwordBox{
  text-align: center;
  .inputBox{
    margin-top: 30px;
    display: inline-block;
    .myInput{
      width: 300px;
      margin-bottom: 20px;
    }
  }
}

</style>
