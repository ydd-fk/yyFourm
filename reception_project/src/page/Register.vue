<template>
  <div>


  <LoginHeader/>
  <div id="box">
    <h3 class="title">注册</h3>
    <el-form :model="userInfo" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="账号" prop="account">
        <el-input type="text" v-model="userInfo.account" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input type="password" v-model="userInfo.pass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="passTwo">
        <el-input type="password" v-model="userInfo.passTwo" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="code" class="codeBox">
        <el-row :gutter="20">
          <el-col :span="10"><el-input v-model="userInfo.code"></el-input></el-col>
          <el-col :span="12">
            <el-image
              style="width: 110px; height: 40px;border-radius: 2px"
              :src="this.codeInfo.verifyCodeSrc"
              @click="this.getVerityCode"
              fit="cover"></el-image>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item class="txt">
        <span>
          已有帐号,<router-link to="/login">立即登录</router-link>
        </span>
      </el-form-item>
      <el-form-item>
        <el-button class="submitBtn" type="success" @click="submitForm('ruleForm')">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
  </div>
</template>

<script>
import LoginHeader from "@/components/common/LoginHeader";
import user from "@/api/user";
import {SUCCESS_CODE,DEFAULT_CODE,ERROR_CODE} from "@/utils/codeUtil"
export default {
  components:{
    LoginHeader
  },
  created() {
    this.getVerityCode();
  },
  data() {
    let checkAccount = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入账号'));
      else callback()
    };
    let checkPass = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入密码'));
      else callback()
    };
    let checkPassTwo = (rule, value, callback) => {
      if(value=='') callback(new Error('请再次输入密码'));
      else if(value!=this.userInfo.pass) callback(new Error('两次密码不一致，请重新输入'));
      else callback();
    };
    let checkCode = (rule, value, callback) => {
      if(value=='') callback(new Error('请输入验证码'));
      else callback()
    }
    return {
      userInfo: {
        account: '',
        pass: '',
        passTwo:'',
        code: ''
      },
      codeInfo:{
        verifyCodeSrc : "",
        codeKey : ""
      },
      loading:false,
      rules: {
        account: [
          { validator: checkAccount, trigger: 'blur' }
        ],
        pass: [
          { validator: checkPass, trigger: 'blur' }
        ],
        passTwo: [
          { validator: checkPassTwo, trigger: 'blur' }
        ],
        code: [
          { validator: checkCode, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.register();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    getVerityCode(){
      console.log("刷新验证码")
      user.getVerityCode().then((data)=>{
        this.codeInfo.codeKey=data.codeKey;
        this.codeInfo.verifyCodeSrc=data.imgSource;
      })
    },
    async register(){
      this.loading=true;
      let registerObj = {
        account:this.userInfo.account,
        password:this.userInfo.pass,
        code:this.userInfo.code,
        codeKey:this.codeInfo.codeKey
      }
      const data = await user.register(registerObj)
      this.loading=false;
      if (data.code==SUCCESS_CODE){
        this.$message.success("注册成功，快去登录吧~");
        this.$router.push({
          name:"Login",
          params:{
          account:this.userInfo.account,
            password:this.userInfo.pass
        }})
        return;
      }
      this.getVerityCode();
      this.$message.error(data.message);
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
    margin: 25px;
    text-align: center;
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
