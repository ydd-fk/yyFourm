package com.app.dao;

import com.app.entity.Comment;
import com.app.entity.CommentReply;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommentDao {

    /**
     * 根据帖子ID查找评论
     * @param postsID
     * @return
     */
    @Results({
            @Result(property = "id",column = "cID",id = true),
            @Result(property = "userID",column = "uID"),
            @Result(property = "postsID",column = "pID"),
            @Result(property = "content",column = "cContent"),
            @Result(property = "replayCount",column = "repCount"),
            @Result(property = "time",column = "cTime"),
            @Result(property = "likes",column = "cLikes"),
            @Result(property = "userIcon",column = "uIcon"),
            @Result(property = "userName",column = "uName"),
            @Result(property = "replyList",column ="cID", many = @Many(select = "com.app.dao.CommentDao.selectCommentReplyLimitTen")),
    })
    @Select("SELECT c.*,uName,uIcon\n" +
            " FROM `comment` c\n" +
            " JOIN `user` u ON c.uID=u.uID" +
            " WHERE c.pID=#{id}")
    List<Comment> selectComments(int postsID);

    /**
     * 查找20条评论回复
     * @param cID
     * @return
     */
    @Results({
            @Result(property = "id",column = "rID",id = true),
            @Result(property = "fromUserID",column = "fromUID"),
            @Result(property = "toUserID",column = "toUID"),
            @Result(property = "fromUserName",column = "uFromName"),
            @Result(property = "toUserName",column = "uToName"),
            @Result(property = "content",column = "rContent"),
            @Result(property = "time",column = "rTime"),
            @Result(property = "likes",column = "rLikes"),
    })
    @Select("SELECT cr.*,u_from.uName uFromName,u_to.uName uToName\n" +
            " FROM comment_reply cr\n" +
            " JOIN `user` u_from ON cr.fromUID=u_from.uID\n" +
            " LEFT JOIN `user` u_to ON cr.toUID=u_to.uID\n" +
            " WHERE cID=#{id}" +
            " LIMIT 0,20")
    List<CommentReply> selectCommentReplyLimitTen(int cID);

    /**
     * 根据评论ID查找所以评论回复
     * @param cID
     * @return
     */
    @Select("SELECT cr.*,u_from.uName uFromName,u_to.uName uToName\n" +
            " FROM comment_reply cr\n" +
            " JOIN `user` u_from ON cr.fromUID=u_from.uID\n" +
            " LEFT JOIN `user` u_to ON cr.toUID=u_to.uID\n" +
            " WHERE cID=#{id}")
    List<CommentReply> selectCommentReply(int cID);

    /**
     * 根据ID查询CommentReply
     * @param id
     * @return
     */
    @Select("SELECT * FROM `comment_reply` WHERE rID=#{id}")
    CommentReply selectCommentReplyByID(int id);

    /**
     * 根据ID查询Comment
     * @param id
     * @return
     */
    @Select("SELECT * FROM `comment` WHERE cID=#{id}")
    Comment selectCommentByID(int id);

    /**
     * 查询用户在帖子中点赞的评论
     * @param pID
     * @param uID
     * @return
     */
    @Select("SELECT c.cID\n" +
            " FROM `comment` c\n" +
            " JOIN comment_likes cl ON c.cID = cl.cID\n" +
            " WHERE pID=#{pID} AND cl.uID=#{uID}")
    int[] selectCommentLikes(int pID,int uID);

    /**
     * 查询用户在帖子中点赞的回复
     * @param pID
     * @param uID
     * @return
     */
    @Select("SELECT crl.rID\n" +
             "FROM `comment_reply` cr\n" +
            " JOIN comment_reply_likes crl ON cr.rID = crl.rID\n" +
            " JOIN `comment` c ON cr.cID=c.cID\n" +
            " WHERE pID=#{pID} AND crl.uID=#{uID}")
    int[] selectCommentReplyLikes(int pID,int uID);

    /**
     * 用户评论帖子
     * @param comment (userID,postsID,content)
     * @return
     */
    @Insert("INSERT INTO `comment` SET uID=#{c.userID},pID=#{c.postsID},cContent=#{c.content},cTime=#{c.time}")
    @Options(useGeneratedKeys = true,keyColumn = "cID",keyProperty = "c.id")
    Integer insertComment(@Param("c") Comment comment);

    /**
     * 用户回复评论
     * @param commentReply
     * @param commentID
     * @return
     */
    @Insert("INSERT INTO `comment_reply` SET cID=#{cID},rContent=#{cr.content},fromUID=#{cr.fromUserID},toUID=#{cr.toUserID}")
    @Options(useGeneratedKeys = true,keyColumn = "rID",keyProperty = "cr.id")
    Integer insertCommentReply(@Param("cr")CommentReply commentReply,@Param("cID")Integer commentID);

    /**
     * 用户回复评论之后，评论的回复数自增1
     * @param commentID
     * @return
     */
    @Update("UPDATE `comment` SET repCount=repCount+1 WHERE cID=#{id}")
    Integer updateIncOneRepCount(int commentID);

    /**
     * 用户删除回复评论之后，评论的回复数自减1
     * @param commentID
     * @return
     */
    @Update("UPDATE `comment` SET repCount=repCount-1 WHERE cID=#{id}")
    Integer updateSubOneRepCount(int commentID);

    /**
     * 用户点赞评论后，评论的点赞数自增1
     * @param commentID
     * @return
     */
    @Update("UPDATE `comment` SET cLikes=cLikes+1 WHERE cID=#{id}")
    Integer updateIncOneCommentLikes(int commentID);

    /**
     * 用户取消点赞评论后，评论的点赞数自减1
     * @param commentID
     * @return
     */
    @Update("UPDATE `comment` SET cLikes=cLikes-1 WHERE cID=#{id}")
    Integer updateSubOneCommentLikes(int commentID);


    /**
     * 用户点赞回复后，回复的点赞数自增1
     * @param replyID
     * @return
     */
    @Update("UPDATE `comment_reply` SET rLikes=rLikes+1 WHERE rID=#{id}")
    Integer updateIncOneCommentReplyLikes(int replyID);

    /**
     * 用户取消点赞回复后，回复的点赞数自减1
     * @param replyID
     * @return
     */
    @Update("UPDATE `comment_reply` SET rLikes=rLikes-1 WHERE rID=#{id}")
    Integer updateSubOneCommentReplyLikes(int replyID);

    /**
     * 用户点赞评论
     * @param userID
     * @param commentID
     * @return
     */
    @Insert("INSERT INTO comment_likes SET uID=#{uID},cID=#{cID}")
    Integer insertCommentLike(@Param("uID")int userID,@Param("cID")int commentID);

    /**
     * 用户点赞回复
     * @param userID
     * @param replyID
     * @return
     */
    @Insert("INSERT INTO comment_reply_likes SET uID=#{uID},rID=#{rID}")
    Integer insertCommentReplyLike(@Param("uID")int userID,@Param("rID")int replyID);

    /**
     * 取消点赞后删除点赞记录
     * @param userID
     * @param commentID
     * @return
     */
    @Delete("DELETE FROM comment_likes WHERE uID=#{uID} AND cID=#{cID}")
    Integer deleteCommentLike(@Param("uID")int userID,@Param("cID")int commentID);

    /**
     * 删除评论（注意设置级联删除）
     * @param commentID
     * @param userID
     * @return
     */
    @Delete("DELETE FROM `comment` WHERE cID=#{cID} AND uID=#{uID}")
    Integer deleteComment(@Param("cID")int commentID,@Param("uID")int userID);

    /**
     * 删除回复（注意添加级联删除）
     * @param replyID
     * @param userID
     * @return
     */
    @Delete("DELETE FROM `comment_reply` WHERE rID=#{rID} AND fromuID=#{uID}")
    Integer deleteCommentReply(@Param("rID")int replyID,@Param("uID")int userID);

    /**
     * 取消点赞
     * @param replyID
     * @param userID
     * @return
     */
    @Delete("DELETE FROM `comment_reply_likes` WHERE uID=#{uID} AND rID=#{rID}")
    Integer deleteCommentReplyLike(@Param("rID") int replyID,@Param("uID") int userID);


}
