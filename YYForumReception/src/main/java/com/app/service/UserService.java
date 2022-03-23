package com.app.service;

import com.app.entity.PageRequest;
import com.app.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    /**
     * 根据account、password查询用户是否存在
     * @param user
     * @return
     */
    User findUser(User user);

    void initUserName(User user);

    /**
     * 添加用户
     * @param user
     * @return
     */
    void addUser(User user);

    /**
     * 查询用户粉丝
     * @param userID
     * @return
     */
    PageInfo<User> queryFans(PageRequest page, int userID);

    /**
     * 查询用户的关注
     * @param userID
     * @return
     */
    PageInfo<User> queryFollows(PageRequest page, int userID);

    /**
     * 关注用户
     * @param userID
     * @param fansID
     * @return
     */
    Integer followUser(int userID,int fansID);

    /**
     * 取关用户
     * @param userID
     * @param fansID
     * @return
     */
    Integer noFollowUser(int userID,int fansID);

    /**
     * 查询用户A是否关注了用户B
     * @param fansID
     * @param userID
     * @return
     */
    Integer queryIsFriend(int fansID,int userID);

    /**
     * 查询用户信息
     * @param userID
     * @return
     */
    User queryUserByID(int userID);

    int queryFollowsTotal(int userID);

    int queryFansTotal(int userID);

    /**
     * 修改签名
     * @param userID
     * @param sign
     * @return
     */
    int alterSign(int userID,String sign);

    /**
     * 修改背景图
     * @param userID
     * @param backImg
     * @return
     */
    int alterBackImg(int userID,String backImg);

    String queryOldBackImg(int userID);

    Integer alterUserInfo(User user);

    Integer alterIcon(int userID,String icon);
    String queryOldIcon(int userID);
    boolean alterPassword(int userID,String oldPassword,String newPassword);
    //查询用户关注某用户的粉丝
    int[] queryUserFollowOtherUserFans(int otherUserID,int userID);
    //查询用户与用户的相互关注
    int[] queryUserFollowOtherUserFollow(int otherUserID,int userID);
}
