package com.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.app.annotation.TokenPromise;
import com.app.entity.PageRequest;
import com.app.entity.Posts;
import com.app.entity.PostsIcon;
import com.app.entity.User;
import com.app.service.PostsService;
import com.app.service.UserService;
import com.app.util.FileUtil;
import com.app.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/posts",produces = "application/json;charset=UTF-8")
@ResponseBody
@RestController
public class PostsController {
    @Autowired
    private PostsService postsService;
    @Autowired
    private UserService userService;

    /**最新帖子列表*/
    @GetMapping("/")
    public Object getPostsList(PageRequest pageRequest){
        return postsService.queryPosts(pageRequest);
    }

    /**用户所收藏的贴子*/
    @GetMapping("/follow/{id}")
    public Object getFollowPostsList(PageRequest pageRequest, @PathVariable("id")int userID){
        return postsService.queryFollowPostsByUserID(pageRequest,userID);
    }

    /**按照点赞最高排序帖子*/
    @GetMapping("/bestlikes")
    public Object getOrderByPosts(Integer pageSize){
        PageRequest pageRequest = new PageRequest(1, pageSize);
        return postsService.queryPostsOrderByLikes(pageRequest);
    }

    /**用户所有帖子列表*/
    @GetMapping("/user/{id}")
    public Object getPostsListForUser(PageRequest pageRequest, @PathVariable("id")int userID){
        return postsService.queryPostsByUserID(pageRequest,userID);
    }

    /**查看帖子详情*/
    @GetMapping("/{id}")
    public Object getPosts(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");
        Posts posts = postsService.queryPostsByID(postsID);
        Integer isFollowUser = 0;
        Integer isLikePosts = 0;
        Integer isFollowPosts = 0;

        if (userID!=null){
            Integer pID = posts.getId();
            isFollowUser = userService.queryIsFriend(userID, posts.getUser().getId());
            isLikePosts = postsService.isLike(userID,pID);
            isFollowPosts = postsService.isFollow(userID,pID);
        }
        jsonObject.put("isFollowUser",isFollowUser);
        jsonObject.put("isLikePosts",isLikePosts);
        jsonObject.put("isFollowPosts",isFollowPosts);
        jsonObject.put("posts",posts);

        return jsonObject;
    }


    /**点赞帖子*/
    @TokenPromise
    @PostMapping("/like/{id}")
    public Object likePosts(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");
        try {
            if (postsService.likesPosts(postsID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**取消点赞帖子*/
    @TokenPromise
    @DeleteMapping("/like/{id}")
    public Object cancelLikePosts(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");

        try {
            if (postsService.noLikesPosts(postsID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**收藏帖子*/
    @TokenPromise
    @PostMapping("/follow/{id}")
    public Object followPosts(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");

        try {
            if (postsService.followPosts(postsID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**取消收藏帖子*/
    @TokenPromise
    @DeleteMapping("/follow/{id}")
    public Object cancelFollowPosts(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");
        try {
            if (postsService.noFollowPosts(postsID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**删除帖子*/
    @TokenPromise
    @DeleteMapping("/{id}")
    public Object delPost(@PathVariable("id")int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");
        if (postsService.removePosts(postsID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }

    /**添加帖子  title detail icons */
    @TokenPromise
    @PostMapping("/")
    public Object addPost(@RequestParam("detail") String detail,@RequestParam("title") String title,@RequestParam(value = "icons",required = false) MultipartFile[] imgFile,HttpServletRequest request) throws IOException {
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer) request.getAttribute("userID");
        User user = new User();
        user.setId(userID);
        Posts posts = new Posts();
        posts.setDetail(detail);
        posts.setTitle(title);
        posts.setUser(user);
        List iconsList = new ArrayList<>();
        if (imgFile!=null){
            for (int i=0;i<imgFile.length;i++) {
                String fileName = FileUtil.fileUpload(imgFile[i]);
                PostsIcon postsIcon = new PostsIcon();
                postsIcon.setUrl(fileName);
                postsIcon.setIndex(i+1);
                iconsList.add(postsIcon);
            }
        }
        posts.setIcons(iconsList);
        try {
            if (postsService.addPosts(posts)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            e.printStackTrace();
        }
        return jsonObject;

    }

    /**修改帖子*/
    @TokenPromise
    @PutMapping("/")
    public Object modifyPosts(@RequestParam("postsID") int postsID,
                              @RequestParam("detail") String detail,
                              @RequestParam("title") String title,
                              @RequestParam(value = "delIcons",required = false) String[] delIcons,
                              @RequestParam(value = "newIcons",required = false) MultipartFile[] imgFile,
                              HttpServletRequest request) {
        Integer userID =(Integer) request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        Posts posts = new Posts();
        posts.setId(postsID);
        posts.setDetail(detail);
        posts.setTitle(title);
        try {
            if(postsService.alterPosts(posts,userID,delIcons,imgFile)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }catch (Exception e){
            jsonObject.put("code",ResponseCode.ERROR_CODE);
        }

        return jsonObject;
    }

    /**搜索帖子*/
    @GetMapping("/search/{key}")
    public Object findSearchKeyPosts(PageRequest pageRequest,@PathVariable("key") String searchKey){
        return postsService.queryPostsBySearchKey(pageRequest,searchKey);
    }
}
