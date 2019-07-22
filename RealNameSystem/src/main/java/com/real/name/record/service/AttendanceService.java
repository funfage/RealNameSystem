package com.real.name.record.service;

import com.real.name.record.entity.Attendance;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AttendanceService {

    Double countWorkerHours(List<Integer> projectDetailIds, Date begin, Date end);

    List<Attendance> findPeriodAttendByProjectDetailId(@Param("projectDetailId") Integer projectDetailId,
                                                       @Param("begin") Date begin,
                                                       @Param("end") Date end);

}
