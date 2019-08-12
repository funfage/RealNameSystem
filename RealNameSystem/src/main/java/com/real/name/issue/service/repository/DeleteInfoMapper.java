package com.real.name.issue.service.repository;

import com.real.name.issue.entity.DeleteInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeleteInfoMapper {

    /**
     * 添加记录
     */
    int saveDeleteInfo(DeleteInfo deleteInfo);

    /**
     * 查询所有记录
     */
    List<DeleteInfo> findAll();

    /**
     * 删除
     */
    int deleteById(Long id);

    /**
     * 根据人员id删除
     */
    int deleteByPersonId(@Param("personId") Integer personId);

    /**
     * 根据人员id和设备id删除
     */
    int deleteByCondition(@Param("personId") Integer personId, @Param("deviceId") String deviceId);

}
