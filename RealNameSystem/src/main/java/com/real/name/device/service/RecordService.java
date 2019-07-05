package com.real.name.device.service;

import com.real.name.device.entity.Record;

import java.util.Date;
import java.util.List;

public interface RecordService {

    /**
     * 查询人员考勤记录
     * @param personId 人员id
     * @param beginTime 开始时间
     * @param endTime 结束时间
     */
    List<Record> findByPersonIdAndTimeBetween(Integer personId, Date beginTime, Date endTime);
}
