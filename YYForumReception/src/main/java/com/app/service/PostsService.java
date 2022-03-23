package com.app.service;


import com.app.entity.PageRequest;
import com.app.entity.Posts;
import com.app.entity.PostsIcon;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostsService {

    /**
     * 分页查询最新帖子列表
     * @param pageRequest
     * @return
     */
    public PageInfo<Posts> queryPosts(PageRequest pageRequest);

    /**
     * 分页查询该用户所有帖子
     * @param pageRequest
     * @param userID
     * @return
     */
    public PageInfo<Posts> queryPostsByUserID(PageRequest pageRequest,int userID);

    /**
     * 根据帖子ID查找帖子详情
     * @param postsID
     * @return
     */
    public Posts queryPostsByID(int postsID);

    /**
     * 分页查询用户收藏的帖子
     * @param pageRequest
     * @param userID
     * @return
     */
    public PageInfo<Posts> queryFollowPostsByUserID(PageRequest pageRequest,int userID);

    /**
     * 根据图片ID查找图片信息
     * @param userID
     * @param idList
     * @return
     */
    public List<PostsIcon> queryIconsByIconIDList(int userID,int[] idList);

    /**
     * 按点赞最高的贴子排序
     * @param pageRequest
     * @return
     */
    public PageInfo<Posts> queryPostsOrderByLikes(PageRequest pageRequest);

    public Integer isLike(int userID,int postsID);

    public Integer isFollow(int userID,int postsID);

    /**
     * 取消收藏帖子
     * @param postsID
     * @param userID
     * @return
     */
    public boolean noFollowPosts(int postsID,int userID) throws Exception;

    /**
     * 用户收藏帖子
     * @param postsID
     * @param userID
     * @return
     * @throws Exception
     */
    public boolean followPosts(int postsID,int userID) throws Exception;

    /**
     * 用户点赞帖子
     * @param postsID
     * @param userID
     * @return
     * @throws Exception
     */
    public boolean likesPosts(int postsID,int userID) throws Exception;

    /**
     * 用户取消点赞帖子
     * @param postsID
     * @param userID
     * @return
     * @throws Exception
     */
    public boolean noLikesPosts(int postsID,int userID) throws Exception;

    /**
     * 添加帖子
     * @param posts
     * @return
     */
    public boolean addPosts(Posts posts) throws Exception;

    /**
     * 删除帖子
     * @param postsID
     * @param userID
     * @return
     */
    public boolean removePosts(int postsID,int userID);

    /**修改帖子*/
    public boolean alterPosts(Posts posts,
                              int userID,
                              String[] delIconList,
                              MultipartFile[] newIcons) throws Exception;

    /**模糊查询帖子列表*/
    public Object queryPostsBySearchKey(PageRequest pageRequest,String searchKey);
}
