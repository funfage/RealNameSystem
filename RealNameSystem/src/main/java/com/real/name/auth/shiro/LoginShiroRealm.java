package com.real.name.auth.shiro;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.RoleService;
import com.real.name.auth.service.UserService;
import com.real.name.auth.service.repository.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 登录认证的realm
 */
public class LoginShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(LoginShiroRealm.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1,token把参数强转为usernamepasswordToken
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        //2,从userPassToken中后去表单提交过来的电话号
        String phone = userPassToken.getUsername();
        //3,从数据库中查询有没有用户名是usename的用户记录
        User user = null;
        try {
            user = userService.getUserByPhone(phone);
        } catch (Exception e) {
            logger.error("根据用户名查询用户失败");
        }
        if (user == null) {
            throw new UnknownAccountException("无此用户");
        }
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new LockedAccountException("用户已被管理员禁用");
        }
        //使用phone充当盐值
        ByteSource salt = ByteSource.Util.bytes(phone);
        //进一步让shiro来帮我们判断用户表单填写并传来的密码是否正确
        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }

    /**
     * 在shiro中专门用来做授权认证的方法
     * @param principals
     * 处理授权认证的方法带来的参数PrincipalCollection principals的三个结论：
     *         1，传进来的是登录成功以后的用户信息，与做登录认证的方法doGetAuthenticationInfo中的AuthenticationInfo对象第一个参数密切相关
     *         2，由于登录认证可能是多个realm对象，所以可能传来多个用户的信息，这个参数本质来说是一个集合，可能有多个元素
     *         3，参数的集合里边的元素的顺序，受realm在spring.xml中配置的顺序影响
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1,从参数principals获取当前登录成功后的用户信息
        User user = principals.oneByType(User.class);
        //2，获取到用户的角色信息
        Set<String> roles = roleService.getRolesByUserId(user.getUserId());
        logger.info("user:{}, role:{}", user, roles);
        //3, 获取关联的权限信息
        Set<String> permissions = userService.getUserPermissions(user.getUserId());
        logger.info("user:{}, permissions:{}", user, permissions);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (roles != null) {
            info.addRoles(roles);
        }
        if (permissions != null) {
            info.addStringPermissions(permissions);
        }
        return info;
    }

}


















