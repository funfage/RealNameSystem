package com.real.name.auth;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.repository.UserMapper;
import com.real.name.others.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void saveUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setStatus(1);
        userMapper.saveUser(user);
    }

    @Test
    public void getUserByUsername() {
        User admin = userMapper.getUserByPhone("admin");
        System.out.println(admin);
    }

    @Test
    public void getUserPermissions() {
        List<String> permissions = userMapper.getUserPermissionIds(1);
        System.out.println(permissions);
    }

    @Test
    public void getUserByPassword() {
        User user = userMapper.getUserByPassword("admin");
        System.out.println(user);
    }

    @Test
    public void getUserProject() {
        Set<String> projectList = userMapper.getUserProject(8);
        System.out.println(projectList);
    }

    @Test
    public void saveUserRole() {
        int i = userMapper.saveUserRole(1, 3);
        System.out.println(i);
    }

    @Test
    public void getUsers() {
        List<User> users = userMapper.getUsers();
        System.out.println(users);
    }

    @Test
    public void saveUserProjects() {
        User user = new User();
        user.setUserId(6);
        Set<String> projectSet = new HashSet<>();
        projectSet.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        projectSet.add("44010620190510008");
        user.setProjectSet(projectSet);
        userMapper.saveUserProjects(user.getUserId(), user.getProjectSet());
    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setUserId(19);
        user.setPassword("aaa");
        int i = userMapper.updateUser(user);
        System.out.println(i);
    }

    @Test
    public void deleteUserByUserId() {
        int i = userMapper.deleteUserByUserId(19);
        System.out.println(i);
    }

    @Test
    public void updateUserRole() {
        User user = new User();
        user.setUserId(20);
        user.setRoleId(2);
        int i = userMapper.updateUserRole(user.getUserId(), user.getRoleId());
        System.out.println(i);
    }

    @Test
    public void getUserByUserId() {
        User userByUserId = userMapper.getUserByUserId(20);
        System.out.println(userByUserId);
    }

    @Test
    public void deleteUserProjectByUserId() {
        int i = userMapper.deleteUserProjectByUserId(20);
        System.out.println(i);
    }

    @Test
    public void countUserNum() {
        Integer integer = userMapper.countUserNum();
        System.out.println(integer);
    }

    @Test
    public void countTodayUserNum() {
        Integer integer = userMapper.countTodayUserNum();
        System.out.println(integer);
    }

}










