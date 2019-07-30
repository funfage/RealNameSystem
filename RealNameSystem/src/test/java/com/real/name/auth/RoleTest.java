package com.real.name.auth;

import com.real.name.auth.service.repository.RoleMapper;
import com.real.name.others.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class RoleTest extends BaseTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void getRolesByUserId() {
        Set<String> roles = roleMapper.getRolesByUserId(1);
        System.out.println(roles);
    }

}
