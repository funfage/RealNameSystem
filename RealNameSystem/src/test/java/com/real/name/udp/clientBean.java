package com.real.name.udp;

import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.others.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;

public class clientBean extends BaseTest {

    @Autowired
    private UDPClient udpClient;

    @Test
    public void clientTest() {
        AccessEvent event = new AccessEvent();
        event.setAddress(new InetSocketAddress("127.0.0.1", 9902));
        event.setFunctionId((byte)0x50);
        event.setDeviceId(223000123);
        udpClient.sendMessage(event);
    }

}
