package com.real.name.common.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

public class UDPTool {
    public static void sendData(byte[] data, String ip, ChannelHandlerContext ctx, Integer outPort){
        InetSocketAddress send = new InetSocketAddress(ip, outPort);
        DatagramPacket dataSend = new DatagramPacket(Unpooled.copiedBuffer(data), send);
        System.out.println("dataSend:"+dataSend);
        ctx.writeAndFlush(dataSend);
    }
}