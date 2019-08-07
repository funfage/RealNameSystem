package com.real.name.record.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.PageUtils;
import com.real.name.common.utils.TimeUtil;
import com.real.name.common.utils.XSSFExcelUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import com.real.name.record.entity.*;
import com.real.name.record.query.ProjectAttendQuery;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.GroupAttendService;
import com.real.name.record.service.ProjectAttendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Time;
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
        List<PersonWorkRecord> personPeriodWorkInfoInProject = attendanceService.findPagePersonPeriodWorkInfoInProject(startDate, endDate, projectCode, pageNum, pageSize);
        ProjectWorkRecord projectWorkRecord = createProjectWorkRecord(startDate, endDate, projectCode, personPeriodWorkInfoInProject);
        Integer total = projectDetailQueryService.countPersonNumByProjectCode(projectCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum + 1);
        map.put("pageSize", pageSize > total ? total : pageSize);
        map.put("total", total);
        map.put("data", projectWorkRecord);
        return ResultVo.success(map);
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
    public ResultVo getPersonWorkDay(@RequestParam("projectCode") String projectCode,
                                     Date startDate,
                                     Date endDate,
                                     @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (startDate == null) {
            startDate = TimeUtil.getMonthFirstDay();
        }
        if (endDate == null) {
            endDate = TimeUtil.getNextMonthFirstDay();
        }
        List<WorkDayInfo> workDayInfoList = new ArrayList<>();
        List<PersonWorkRecord> personPeriodWorkInfoInProject = attendanceService.findPagePersonPeriodWorkInfoInProject(startDate, endDate, projectCode, pageNum, pageSize);
        for (PersonWorkRecord personWorkRecord : personPeriodWorkInfoInProject) {
            List<Attendance> attendanceList = personWorkRecord.getAttendanceList();
            int workDays = 0;
            Double hours = 0.0;
            //整合员工出勤天数和工时
            for (Attendance attendance : attendanceList) {
                if (attendance.getWorkHours() != 0) {
                    workDays++;
                    hours += attendance.getWorkHours();
                }
            }
            WorkDayInfo workDayInfo = new WorkDayInfo();
            workDayInfo.setPersonName(personWorkRecord.getPersonName());
            workDayInfo.setIdCardNumber(personWorkRecord.getIdCardNumber());
            workDayInfo.setWorkType(personWorkRecord.getWorkType());
            workDayInfo.setSubordinateCompany(personWorkRecord.getSubordinateCompany());
            workDayInfo.setTeamName(personWorkRecord.getTeamName());
            workDayInfo.setWorkDay(workDays);
            workDayInfo.setWorkHours(hours);
            workDayInfoList.add(workDayInfo);
        }
        //查询记录总条数
        Integer total = projectDetailQueryService.countPersonNumByProjectCode(projectCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum + 1);
        map.put("pageSize", pageSize > total ? total : pageSize);
        map.put("total", total);
        map.put("data", workDayInfoList);
        return ResultVo.success(map);
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
     * @param status 0上个月 1本月
     */
    @GetMapping("/getProjectGroupHours")
    public ResultVo getProjectGroupHours(@RequestParam("projectCode") String projectCode,
                                         @RequestParam(name = "status", defaultValue = "1") Integer status) {
        if (status == 0) {
            Map<String, Object> map = attendanceService.countPeriodGroupWorkHoursInProject(projectCode, TimeUtil.getLastMonthFirstDay(), TimeUtil.getMonthFirstDay());
            return ResultVo.success(map);
        } else {
            Map<String, Object> map = attendanceService.countPeriodGroupWorkHoursInProject(projectCode, TimeUtil.getMonthFirstDay(), TimeUtil.getNextMonthFirstDay());
            return ResultVo.success(map);
        }
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
        List<PersonWorkRecord> personPeriodWorkInfoInProject = attendanceService.findPersonPeriodWorkInfoInProject(startDate, endDate, projectCode);
        ProjectWorkRecord projectWorkRecord = createProjectWorkRecord(startDate, endDate, projectCode, personPeriodWorkInfoInProject);
        List<String> headList = new ArrayList<>();
        headList.add("姓名");
        headList.add("身份证号码");
        headList.add("性别");
        headList.add("所属公司");
        headList.add("班组");
        headList.add("工种");
        headList.add("出勤天数");
        headList.add("考勤小时");
        List<String> dayBetweenFormat = TimeUtil.getDayBetweenFormat(startDate, endDate);
        //生成报表
        try {
            XSSFExcelUtils.exportXSSFExcel(projectWorkRecord, headList, dayBetweenFormat);
        } catch (IOException e) {
            logger.error("生成报表失败, projectName:{}, e:{}", projectWorkRecord.getProjectName(), e);
            return ResultVo.failure(ResultError.GENERATE_EXCEL_ERROR);
        }
        return ResultVo.success(projectWorkRecord.getProjectCode());
    }


    private ProjectWorkRecord createProjectWorkRecord(Date startDate, Date endDate, String projectCode, List<PersonWorkRecord> personPeriodWorkInfoInProject) {
        //查询项目名
        String projectName = projectService.findProjectName(projectCode);
        if (StringUtils.isEmpty(projectName)) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        ProjectWorkRecord projectWorkRecord = new ProjectWorkRecord();
        projectWorkRecord.setProjectCode(projectCode);
        projectWorkRecord.setProjectName(projectName);
        projectWorkRecord.setStartDate(startDate);
        projectWorkRecord.setEndDate(endDate);
        projectWorkRecord.setCreateTime(new Date());
        for (PersonWorkRecord record : personPeriodWorkInfoInProject) {
            List<Attendance> attendanceList = record.getAttendanceList();
            double allWorkHours = 0.0;
            int workDays = 0;
            for (Attendance attendance : attendanceList) {
                if (attendance.getStatus() == 1) {
                    if (attendance.getWorkHours() != null && attendance.getWorkHours() > 0) {
                        allWorkHours += attendance.getWorkHours();
                        workDays++;
                    }
                }
            }
            record.setWorkDay(workDays);
            record.setWorkHours(allWorkHours);
        }
        projectWorkRecord.setPersonWorkRecordList(personPeriodWorkInfoInProject);
        return projectWorkRecord;
    }

}














