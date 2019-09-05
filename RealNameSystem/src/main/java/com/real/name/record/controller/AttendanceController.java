package com.real.name.record.controller;

import com.alibaba.fastjson.JSONArray;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.TimeUtil;
import com.real.name.common.utils.XSSFExcelUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.GroupAttend;
import com.real.name.record.query.*;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.GroupAttendService;
import com.real.name.record.service.PersonAttendService;
import com.real.name.record.service.ProjectAttendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/checkAttend")
public class AttendanceController {

    private Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private GroupAttendService groupAttendService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectAttendService projectAttendService;

    @Autowired
    private PersonAttendService personAttendService;

    /**
     * 修改人员考勤信息，只有超级管理员可以操作
     */
    @Transactional
    @PostMapping("/modifyAttend")
    public ResultVo modifyAttend(@RequestParam("personId") Integer personId,
                                 @RequestParam("projectCode") String projectCode,
                                 @RequestParam("teamSysNo") Integer teamSysNo,
                                 @RequestParam("workDate") Date workDate,
                                 @RequestParam("attendanceId") Long attendanceId,
                                 @RequestParam("periodTimes") String periodTimesStr) {
        List<PeriodTime> periodTimes;
        try {
            periodTimes = JSONArray.parseArray(periodTimesStr, PeriodTime.class);
        } catch (Exception e) {
            logger.error("json转换为PeriodTime数组失败");
            return ResultVo.failure();
        }
        //计算工时
        long totalTime = 0L;
        Long minTime = Long.MAX_VALUE;
        Long maxTime = 0L;
        for (PeriodTime periodTime : periodTimes) {
            Long startTime = periodTime.getStartTime();
            Long endTime = periodTime.getEndTime();
            if (startTime == null || endTime == null) {
                throw new AttendanceException(ResultError.PERIOD_TIME_NULL);
            } else if (startTime <= endTime) {
                totalTime += periodTime.getEndTime() - periodTime.getStartTime();
            } else {
                throw new AttendanceException(ResultError.PERIOD_TIME_ERROR);
            }
            if (minTime > startTime) {
                minTime = startTime;
            }
            if (maxTime < endTime) {
                maxTime = endTime;
            }
        }
        //修改人员出勤情况，并重新统计项目和班组当日考勤情况
        attendanceService.updateAndRecountAttendByAdmin(attendanceId, TimeUtil.getHours(totalTime),
                new Date(minTime), new Date(maxTime), projectCode, teamSysNo);
        //删除人员当天的打卡记录
        Date dateBegin = TimeUtil.getDateBegin(workDate);
        Date dateEnd = TimeUtil.getDateEnd(workDate);
        personAttendService.deletePersonAttendInOneDay(personId, dateBegin, dateEnd);
        //重新生成打卡记录
        personAttendService.createPersonAttendInOnDay(personId, periodTimes);
        return ResultVo.success();
    }

    /**
     * 查询某个人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    @JSONS(
            @JSON(type = ProjectWorkRecord.class, filter = "projectCode,projectName,createTime,startDate,endDate")
    )
    @GetMapping("/getAttendance")
    public ResultVo getAttendance(@RequestParam(name = "projectCode") String projectCode,
                                  @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "40") Integer pageSize) {
        Date startDate = TimeUtil.getMonthFirstDay();
        Date endDate = new Date(System.currentTimeMillis());
        return attendanceService.getAttendance(startDate, endDate, projectCode, pageNum, pageSize);
    }

    @GetMapping("/searchAttendanceInPro")
    public ResultVo searchAttendanceInPro(ProjectAttendanceQuery query) {
        if (StringUtils.isEmpty(query.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目");
        }
        return attendanceService.searchAttendanceInPro(query);
    }

    /**
     * 获取某项目下所有班组每周的总工时
     */
    @GetMapping("/countWorkerGroupHours")
    public ResultVo countWorkerGroupHours(String projectCode) {
        Map<String, Object> map = new HashMap<>();
        //获取该项目下所有班组信息
        List<ProjectDetailQuery> workerGroupInProject = projectDetailQueryService.getWorkerGroupInProject(projectCode);
        for (ProjectDetailQuery projectDetailQuery : workerGroupInProject) {
            WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
            //获取某个班组的总工时
            Double hours = groupAttendService.countGroupHoursInPeriod(workerGroup.getTeamSysNo(), TimeUtil.getTimesWeekBegin(), TimeUtil.getTimesWeekEnd());
            if (hours == null) {
                hours = 0.0;
            }
            //获取班组下人数
            Integer groupNum = projectDetailQueryService.getPersonNumInGroup(workerGroup.getTeamSysNo());
            //获取班组下总工时
            map.put(workerGroup.getTeamName(), groupNum + "," + hours);
        }
        return ResultVo.success(map);
    }

