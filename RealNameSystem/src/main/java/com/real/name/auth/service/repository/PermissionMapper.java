package com.real.name.auth.service.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface PermissionMapper {

    /**
     * 获取属于set集合中的权限
     */
    Set<String> getPermissionInSet(@Param("idSet") Set<Integer> idSet);

}
