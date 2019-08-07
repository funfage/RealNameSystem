package com.real.name.common.config;

import com.real.name.common.handler.JsonReturnHandler;
import com.real.name.common.utils.PathUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new JsonReturnHandler());
    }

    /**
     * 前端预览文件路径配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + PathUtil.getImgBasePath());
        registry.addResourceHandler("/payFile/**").addResourceLocations("file:" + PathUtil.getPayFileBasePath());
        registry.addResourceHandler("/contractFile/**").addResourceLocations("file:" + PathUtil.getContractFilePath());
        registry.addResourceHandler("/excel/**").addResourceLocations("file:" + PathUtil.getExcelFilePath());
    }

    /**
     * 跨域配置
     */
   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("*");
    }*/

}
