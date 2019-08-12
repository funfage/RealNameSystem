package com.real.name.record.service.repository;

import com.real.name.record.entity.PersonAttend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface PersonAttendMapper {

    /**
     * 保存personAttend
     */
    int savePersonAttend(@Param("personAttend") PersonAttend personAttend);

    /**
     * 删除人员某一天的刷卡记录
     */
    int deletePersonAttendInOneDay(@Param("personId") Integer personId,
                                   @Param("dayStart") Date dayStart,
                                   @Param("dayEnd") Date dayEnd);

}

















