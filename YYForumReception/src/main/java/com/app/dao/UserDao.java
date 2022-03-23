package com.app.dao;

import com.app.entity.User;
import com.github.pagehelper.ISelect;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    /**
     * 检查用户是否存在
     * @param user
     * @return
     */
    @Select("SELECT * FROM `user` WHERE uAccount=#{user.account} AND uPassword=#{user.password}")
    @Results(id = "UserMap",
            value = {
            @Result(column = "uID",property = "id",id = true),
            @Result(column = "uAccount",property = "account"),
            @Result(column = "uPassword",property = "password"),
            @Result(column = "uIcon",property = "icon"),
            @Result(column = "uName",property = "name"),
            @Result(column = "uRegTime",property = "regTime"),
            @Result(column = "isFriend",property = "isFriend"),
            @Result(column = "uSign",property = "sign"),
            @Result(column = "uBackImg",property = "backImg"),
            @Result(column = "uSex",property = "sex"),
            @Result(column = "uBirthday",property = "birthday"),
    })
    public User checkUser(@Param("user") User user);

    @Update("UPDATE `user` set uName=#{user.name} WHERE uid=#{user.id}")
    Integer updateUserName(@Param("user") User user);


    /**
     * 添加用户
     * @param user
     * @return
     */
    @Insert("INSERT INTO `user` SET uAccount=#{user.account},uPassword=#{user.password}")
    @Options(useGeneratedKeys = true,keyProperty = "user.id",keyColumn = "uID")
    public void insertUser(@Param("user") User user);

    /**
     * 根据用户ID查询用户信息
     * @param id
     */
    @Select("SELECT uID,uIcon,uName,uRegTime,uSign,uBackImg,uSex,uBirthday FROM `user` WHERE uID=#{id}")
    @ResultMap("UserMap")
    public User selectUserByID(int id);

    /**
     * 根据用户ID查询当前用户的粉丝
     * @param id
     * @return
     */
    @Select("select a.fansID uID,uName,uIcon,uSign,\n" +
            " if(b.fansID is not null, 1, 0) as isFriend\n" +
            "from user_follow_user a\n" +
            "left join user_follow_user b on a.fansID = b.uID and a.uID = b.fansID\n" +
            "JOIN `user` u ON a.fansID=u.uid\n" +
            "WHERE a.uID=#{id}")
    @ResultMap("UserMap")
    public List<User> selectFans(int id);

    /**
     * 根据用户ID查询当前用户关注的人
     * @param id
     * @return
     */
    @Select("select a.uID uID,uName,uIcon,uSign,\n" +
            "if(b.fansID is not null, 1, 0) as isFriend\n" +
            "from user_follow_user a\n" +
            "left join user_follow_user b on a.fansID = b.uID and a.uID = b.fansID\n" +
            "JOIN `user` u ON a.uID=u.uid\n" +
            "WHERE a.fansID=#{id}")
    @ResultMap("UserMap")
    public List<User> selectFollows(int id);

    /**
     * 用户关注用户
     * @param uID
     * @param fansID
     * @return
     */
    @Insert("INSERT INTO user_follow_user (uID,fansID) VALUES(#{uID},#{fansID})")
    public Integer insertFollowsUser(@Param("uID") int uID,@Param("fansID")int fansID);


    /**
     * 用户取关用户
     * @param uID
     * @param fansID
     * @return
     */
    @Delete("DELETE FROM user_follow_user WHERE uID=#{uID} AND fansID=#{fansID}")
    public Integer deleteFollowsUser(@Param("uID") int uID,@Param("fansID")int fansID);

    @Select("SELECT COUNT(fuID)\n" +
            "FROM user_follow_user\n" +
            "WHERE fansID=#{fansID} AND uID=#{uID}")
    public Integer selectIsFriend(@Param("fansID")int fansID,@Param("uID") int uID);

    /**
     * 查找用户关注的人数
     * @param userID
     * @return
     */
    @Select("SELECT COUNT(fuID)\n" +
            "FROM user_follow_user\n" +
            "WHERE fansID=#{id}")
    int selectFollowsTotal(int userID);

    /**
     * 查找用户的粉丝数
     * @param userID
     * @return
     */
    @Select("SELECT COUNT(fuID)\n" +
            "FROM user_follow_user\n" +
            "WHERE uID=#{id}")
    int selectFansTotal(int userID);

    /**
     * 修改用户签名
     * @param user
     * @return
     */
    @Update("UPDATE `user` set uSign=#{user.sign} WHERE uid=#{user.id}")
    Integer updateSign(@Param("user") User user);


    /**
     * 修改用户背景图
     * @param user
     * @return
     */
    @Update("UPDATE `user` set uBackImg=#{user.backImg} WHERE uid=#{user.id}")
    Integer updateBackImg(@Param("user") User user);

    /**
     * 查询旧图
     * @param userID
     * @return
     */
    @Select("select uBackImg from user where uID=#{userID}")
    String selectOldBackImg(int userID);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Update({"<script>",
            "UPDATE `user` SET ",
            "<if test='user.birthday != null'>",
            "uBirthday=#{user.birthday}," ,
            "</if>",
            "uName=#{user.name}," +
            "uSex=#{user.sex} " +
            "WHERE uID=#{user.id}",
            "</script>"})
    Integer updateUserInfo(@Param("user") User user);

    /**
     * 查看用户头像
     * @param userID
     * @return
     */
    @Select("select uIcon from user where uID=#{userID}")
    String selectOldIcon(int userID);

    /**
     * 修改用户背景图
     * @param user
     * @return
     */
    @Update("UPDATE `user` set uIcon=#{user.icon} WHERE uid=#{user.id}")
    Integer updateIcon(@Param("user") User user);


    /**
     * 用户修改密码
     * @param user
     * @param newPassword
     * @return
     */
    @Update("UPDATE `user` set uPassword=#{newPwd} WHERE uid=#{user.id} AND uPassword=#{user.password}")
    Integer updatePassword(@Param("user") User user,@Param("newPwd")String newPassword);

    /**
     * 查询用户关注的某用户粉丝列表
     * @param otherUserID
     * @param userID
     * @return
     */
    @Select("SELECT uID\n" +
            "FROM user_follow_user\n" +
            "WHERE fansID=#{uID} AND uID in(SELECT fansID\n" +
            "FROM user_follow_user\n" +
            "WHERE uID=#{otherID})")
    int[] selectUserFollowOtherUserFans(@Param("otherID") int otherUserID,@Param("uID") int userID);

    /**
     * 查询用户与用户的共同关注
     * @param otherUserID
     * @param userID
     * @return
     */
    @Select("SELECT uID\n" +
            "FROM user_follow_user\n" +
            "WHERE fansID=#{uID} AND uID in(SELECT uID\n" +
            "FROM user_follow_user\n" +
            "WHERE fansID=#{otherID})")
    int[] selectUserFollowOtherUserFollow(@Param("otherID") int otherUserID,@Param("uID") int userID);
}
