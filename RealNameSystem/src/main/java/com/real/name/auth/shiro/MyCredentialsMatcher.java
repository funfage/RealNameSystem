package com.real.name.auth.shiro;

import com.real.name.auth.service.AuthUtils;
import com.real.name.auth.entity.Role;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 自定义密码比较规则
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    private Logger logger = LoggerFactory.getLogger(MyCredentialsMatcher.class);

    @Autowired
    private UserService userService;


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        String password = new String(userPassToken.getPassword());
       /* //从服务器端取出盐值
        String uuidSalt = (String) SecurityUtils.getSubject().getSession().getAttribute("uuidSalt");
        try {
            //封装之前将密码进行一次解密
            password = AesEncryptUtil.desEncrypt(password, uuidSalt, uuidSalt);
        } catch (Exception e) {
            logger.error("password解密失败, e{}", e);
        }
        //得到用户输入进行加密后的密码
        String formPassword = (new SimpleHash(
                ShiroConstant.algorithmName, //加密算法
                password,//加密源
                userPassToken.getUsername(), //盐值
                ShiroConstant.hashIterations //加密次数
        )).toString();*/
        //得到正确加密后的数据库中的密码
        String credentials = (String) this.getCredentials(info);
        //匹对
        boolean isEquals = password.equals(credentials);
        if (isEquals) {
            //将用户对象存入会话
            User user = userService.getUserByPassword(credentials);
            Set<Role> roles = user.getRoles();
            Set<String> projectSet = new HashSet<>();
            for (Role role : roles) {
                if (AuthUtils.ProjectRole.equals(role.getRoleName())) {
                    //如果是项目管理员则从user_project查询用户绑定的项目
                    Set<String> userProject = userService.getUserProject(user.getUserId());
                    projectSet.addAll(userProject);
                }
            }
            if (projectSet.size() > 0) {
                user.setProjectSet(projectSet);
            }
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            logger.info("存入session中的对象为:{}", user);
        }
        return isEquals;
    }
}





















