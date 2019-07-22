package com.real.name.record;

import com.real.name.common.utils.TimeUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.repository.ProjectRepository;
import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.GroupAttend;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.GroupAttendMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class AttendanceTest extends BaseTest {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Test
    public void saveAttendance() {
        Attendance attendance = new Attendance();
        ProjectDetailQuery query = new ProjectDetailQuery();
        query.setId(8);
        attendance.setWorkHours((double) 12349839L);
        attendance.setWorkTime(new Date());
        attendanceMapper.saveAttendance(attendance);
    }

    @Test
    public void countGroupTime() {
        //查询所有的projectCode
        List<String> allProjectCode = projectRepository.findAllProjectCode();
        for (String projectCode : allProjectCode) {
            //查询该项目下所有班组信息
            List<ProjectDetailQuery> groupList = projectDetailQueryService.getWorkerGroupInProject(projectCode);
            for (ProjectDetailQuery projectDetailQuery : groupList) {
                WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
                //获取该班组所有的project_detail_id
                List<Integer> ids = projectDetailQueryService.getProjectIdByGroup(workerGroup.getTeamSysNo());
                Date todayBegin = new Date(TimeUtil.getTodayBegin() * 1000 - 86400000);
                Date todayEnd = new Date(TimeUtil.getTomorrowBegin() * 1000 - 86400000);
                //获取班组当天下的总工时
                Double hours = attendanceService.countWorkerHours(ids, todayBegin, todayEnd);
                if (hours != null) {
                    GroupAttend groupAttend = new GroupAttend();
                    groupAttend.setWorkerGroup(workerGroup);
                    groupAttend.setProject(new Project(projectCode));
                    groupAttend.setWorkHours(hours);
                    groupAttend.setWorkTime(new Date());
                    groupAttendMapper.saveGroupAttend(groupAttend);
                }
            }
        }
    }


}
