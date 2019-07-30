package com.real.name.auth.service.implement;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.auth.service.UserService;
import com.real.name.auth.service.repository.PermissionMapper;
import com.real.name.auth.service.repository.UserMapper;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional
    public void registerUser(User user) {
        //先将用户设置为无效
        user.setStatus(0);
        //保存用户
        int effNum = userMapper.saveUser(user);
        if (effNum <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
        //保存用户角色关联
        effNum = userMapper.saveUserRole(user.getUserId(), user.getRoleId());
        if (effNum < 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
        boolean containProjectRole = AuthUtils.isContainProjectRoleById(user.getRoleId());
        if (containProjectRole) {
            //保存项目管理员与项目的关联
            userMapper.saveUserProjects(user.getUserId(), user.getProjectSet());
        }
    }

    @Override
    public void updateUser(User user) {
        int i = userMapper.updateUser(user);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateUserByAdmin(User user) {
        //查询用户原本信息
        User selectUser = userMapper.getUserByUserId(user.getUserId());
        //更新用户角色信息
        if (user.getRoleId() != null) {
            //如果选择的是项目管理员
            if (user.getRoleId() == AuthUtils.ProjectRoleId) {
                //删除原本关联的项目信息
                userMapper.deleteUserProjectByUserId(user.getUserId());
                //保存用户与项目新的关联
                if (user.getProjectSet().size() > 0) {
                    userMapper.saveUserProjects(user.getUserId(), user.getProjectSet());
                }
            } else if (AuthUtils.isContainProjectRole(selectUser)) {//如果用户原本是项目管理员被修改为不是项目管理员
                //删除原本关联的项目信息
                userMapper.deleteUserProjectByUserId(user.getUserId());
            }
            int i = userMapper.updateUserRole(user.getUserId(), user.getRoleId());
            if (i <= 0) {
                throw new AttendanceException(ResultError.UPDATE_ERROR);
            }
        }
        //更新用户信息
        int i = userMapper.updateUser(user);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Override
    public int deleteUserByUserId(Integer userId) {
        return userMapper.deleteUserByUserId(userId);
    }

    @Override
    public List<User> getUsers() {
        return userMapper.getUsers();
    }

    @Override
    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    public Set<String> getUserPermissions(Integer userId) {
        List<String> permissionIdsList = userMapper.getUserPermissionIds(userId);
        Set<Integer> permissionIdsSet = new HashSet<>();
        for (String permissionIds : permissionIdsList) {
            if (StringUtils.hasText(permissionIds)) {
                String[] idList = permissionIds.split(",");
                for (String permissionId : idList) {
                    permissionIdsSet.add(Integer.parseInt(permissionId));
                }
            }
        }
        if (permissionIdsSet.size() > 0) {
            return permissionMapper.getPermissionInSet(permissionIdsSet);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByPassword(String password) {
        return userMapper.getUserByPassword(password);
    }

    @Override
    public Set<String> getUserProject(Integer userId) {
        return userMapper.getUserProject(userId);
    }

    @Override
    public int saveUserRole(Integer userId, Integer roleId) {
        return userMapper.saveUserRole(userId, roleId);
    }


}
















