package com.real.name.record;

import com.real.name.others.BaseTest;
import com.real.name.record.entity.ProjectAttend;
import com.real.name.record.query.ProjectAttendQuery;
import com.real.name.record.service.repository.ProjectAttendMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectAttendMapperTest extends BaseTest {

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

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



}
