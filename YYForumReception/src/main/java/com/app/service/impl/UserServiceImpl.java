package com.app.service.impl;

import com.app.dao.UserDao;
import com.app.entity.PageRequest;
import com.app.entity.User;
import com.app.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findUser(User user) {
        return userDao.checkUser(user);
    }

    @Override
    public void addUser(User user) {
        userDao.insertUser(user);
    }

    @Override
    public void initUserName(User user){
        String userName = "YY"+user.getId();
        user.setName(userName);
        userDao.updateUserName(user);
    }

    @Override
    public PageInfo<User> queryFans(PageRequest page,int userID) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<User> users = userDao.selectFans(userID);
        return new PageInfo<User>(users);
    }

    @Override
    public PageInfo<User> queryFollows(PageRequest page,int userID) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<User> users = userDao.selectFollows(userID);
        return new PageInfo<User>(users);
    }

    @Override
    public Integer followUser(int userID, int fansID) {
        return userDao.insertFollowsUser(userID,fansID);
    }

    @Override
    public Integer noFollowUser(int userID, int fansID) {
        return userDao.deleteFollowsUser(userID,fansID);
    }

    @Override
    public Integer queryIsFriend(int fansID, int userID) {
        return userDao.selectIsFriend(fansID,userID);
    }

    @Override
    public User queryUserByID(int userID) {
        return userDao.selectUserByID(userID);
    }

    @Override
    public int queryFollowsTotal(int userID) {
        return userDao.selectFollowsTotal(userID);
    }

    @Override
    public int queryFansTotal(int userID) {
        return userDao.selectFansTotal(userID);
    }

    @Override
    public int alterSign(int userID, String sign) {
        User user = new User();
        user.setId(userID);
        user.setSign(sign);
        return userDao.updateSign(user);
    }

    @Override
    public int alterBackImg(int userID, String backImg) {
        User user = new User();
        user.setId(userID);
        user.setBackImg(backImg);
        return userDao.updateBackImg(user);
    }

    @Override
    public String queryOldBackImg(int userID) {
        return userDao.selectOldBackImg(userID);
    }

    @Override
    public Integer alterUserInfo(User user) {
        return userDao.updateUserInfo(user);
    }

    @Override
    public Integer alterIcon(int userID, String icon) {
        User user = new User();
        user.setId(userID);
        user.setIcon(icon);
        return userDao.updateIcon(user);
    }

    @Override
    public String queryOldIcon(int userID) {
        return userDao.selectOldIcon(userID);
    }

    @Override
    public boolean alterPassword(int userID, String oldPassword, String newPassword) {
        User user = new User();
        user.setId(userID);
        user.setPassword(oldPassword);
        return userDao.updatePassword(user,newPassword)==1;
    }

    @Override
    public int[] queryUserFollowOtherUserFans(int otherUserID, int userID) {
        return userDao.selectUserFollowOtherUserFans(otherUserID,userID);
    }

    @Override
    public int[] queryUserFollowOtherUserFollow(int otherUserID, int userID) {
        return userDao.selectUserFollowOtherUserFollow(otherUserID,userID);
    }


}
