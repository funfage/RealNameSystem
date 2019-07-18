package com.real.name.udp;

import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.model.AccessFunction;
import com.real.name.device.netty.utils.ConvertUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class client {

    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient();
        sendData(udpClient);
        //udpClient.destroy();
    }

    private static void sendData(UDPClient udpClient) {
        AccessEvent event = new AccessEvent();
        event.setAddress(new InetSocketAddress("127.0.0.1", 61005));
        event.setFunctionId((byte)0x50);
        event.setDeviceId(223000123);
        byte data[] = new byte[32];
        data[12] = 0x01;
        data[13] = 0x01;
        data[16] = 0x0D;
        data[17] = (byte) 0xD7;
        data[18] = 0x37;
        data[19] = 0x00;
        data[20] = 0x20;
        data[21] = 0x19;
        data[22] = 0x07;
        data[23] = 0x14;
        data[24] = 0x22;
        data[25] = 0x14;
        data[26] = 0x30;
        event.setData(data);
        udpClient.sendMessage(event, "127.0.0.1", 61005);
    }

    private static void testQueryAuthority() {
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
        udpClient.sendMessage(event, "169.254.39.58", 60000);
        udpClient.destroy();
    }

}
