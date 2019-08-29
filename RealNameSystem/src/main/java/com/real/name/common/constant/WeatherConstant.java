package com.real.name.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConstant {

    public static String appid;

    public static String appsecret;

    @Value("${weather.appid}")
    public void setAppid(String appid) {
        WeatherConstant.appid = appid;
    }

    @Value("${weather.appsecret}")
    public void setAppsecret(String appsecret) {
        WeatherConstant.appsecret = appsecret;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getAppsecret() {
        return appsecret;
    }

}
