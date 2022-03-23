package com.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.annotation.TokenPromise;
import com.app.entity.Comment;
import com.app.entity.CommentReply;
import com.app.entity.PageRequest;
import com.app.service.CommentService;
import com.app.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RequestMapping(value = "/comment",produces = "application/json;charset=UTF-8")
@ResponseBody
@RestController
class CommentController {
    @Autowired
    CommentService commentService;

    /**查看帖子详情*/
    @GetMapping("/posts/{id}/")
    public Object getComment(@PathVariable("id") int postsID, PageRequest pageRequest){
        return commentService.queryCommentList(pageRequest,postsID);
    }
    /**删除评论*/
    @TokenPromise
    @DeleteMapping("/{id}")
    public Object delComment(@PathVariable("id")int commentID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer)request.getAttribute("userID");
        if(commentService.delComment(commentID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }

    /**删除回复*/
    @TokenPromise
    @DeleteMapping("/reply/{id}")
    public Object delCommentReply(@PathVariable("id")int replyID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer)request.getAttribute("userID");
        if(commentService.delCommentReply(replyID,userID)) jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        else jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        return jsonObject;
    }

    /**查询点赞的评论和回复ID列*/
    @TokenPromise
    @GetMapping("/like/{id}")
    public Object getCommentLike(@PathVariable("id") int postsID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer)request.getAttribute("userID");
        int[] commentLikesArr = commentService.queryCommentLikes(postsID, userID);
        int[] commentReplyLikesArr = commentService.queryCommentReplyLikes(postsID, userID);
        jsonObject.put("commentLikesArr",commentLikesArr);
        jsonObject.put("commentReplyLikesArr",commentReplyLikesArr);
        return jsonObject;
    }

    /**添加评论*/
    //postsID,content
    @TokenPromise
    @PostMapping("/")
    public Object addComment(@RequestBody Comment comment, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer userID =(Integer)request.getAttribute("userID");
        comment.setUserID(userID);
        comment.setTime(new Date());
        if(commentService.addComment(comment)){
            jsonObject.put("code", ResponseCode.SUCCESS_CODE);
            jsonObject.put("comment", comment);
        }else {
            jsonObject.put("code", ResponseCode.DEFAULT_CODE);
            jsonObject.put("message", "发送评论失败");

        }
        return jsonObject;
    }

    /**回复用户*/
    //cID,content,fromUserID,toUserID
    @TokenPromise
    @PostMapping("/reply")
    public Object addCommentReply(@RequestBody Map<String,Object> map, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer commentID = (Integer) map.get("commentID");
        CommentReply commentReply = JSON.parseObject(JSON.toJSONString(map.get("commentReply")), CommentReply.class);
        Integer userID =(Integer)request.getAttribute("userID");
        commentReply.setFromUserID(userID);
        if(commentService.addCommentReply(commentReply,commentID)){
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
            jsonObject.put("commentReply",commentReply);
        }else{
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
            jsonObject.put("message","回复失败");
        }

        return jsonObject;
    }

    /**点赞评论*/
    @TokenPromise
    @PutMapping("/like/{id}")
    public Object likeComment(@PathVariable("id") int commentID,HttpServletRequest request){
        Integer userID =(Integer)request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        if(commentService.likeComment(commentID,userID)){
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        }else{
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**取消点赞评论*/
    @TokenPromise
    @DeleteMapping("/like/{id}")
    public Object cancelLikeComment(@PathVariable("id") int commentID,HttpServletRequest request){
        Integer userID =(Integer)request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        if(commentService.cancelLikeComment(commentID,userID)){

            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        }else{

            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**点赞评论*/
    @TokenPromise
    @PutMapping("/reply/like/{id}")
    public Object likeCommentReply(@PathVariable("id") int replyID,HttpServletRequest request){
        Integer userID =(Integer)request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        if(commentService.likeCommentReply(replyID,userID)){
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        }else{
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

    /**取消点赞评论*/
    @TokenPromise
    @DeleteMapping("/reply/like/{id}")
    public Object cancelLikeCommentReply(@PathVariable("id") int replyID,HttpServletRequest request){
        Integer userID =(Integer)request.getAttribute("userID");
        JSONObject jsonObject = new JSONObject();
        if(commentService.cancelLikeCommentReply(replyID,userID)){
            jsonObject.put("code",ResponseCode.SUCCESS_CODE);
        }else{
            jsonObject.put("code",ResponseCode.DEFAULT_CODE);
        }
        return jsonObject;
    }

}
