package com.app.service.impl;

import com.app.dao.MessageDao;
import com.app.entity.Message;
import com.app.entity.PageOffset;
import com.app.entity.PageRequest;
import com.app.entity.User;
import com.app.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDao messageDao;
    @Override
    public Message addMessage(int from_userID, int to_userID, String txt) {
        Message message = new Message();
        User user = new User();
        user.setId(from_userID);
        message.setFromUser(user);
        message.setToUserID(to_userID);
        message.setTxt(txt);
        messageDao.insertMessage(message);
        return message;
    }

    @Override
    public Object queryMessagesByUserID(int userID, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Message> messages = messageDao.selectMessagesByUserID(userID);
        return new PageInfo<>(messages);
    }

    @Override
    public PageInfo<Message> queryHistoryMessage(PageOffset pageOffset,int from_userID, int to_userID) {
        PageHelper.offsetPage(pageOffset.getOffset(),pageOffset.getLimit());
        List<Message> messages = messageDao.selectHistoryMessage(from_userID, to_userID);
        Collections.reverse(messages);
        return new PageInfo<>(messages);
    }

    @Override
    public void messageIsRead(int from_userID, int to_userID) {
        messageDao.updateIsRead(from_userID,to_userID);
    }
}
