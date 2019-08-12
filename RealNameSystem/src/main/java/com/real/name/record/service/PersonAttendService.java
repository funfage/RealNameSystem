package com.real.name.record.service;

import com.real.name.record.query.PeriodTime;

import java.util.Date;
import java.util.List;

public interface PersonAttendService {

    /**
     * 删除人员某一天的刷卡记录
     */
    void deletePersonAttendInOneDay(Integer personId, Date dayStart, Date dayEnd);

    /**
     * 生成人员某一天的打卡记录
     */
    void createPersonAttendInOnDay(Integer personId, List<PeriodTime> periodTimes);

}
