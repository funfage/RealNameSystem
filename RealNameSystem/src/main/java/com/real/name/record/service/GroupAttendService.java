package com.real.name.record.service;

import com.real.name.record.entity.GroupAttend;

import java.util.Date;
import java.util.List;

public interface GroupAttendService {

    /**
     * 查询班组一段时间的出勤工时
     */
    Double countGroupHoursInPeriod(Integer teamSysNo, Date begin, Date end);

    /**
     * 查询某个时间段班组的出勤信息
     */
    List<GroupAttend> getGroupAttendPeriodInfo(Integer teamSysNo, Date begin, Date end);


}
