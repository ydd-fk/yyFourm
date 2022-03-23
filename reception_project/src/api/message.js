import request from "@/http";
export default {
  getMessageLately:({pageNum,pageSize})=>request.get(`/message?pageNum=${pageNum}&pageSize=${pageSize}`),
  getHistoryMessage:({offset,limit,to_userID})=>request.get(`/message/history?to_userID=${to_userID}&offset=${offset}&limit=${limit}`),
  isRead:(fromUserID)=>request.put(`/message/read/${fromUserID}`)
}
