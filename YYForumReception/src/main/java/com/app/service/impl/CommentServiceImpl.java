package com.app.service.impl;

import com.app.dao.CommentDao;
import com.app.dao.PostsDao;
import com.app.entity.Comment;
import com.app.entity.CommentReply;
import com.app.entity.PageRequest;
import com.app.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;
    @Autowired
    PostsDao postsDao;

    @Override
    public Object queryCommentList(PageRequest pageRequest,int postsID) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Comment> comments = commentDao.selectComments(postsID);
        return new PageInfo<>(comments);
    }

    @Override
    @Transactional
    public boolean addComment(Comment comment) {
        if (commentDao.insertComment(comment)==0) return false;
        postsDao.postsIncOneComments(comment.getPostsID());
        return true;
    }

    @Override
    @Transactional
    public boolean addCommentReply(CommentReply commentReply, int commentID) {
        if (commentDao.insertCommentReply(commentReply,commentID)==0) return false;
        commentDao.updateIncOneRepCount(commentID);
        return true;
    }

    @Override
    @Transactional
    public boolean likeComment(int commentID,int userID) {
        if(commentDao.insertCommentLike(userID,commentID)<=0) return false;
        commentDao.updateIncOneCommentLikes(commentID);
        return true;
    }

    @Override
    @Transactional
    public boolean cancelLikeComment(int commentID, int userID) {
        if (commentDao.deleteCommentLike(userID,commentID)<=0) return false;
        commentDao.updateSubOneCommentLikes(commentID);
        return true;
    }

    @Override
    public int[] queryCommentLikes(int postsID, int userID) {
        return commentDao.selectCommentLikes(postsID,userID);
    }

    @Override
    public int[] queryCommentReplyLikes(int postsID, int userID) {
        return commentDao.selectCommentReplyLikes(postsID,userID);
    }

    @Override
    public boolean delComment(int commentID, int userID) {
        return commentDao.deleteComment(commentID,userID)>=1;
    }

    @Override
    public boolean delCommentReply(int replyID, int userID) {
        return commentDao.deleteCommentReply(replyID,userID)>=1;
    }

    @Override
    public boolean likeCommentReply(int replyID, int userID) {
        if(commentDao.insertCommentReplyLike(userID,replyID)<=0) return false;
        commentDao.updateIncOneCommentReplyLikes(replyID);
        return true;
    }

    @Override
    public boolean cancelLikeCommentReply(int replyID, int userID) {
        if(commentDao.deleteCommentReplyLike(replyID,userID)<=0) return false;
        commentDao.updateSubOneCommentReplyLikes(replyID);
        return true;
    }
}
