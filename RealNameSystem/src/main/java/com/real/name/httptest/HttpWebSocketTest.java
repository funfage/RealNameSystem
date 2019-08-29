package com.real.name.httptest;

import com.real.name.common.result.ResultVo;
import com.real.name.common.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class HttpWebSocketTest {

    @Autowired
    private WebSocket webSocket;

    @GetMapping("webSocketTest")
    public ResultVo webSocketTest() {
        webSocket.sendMessageToAll("hi");
        webSocket.sendMessage("hi");
        return ResultVo.success();
    }

}
