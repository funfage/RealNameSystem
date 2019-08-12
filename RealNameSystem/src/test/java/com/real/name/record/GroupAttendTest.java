package com.real.name.record;

import com.real.name.common.utils.TimeUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.record.entity.GroupAttend;
import com.real.name.record.service.repository.GroupAttendMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GroupAttendTest extends BaseTest {

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Test
    public void saveGroupAttend() {
        GroupAttend groupAttend = new GroupAttend();
        Project project = new Project();
        project.setProjectCode("3493984");
        WorkerGroup group = new WorkerGroup();
        group.setTeamSysNo(2424);
        groupAttend.setProject(project);
        groupAttend.setWorkerGroup(group);
        groupAttend.setGroupAttendNum(2);
        groupAttend.setGroupAttendErrNum(1);
        int i = groupAttendMapper.saveGroupAttend(groupAttend);
        System.out.println(i);
    }

    @Test
    public void updateByTeamSysNo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = dateFormat.parse("2019-08-07 16:36:00");
        Date dateBegin = TimeUtil.getDateBegin(parse);
        Date dateEnd = TimeUtil.getDateEnd(new Date());
        int i = groupAttendMapper.updateByTeamSysNo(1562649811, 2.3, -1, 1, dateBegin, dateEnd);
        System.out.println(i);
    }

    @Test
    public void countGroupHoursInPeriod() {
        Date begin = new Date(TimeUtil.getTodayBeginMilliSecond());
        Date end = new Date(TimeUtil.getTomorrowBeginMilliSecond());
        double hours = groupAttendMapper.countGroupHoursInPeriod(1562649690, begin, end);
        System.out.println(hours);
    }

    @Test
    public void getGroupAttendPeriodInfo() {
        Date begin = TimeUtil.getMonthFirstDay();
        Date end = TimeUtil.getNextMonthFirstDay();
        List<GroupAttend> groupList = groupAttendMapper.getGroupAttendPeriodInfo(1562649690, begin, end);
        System.out.println(groupList);
    }



}
