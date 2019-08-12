package com.real.name.record.service.implement;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.TimeUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.record.entity.Attendance;
import com.real.name.record.query.PersonWorkRecord;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.GroupAttendMapper;
import com.real.name.record.service.repository.ProjectAttendMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

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
    public List<PersonWorkRecord> findPersonPeriodWorkInfoInProject(Date startDate, Date endDate, String projectCode) {
        return attendanceMapper.findPersonPeriodWorkInfoInProject(startDate, endDate, projectCode);
    }

    @Override
    public List<PersonWorkRecord> findPagePersonPeriodWorkInfoInProject(Date startTime, Date endTime, String projectCode, Integer pageNum, Integer pageSize) {
        int offset = pageNum * pageSize;
        int limit = pageSize;
        return attendanceMapper.findPagePersonPeriodWorkInfoInProject(startTime, endTime, projectCode, offset, limit);
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
            map.put(workerGroup.getTeamName(),  hours);
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






}