    /**
     * 获取某个项目下某个时间段所有人员的出勤情况
     */
    @GetMapping("/getPersonWorkDay")
    public ResultVo getPeopleWorkDayInProject(@RequestParam("projectCode") String projectCode,
                                     @RequestParam(value = "startDate", required = false) Date startDate,
                                     @RequestParam(value = "endDate", required = false) Date endDate,
                                     @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (startDate == null) {
            startDate = TimeUtil.getMonthFirstDay();
        }
        if (endDate == null) {
            endDate = TimeUtil.getNextMonthFirstDay();
        }
        return attendanceService.getPeopleWorkDayInProject(startDate, endDate, projectCode, pageNum, pageSize);
    }

    /**
     * 获取某个班组一个月内每天的工时
     */
    @GetMapping("/getGroupHoursInMonth")
    public ResultVo getGroupHoursInMonth(Integer teamSysNo) {
        Date monthFirstDay = TimeUtil.getMonthFirstDay();
        Date monthLastDay = TimeUtil.getNextMonthFirstDay();
        List<GroupAttend> groupAttendInfo = groupAttendService.getGroupAttendPeriodInfo(teamSysNo, monthFirstDay, monthLastDay);
        return ResultVo.success(groupAttendInfo);
    }

    /**
     * 获取项目本月或上个月班组的工时
     */
    @GetMapping("/getProjectGroupHours")
    public ResultVo getProjectGroupHours(@RequestParam("projectCode") String projectCode) {
        Map<String, Object> map = new HashMap<>();
        //上个月
        Map<String, Object> lastMonth = attendanceService.countPeriodGroupWorkHoursInProject(projectCode, TimeUtil.getLastMonthFirstDay(), TimeUtil.getMonthFirstDay());
        //这个月
        Map<String, Object> thisMonth = attendanceService.countPeriodGroupWorkHoursInProject(projectCode, TimeUtil.getMonthFirstDay(), TimeUtil.getNextMonthFirstDay());
        map.put("lastMonth", lastMonth);
        map.put("thisMonth", thisMonth);
        return ResultVo.success(map);
    }

    /**
     * 首页考勤数据累计
     * @param status 0:统计昨天的考勤
     *               1:统计这个月开始到昨天的考勤
     *               2:统计今年开始到昨天的考勤
     *               3:统计所有考勤
     */
    @GetMapping("/mainPageSumAttendInfo")
    public ResultVo mainPageSumAttendInfo(@RequestParam(name = "status", defaultValue = "0") Integer status) {
        Date startDate = null;
        if (status == 0) {
            startDate = TimeUtil.getYesterdayBegin();
        } else if (status == 1) {
            startDate = TimeUtil.getMonthFirstDay();
        } else if (status == 2) {
            startDate = TimeUtil.getYearFirstDay();
        } else if (status > 3){
            return ResultVo.failure();
        }
        Date endDate = TimeUtil.getTodayBegin();
        return ResultVo.success(attendanceService.periodAttendInfoSum(startDate, endDate));
    }

    /**
     * 首页项目考勤top榜
     * @param status 0按人数升序
     *               1按考勤工时升序
     *               2按考勤人次升序
     *               3按异常考勤次数升序
     */
    @GetMapping("/mainPageProjectAttendRange")
    public ResultVo mainPageProjectAttendRange(@RequestParam(name = "status", defaultValue = "0") Integer status) {
        Date startTime = TimeUtil.getMonthBefore();
        Date endTime = TimeUtil.getTodayBegin();
        List<ProjectAttendQuery> data;
        if (status == 0) {
            data = projectAttendService.rangeByPersonNum();
        } else if (status == 1) {
            data = projectAttendService.rangeBySumWorkHours(startTime, endTime);
        } else if (status == 2) {
            data = projectAttendService.rangeBySumAttendNum(startTime, endTime);
        } else if (status == 3) {
            data = projectAttendService.rangeBySumAttendErrNum(startTime, endTime);
        } else {
            return ResultVo.failure();
        }
        return ResultVo.success(data);
    }

    /**
     * 生成考勤数据的报表
     */
    @GetMapping("exportRecords")
    public ResultVo exportRecords(@RequestParam("startDate") Date startDate,
                                  @RequestParam("endDate") Date endDate,
                                  @RequestParam("projectCode") String projectCode) {
        return attendanceService.exportRecords(startDate, endDate, projectCode);
    }




}














