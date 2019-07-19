package com.real.name.Project;

import com.real.name.common.utils.CommonUtils;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectDetailQueryTest extends BaseTest {

    @Autowired
    private ProjectDetailQueryMapper mapper;

    @Test
    public void findByWorkRoleAndProjectCodeTest() {
        List<ProjectDetailQuery> list = mapper.findOtherAdmins("067R9HQR0dmw178890C4BKubNU2d9gG7");
        System.out.println(list);
    }

    @Test
    public void findOtherWorkerTest() {
        List<ProjectDetailQuery> list = mapper.findOtherWorker();
        System.out.println(list);
    }

    @Test
    public void getPersonAndWorkerGroupInfo() {
        List<ProjectDetailQuery> list = mapper.getPersonAndWorkerGroupInfo("44010620190510008");
        System.out.println(list);
    }
    
    @Test
    public void getProjectIdsByPersonId() {
        List<String> projectCodes  = mapper.getProjectIdsByPersonId(102);
        List<String> collect = projectCodes.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void getSendInfo() {
        ProjectDetailQuery sendInfo = mapper.getSendInfo(1131, "067R9HQR0dmw178890C4BKubNU2d9gG7");
        System.out.println(sendInfo);
    }

    @Test
    public void getWorkGroupPersonNum() {
        List<GroupPersonNum> workGroupPersonNum = mapper.getWorkGroupPersonNum("067R9HQR0dmw178890C4BKubNU2d9gG7");
        System.out.println(workGroupPersonNum);
    }

    @Test
    public void findPersonWorkHoursInfo() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = CommonUtils.initDateByMonth();
        Date end = new Date(System.currentTimeMillis());
        List<ProjectDetailQuery> queryList = mapper.findPersonWorkHoursInfo(start, end);
        System.out.println(queryList);
    }

    @Test
    public void delete() {
        int i = mapper.deletePersonInProject("36bj84W235Zgc8O78yuS32510ppMkHfe", 104);
        System.out.println(i);
    }

    @Test
    public void findTeamName() {
        String teamName = mapper.findTeamName("36bj84W235Zgc8O78yuS32510ppMkHfe", 95);
        System.out.println(teamName);
    }

    @Test
    public void findProjectCodeByTeamSysNo() {
        String projectCode = mapper.findProjectCodeByTeamSysNo(1562649690);
        System.out.println(projectCode);
    }

    @Test
    public void findPersonByTeamSyNo() {
        List<Person> personList = mapper.findPersonByTeamSyNo(1562649690);
        System.out.println(personList);
    }

    @Test
    public void findProjectDetail() {
        List<ProjectDetailQuery> projectDetail = mapper.findProjectDetail("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(projectDetail);
    }

    @Test
    public void getProjectIssueDetail() {
        List<ProjectDetailQuery> projectIssueDetail = mapper.getProjectFaceIssueDetail("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(projectIssueDetail);
    }

    @Test
    public void getProjectAccessIssueDetail() {
        List<ProjectDetailQuery> projectAccessIssueDetail = mapper.getProjectAccessIssueDetail("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(projectAccessIssueDetail);
    }


}
