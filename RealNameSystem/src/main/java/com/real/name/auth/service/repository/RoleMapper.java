package com.real.name.auth.service.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface RoleMapper {

    /**
     * 获取某个用户所有角色
     */
    Set<String> getRolesByUserId(@Param("userId") Integer userId);

}


















