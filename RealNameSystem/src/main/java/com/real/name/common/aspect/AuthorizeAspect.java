package com.real.name.common.aspect;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.CookieUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthorizeAspect {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.real.name.project.controller.*.*(..)) ||" +
            "execution(public * com.real.name.person.controller.*.*(..))"
    )
    public void verify() {}

    @Before("verify()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        if (false) {
            // 查询cookie
            Cookie cookie = CookieUtils.get(request, CookieUtils.TOKEN);
            if (cookie == null) {
                System.out.println("【登录校验】Cookie中查不到token");
                throw new AttendanceException(ResultError.LOGIN_TOKEN_ERROR);
            }

            // 去redis里查询
            String tokenValue = redisTemplate.opsForValue()
                    .get(String.format(CookieUtils.TOKEN_PREFIX, cookie.getValue()));
            if (StringUtils.isEmpty(tokenValue)) {
                System.out.println("【登录校验】Cookie中查不到token");
                throw new AttendanceException(ResultError.LOGIN_TOKEN_ERROR);
            }
        }
    }
}
