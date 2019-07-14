package com.real.name.device.netty.codc;

import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.utils.ConvertUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AccessEventEncoder extends MessageToMessageEncoder<AccessEvent> {

    private Logger logger = LoggerFactory.getLogger(AccessEventEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AccessEvent event, List<Object> list) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer(AccessConstant.BASE_LENGTH);
        //类型
        buffer.writeByte(event.getType());
        //功能号
        buffer.writeByte(event.getFunctionId());
        //保留
        buffer.writeShort(event.getReserved());
        //设备id
        byte[] bytes = ConvertUtils.intToByte4(event.getDeviceId());
        bytes = ConvertUtils.reverse(bytes);
        buffer.writeBytes(bytes);
        //数据
        byte[] data = ConvertUtils.fillData(event.getData());
        buffer.writeBytes(data);
        //流水号
        buffer.writeInt(event.getSequenceId());
        //扩展数据
        byte[] externalData = ConvertUtils.fillExternalData(event.getExternalData());
        buffer.writeBytes(externalData);
        logger.warn("向门禁控制器发送的16进制数据为: " + ByteBufUtil.hexDump(buffer).toUpperCase());
        list.add(new DatagramPacket(buffer, event.getAddress()));
    }
}
























