package com.real.name.auth.controller;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.UserService;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CookieUtils;
import com.real.name.auth.service.AdminService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;



    /**
     * 用户登录提交访问的方法
     */
    @PostMapping("/submitLogin")
    public ResultVo submitLogin(@RequestParam("phone") String phone,
                                @RequestParam("password") String password,
                                @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
        //1,认证的核心组件,获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //登录认证的第二步,将表单提交过来的用户名和密码封装到token对象
        UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
        //开启记住我功能
        if (rememberMe != null && rememberMe == 1) {
            token.setRememberMe(true);
        }
        //3,调用subject对象里的login方法,认证
        try {
            subject.login(token);
            Session session = SecurityUtils.getSubject().getSession();
            User user = (User) session.getAttribute("user");
            return ResultVo.success(user.getRoles());
        } catch (UnknownAccountException e) {
            return ResultVo.failure(ResultError.USER_NOT_EXIST);
        } catch (LockedAccountException e) {
            return ResultVo.failure(ResultError.USER_IS_DISABLE);
        } catch (AuthenticationException e) {
            return ResultVo.failure(ResultError.LOGIN_FAILURE);
        }
    }

    /**
     * 用户注册
     */
    @Transactional
    @PostMapping("/userRegister")
    public ResultVo userRegister(User user) {
        //TODO 判断用户输入的验证码书否正确
        //校验用户输入的信息是否符合要求
        verifyUser(user);
        //判断两次输入的密码是否正确
        if (StringUtils.isEmpty(user.getPassword()) || !user.getPassword().equals(user.getPasswordAgain())) {
            throw new AttendanceException(ResultError.USER_PASSWORD_NO_MATCH);
        }
        //先将用户设置为无效
        user.setStatus(0);
        //保存用户
        int effNum = userService.saveUser(user);
        if (effNum <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
        //保存用户角色关联
        effNum = userService.saveUserRole(user.getUserId(), user.getRoleId());
        if (effNum < 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
        return ResultVo.success();
    }

    @GetMapping("sessionTest")
    public ResultVo sessionTest() {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        return ResultVo.success(user);
    }

    /**
     * 用户还未登录访问的方法
     */
    @GetMapping("/noLogin")
    public ResultVo noLogin(HttpSession session) {
        //生成一组16位的随机数作为盐值
        int hashcodeV = UUID.randomUUID().hashCode();
        if(hashcodeV < 0){
            hashcodeV = -hashcodeV;
        }
        String uuidSalt = String.format("%016d", hashcodeV);
        //把uuid的盐值，同时保存到前后端中
        session.setAttribute("uuidSalt", uuidSalt);
        return ResultVo.failure(uuidSalt, ResultError.USER_UN_LOGIN);
    }

    /**
     * 登录成功跳转到这个路径
     */
    @GetMapping("/loginSuccess")
    public ResultVo loginSuccess() {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        return ResultVo.success(user.getRoles());
    }

    /**
     * 未授权跳转的路径
     */
    @GetMapping("/unauthorized")
    public ResultVo unauthorized() {
        return ResultVo.failure(ResultError.USER_UN_AUTHORIZED);
    }

    /**
     * 退出登录跳转到的路径
     */
    @PostMapping("/logout")
    public ResultVo logout() {
        SecurityUtils.getSubject().logout();
        return ResultVo.success("退出登录");
    }

    private void verifyUser(User user) {
        if (user.getPhone() == null) {
            throw new AttendanceException(ResultError.PHONE_EMPTY);
        } else if (userService.getUserByPhone(user.getPhone()) != null) {
            throw new AttendanceException(ResultError.USER_HAS_REGISTER);
        } else if (user.getPassword() == null) {
            throw new AttendanceException(ResultError.PASSWORD_EMPTY);
        } else if (user.getRoleId() == null) {
            throw new AttendanceException(ResultError.ROLE_EMPTY);
        }
    }

}