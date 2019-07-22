package com.real.name.record.service.repository;

import com.real.name.record.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AttendanceMapper {

    int saveAttendance(Attendance attendance);

    /**
     * 统计某个班组的工时
     */
    Double countWorkerHours(@Param("projectDetailIds") List<Integer> projectDetailIds,
                            @Param("begin")Date weekBegin,
                            @Param("end") Date weekEnd);

    List<Attendance> findPeriodAttendByProjectDetailId(@Param("projectDetailId") Integer projectDetailId,
                                                       @Param("begin") Date begin,
                                                       @Param("end") Date end);


}
