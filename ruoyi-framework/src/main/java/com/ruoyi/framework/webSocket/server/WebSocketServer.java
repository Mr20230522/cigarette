package com.ruoyi.framework.webSocket.server;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

import javax.websocket.*;

import javax.websocket.server.ServerEndpoint;

import java.util.Map;

import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;





@ServerEndpoint(value = "/webSocket")

@Component

public class WebSocketServer {

    private static Map<String , Session> clientMap = new ConcurrentHashMap<>();



    /**
     * 客户端与服务端连接成功
     * @param session
     */

    @OnOpen

    public void onOpen(Session session){

        System.out.println("ws连接成功");

        clientMap.put(session.getId(),session);

    }



    /**
     * 客户端与服务端连接关闭
     * @param session
     */

    @OnClose

    public void onClose(Session session){

        clientMap.remove(session.getId());

    }



    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     */

    @OnError

    public void onError(Throwable error,Session session) {

        error.printStackTrace();

    }



    /**
     * 客户端向服务端发送消息
     * @param message 传递信息
     */

    @OnMessage

    public void onMsg(String message) {

        System.out.println("收到前端消息："+message);

        //以下为模拟发送消息

        sendMessage();

    }



    /**
     * 定时向客户端发送数据（10秒间隔）
     */

    @Scheduled(cron = "0/10 * * * * ?")

    private void sendMessage(){

        //获得Map的Key的集合

        Set<String> sessionIdSet = clientMap.keySet();

        // 此处相当于一个广播操作//迭代Key集合

        for (String sessionId : sessionIdSet) {

            //根据Key得到value

            Session session = clientMap.get(sessionId);

            //发送消息给客户端，每10s给前端推送数据

            session.getAsyncRemote().sendText("我是后端发来的数据");

        }

    }

}

