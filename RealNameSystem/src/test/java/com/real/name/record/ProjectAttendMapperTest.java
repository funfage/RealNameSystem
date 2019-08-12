package com.real.name.record;

import com.real.name.common.utils.TimeUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.project.service.repository.ProjectQueryMapper;
import com.real.name.record.entity.GroupAttend;
import com.real.name.record.entity.ProjectAttend;
import com.real.name.record.query.ProjectAttendQuery;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.GroupAttendMapper;
import com.real.name.record.service.repository.ProjectAttendMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectAttendMapperTest extends BaseTest {

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Autowired
    private ProjectQueryMapper projectQueryMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Test
    public void projectAttendNumAndHoursAndErrNumTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        Map<String, Object> periodAttendNumAndWorkHours = projectAttendMapper.getPeriodAttendNumAndWorkHours(start, end);
        Integer attendErrNum = projectAttendMapper.getPeriodAttendErrNum(start, end);
        periodAttendNumAndWorkHours.put("attendErrNum", attendErrNum);
        Set<String> projectSet = new HashSet<>();
        projectSet.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        Map<String, Object> periodAttendNumAndWorkHoursByProjectCodeSet = projectAttendMapper.getPeriodAttendNumAndWorkHoursByProjectCodeSet(start, end, projectSet);
        Integer periodAttendErrNumByProjectCodeSet = projectAttendMapper.getPeriodAttendErrNumByProjectCodeSet(start, end, projectSet);
        periodAttendNumAndWorkHoursByProjectCodeSet.put("periodAttendErrNumByProjectCodeSet", periodAttendErrNumByProjectCodeSet);
    }

    @Test
    public void saveProjectAttend() {
        ProjectAttend projectAttend = new ProjectAttend();
        projectAttend.setProjectCode("7gU44Drw914wQe8aYK245ks8v32Iu50X");
        projectAttend.setProjectAttendNum(1);
        projectAttend.setProjectAttendErrNum(0);
        int i = projectAttendMapper.saveProjectAttend(projectAttend);
        System.out.println(i);
    }

    @Test
    public void updateByProjectCode() {
        Date dateBegin = TimeUtil.getDateBegin(new Date());
        Date dateEnd = TimeUtil.getDateEnd(new Date());
        int i = projectAttendMapper.updateByProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe", -2.0, 3, -1, dateBegin, dateEnd);
        System.out.println(i);
    }

    @Test
    public void rangeByPersonNum() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        List<ProjectAttendQuery> projectAttendQueries = projectAttendMapper.rangeByPersonNum();
        System.out.println(projectAttendQueries);
        List<ProjectAttendQuery> projectAttendQueries1 = projectAttendMapper.rangeBySumWorkHours(start, end);
        System.out.println(projectAttendQueries1);
        List<ProjectAttendQuery> projectAttendQueries2 = projectAttendMapper.rangeBySumAttendNum(start, end);
        System.out.println(projectAttendQueries2);
        List<ProjectAttendQuery> projectAttendQueries3 = projectAttendMapper.rangeBySumAttendErrNum(start, end);
        System.out.println(projectAttendQueries3);
    }

    @Test
    public void countGroupAndProjectAttend() {
        Date todayBegin = TimeUtil.getYesterdayBegin();
        Date todayEnd = TimeUtil.getTodayBegin();
        //查询所有的projectCode
        List<String> allProjectCode = projectQueryMapper.findAllProjectCode();
        for (String projectCode : allProjectCode) {
            //查询该项目下所有班组信息
            List<ProjectDetailQuery> groupList = projectDetailQueryMapper.getWorkerGroupInProject(projectCode);
            //统计班组相关考勤数据
            for (ProjectDetailQuery projectDetailQuery : groupList) {
                WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
                //获取该班组所有的project_detail_id
                List<Integer> projectDetailIds = projectDetailQueryMapper.getProjectDetailIdByGroup(workerGroup.getTeamSysNo());
                //获取当天班组考勤的总工时, 总考勤次数, 不统计异常数据
                Double workHours = attendanceMapper.getPeriodWorkHoursInPIds(projectDetailIds, todayBegin, todayEnd);
                Integer groupAttendNum = attendanceMapper.getPeriodAttendNumInPIds(projectDetailIds, todayBegin, todayEnd);
                //获取班组当天考勤异常的次数
                Integer groupAttendErrNum = attendanceMapper.getPeriodAttendErrNumInPIds(projectDetailIds, todayBegin, todayEnd);
                //保存班组当天考勤信息
                GroupAttend groupAttend = new GroupAttend();
                groupAttend.setWorkerGroup(workerGroup);
                groupAttend.setProject(new Project(projectCode));
                groupAttend.setWorkHours(workHours);
                groupAttend.setGroupAttendNum(groupAttendNum);
                groupAttend.setGroupAttendErrNum(groupAttendErrNum);
                groupAttend.setWorkTime(new Date());
                groupAttendMapper.saveGroupAttend(groupAttend);
            }
            //统计项目相关考勤数据
            List<Integer> projectDetailIds = projectDetailQueryMapper.getIdsByProjectCode(projectCode);
            //获取当天项目考勤的总工时, 总考勤次数, 不统计异常数据
            Double workHours = attendanceMapper.getPeriodWorkHoursInPIds(projectDetailIds, todayBegin, todayEnd);
            Integer projectAttendNum = attendanceMapper.getPeriodAttendNumInPIds(projectDetailIds, todayBegin, todayEnd);
            //获取项目当天考勤异常的次数
            Integer projectAttendErrNum = attendanceMapper.getPeriodAttendErrNumInPIds(projectDetailIds, todayBegin, todayEnd);
            //保存项目当天考勤信息
            ProjectAttend projectAttend = new ProjectAttend();
            projectAttend.setProjectCode(projectCode);
            projectAttend.setWorkHours(workHours);
            projectAttend.setProjectAttendNum(projectAttendNum);
            projectAttend.setProjectAttendErrNum(projectAttendErrNum);
            projectAttendMapper.saveProjectAttend(projectAttend);
        }
    }

}
