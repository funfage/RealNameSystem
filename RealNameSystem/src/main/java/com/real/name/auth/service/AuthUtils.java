package com.real.name.auth.service;

import com.real.name.auth.entity.Role;
import com.real.name.auth.entity.User;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class AuthUtils {

    public final static Integer ProjectRoleId = 2;

    public final static String AdminRole = "admin";
    
    public final static String ProjectRole = "project";
    
    public final static String ContractRole = "contract";
    
    public final static String AccountRole = "account";
    
    public final static String GuestRole = "guest";

    /**
     * 根据id判断是否是项目管理员
     */
    public static boolean isContainProjectRoleById(Integer roleId) {
        return roleId == ProjectRoleId;
    }

    /**
     * 判断用户是否只包含项目管理员角色
     */
    public static boolean isOnlyProjectRole(User user) {
        return isContainProjectRole(user) && !isContainFuncRole(user);
    }

    /**
     * 判断某个用户是否只包含职能部角色
     */
    public static boolean isContainFuncRole(User user) {
        if (user == null) {
            throw new AttendanceException(ResultError.USER_UN_LOGIN);
        }
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (ContractRole.equals(role.getRoleName())
                    || AccountRole.equals(role.getRoleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个用户是否包含项目管理员角色
     */
    public static boolean isContainProjectRole(User user) {
        if (user == null) {
            throw new AttendanceException(ResultError.USER_UN_LOGIN);
        }
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (ProjectRole.equals(role.getRoleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个用户是否包含超级管理员角色
     */
    public static boolean isContainAdminRole(User user) {
        if (user == null) {
            throw new AttendanceException(ResultError.USER_UN_LOGIN);
        }
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (AdminRole.equals(role.getRoleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个用户是否包含访客角色
     */
    public static boolean isContainGuestRole(User user) {
        if (user == null) {
            throw new AttendanceException(ResultError.USER_UN_LOGIN);
        }
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (GuestRole.equals(role.getRoleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为response设置header，实现跨域
     */
    public static void setHeader(HttpServletRequest request, HttpServletResponse response) {
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
    }

}
