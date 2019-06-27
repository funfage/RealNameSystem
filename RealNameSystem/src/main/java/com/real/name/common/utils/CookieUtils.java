package com.real.name.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtils {

    /**
     * 过期时间，2小时
     */
    public static final Integer EXPIRE = 7200;

    /**
     * token
     */
    public static final String TOKEN = "token";

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = TOKEN + "_%s";

    /**
     * 设置
     *
     * @param response 响应体
     * @param name     cookie name
     * @param value    cookie value
     * @param maxAge   cookie 过期时间
     */
    public static void set(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param request 请求体
     * @param name    cookie name
     * @return Cookie
     */
    public static Cookie get(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装成Map
     *
     * @param request
     * @return Map集合
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}
