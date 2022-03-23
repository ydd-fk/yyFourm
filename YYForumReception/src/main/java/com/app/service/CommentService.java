package com.app.service;

import com.app.entity.Comment;
import com.app.entity.CommentReply;
import com.app.entity.PageRequest;
import com.app.entity.Posts;

public interface CommentService {
    /**
     * 分页帖子评论
     * @param pageRequest
     * @param postsID
     * @return
     */
    public Object queryCommentList(PageRequest pageRequest,int postsID);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    public boolean addComment(Comment comment);

    /**
     * 添加回复
     * @param commentReply
     * @param commentID
     * @return
     */
    public boolean addCommentReply(CommentReply commentReply, int commentID);


    public boolean likeComment(int commentID,int userID);

    public boolean cancelLikeComment(int commentID,int userID);

    /**
     * 查询点赞的评论ID
     * @param postsID
     * @param userID
     * @return
     */
    public int[] queryCommentLikes(int postsID,int userID);
    /**
     * 查询点赞的回复ID
     * @param postsID
     * @param userID
     * @return
     */
    public int[] queryCommentReplyLikes(int postsID,int userID);

    /**
     * 删除评论
     * @param commentID
     * @param userID
     * @return
     */
    public boolean delComment(int commentID,int userID);

    /**
     * 删除回复
     * @param replyID
     * @param userID
     * @return
     */
    public boolean delCommentReply(int replyID,int userID);

    /**
     * 点赞回复
     * @param replyID
     * @param userID
     * @return
     */
    public boolean likeCommentReply(int replyID,int userID);

    /**
     * 取消点赞回复
     * @param replyID
     * @param userID
     * @return
     */
    public boolean cancelLikeCommentReply(int replyID,int userID);

}
