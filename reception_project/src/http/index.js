import axios from "axios";
import authUtil from "@/utils/authUtil";
import config from "@/http/config";
import {TOKEN_EXPIRE_CODE, TOKEN_NONE_CODE} from "@/utils/codeUtil";
import {Message} from "element-ui";
import store from "@/store";
import router from "@/router";

const request =axios.create({
  baseURL:config.baseURL
})

request.interceptors.request.use((requestConfig)=>{
  requestConfig.headers.authorization = authUtil.getAuthorization();
  return requestConfig;
})
request.interceptors.response.use(async (response)=>{
  try {
    const {code} = response.data;
    if (code==TOKEN_EXPIRE_CODE||code==TOKEN_NONE_CODE){
      console.log("error")
      authUtil.removeAuthorization();
      Message({
        message: response.data.message,
        type: 'info',
        duration: 5 * 1000
      })
      if (code==TOKEN_EXPIRE_CODE) store.commit("removeUser");
      await router.push("/login")
    }

  }catch (e){}
  return response.data;
})

export default request;
