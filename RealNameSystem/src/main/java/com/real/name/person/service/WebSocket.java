package com.real.name.person.service;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
public class WebSocket  {

    private static PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        WebSocket.personService = personService;
    }

    private Session session;

    /**
     * 定义容器 存储session
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("WebSocket new connection");
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("WebSocket breakdown");
    }

    @OnMessage
    public void onMessage(String message) {
//        System.out.println("WebSocket receive new message: " + message);
//
//        Person person = JSON.parseObject(message, Person.class);
//        //System.out.println(person);
//
//        personService.create(person);
//        System.out.println("插入数据库：" + message);

//        // 给设备发送人员信息
//        String url = HTTPTool.baseURL + "person/create";
//        Map<String, Object> map = new HashMap<>();
//        map.put("pass", Device.PASS);
//        map.put("person", person.toJSON());
//        ResultVo rvo = HTTPTool.postUrlForParam(url, map);
//
//        System.out.println("添加人员：" + rvo);
//
//        if (rvo.getSuccess()) {
//            // 给设备添加人员照片
//            url = HTTPTool.baseURL + "face/create";
//            map.put("personId", person.getPersonId().toString());
//            map.put("imgBase64", person.getHeadImage());
//            ResultVo rv = HTTPTool.postUrlForParam(url, map);
//            System.out.println("添加人员头像：" + rv);
//        }
    }

    public void sendMessage(String message) {
        for (WebSocket webSocket : webSocketSet) {
            System.out.println("WebSocket send message: " + message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                throw new  AttendanceException(ResultError.NETWORK_ERROR);
            }
        }
    }
}
