import request from "@/http";
export default {
  getCommentsList:(postsID,pageNum,pageSize)=>request.get(`comment/posts/${postsID}/?pageNum=${pageNum}&pageSize=${pageSize}`),
  addComment:({postsID,content})=>request.post('/comment/',{postsID,content}),
  addCommentReply:({commentID,commentReply:{content,toUserID}})=>request.post('/comment/reply',{commentID,commentReply:{content,toUserID}}),
  delComment:(commentID)=>request.delete(`/comment/${commentID}`),
  delCommentReply:(replyID)=>request.delete(`/comment/reply/${replyID}`),
  likeComment:(commentID)=>request.put(`/comment/like/${commentID}`),
  cancelLikeComment:(commentID)=>request.delete(`/comment/like/${commentID}`),
  likeCommentReply:(replyID)=>request.put(`/comment/reply/like/${replyID}`),
  cancelLikeCommentReply:(replyID)=>request.delete(`/comment/reply/like/${replyID}`),
  getCommentLikes:(postsID)=>request.get(`/comment/like/${postsID}`)
}
