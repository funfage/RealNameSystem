package com.real.name.record.service;

import com.real.name.common.result.ResultVo;
import com.real.name.record.query.PersonWorkRecord;
import com.real.name.record.query.ProjectAttendanceQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    /**
     * 超级管理员修改某个人员某个工作日的考勤信息
     * @param workHours 工时
     * @param startTime 开始工作时间
     * @param endTime 结束工作时间
     */
    void updateAndRecountAttendByAdmin(Long attendanceId, double workHours, Date startTime, Date endTime, String projectCode, Integer teamSysNo);

    /**
     * 获取人员的考勤
     */
    ResultVo getAttendance(Date startTime, Date endTime, String projectCode, Integer pageNum, Integer pageSize);

    /**
     * 搜素项目下的考勤
     */
    ResultVo searchAttendanceInPro(ProjectAttendanceQuery query);

    /**
     * 获取某个项目下某个时间段所有人员的出勤情况
     */
    ResultVo getPeopleWorkDayInProject(Date startTime, Date endTime, String projectCode, Integer pageNum, Integer pageSize);

    /**
     * 获取某项目下所有班组某个时间段的总工时
     */
    Map<String, Object> countPeriodGroupWorkHoursInProject(String projectCode, Date startDate, Date endDate);

    /**
     * 首页考勤数据累计
     */
    Map<String, Object> periodAttendInfoSum(Date startDate, Date endDate);

    /**
     * 生成报表
     */
    ResultVo exportRecords(Date startTime, Date endTime, String projectCode);

}
