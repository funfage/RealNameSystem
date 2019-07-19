package com.real.name;

import com.real.name.common.utils.SpringUtil;
import com.real.name.device.netty.UDPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.InetSocketAddress;

@Import(SpringUtil.class)
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class NameApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(NameApplication.class);

    @Autowired
    private UDPServer udpServer;

    public static void main(String[] args) {
        SpringApplication.run(NameApplication.class, args);
        /*UdpServerStart.startUdpServer();*/

    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
    @Override
    public void run(String... args) throws Exception {
//        udpServer.start(new InetSocketAddress(61008));
    }
}
