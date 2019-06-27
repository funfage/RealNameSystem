package com.real.name.login.controller;

import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CookieUtils;
import com.real.name.login.entity.Admin;
import com.real.name.login.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResultVo login(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpServletResponse response) {

        Optional<Admin> admin = adminService.find(username, password);
        if (admin.isPresent()) {

            // 设置token至redis
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(String.format(CookieUtils.TOKEN_PREFIX, token),
                    admin.get().getAdminId().toString(), CookieUtils.EXPIRE, TimeUnit.SECONDS);

            // 设置token至cookie
            CookieUtils.set(response, CookieUtils.TOKEN, token, CookieUtils.EXPIRE);

            return ResultVo.success(admin.get());

        } else {
            return ResultVo.failure(ResultError.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResultVo logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 从cookie里查询name=token的
        Cookie cookie = CookieUtils.get(request, CookieUtils.TOKEN);

        if (cookie != null) {
            // 2. 清除redis
            redisTemplate.opsForValue().getOperations()
                    .delete(String.format(CookieUtils.TOKEN_PREFIX, cookie.getValue()));

            // 3. 清除cookie
            CookieUtils.set(response, CookieUtils.TOKEN, null, 0);
        }
        return ResultVo.success();
    }
}