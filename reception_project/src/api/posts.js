import request from "@/http";
import {formatDate} from "element-ui/src/utils/date-util";
export default {
  /**
   * 分页查询最新帖子列表
   * @param pageNum 页码
   * @param pageSize 每页显示条数
   * @returns {Promise<AxiosResponse<any>>}
   */
  getNewList:({pageNum,pageSize})=>request.get(`/posts/?pageNum=${pageNum}&pageSize=${pageSize}`),
  /**
   * 查询点赞最高的postsSize张帖子
   * @param postsSize 条数
   * @returns {Promise<AxiosResponse<any>>}
   */
  getBestLikes:(postsSize)=>request.get(`/posts/bestlikes/?pageSize=${postsSize}`),
  /**
   * 查询帖子信息
   * @param postsID
   * @returns {Promise<AxiosResponse<any>>}
   */
  getPostsByID:(postsID)=>request.get(`/posts/${postsID}`),
  /**
   * 点赞帖子
   * @param postsID
   * @returns {Promise<AxiosResponse<any>>}
   */
  like:(postsID)=>request.post(`/posts/like/${postsID}`),
  /**
   * 取消点赞帖子
   * @param postsID
   * @returns {Promise<AxiosResponse<any>>}
   */
  cancelLike:(postsID)=>request.delete(`/posts/like/${postsID}`),
  /**
   * 收藏帖子
   * @param postsID
   * @returns {Promise<AxiosResponse<any>>}
   */
  follow:(postsID)=>request.post(`/posts/follow/${postsID}`),
  /**
   * 取消收藏帖子
   * @param postsID
   * @returns {Promise<AxiosResponse<any>>}
   */
  cancelFollow:(postsID)=>request.delete(`/posts/follow/${postsID}`),
  /**
   * 查看用户收藏的帖子
   * @param userID
   * @param pageNum
   * @param pageSize
   * @returns {Promise<AxiosResponse<any>>}
   */
  getFollowsPosts:({userID,pageNum,pageSize})=>request.get(`posts/follow/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  /**获取用户编写帖子*/
  getUsersPosts:({userID,pageNum,pageSize})=>request.get(`posts/user/${userID}?pageNum=${pageNum}&pageSize=${pageSize}`),
  /**用户删除帖子*/
  removePosts:(postsID)=>request.delete(`posts/${postsID}`),
  /**添加帖子*/
  addPosts:({formData})=>request.post(`/posts/`, formData,{
    headers: {'Content-Type': 'multipart/form-data'}
  }),
  /**修改帖子信息*/
  modifyPosts:({formData})=>request.put(`/posts/`,formData,{
    headers: {'Content-Type': 'multipart/form-data'}
  }),
  /**查询帖子*/
  searchPosts:({searchKey,pageNum,pageSize})=>request.get(`/posts/search/${searchKey}?pageNum=${pageNum}&pageSize=${pageSize}`)
}
