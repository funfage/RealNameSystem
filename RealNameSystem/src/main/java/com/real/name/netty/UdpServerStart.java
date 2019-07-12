package com.real.name.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.LoggerFactory;


public class UdpServerStart implements Runnable {

    private org.slf4j.Logger Logger = LoggerFactory.getLogger(UdpServerStart.class);

    private final Integer port = 9902;

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ControllerContainer.getInstance();
            Bootstrap b = new Bootstrap();
            //3.配置启动器
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    //指定为广播模式
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ReadHeadServerHandler());

            //bind到指定端口，并返回一个channel，该端口就是监听UDP报文的端口
            Channel channel = b.bind(port).sync().channel();
            /*等待future完成
             *channel.closeFuture()不做任何操作，只是简单的返回channel对象中的closeFuture对象，
             * 对于每个Channel对象，都会有唯一的一个CloseFuture，用来表示关闭的Future
             * 如果用户操作调用了sync或者await方法，会在对应的future对象上阻塞用户线程
             */
            channel.closeFuture().await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Logger.info("退出UdpServer");
            group.shutdownGracefully();
        }
    }

    public static void startUdpServer() {
        try {
            new Thread(new UdpServerStart()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
