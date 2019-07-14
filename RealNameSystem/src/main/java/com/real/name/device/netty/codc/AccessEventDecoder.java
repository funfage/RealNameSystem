package com.real.name.device.netty.codc;

import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.serial.BufferFactory;
import com.real.name.device.netty.utils.ConvertUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class AccessEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private Logger logger = LoggerFactory.getLogger(AccessEventDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf buffer = datagramPacket.content();
        //读取类型
        byte type = buffer.readByte();
        //读取功能号
        byte functionId = buffer.readByte();
        //保留
        short reserved = buffer.readShort();
        //读取设备号
        ByteBuf buf = BufferFactory.getBuffer(new byte[4]);
        byte[] bytes = new byte[4];
        buffer.readBytes(bytes);
        bytes = ConvertUtils.reverse(bytes);
        int deviceId = ConvertUtils.byte4ToInt(bytes, 0);
        //读取数据
        byte[] data = new byte[AccessConstant.DATA_LENGTH];
        //读取数据
        buffer.readBytes(data);
        //读取流水号
        int sequenceId = buffer.readInt();
        //读取扩展数据
        byte[] externalData = new byte[AccessConstant.EXTERNAL_DATA_LENGTH];
        buffer.readBytes(externalData);
        //获取设备ip和端口
        String hostName = datagramPacket.sender().getHostString();
        int port = datagramPacket.sender().getPort();
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setType(type);
        accessEvent.setReserved(reserved);
        accessEvent.setDeviceId(deviceId);
        accessEvent.setFunctionId(functionId);
        accessEvent.setData(data);
        accessEvent.setSequenceId(sequenceId);
        accessEvent.setExternalData(externalData);
        accessEvent.setAddress(new InetSocketAddress(hostName, port));
        logger.warn("解码后的AccessEvent为:{}", accessEvent.toString());
        //解析出消息对象, 继续往下面的handler传递
        list.add(accessEvent);
    }
}
