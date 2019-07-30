package com.real.name.auth.service;

import com.real.name.auth.service.repository.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> getRolesByUserId(Integer userId) {
        return roleMapper.getRolesByUserId(userId);
    }

}











