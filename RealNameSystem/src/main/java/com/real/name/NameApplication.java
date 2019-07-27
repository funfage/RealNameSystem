package com.real.name;

import com.real.name.device.netty.UDPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Import(SpringUtil.class)
@EnableCaching //开启缓存支持
@EnableScheduling //开启定时任务
@SpringBootApplication
public class NameApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(NameApplication.class);

    @Autowired
    private UDPServer udpServer;

    public static void main(String[] args) {
        SpringApplication.run(NameApplication.class, args);
        /*UdpServerStart.startUdpServer();*/
    }

    @Override
    public void run(String... args) throws Exception {
//        udpServer.start(new InetSocketAddress(61008));
    }
}
