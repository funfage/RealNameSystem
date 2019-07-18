package com.real.name.device.netty;

import com.real.name.device.netty.codc.AccessEventDecoder;
import com.real.name.device.netty.codc.AccessEventEncoder;
import com.real.name.device.netty.codc.AccessEventHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class UDPServer {

    private Logger logger = LoggerFactory.getLogger(UDPServer.class);

    private EventLoopGroup group;

    private Bootstrap bootstrap;

    public ChannelFuture start(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        // 设置循环线程组事例
        bootstrap.group(group);
        // 设置channel工厂
        bootstrap.channel(NioDatagramChannel.class)
                //设置套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                // 设置管道
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    public void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new AccessEventDecoder());//解码器
                        ch.pipeline().addLast(new AccessEventEncoder());//编码器
                        ch.pipeline().addLast(new AccessEventHandler());
                    }
                })
                .localAddress(address);
        ChannelFuture channelFuture = bootstrap.bind().syncUninterruptibly();
        if (channelFuture != null && channelFuture.isSuccess()) {
            logger.warn("udp服务启动成功, 监听的端口为:{}", address.getPort());
            Channel channel = channelFuture.channel();
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                logger.error("udp服务启动失败, e{}", e);
            } finally{
                group.shutdownGracefully();
            }
        } else {
            logger.warn("udp服务启动失败");
        }
        return channelFuture;
    }
}

























