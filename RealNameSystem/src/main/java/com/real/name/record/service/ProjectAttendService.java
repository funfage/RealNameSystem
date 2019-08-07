package com.real.name.record.service;

import com.real.name.record.query.ProjectAttendQuery;

import java.util.Date;
import java.util.List;

public interface ProjectAttendService {

    /**
     * 按照项目总人数升序
     */
    List<ProjectAttendQuery> rangeByPersonNum();

    /**
     * 按照项目考勤工时升序
     */
    List<ProjectAttendQuery> rangeBySumWorkHours(Date startTime, Date endTime);

    /**
     * 按照项目考勤次数升序
     */
    List<ProjectAttendQuery> rangeBySumAttendNum(Date startTime, Date endTime);

    /**
     * 按照项目考勤异常次数升序
     */
    List<ProjectAttendQuery> rangeBySumAttendErrNum(Date startTime, Date endTime);

}
