package com.app.service;

import com.app.entity.Message;
import com.app.entity.PageOffset;
import com.app.entity.PageRequest;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MessageService {

    /**发送信息*/
    Message addMessage(int from_userID,int to_userID,String txt);

    /**查询最新联系*/
    Object queryMessagesByUserID(int userID, PageRequest pageRequest);

    /**查询历史聊天记录*/
    PageInfo<Message> queryHistoryMessage(PageOffset pageOffset, int from_userID, int to_userID);

    /**设置消息已读*/
    void messageIsRead(int from_userID,int to_userID);
}
