package com.app.service.impl;

import com.app.dao.PostsDao;
import com.app.dao.PostsIconDao;
import com.app.entity.PageRequest;
import com.app.entity.Posts;
import com.app.entity.PostsIcon;
import com.app.service.PostsService;
import com.app.util.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsDao postsDao;

    @Autowired
    private PostsIconDao postsIconDao;

    public PageInfo<Posts> queryPosts(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Posts> postsList = postsDao.selectPostsList();
        return new PageInfo<Posts>(postsList);
    }

    public PageInfo<Posts> queryPostsByUserID(PageRequest pageRequest,int userID) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Posts> postsList = postsDao.selectPostsListByUserID(userID);
        return new PageInfo<Posts>(postsList);
    }

    @Override
    public Posts queryPostsByID(int postsID) {
        return postsDao.selectPostsByPostsID(postsID);
    }

    @Override
    public PageInfo<Posts> queryFollowPostsByUserID(PageRequest pageRequest, int userID) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Posts> postsList = postsDao.selectFollowPostsByUserID(userID);
        return new PageInfo<Posts>(postsList);
    }

    @Override
    public List<PostsIcon> queryIconsByIconIDList(int userID, int[] idList) {
        return postsIconDao.selectIcons(userID,idList);
    }

    @Override
    public PageInfo queryPostsOrderByLikes(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Posts> postsList = postsDao.selectPostsOrderByLikes();
        return new PageInfo<Posts>(postsList);
    }

    @Override
    public Integer isLike(int userID, int postsID) {
        return postsDao.selectIsLike(userID,postsID);
    }

    @Override
    public Integer isFollow(int userID, int postsID) {
        return postsDao.selectIsFollow(userID,postsID);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean noFollowPosts(int postsID, int userID) throws Exception {
        if(postsDao.deleteFollowPosts(postsID, userID)<=0) return false;
        if(postsDao.postsSubOneCollections(postsID)<=0) throw new Exception("帖子收藏记录自减错误");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean followPosts(int postsID, int userID) throws Exception {
        if(postsDao.insertFollowPosts(postsID, userID)<=0) return false;
        if(postsDao.postsIncOneCollections(postsID)<=0) throw new Exception("帖子收藏记录自增错误");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likesPosts(int postsID, int userID) throws Exception {
        if(postsDao.insertLikesPosts(postsID, userID)<=0) return false;
        if(postsDao.postsIncOneLikes(postsID)<=0) throw new Exception("帖子收藏记录自增错误");
        return true;
    }

    @Override
    @Transactional
    public boolean noLikesPosts(int postsID, int userID) throws Exception {
        if(postsDao.deleteLikesPosts(postsID, userID)<=0) return false;
        if(postsDao.postsSubOneLikes(postsID)<=0) throw new Exception("帖子收藏记录自减错误");
        return true;
    }

    @Override
    @Transactional
    public boolean addPosts(Posts posts) throws Exception{
        if(postsDao.insertPosts(posts)<=0) return false;
        if(posts.getIcons().size()==0) return true;
        if(postsIconDao.insertIcons(posts.getIcons(),posts.getId())<=0) throw new Exception("插入图片失败");
        return true;
    }

    @Override
    @Transactional
    public boolean removePosts(int postsID, int userID) {
        List<PostsIcon> iconList = postsIconDao.selectIconListByPostID(postsID);
        if(postsDao.selectPostsIsForUser(postsID,userID)<=0) return false;
        if(postsDao.deletePosts(postsID,userID)<=0) return false;
        if (iconList.size()!=0&&iconList!=null) iconList.forEach(postsIcon -> FileUtil.fileDelete(postsIcon.getUrl()));
        return true;
    }

    @Override
    @Transactional
    public boolean alterPosts(Posts posts,
                              int userID,
                              String[] delIconList,
                              MultipartFile[] newIcons) throws Exception {
        //1、查询用户是否拥有该帖
        if (postsDao.selectPostsIsForUser(posts.getId(),userID)!=1) throw new Exception("当前用户无权限修改此帖");
        //2、根据pID，delIconUrl 查询count值是否等于delIconList的长度
        if (delIconList!=null&&delIconList.length!=0){
            Integer exitsLength = postsIconDao.selectIconsCount(posts.getId(), delIconList);
            if (exitsLength!=delIconList.length) throw new Exception("图片名称异常");
            //2.1、如果等于长度与查询出来长度的相等，则删除图片
            postsIconDao.deleteIcons(delIconList);
            for (String iconName : delIconList) {
                System.out.println("del icon: "+iconName);
                FileUtil.fileDelete(iconName);
            }
        }
        //4、更新帖子信息
        postsDao.updatePosts(posts,userID);
        List<PostsIcon> postsIconList = new ArrayList<>();
        //3、下载图片
        try {
            if (newIcons!=null){
                for (int i = 0; i < newIcons.length; i++) {
                    PostsIcon postsIcon = new PostsIcon();
                    postsIcon.setUrl(FileUtil.fileUpload(newIcons[i]));
                    postsIcon.setIndex(i);
                    postsIconList.add(postsIcon);
                }
            }
        }catch (IOException e){
            System.out.println("上传图片失败");
        }
        //4、保存图片至数据库
        if(postsIconList.size()!=0) postsIconDao.insertIcons(postsIconList,posts.getId());
        return true;
    }

    @Override
    public Object queryPostsBySearchKey(PageRequest pageRequest,String searchKey) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Posts> postsList = postsDao.selectPostsLikeSearchKey(searchKey);
        return new PageInfo<>(postsList);
    }


}
