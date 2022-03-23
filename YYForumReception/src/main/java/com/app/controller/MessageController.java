package com.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.app.annotation.TokenPromise;
import com.app.entity.PageOffset;
import com.app.entity.PageRequest;
import com.app.service.MessageService;
import com.app.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping
    public Object queryMessageLately(PageRequest pageRequest,HttpServletRequest request){
        Integer userID = (Integer)request.getAttribute("userID");
        return messageService.queryMessagesByUserID(userID,pageRequest);
    }

    @GetMapping("/history")
    public Object queryHistoryMessage(PageOffset pageOffset,
                                      @RequestParam("to_userID") Integer to_userID,
                                      HttpServletRequest request){
        Integer userID = (Integer)request.getAttribute("userID");
        return messageService.queryHistoryMessage(pageOffset,to_userID,userID);
    }

    @TokenPromise
    @PutMapping("/read/{id}")
    public Object messageIsRead(@PathVariable("id")int from_userID,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Integer to_userID =(Integer) request.getAttribute("userID");
        messageService.messageIsRead(from_userID,to_userID);
        jsonObject.put("code", ResponseCode.SUCCESS_CODE);
        return  jsonObject;
    }
}
