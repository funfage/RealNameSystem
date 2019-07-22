package com.real.name.record.service.repository;

import com.real.name.record.entity.GroupAttend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface GroupAttendMapper {

    int saveGroupAttend(GroupAttend groupAttend);

    Double countGroupHoursInPeriod(@Param("teamSysNo") Integer teamSysNo, @Param("begin") Date begin, @Param("end") Date end);

    List<GroupAttend> getGroupAttendPeriodInfo(@Param("teamSysNo") Integer teamSysNo, @Param("begin") Date begin, @Param("end") Date end);

}
