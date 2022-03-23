package com.app.dao;

import com.app.entity.PostsIcon;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostsIconDao {

    /**
     * 根据帖子ID查询帖子所有图片
     * @param id
     * @return
     */
    @Results(id = "iconMap",
            value = {
            @Result(property = "id",column = "icID",id = true),
            @Result(property = "index",column = "icIndex"),
            @Result(property = "url",column = "icUrl")
    })
    @Select("SELECT * FROM postsicon WHERE pID=#{id}")
    public List<PostsIcon> selectIconListByPostID(int id);

    /**
     * 根据用户id以及图片id数组查询满足条件的图片信息
     * @param userID
     * @param idList
     * @return
     */
    @Select({"<script>",
            "SELECT icID,icUrl\n" +
            "FROM postsicon pi\n" +
            "JOIN posts p on p.pID=pi.pID\n" +
            "JOIN `user` u on p.uID=u.uID\n" +
            "WHERE u.uID=#{userID} AND icID in\n",
            "<foreach collection='idList' item='id' open='(' close=')' separator=','>",
            "#{id}",
            "</foreach>",
            "</script>"})
    @ResultMap("iconMap")
    public List<PostsIcon> selectIcons(@Param("userID") int userID,@Param("idList") int[] idList);



    /**
     * 批量添加图片
     * @param postsIconList
     * @param postsID
     * @return
     */
    @Insert({"<script>",
            "INSERT INTO postsicon (pID,icIndex,icUrl) VALUES ",
            "<foreach collection='postsIconList' item='icon' index='index' separator=','>",
            "(#{pID},#{icon.index},#{icon.url})",
            "</foreach>",
            "</script>"})
    public Integer insertIcons(@Param("postsIconList") List<PostsIcon> postsIconList,@Param("pID")int postsID);

    /**
     * 根据图片ID批量删除图片
     * @param iconNameArr
     * @return
     */
    @Delete({"<script>",
            "DELETE FROM postsicon WHERE icUrl in",
            "<foreach collection='iconNameArr' item='name' open='(' close=')' separator=','>",
            "#{name}",
            "</foreach>",
            "</script>"})
    public Integer deleteIcons(@Param("iconNameArr") String[]iconNameArr);

    /**
     * 根据帖子ID清除图片
     * @param postsID
     * @return
     */
    @Delete("DELETE FROM `postsicon` WHERE pID=#{pID}")
    public Integer deleteIconsByPostsID(@Param("pID")int postsID);

    /**
     * 根据图片名称和帖子ID查询数量
     * @param postsID
     * @param iconNameArr
     * @return
     */
    @Select({"<script>",
            "SELECT COUNT(icID)\n" +
            "FROM postsicon\n" +
            "WHERE pID=#{pID} AND icUrl in",
            "<foreach collection='iconNameArr' item='name' open='(' close=')' separator=','>",
            "#{name}",
            "</foreach>",
            "</script>"})
    public Integer selectIconsCount(@Param("pID")int postsID,@Param("iconNameArr") String[]iconNameArr);


}
