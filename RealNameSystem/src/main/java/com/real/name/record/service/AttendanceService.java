package com.real.name.record.service;

import com.real.name.record.query.PersonWorkRecord;

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
     * 查询某个人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<PersonWorkRecord> findPersonPeriodWorkInfoInProject(Date startDate, Date endDate, String projectCode);

    /**
     * 分页查询人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<PersonWorkRecord> findPagePersonPeriodWorkInfoInProject(Date startTime, Date endTime, String projectCode, Integer offset, Integer limit);

    /**
     * 获取某项目下所有班组某个时间段的总工时
     */
    Map<String, Object> countPeriodGroupWorkHoursInProject(String projectCode, Date startDate, Date endDate);

    /**
     * 首页考勤数据累计
     */
    Map<String, Object> periodAttendInfoSum(Date startDate, Date endDate);

}
