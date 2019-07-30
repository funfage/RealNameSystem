package com.real.name.auth.service;

import com.real.name.auth.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    /**
     * 保存用户信息
     */
    void registerUser(User user);

    /**
     * 更新用户信息
     */
    void updateUser(User user);

    /**
     * 管理员修改用户信息
     */
    void updateUserByAdmin(User user);

    /**
     * 根据用户id删除用户
     */
    int deleteUserByUserId(Integer userId);

    /**
     * 查询所有用户
     */
    List<User> getUsers();

    /**
     *  根据用户名查询用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 获取某个用户所有权限id
     */
    Set<String> getUserPermissions(Integer userId);

    /**
     * 根据密码查询用户
     */
    User getUserByPassword(String password);

    /**
     * 查询用户所帮定的项目id
     */
    Set<String> getUserProject(Integer userId);

    /**
     * 保存用户与角色关联信息
     */
    int saveUserRole(Integer userId, Integer roleId);

}
