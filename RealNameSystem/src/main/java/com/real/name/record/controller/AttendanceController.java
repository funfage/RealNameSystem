package com.real.name.record.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.PageUtils;
import com.real.name.common.utils.TimeUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.GroupAttend;
import com.real.name.record.entity.WorkDayInfo;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.GroupAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/checkAttend")
public class AttendanceController {

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private GroupAttendService groupAttendService;

    @GetMapping("/getAttendance")
    public ResultVo getAttendance() {
        Date start = TimeUtil.initDateByMonth();
        Date end = new Date(System.currentTimeMillis());
        List<ProjectDetailQuery> queryList = projectDetailQueryService.findPersonWorkHoursInfo(start, end);
        return ResultVo.success(queryList);
    }

    /**
     * 获取某项目下所有 班组每周的总工时
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
     * 获取某个项目下所有人员的出勤情况
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
            endDate = TimeUtil.getMonthLastDay();
        }
        PageHelper.startPage(pageNum + 1, pageSize);
        //查询该项目下所有project_detail_id和人员信息
        List<ProjectDetailQuery> projectDetailQueries = projectDetailQueryService.findIdAndPersonInProject(projectCode);
        PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(projectDetailQueries);
        List<WorkDayInfo> workDayInfoList = new ArrayList<>();
        for (ProjectDetailQuery projectDetailQuery : projectDetailQueries) {
            Person person = projectDetailQuery.getPerson();
            List<Attendance> attendanceList = attendanceService.findPeriodAttendByProjectDetailId(projectDetailQuery.getId(), startDate, endDate);
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
            workDayInfo.setPersonName(person.getPersonName());
            workDayInfo.setIdCardNumber(person.getIdCardNumber());
            workDayInfo.setWorkType(person.getWorkType());
            workDayInfo.setSubordinateCompany(person.getSubordinateCompany());
            workDayInfo.setTeamName(projectDetailQuery.getWorkerGroup().getTeamName());
            workDayInfo.setWorkDay(workDays);
            workDayInfo.setWorkHours(hours);
            workDayInfoList.add(workDayInfo);
        }
        return PageUtils.pageResult(pageInfo, workDayInfoList);
    }

    /**
     * 获取某个班组一个月内每天的工时
     */
    @GetMapping("/getGroupHoursInMonth")
    public ResultVo getGroupHoursInMonth(Integer teamSysNo) {
        Date monthFirstDay = TimeUtil.getMonthFirstDay();
        Date monthLastDay = TimeUtil.getMonthLastDay();
        List<GroupAttend> groupAttendInfo = groupAttendService.getGroupAttendPeriodInfo(teamSysNo, monthFirstDay, monthLastDay);
        return ResultVo.success(groupAttendInfo);
    }

}














