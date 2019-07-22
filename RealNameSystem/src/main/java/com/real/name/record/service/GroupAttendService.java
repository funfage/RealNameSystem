package com.real.name.record.service;

import com.real.name.record.entity.GroupAttend;

import java.util.Date;
import java.util.List;

public interface GroupAttendService {

    int saveGroupAttend(GroupAttend groupAttend);


    Double countGroupHoursInPeriod(Integer teamSysNo, Date begin, Date end);

    List<GroupAttend> getGroupAttendPeriodInfo(Integer teamSysNo, Date begin, Date end);


}
