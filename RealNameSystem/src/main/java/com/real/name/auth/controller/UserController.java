package com.real.name.auth.controller;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.UserService;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.JedisService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

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
            //设置session中的认证信息过期时间，默认为30分钟，单位为毫秒，这里设置为6个小时
            session.setTimeout(6 * 3600 * 1000);
            Map<String, Object> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("phone", user.getPhone());
            map.put("roles", user.getRoles());
            return ResultVo.success(map);
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
    @PostMapping("/userRegister")
    public ResultVo userRegister(User user) {
        //校验用户输入的信息是否符合要求
        verifyUser(user);
        //根据电话号码查询用户是否已注册
        User selectUser = userService.getUserByPhone(user.getPhone());
        if (selectUser != null || selectUser.getStatus() == 1) {
            return ResultVo.failure(ResultError.USER_HAS_REGISTER);
        }
        //判断两次输入的密码是否正确
        if (StringUtils.isEmpty(user.getPassword()) || !user.getPassword().equals(user.getPasswordAgain())) {
            return ResultVo.failure(ResultError.USER_PASSWORD_NO_MATCH);
        }
        //获取发送的验证码
        String sendCode = (String) jedisStrings.get(user.getPhone());
        //判断用户输入的验证码书否正确
        if (StringUtils.isEmpty(user.getAuthCode()) || !user.getAuthCode().equals(sendCode)) {
            return ResultVo.failure(ResultError.AUTH_CODE_ERROR);
        }
        //注册用户
        userService.registerUser(user);
        return ResultVo.success();
    }

    /**
     * 用户忘记密码修改
     */
    @Transactional
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(User user) {
        if (StringUtils.isEmpty(user.getPhone())) {
            return ResultVo.failure(ResultError.PHONE_EMPTY);
        } else if (StringUtils.isEmpty(user.getPassword())) {
            return ResultVo.failure(ResultError.PASSWORD_EMPTY);
        }
        String sendCode = (String) jedisStrings.get(user.getPhone());
        //判断用户输入的验证码是否正确
        if (StringUtils.isEmpty(user.getAuthCode()) || !user.getAuthCode().equals(sendCode)) {
            return ResultVo.failure(ResultError.OPERATOR_ERROR);
        }
        //判断两次输入的密码是否一致
        if (!user.getPassword().equals(user.getPasswordAgain())) {
            return ResultVo.failure(ResultError.USER_PASSWORD_NO_MATCH);
        }
        //修改用户密码
        userService.updateUser(user);
        return ResultVo.success();
    }

    /**
     * @param operator 用户申请是否批准 1:批准, 0:不批准
     */
    @GetMapping("/userApply")
    public ResultVo userApply(@Param("userId") Integer userId,
                              @Param("operator") Integer operator) {
        User user = new User(userId);
        user.setStatus(operator);
        userService.updateUser(user);
        return ResultVo.success();
    }

    /**
     * 超级管理员对用户信息的修改
     */
    @PostMapping("/updateUserByAdmin")
    public ResultVo updateUserByAdmin(User user) {
        userService.updateUserByAdmin(user);
        return ResultVo.success();
    }

    /**
     * 对用户信息的删除
     */
    @GetMapping("/deleteUser")
    public ResultVo deleteUser(Integer userId) {
        int i = userService.deleteUserByUserId(userId);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
        return ResultVo.success();
    }


    /**
     * ==================================以下只与查询有关================================
     */

    /**
     * 查询所有用户信息
     */
    @GetMapping("getUsers")
    public ResultVo getUsers() {
        List<User> users = userService.getUsers();
        return ResultVo.success(users);
    }

    /**
     * 根据电话号码查询用户
     */
    @GetMapping("/getUserByPhone")
    public ResultVo getUserByPhone(@Param("phone") String phone) {
        User user = userService.getUserByPhone(phone);
        return ResultVo.success(user);
    }

    /**
     * 退出登录跳转到的路径
     */
    @PostMapping("/logout")
    public ResultVo logout() {
        SecurityUtils.getSubject().logout();
        return ResultVo.success("退出登录");
    }

    /**
     * 未授权跳转的路径
     */
    @GetMapping("/unauthorized")
    public ResultVo unauthorized() {
        return ResultVo.failure(ResultError.USER_UN_AUTHORIZED);
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