<template>
  <div id="loginOutBox">
    <LoginHeader/>
    <div id="box">
      <h3 class="title">登录</h3>
      <el-form :model="userInfo" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="账号" prop="account">
          <el-input type="text" v-model="userInfo.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input type="password" v-model="userInfo.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" class="codeBox">
          <el-row :gutter="20">
            <el-col :span="10"><el-input v-model="userInfo.code"></el-input></el-col>
            <el-col :span="12">
              <el-image
                @click="getVerityCode"
                style="width: 110px; height: 40px;"
                :src="codeInfo.verifyCodeSrc"
                fit="cover"></el-image>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item class="txt">
        <span>
          没有账号？<router-link to="/register">立即注册</router-link>
        </span>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" class="submitBtn" type="primary" @click="submitForm('ruleForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>


</template>

<script>
import LoginHeader from "@/components/common/LoginHeader";
import user from "@/api/user";
import {SUCCESS_CODE,DEFAULT_CODE,ERROR_CODE} from "@/utils/codeUtil"
import authUtil from "@/utils/authUtil";
import {mapMutations} from 'vuex';
export default {
  components: {LoginHeader},
  created() {
    this.getVerityCode();
    const {account,password} = this.$route.params
    this.userInfo.account=account;
    this.userInfo.pass=password;
  },
  data() {
    let checkAccount = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入账号'));
      else callback()
    }
    let checkPass = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入密码'));
      else callback()
    }
    let checkCode = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入验证码'));
      else callback()
    }
    return {
      userInfo: {
        account: '',
        pass: '',
        code: ''
      },
      rules: {
        account: [
          { validator: checkAccount, trigger: 'blur' }
        ],
        pass: [
          { validator: checkPass, trigger: 'blur' }
        ],
        code: [
          { validator: checkCode, trigger: 'blur' }
        ]
      },
      codeInfo:{
        verifyCodeSrc : "",
        codeKey : ""
      },
      loading:false
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.login();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    async login(){
      this.loading = true;
      let loginObj = {
        account:this.userInfo.account,
        password : this.userInfo.pass,
        code : this.userInfo.code,
        codeKey : this.codeInfo.codeKey
      }
      console.log(loginObj)
      let data = await user.login(loginObj);
      this.loading=false;
      if(data.code==SUCCESS_CODE){
        console.log("===userInfo===")
        console.log(data.userInfo)
        this.$message.success("欢迎回来"+data.userInfo.name);
        this.$router.push({name:"Index",params:{account:"123456",password:"123456"}})
        authUtil.setAuthorization(data.token)
        this.$store.commit("setUser", {user: data.userInfo})
        return;
      }
      this.getVerityCode();
      this.$message.error(data.message);
    },
    getVerityCode(){
      console.log("刷新验证码")
      user.getVerityCode().then((data)=>{
        this.codeInfo.codeKey=data.codeKey;
        this.codeInfo.verifyCodeSrc=data.imgSource;
      })
    }
  }
}
</script>

<style scoped lang="less">
#box{
  padding: 0 0px 15px 0;
  border-radius: 5px;
  border: 1px solid #eeeeee;
  margin: 100px auto;
  width: 420px;
  .title{
    text-align: center;
    margin: 25px;
  }
  .submitBtn{
    width: 100%;
  }
  .demo-ruleForm{
    padding: 0 60px 0px 0;
  }
  .txt{
    height: 10px;
    text-align: right;
    span{
      font-size: 0.6em;
    }
  }
  .codeBox{
    margin: 0;
  }
}
</style>
