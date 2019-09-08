package com.real.name.auth.service.repository;

import com.real.name.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    /**
     * 保存用户信息
     */
    int saveUser(@Param("user") User user);

    /**
     * 保存项目管理员与项目信息
     */
    int saveUserProjects(@Param("userId") Integer userId,
                         @Param("projectSet") Set<String> projectSet);

    /**
     * 更新用户信息
     */
    int updateUser(@Param("user") User user);

    /**
     * 根据电话号码更新用户信息
     */
    int updateUserByPhone(@Param("user") User user);

    /**
     * 更新用户角色
     */
    int updateUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 根据用户id删除用户
     */
    int deleteUserByUserId(Integer userId);

    /**
     * 保存用户与角色关联信息
     */
    int saveUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 删除用户与项目的关联
     */
    int deleteUserProjectByUserId(@Param("userId") Integer userId);

    /**
     * 查询所有用户
     */
    List<User> getUsers();

    /**
     * 根据用户id查询用户
     */
    User getUserByUserId(@Param("userId") Integer userId);

    /**
     *  根据用户名查询用户信息
     */
    User getUserByPhone(@Param("phone") String phone);

    /**
     * 查询某个用户所拥有的权限id
     */
    List<String> getUserPermissionIds(Integer userId);

    /**
     * 根据密码查询用户
     */
    User getUserByPassword(@Param("password") String password);

    /**
     * 查询用户所帮定的项目id
     */
    Set<String> getUserProject(@Param("userId") Integer userId);

    /**
     * 获取总用户数量
     */
    Integer countUserNum();

    /**
     * 获取当日注册的用户数量
     */
    Integer countTodayUserNum();



}
