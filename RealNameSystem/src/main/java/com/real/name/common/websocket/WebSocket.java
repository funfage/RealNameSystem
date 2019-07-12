package com.real.name.common.websocket;

import com.real.name.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
public class WebSocket  {

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private static PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        WebSocket.personService = personService;
    }

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 连接建立成功调用的方法
     * @param session 会话
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        logger.info("WebSocket new connection");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中移除
        webSocketSet.remove(this);
        logger.info("WebSocket breakdown");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("收到前端传来是消息:" + message);
    }

    /**
     * 发送消息给某个客户端
     * @param message 消息
     */
    public void sendMessage(String message){
        try {
            logger.info("sendMessage, 成功发送一条消息");
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error(" webSocket sendMessage error e:{}", e);
        }
    }

    /**
     * 群发消息
     * @param message 需要发送的消息
     */
    public void sendMessageToAll(String message){
        logger.info("sendMessageToAll, 成功群发一条信息:" + message);
        for (WebSocket webSocket : webSocketSet) {
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                logger.error("webSocket sendMessageToAll error e:{}", e);
            }
        }
    }
}
