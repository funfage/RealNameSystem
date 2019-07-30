package com.real.name.auth;

import com.real.name.auth.service.repository.PermissionMapper;
import com.real.name.others.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class PermissionTest extends BaseTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void getPermissionInSet() {
        Set<Integer> idSet = new HashSet<>();
        idSet.add(1);
        idSet.add(2);
        idSet.add(3);
        Set<String> permission = permissionMapper.getPermissionInSet(idSet);
        System.out.println(permission);
    }



}
