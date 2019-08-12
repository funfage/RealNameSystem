package com.real.name.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NationConstant {

    public static String url;

    public static String appid;

    public static String appsecret;

    public static String format;

    public static String version;

    @Value("${nation.url}")
    public void setUrl(String url) {
        NationConstant.url = url;
    }

    @Value("${nation.appid}")
    public void setAppid(String appid) {
        NationConstant.appid = appid;
    }

    @Value("${nation.appsecret}")
    public void setAppsecret(String appsecret) {
        NationConstant.appsecret = appsecret;
    }

    @Value("${nation.format}")
    public void setFormat(String format) {
        NationConstant.format = format;
    }

    @Value("${nation.version}")
    public void setVersion(String version) {
        NationConstant.version = version;
    }

    public NationConstant() {
    }

    public static String getUrl() {
        return url;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getAppsecret() {
        return appsecret;
    }

    public static String getFormat() {
        return format;
    }

    public static String getVersion() {
        return version;
    }

    /*public final static String URL = "http://182.148.48.165:8090/open.api";
    public final static String VERSION = "1.0";
    public final static String APPID = "44010620190510008";
    public final static String APPSCRECT = "2544fb6c1cb77ebbe8f6a91c4d0deeab";
    public final static String FORMART = "json";
    public final static String CORPCODE = "91445321MA524J9J44";
    public final static String CORPNAME = "";*/
}
