package com.real.name.device.netty;

import com.real.name.device.netty.codc.AccessEventDecoder;
import com.real.name.device.netty.codc.AccessEventEncoder;
import com.real.name.device.netty.model.AccessEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class UDPClient {

    private Logger logger = LoggerFactory.getLogger(UDPClient.class);

    private EventLoopGroup group;

    private Bootstrap bootstrap;

    private Channel channel;


    //@PostConstruct
    public UDPClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new AccessEventDecoder());
                        ch.pipeline().addLast(new AccessEventEncoder());
                    }
                });
    }

    private void connect(InetSocketAddress address) {
        //连接设备
        ChannelFuture connect = bootstrap.connect(address).syncUninterruptibly();
        if (connect != null && connect.isSuccess()) {
            logger.warn("udpClient连接成功host={}, port={}", address.getHostName(), address.getPort());
            channel = connect.channel();
        } else {
            logger.error("udpClient连接失败host={}, port={}", address.getHostName(), address.getPort());
        }
    }

    public void sendMessage(AccessEvent event) {
        if (channel == null || !channel.isActive()) {
            connect(event.getAddress());
        }
        channel.writeAndFlush(event);
    }

    public void destroy() {
        try {
            if (channel != null) {
                ChannelFuture await = channel.close().await();
                if (!await.isSuccess()) {
                    logger.warn("udp的channel关闭失败, {}", await.cause());
                }
                logger.warn("udp的channel关闭成功");
            }
            Future<?> future1 = group.shutdownGracefully().await();
            if (!future1.isSuccess()) {
                logger.warn("udpServer的group关闭失败, {}", future1.cause());
            }
            logger.warn("udp服务关闭成功");
        } catch (InterruptedException e) {
            logger.warn("udp的channel关闭失败");
            e.printStackTrace();
        }
    }

}
