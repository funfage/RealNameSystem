package com.real.name.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServerStart implements  Runnable {
    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ControllerContainer.getInstance();
            Bootstrap b = new Bootstrap();

            boolean autorelease = false;
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new DutouServerHandler());

            Channel channel = b.bind(9902).sync().channel();
            channel.closeFuture().await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("退出程序");
            group.shutdownGracefully();

        }
    }
    public  static void startUdpServer(){
        try {
            new Thread(new UdpServerStart()).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
