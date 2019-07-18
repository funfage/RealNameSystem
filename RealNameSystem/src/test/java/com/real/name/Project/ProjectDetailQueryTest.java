package com.real.name.Project;

import com.real.name.common.utils.CommonUtils;
import com.real.name.others.BaseTest;
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


}
