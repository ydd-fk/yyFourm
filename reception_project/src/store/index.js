import Vue from "vue";
import Vuex from "vuex";
import userApi from "@/api/user";
import authUtil from "@/utils/authUtil";
import {SUCCESS_CODE} from "@/utils/codeUtil";
import {Message} from "element-ui";

Vue.use(Vuex)

const state = {
  user:{
    id:0,
    account:"",
    icon:"",
    name:"",
    regTime:"",
    sign:"",
    backImg:"",
    sex:-1,
    birthDay:""
  }
}
const getters = {

}
const mutations = {
  setUser:(state,payload)=>{
    let userVal = payload.user;
    userVal.icon = userVal.icon==null?"":userVal.icon;
    userVal.backImg = userVal.backImg==null?"":userVal.backImg;
    state.user = userVal;
  },
  setSign:(state,sign)=>{
    console.log("setSign")
    state.user.sign = sign;
  },
  setBackImg:(state,backImg)=>{
    console.log("setBackImg")
    state.user.backImg = backImg;
  },
  setBirthday(state,birthday){
    console.log("setBirthday")
    state.user.birthday = birthday;
  },
  setName(state,name){
    console.log("setName")
    state.user.name = name;
  },
  setSex(state,sex){
    console.log("setSex")
    state.user.sex = sex;
  },
  setIcon(state,icon){
    console.log("setIcon")
    state.user.icon = icon;
  },
  removeUser:(state)=>{
    console.log("removeUser")
    authUtil.removeAuthorization();
    state.user={
      id:0,
      account:"",
      icon:"",
      name:"",
      regTime:"",
      sign:"",
      backImg:"",
      sex:-1,
      birthDay:""
    }
  }
}
const actions = {
  initUser({commit}){
    if (authUtil.getAuthorization()!=null) {
      console.log("setUser")
      userApi.getUser().then(data=>{
        commit('setUser',{user:data})
      })
    }
  },
  setSign({commit},sign){
    userApi.modifySign(sign).then((data)=>{
      if (data.code == SUCCESS_CODE) {
        commit("setSign",sign);
        Message.success("签名已修改")
      } else Message.error("修改签名失败，稍后请重试")
    })
  }
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})
