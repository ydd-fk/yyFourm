package com.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.entity.Message;
import com.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/wsserver/{userID}")
@Component
public class MyWebSocketServer{

    private static MessageService messageService;
    @Autowired
    private void setMessageService(MessageService messageService){
        MyWebSocketServer.messageService =messageService;
    }
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,MyWebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";
    @OnOpen
    public void onOpen(Session session, @PathParam("userID") String userId) {
        System.out.println("用户"+userId+"连接成功");
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) webSocketMap.remove(userId);
        webSocketMap.put(userId, this);
    }

    //toUserID txt
    @OnMessage
    public void onMessage(String message, Session session){
        JSONObject jsonObject = JSON.parseObject(message);
        Integer toUserID = (Integer) jsonObject.get("toUserID");
        String txt = (String) jsonObject.get("txt");
        String name = (String) jsonObject.get("from_userName");
        String icon = (String) jsonObject.get("from_userIcon");
        Integer fromUserID = Integer.parseInt(userId);
        try {
            JSONObject resultJson = new JSONObject();
            Message newMessage = messageService.addMessage(fromUserID,toUserID,txt);
            newMessage.setTime(new Date());
            newMessage.getFromUser().setName(name);
            newMessage.getFromUser().setIcon(icon);
            resultJson.put("message",newMessage);
            webSocketMap.get(userId).sendMessage(resultJson.toJSONString());
            if (webSocketMap.containsKey(toUserID+"")) webSocketMap.get(toUserID+"").sendMessage(resultJson.toJSONString());
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @OnClose
    public void onClose(){
        System.out.println("用户"+userId+"关闭连接");
        if (webSocketMap.containsKey(userId)) webSocketMap.remove(userId);
    }
}
