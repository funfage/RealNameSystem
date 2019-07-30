package com.real.name.auth.service;

import java.util.Set;

public interface RoleService {

    /**
     * 获取某个用户所有角色
     */
    Set<String> getRolesByUserId(Integer userId);

}
