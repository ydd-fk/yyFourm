package com.app.dao;

import com.app.entity.Posts;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostsDao {

    /**
     * 查看最新的帖子与作者信息
     * @return
     */
    @Results(id = "PostsMap",value = {
            @Result(id = true,property = "id",column = "pID"),
            @Result(property = "title",column = "pTitle"),
            @Result(property = "detail",column = "pDetail"),
            @Result(property = "time",column = "pTime"),
            @Result(property = "likes",column = "pLikes"),
            @Result(property = "collections",column = "pCollections"),
            @Result(property = "comments",column = "pComments"),
            @Result(property = "user",column = "uID",one = @One(select = "com.app.dao.UserDao.selectUserByID")),
            @Result(property = "icons",column = "pID",many = @Many(select = "com.app.dao.PostsIconDao.selectIconListByPostID")),
    })
    @Select("SELECT u.uID uID,uIcon,uName,pID,pTitle,pDetail,pTime,pLikes,pCollections,pComments" +
            " FROM `posts` p " +
            " JOIN `user` u ON p.uID=u.uID" +
            " order by pTime DESC")
    public List<Posts> selectPostsList();

    @Select("SELECT u.uID uID,uIcon,uName,pID,pTitle,pDetail,pTime,pLikes,pCollections,pComments\n" +
            "FROM `posts` p \n" +
            "JOIN `user` u ON p.uID=u.uID\n" +
            "WHERE pID = #{id}")
    @ResultMap("PostsMap")
    public Posts selectPostsByPostsID(int id);

    /**
     * 查询是否用户点赞过某帖子
     * @param userID
     * @param postsID
     * @return
     */
    @Select("SELECT COUNT(plID)\n" +
            "FROM posts_likes\n" +
            "WHERE uID=#{uID} AND pID=#{pID}")
    public Integer selectIsLike(@Param("uID") int userID,@Param("pID") int postsID);

    /**
     * 查询用户是否收藏过某帖子
     * @param userID
     * @param postsID
     * @return
     */
    @Select("SELECT COUNT(fpID)\n" +
            "FROM user_follow_posts\n" +
            "WHERE uID=#{uID} AND pID=#{pID}")
    public Integer selectIsFollow(@Param("uID") int userID,@Param("pID") int postsID);
    /**
     * 添加帖子并返回帖子主键
     * @param posts
     * @return
     */
    @Insert("INSERT INTO posts SET uID=#{posts.user.id},pTitle=#{posts.title},pDetail=#{posts.detail}")
    @Options(useGeneratedKeys = true,keyProperty = "posts.id",keyColumn = "pID")
    public Integer insertPosts(@Param("posts")Posts posts);

    /**
     * 根据帖子ID修改帖子信息
     * @param posts
     * @param userID 根据userID判断用户是否有权修改
     * @return
     */
    @Update("UPDATE posts\n" +
            " SET ptitle=#{posts.title},pDetail=#{posts.detail}\n" +
            " WHERE pID=#{posts.id} AND uID=#{uID}")
    public Integer updatePosts(@Param("posts")Posts posts,@Param("uID")int userID);


    /**
     * 根据帖子ID删除帖子
     * @param postsID
     * @param userID 根据userID判断该帖子是否属于该用户，如果不属于则删除无效
     * @return
     */
    @Delete("DELETE FROM posts WHERE pID=#{pID} AND uID=#{uID}")
    public Integer deletePosts(@Param("pID")int postsID,@Param("uID")int userID);


    /**
     * 根据用户ID查询该用户所有帖子
     * @param userID
     * @return
     */
    @Select("SELECT u.uID uID,uIcon,uName,pID,pTitle,pDetail,pTime,pLikes,pCollections,pComments" +
            " FROM `posts` p " +
            " JOIN `user` u ON p.uID=u.uID" +
            " where p.uID=#{id}"+
            " order by pTime DESC")
    @ResultMap("PostsMap")
    public List<Posts> selectPostsListByUserID(int userID);

    /**
     * 根据用户ID查询用户收藏的帖子
     * @param userID
     * @return
     */
    @Select("SELECT p.pID,pTitle,pDetail,pTime,pLikes,pCollections,pComments\n" +
            "FROM user_follow_posts up\n" +
            "JOIN posts p ON p.pid=up.pid\n" +
            "WHERE up.uID=#{id}")
    @ResultMap("PostsMap")
    public List<Posts> selectFollowPostsByUserID(int userID);


    /**
     * 用户取消收藏帖子
     * @param postsID
     * @param userID
     * @return
     */
    @Delete("DELETE FROM user_follow_posts WHERE uID=#{uID} AND pID=#{pID}")
    public Integer deleteFollowPosts(@Param("pID")int postsID,@Param("uID")int userID);

    /**
     * 调用取消收藏（DeleteFollowPosts）方法后，再调用此方法将帖子收藏数字段自减1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pCollections=pCollections-1 WHERE pID=#{id}")
    public Integer postsSubOneCollections(int postsID);

    /**
     * 用户收藏帖子
     * @param postsID
     * @param userID
     * @return
     */
    @Insert("INSERT INTO user_follow_posts SET uID=#{uID},pID=#{pID}")
    public Integer insertFollowPosts(@Param("pID")int postsID,@Param("uID")int userID);

    /**
     * 调用收藏（InsertFollowPosts）方法后，再调用此方法将帖子收藏数字段自增1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pCollections=pCollections+1 WHERE pID=#{id}")
    public Integer postsIncOneCollections(int postsID);

    /**
     * 用户点赞帖子
     * @param postsID
     * @param userID
     * @return
     */
    @Insert("INSERT INTO posts_likes SET uID=#{uID},pID=#{pID}")
    public Integer insertLikesPosts(@Param("pID")int postsID,@Param("uID")int userID);

    /**
     * 用户调用点赞帖子（insertLikesPosts）方法后，在调用此方法将帖子点赞数字段自增1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pLikes=pLikes+1 WHERE pID=#{id}")
    public Integer postsIncOneLikes(int postsID);

    /**
     * 用户取消点赞帖子
     * @param postsID
     * @param userID
     * @return
     */
    @Delete("DELETE FROM posts_likes WHERE uID=#{uID} AND pID=#{pID}")
    public Integer deleteLikesPosts(@Param("pID")int postsID,@Param("uID")int userID);

    /**
     * 用户调用取消点赞帖子（deleteLikesPosts）方法后，在调用此方法将帖子点赞数字段自减1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pLikes=pLikes-1 WHERE pID=#{id}")
    public Integer postsSubOneLikes(int postsID);


    /**
     * 用户删除评论后评论数自减1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pComments=pComments-1 WHERE pID=#{id}")
    public Integer postsSubOneComments(int postsID);

    /**
     * 用户评论后评论数自增1
     * @param postsID
     * @return
     */
    @Update("UPDATE posts SET pComments=pComments+1 WHERE pID=#{id}")
    public Integer postsIncOneComments(int postsID);

    /**
     * 查询帖子是否属于某用户
     * @param postsID
     * @param userID
     * @return
     */
    @Select("SELECT COUNT(pID)\n" +
            "FROM posts\n" +
            "WHERE pID=#{pID} AND uID=#{uID}")
    public Integer selectPostsIsForUser(@Param("pID") int postsID,@Param("uID") int userID);

    /**
     * 按照点赞数排序查找帖子
     * @return
     */
    @Select("SELECT pID,pTitle,pLikes FROM posts ORDER BY pLikes DESC")
    @ResultMap("PostsMap")
    public List<Posts> selectPostsOrderByLikes();

    /**
     * 模糊查询帖子
     * @param searchKey
     * @return
     */
    @Select("SELECT u.uID uID,uName,pID,pTitle,pDetail,pTime,pLikes,pCollections,pComments\n" +
            "FROM `posts` p \n" +
            "JOIN `user` u ON p.uID=u.uID\n" +
            "WHERE LOCATE(#{searchKey}, pTitle)>0 OR LOCATE(#{searchKey}, pDetail)>0 ")
    @ResultMap("PostsMap")
    public List<Posts> selectPostsLikeSearchKey(String searchKey);

}
