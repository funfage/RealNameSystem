package com.real.name.record.service.repository;

import com.real.name.record.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendanceMapper {

    int saveAttendance(Attendance attendance);

}
