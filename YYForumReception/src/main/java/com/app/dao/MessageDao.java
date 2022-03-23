package com.app.dao;

import com.app.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MessageDao {

    @Results(id = "MessageMap",value = {
            @Result(id = true,property = "id",column = "msID"),
            @Result(property = "txt",column = "msTxt"),
            @Result(property = "time",column = "msTime"),
            @Result(property = "toUserID",column = "to_uID"),
            @Result(property = "isRead",column = "msIsRead"),
            @Result(property = "fromUser.id",column = "from_uID"),
            @Result(property = "fromUser.icon",column = "uIcon"),
            @Result(property = "fromUser.name",column = "uName"),
            })
    @Select("select msID,\n" +
            "IF(from_uID=${userID},to_user.uIcon,from_user.uIcon) uIcon,\n" +
            "IF(from_uID=${userID},to_uID,from_uID) from_uID,\n" +
            "IF(from_uID=${userID},to_user.uName,from_user.uName) uName,\n" +
            "msTxt,\n" +
            "msTime,\n" +
            "msIsRead,\n" +
            "to_uID\n" +
            "from message\n" +
            "JOIN `user` from_user on from_uID=from_user.uID\n" +
            "JOIN `user` to_user on to_uID=to_user.uID\n" +
            "where (least(from_uID, to_uID), greatest(from_uID, to_uID), msTime)       \n" +
            "in(\n" +
            "    select \n" +
            "       least(from_uID, to_uID) as x, greatest(from_uID, to_uID) as y, \n" +
            "       max(msTime) as msTime\n" +
            "    from message \n" +
            "    group by x, y\n" +
            ") AND (from_uID=#{userID} OR to_uID=#{userID})" +
            "ORDER BY msTime DESC")
    List<Message> selectMessagesByUserID(@Param("userID") int userID);


    @Insert("INSERT INTO message SET " +
            "from_uid=#{message.fromUser.id}," +
            "to_uID=#{message.toUserID}," +
            "msTxt=#{message.txt}")
    @Options(useGeneratedKeys = true,keyProperty = "message.id",keyColumn = "msID")
    Integer insertMessage(@Param("message") Message message);

    @Select("SELECT *\n" +
            "FROM message\n" +
            "WHERE (to_uID=#{toUID} AND from_uID=#{fromUID}) or (to_uID=#{fromUID} AND from_uID=#{toUID})\n" +
            "ORDER BY msTime DESC")
    @ResultMap("MessageMap")
    List<Message> selectHistoryMessage(@Param("fromUID")int from_userID,@Param("toUID")int to_userID);

    @Update("UPDATE message SET msIsRead=1 WHERE (to_uID=#{toUID} AND from_uID=#{fromUID}) or (to_uID=#{fromUID} AND from_uID=#{toUID})")
    void updateIsRead(@Param("fromUID")int from_userID,@Param("toUID")int to_userID);


}
