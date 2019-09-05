package com.real.name.record.service.implement;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.TimeUtil;
import com.real.name.common.utils.XSSFExcelUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.record.entity.Attendance;
import com.real.name.record.query.PersonWorkRecord;
import com.real.name.record.query.ProjectAttendanceQuery;
import com.real.name.record.query.ProjectWorkRecord;
import com.real.name.record.query.WorkDayInfo;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.GroupAttendMapper;
import com.real.name.record.service.repository.ProjectAttendMapper;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Transactional
    @Override

    public void updateAndRecountAttendByAdmin(Long attendanceId, double workHours, Date startTime, Date endTime, String projectCode, Integer teamSysNo) {
        //查询attendanceId查询attendance
        Attendance selectAttend = attendanceMapper.findByAttendanceId(attendanceId);
        //计算差值
        double hours = workHours - selectAttend.getWorkHours();
        Integer status = selectAttend.getStatus();
        Integer attendNum = 0;
        Integer attendErrNum = 0;
        if (status != 1) {
            attendNum = 1;
            attendErrNum = -1;
        }
        //重新统计项目和班组当天的考勤累计
        Date dateBegin = TimeUtil.getDateBegin(selectAttend.getWorkTime());
        Date dateEnd = TimeUtil.getDateEnd(selectAttend.getWorkTime());
        projectAttendMapper.updateByProjectCode(projectCode, hours, attendNum, attendErrNum, dateBegin, dateEnd);
        groupAttendMapper.updateByTeamSysNo(teamSysNo, hours, attendNum, attendErrNum, dateBegin, dateEnd);
        //修改人员考勤
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(attendanceId);
        attendance.setWorkHours(workHours);
        attendance.setStartTime(startTime);
        attendance.setEndTime(endTime);
        attendance.setStatus(1);
        int i = attendanceMapper.updateAttendanceByAttendanceId(attendance);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Override
    public ResultVo getAttendance(Date startTime, Date endTime, String projectCode, Integer pageNum, Integer pageSize) {
        int offset = pageNum * pageSize;
        int limit = pageSize;
        //分页查询人员在指定时间的考勤
        List<PersonWorkRecord> personWorkRecords = attendanceMapper.findPagePersonPeriodWorkInfoInProject(startTime, endTime, projectCode, offset, limit);
        ProjectWorkRecord projectWorkRecord = createProjectWorkRecord(startTime, endTime, projectCode, personWorkRecords);
        Integer total = attendanceMapper.countPersonPeriodWorkInfoInProject(startTime, endTime, projectCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum + 1);
        map.put("pageSize", pageSize > total ? total : pageSize);
        map.put("total", total);
        map.put("data", projectWorkRecord);
        return ResultVo.success(map);
    }

    @Override
    public ResultVo searchAttendanceInPro(ProjectAttendanceQuery query) {
        Date startTime;
        Date endTime;
        if (query.getSearchDate() != null) {
            startTime = TimeUtil.getDateBegin(query.getSearchDate());
            endTime = TimeUtil.getDateEnd(query.getSearchDate());
        } else {
            startTime = TimeUtil.getMonthFirstDay();
            endTime = TimeUtil.getMonthLastDay();
        }
        int offset = query.getPageNum() * query.getPageSize();
        int limit = query.getPageSize();
        List<PersonWorkRecord> personWorkRecords = attendanceMapper.searchAttendanceInPro(query.getProjectCode(), query.getSubContractorId(),
                query.getPersonName(), startTime, endTime, offset, limit);
        Integer total = attendanceMapper.countSearchAttendanceInPro(query.getProjectCode(), query.getSubContractorId(), query.getPersonName(), startTime, endTime);
        Map<String, Object> map = new HashMap<>();
        map.put("data", personWorkRecords);
        map.put("pageNum", query.getPageNum());
        map.put("pageSize", query.getPageSize() > total ? total : query.getPageSize());
        map.put("total", total);
        return ResultVo.success(map);
    }

    @Override
    public ResultVo getPeopleWorkDayInProject(Date startTime, Date endTime, String projectCode, Integer pageNum, Integer pageSize) {
        List<WorkDayInfo> workDayInfoList = new ArrayList<>();
        int offset = pageNum * pageSize;
        int limit = pageSize;
        List<PersonWorkRecord> personPeriodWorkInfoInProject = attendanceMapper.findPagePersonPeriodWorkInfoInProject(startTime, endTime, projectCode, offset, limit);
        for (PersonWorkRecord personWorkRecord : personPeriodWorkInfoInProject) {
            List<Attendance> attendanceList = personWorkRecord.getAttendanceList();
            Map<String, Object> map = countPersonPeriodWorkInfo(attendanceList);
            WorkDayInfo workDayInfo = new WorkDayInfo();
            workDayInfo.setPersonName(personWorkRecord.getPersonName());
            workDayInfo.setIdCardNumber(personWorkRecord.getIdCardNumber());
            workDayInfo.setWorkType(personWorkRecord.getWorkType());
            workDayInfo.setSubordinateCompany(personWorkRecord.getSubordinateCompany());
            workDayInfo.setTeamName(personWorkRecord.getTeamName());
            workDayInfo.setWorkDay((Integer) map.get("workDays"));
            workDayInfo.setWorkHours((Double) map.get("allWorkHours"));
            workDayInfo.setPersonStatus(personWorkRecord.getPersonStatus());
            workDayInfoList.add(workDayInfo);
        }
        //查询记录总条数
        Integer total = attendanceMapper.countPersonPeriodWorkInfoInProject(startTime, endTime, projectCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum + 1);
        map.put("pageSize", pageSize > total ? total : pageSize);
        map.put("total", total);
        map.put("data", workDayInfoList);
        return ResultVo.success(map);
    }

    @Override
    public Map<String, Object> countPeriodGroupWorkHoursInProject(String projectCode, Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<>();
        //获取该项目下所有班组信息
        List<ProjectDetailQuery> workerGroupInProject = projectDetailQueryMapper.getWorkerGroupInProject(projectCode);
        for (ProjectDetailQuery projectDetailQuery : workerGroupInProject) {
            WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
            //获取某个班组的总工时
            Double hours = groupAttendMapper.countGroupHoursInPeriod(workerGroup.getTeamSysNo(), startDate, endDate);
            //获取班组下总工时
            map.put(workerGroup.getTeamName(), hours);
        }
        return map;
    }

    @Override
    public Map<String, Object> periodAttendInfoSum(Date startTime, Date endTime) {
        //从session获取用户
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if (AuthUtils.isOnlyProjectRole(user)) { // 判断用户是否只包含项目管理员角色
            Set<String> projectSet = user.getProjectSet();
            //累计考勤次数和考勤工时
            Map<String, Object> map = projectAttendMapper.getPeriodAttendNumAndWorkHoursByProjectCodeSet(startTime, endTime, projectSet);
            //累计异常数据
            Integer attendErrNum = projectAttendMapper.getPeriodAttendErrNumByProjectCodeSet(startTime, endTime, projectSet);
            map.put("attendErrNum", attendErrNum);
            return map;
        } else {
            //累计考勤次数和考勤工时
            Map<String, Object> map = projectAttendMapper.getPeriodAttendNumAndWorkHours(startTime, endTime);
            //累计异常数据
            Integer attendErrNum = projectAttendMapper.getPeriodAttendErrNum(startTime, endTime);
            map.put("attendErrNum", attendErrNum);
            return map;
        }
    }

    @Override
    public ResultVo exportRecords(Date startTime, Date endTime, String projectCode) {
        List<PersonWorkRecord> personWorkRecords = attendanceMapper.findPersonPeriodWorkInfoInProject(startTime, endTime, projectCode);
        ProjectWorkRecord projectWorkRecord = createProjectWorkRecord(startTime, endTime, projectCode, personWorkRecords);
        List<String> headList = new ArrayList<>();
        headList.add("姓名");
        headList.add("身份证号码");
        headList.add("性别");
        headList.add("所属公司");
        headList.add("班组");
        headList.add("工种");
        headList.add("出勤天数");
        headList.add("考勤小时");
        List<String> dayBetweenFormat = TimeUtil.getDayBetweenFormat(startTime, endTime);
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
            Map<String, Object> map = countPersonPeriodWorkInfo(attendanceList);
            record.setWorkDay((Integer) map.get("workDays"));
            record.setWorkHours((Double) map.get("allWorkHours"));
        }
        projectWorkRecord.setPersonWorkRecordList(personPeriodWorkInfoInProject);
        return projectWorkRecord;
    }

    private Map<String, Object> countPersonPeriodWorkInfo(List<Attendance> attendanceList) {
        double allWorkHours = 0.0;
        int workDays = 0;
        for (Attendance attendance : attendanceList) {
            if (attendance.getStatus() != null && attendance.getStatus() == 1) {
                if (attendance.getWorkHours() != null && attendance.getWorkHours() > 0) {
                    allWorkHours += attendance.getWorkHours();
                    workDays++;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("allWorkHours", allWorkHours);
        map.put("workDays", workDays);
        return map;
    }

}
