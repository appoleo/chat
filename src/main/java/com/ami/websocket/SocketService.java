package com.ami.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangchendong@innjoy.me
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{userId}")
public class SocketService {

    private String userId;

    private static final Map<String, Session> USERS = new LinkedHashMap<>();

    /**
     * 连接时执行
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.userId = userId;
        USERS.put(userId, session);
        log.info("新连接：{}", userId);
    }

    /**
     * 关闭时执行
     */
    @OnClose
    public void onClose() {
        log.info("连接：{} 关闭", this.userId);
    }

    /**
     * 收到消息时执行
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("收到用户{}的消息：{}", this.userId, message);
        // 回复用户
        if (CollectionUtils.isEmpty(USERS)) {
            return;
        }
        String id = session.getPathParameters().get("userId");

        for (Session sessionT : USERS.values()) {
            sessionT.getBasicRemote().sendText(id + ":" + message);
        }
    }

    /**
     * 连接错误时执行
     */
    @SuppressWarnings("unused")
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("用户id为：{}的连接发送错误", this.userId);
        error.printStackTrace();
    }

    public void send(String message) throws IOException {
        if (CollectionUtils.isEmpty(USERS)) {
            return;
        }
        for (Session session : USERS.values()) {
            session.getBasicRemote().sendText(message);
        }
    }
}
