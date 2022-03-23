import request from "@/http";
export default {
  //获取验证码
  getVerityCode:()=>request.get("/verifycode"),
  //登录
  login:({account,password,code,codeKey})=>request.post("/user/login",{user:{account,password},code,codeKey}),
  //注册
  register:({account,password,code,codeKey})=>request.post("/user/register",{user:{account,password},code,codeKey}),
  //查看关注的人
  findFollowsUser:(userID,pageNum,pageSize)=>request.get(`/user/follows/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  //查看用户的粉丝列表
  findFansUser:(userID,pageNum,pageSize)=>request.get(`/user/fans/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  //查看别的用户关注的人
  findOtherFollowsUser:(userID,pageNum,pageSize)=>request.get(`/user/other/follows/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  //查看别的用户的粉丝列表
  findOtherFansUser:(userID,pageNum,pageSize)=>request.get(`/user/other/fans/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  //关注用户
  followUser:(userID)=>request.post(`/user/follows/${userID}`),
  //取关用户
  cancelFollowUser:(userID)=>request.delete(`/user/follows/${userID}`),
  //获取当前用户信息
  getUser:()=>request.get("/user/"),
  //获取用户信息
  getOtherUser:(userID)=>request.get(`/user/${userID}`),
  //获取关注的人数 和 粉丝数
  getTotal:(userID)=>request.get(`/user/total/${userID}`),
  //修改签名
  modifySign:(sign)=>request.put(`/user/modify/sign`,{sign}),
  //修改背景图
  modifyBackImg:(formData)=>request.post(`/user/modify/back`,formData,{
    headers: {'Content-Type': 'multipart/form-data'}
  }),
  //修改用户信息
  modifyUserInfo:({birthday,name,sex})=>request.put("/user/",{birthday,name,sex}),
  //修改用户头像
  modifyIcon:(formData)=>request.post(`/user/modify/icon`,formData,{
    headers: {'Content-Type': 'multipart/form-data'}
  }),
  //修改用户密码
  modifyPwd:({oldPwd,newPwd})=>request.put("/user/modify/pwd",{oldPwd,newPwd})
}
