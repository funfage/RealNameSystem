package com.real.name;

import com.real.name.common.utils.SpringUtil;
import com.real.name.netty.UdpServerStart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Import(SpringUtil.class)
@EnableCaching
@SpringBootApplication
public class NameApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {


        SpringApplication.run(NameApplication.class, args);
        UdpServerStart.startUdpServer();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
