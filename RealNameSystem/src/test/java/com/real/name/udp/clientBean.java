package com.real.name.udp;

import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.model.AccessFunction;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.others.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;

public class clientBean extends BaseTest {

    @Autowired
    private UDPClient udpClient;

    @Test
    public void clientTest() {
        AccessEvent event = new AccessEvent();
        event.setFunctionId((byte)0x50);
        event.setDeviceId(223000123);
        udpClient.sendMessage(event, "127.0.0.1", 61005);
    }

    @Test
    public void testQueryAuthority() {
        UDPClient udpClient = new UDPClient();
        AccessEvent event = new AccessEvent();
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        event.setFunctionId((byte)0x5A);
        event.setDeviceId(223182363);
        dataBuffer.put(AccessFunction.QUERY_AUTHORITY);
        byte[] cardIndexBytes = ConvertUtils.intToByte4(Integer.parseInt("22938014"));
        cardIndexBytes = ConvertUtils.reverse(cardIndexBytes);
        dataBuffer.put(cardIndexBytes);
        event.setData(dataBuffer.array());
        udpClient.sendMessage(event, "169.254.39.58", 61005);
        udpClient.destroy();
    }
}
