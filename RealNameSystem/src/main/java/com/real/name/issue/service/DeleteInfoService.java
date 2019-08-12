package com.real.name.issue.service;

import com.real.name.issue.entity.DeleteInfo;
import org.apache.ibatis.annotations.Param;

public interface DeleteInfoService {

    int saveDeleteInfo(DeleteInfo deleteInfo);

    /**
     * 根据人员id删除
     */
    int deleteByPersonId(@Param("personId") Integer personId);

    /**
     * 根据人员id和设备id删除
     */
    int deleteByCondition(Integer personId, String deviceId);



}
