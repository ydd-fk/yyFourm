package com.app.controller;
import com.alibaba.fastjson.JSON;
import com.app.annotation.TokenPromise;
import com.app.entity.PageRequest;
import com.app.entity.User;
import com.app.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.app.util.FileUtil;
import com.app.util.JWTUtil;
import com.app.util.KaptchaUtil;
import com.app.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequestMapping(value = "/user",produces = "application/json;charset=UTF-8")
@ResponseBody
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private KaptchaUtil kaptchaUtil;

    /**获取当前用户信息*/
    @TokenPromise
    @GetMapping("/")
    public Object getUser(HttpServletRequest request){
        Integer userID =(Integer)request.getAttribute("userID");
        return userService.queryUserByID(userID);
    }

    /**获取别人的用户信息*/
    @GetMapping("/{id}")
    public Object getOtherUser(@PathVariable("id")int userID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        try {
            Integer id =(Integer) request.getAttribute("userID");
            jsonObject.put("isFollow",userService.queryIsFriend(id,userID));
        }catch (Exception e){
            jsonObject.put("ifFollow",0);
        }
        jsonObject.put("user",userService.queryUserByID(userID));
        return jsonObject;
    }


    /**登录*/
    @PostMapping("/login")
    public Object login(@RequestBody Map<String,Object> jsonMap){
        User user = JSON.parseObject(JSON.toJSONString(jsonMap.get("user")), User.class);
        String code =(String)jsonMap.get("code");
        String codeKey =(String)jsonMap.get("codeKey");
        JSONObject jsonObject = new JSONObject();
        if(!kaptchaUtil.checkVerifyCode(codeKey,code)){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            jsonObject.put("message","验证码错误");
            return jsonObject;
        }
        User findUser = userService.findUser(user);
        if(findUser==null){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            jsonObject.put("message","登陆失败");
        }else{
            findUser.setPassword(null);
            Map<String,String> map = new HashMap<>();
            map.put("id",findUser.getId()+"");
            map.put("icon",findUser.getIcon());
            map.put("account",findUser.getAccount());
            map.put("name",findUser.getName());
            String token = JWTUtil.getToken(map);
            jsonObject.put("token",token);
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            jsonObject.put("userInfo",findUser);
        }
        return jsonObject;

    }
    /**注册*/
    @PostMapping("/register")
    public Object register(@RequestBody Map<String,Object> jsonMap){
        User user = JSON.parseObject(JSON.toJSONString(jsonMap.get("user")), User.class);
        String code =(String)jsonMap.get("code");
        String codeKey =(String)jsonMap.get("codeKey");
        JSONObject jsonObject = new JSONObject();
        if(!kaptchaUtil.checkVerifyCode(codeKey,code)){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            jsonObject.put("message","验证码错误");
        }
        try {
            userService.addUser(user);
            userService.initUserName(user);
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            jsonObject.put("message","注册成功");
        }catch (Exception e){
            System.out.println(e);
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            jsonObject.put("message","注册失败");
        }

        return jsonObject;
    }
    /**查看粉丝列表*/
    @GetMapping("/fans/{id}")
    public Object getFans(PageRequest page,@PathVariable("id") int userID){
        return userService.queryFans(page,userID);
    }
    /**查看关注列表*/
    @GetMapping("/follows/{id}")
    public Object getFollows(PageRequest page,@PathVariable("id") int userID){
        return userService.queryFollows(page,userID);
    }
    /**查看粉丝列表*/
    @GetMapping("/other/fans/{id}")
    public Object getOtherFans(PageRequest page,
                               @PathVariable("id") int userID,
                               HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fans",userService.queryFans(page,userID));
        try {
            Integer id =(Integer) request.getAttribute("userID");
            if (id==null)jsonObject.put("userFollows",new int[]{});
            else jsonObject.put("userFollows",userService.queryUserFollowOtherUserFans(userID,id));
        }catch(Exception e){}
        return jsonObject;
    }
    /**查看关注列表*/
    @GetMapping("/other/follows/{id}")
    public Object getOtherFollows(PageRequest page,
                                  @PathVariable("id") int userID,
                                  HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("follows",userService.queryFollows(page,userID));
        try {
            Integer id =(Integer) request.getAttribute("userID");
            if (id==null)jsonObject.put("userFollows",new int[]{});
            else jsonObject.put("userFollows",userService.queryUserFollowOtherUserFollow(userID,id));
        }catch(Exception e){}
        return jsonObject;
    }
    /**关注用户*/
    @TokenPromise
    @PostMapping("/follows/{id}")
    public Object followsUser(@PathVariable("id") int fromUserID, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        int userID = (Integer)request.getAttribute("userID");
        if(userService.followUser(fromUserID, userID)>0){
            jsonObject.put("message","关注成功");
            jsonObject.put("code", ResponseCode.SUCCESS_CODE);
        }else{
            jsonObject.put("message","关注失败");
            jsonObject.put("code", ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**取关用户*/
    @TokenPromise
    @DeleteMapping("/follows/{id}")
    public Object noFollowsUser(@PathVariable("id") int fromUserID, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        int userID = (Integer)request.getAttribute("userID");
        if(userService.noFollowUser(fromUserID, userID)>0){
            jsonObject.put("message","取关成功");
            jsonObject.put("code", ResponseCode.SUCCESS_CODE);
        }else{
            jsonObject.put("message","取关失败");
            jsonObject.put("code", ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**查询粉丝数和关注的人数*/
    @GetMapping("/total/{id}")
    public Object followAndFansTotal(@PathVariable("id")int userID){
        JSONObject jsonObject = new JSONObject();
        int fansTotal = userService.queryFansTotal(userID);
        int followTotal = userService.queryFollowsTotal(userID);
        jsonObject.put("fansTotal",fansTotal);
        jsonObject.put("followTotal",followTotal);
        return jsonObject;
    }

    /**修改签名*/
    @TokenPromise
    @PutMapping("/modify/sign")
    public Object modifySign(@RequestBody Map<String,String> map,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String sign = map.get("sign");
        Integer userID = (Integer)request.getAttribute("userID");
         if(userService.alterSign(userID, sign)==1) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
         else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }

    /**修改背景图*/
    @TokenPromise
    @PostMapping("/modify/back")
    public Object modifyBack(@RequestParam("image") MultipartFile imgFile, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        try {
            String fileName = FileUtil.fileUpload(imgFile);
            Integer userID = (Integer) request.getAttribute("userID");
            String oldFileName = userService.queryOldBackImg(userID);
            FileUtil.fileDelete(oldFileName);
            jsonObject.put("fileName",fileName);
            if(userService.alterBackImg(userID, fileName)==1) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (IOException e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }

        return jsonObject;
    }

    /**更新用户信息*/
    @TokenPromise
    @PutMapping("/")
    public Object modifyUserInfo(@RequestBody User user,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID = (Integer) request.getAttribute("userID");
        user.setId(userID);

        if (userService.alterUserInfo(user)==1) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }

    /**更新用户头像*/
    @TokenPromise
    @PostMapping("/modify/icon")
    public Object modifyIcon(@RequestParam("image") MultipartFile imgFile, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        try {
            String fileName = FileUtil.fileUpload(imgFile);
            Integer userID = (Integer) request.getAttribute("userID");
            String oldFileName = userService.queryOldIcon(userID);
            FileUtil.fileDelete(oldFileName);
            jsonObject.put("fileName",fileName);
            if(userService.alterIcon(userID, fileName)==1) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (IOException e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }

        return jsonObject;
    }

    /**更新用户密码*/
    @TokenPromise
    @PutMapping("/modify/pwd/modify/pwd")
    public Object modifyPassword(@RequestBody Map<String,String>map,HttpServletRequest request){
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        Integer userID = (Integer) request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        if (userService.alterPassword(userID,oldPwd,newPwd)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }
}
