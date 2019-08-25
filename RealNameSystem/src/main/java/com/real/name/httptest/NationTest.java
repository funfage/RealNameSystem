package com.real.name.httptest;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.result.ResultVo;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.nation.utils.NationalUtils;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.subcontractor.entity.SubContractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Subgraph;

@RestController
@RequestMapping("test")
public class NationTest {

    private Logger logger = LoggerFactory.getLogger(NationTest.class);

    @GetMapping("/uploadProjectTest")
    public ResultVo uploadProjectTest() {
        Project project = new Project();
        project.setProjectCode("dfdfdff");
        project.setAddress("address");
        project.setCategory("01");
        project.setContractorCorpName("dgdg");
        project.setDescription("dfdf");
        project.setName("dgddfff");
        JSONObject result = NationalUtils.uploadProject(project);
        System.out.println(result.toJSONString());
        return ResultVo.success(result);
    }

    @GetMapping("/uploadSubContractor")
    public ResultVo uploadSubContractor() {
        SubContractor subContractor = new SubContractor();
        Project project = new Project();
        project.setProjectCode("44010620190510008");
        subContractor.setProject(project);
        subContractor.setCorpName("corpname");
        subContractor.setCorpCode("corpcode");
        subContractor.setCorpType("corptype");
        subContractor.setBankCode("001");
        subContractor.setBankNumber("243749344832");
        subContractor.setBankLinkNumber("2349834593");
        subContractor.setBankName("name");
        JSONObject jsonObject = NationalUtils.uploadSubContractor(subContractor);
        if (jsonObject.getBoolean("error")) {
            logger.error("上传班组信息出现异常");
            return ResultVo.failure(jsonObject.getString("data"));
        }
        return ResultVo.success(jsonObject);
    }

    @GetMapping("/uploadGroupTest")
    public ResultVo uploadGroupTest() {
        ProjectDetailQuery projectDetailQuery = new ProjectDetailQuery();
        WorkerGroup workerGroup = new WorkerGroup();
        workerGroup.setCorpCode("corpcode");
        workerGroup.setCorpName("corpname");
        workerGroup.setRemark("remark");
        workerGroup.setResponsiblePersonIdNumber("357835738534");
        workerGroup.setResponsiblePersonIdCardType(1);
        workerGroup.setResponsiblePersonPhone("15538202828");
        workerGroup.setResponsiblePersonName("张三");
        workerGroup.setTeamName("teamname");
        projectDetailQuery.setWorkerGroup(workerGroup);
        Project project = new Project();
        project.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        projectDetailQuery.setProject(project);
        JSONObject jsonObject = NationalUtils.uploadGroup(projectDetailQuery);
        if (jsonObject.getBoolean("error")) {
            logger.error("上传班组信息出现异常");
            return ResultVo.failure(jsonObject.getString("data"));
        }
        return ResultVo.success(jsonObject);
    }

    @GetMapping("/uploadPerson")

    public ResultVo uploadPerson() {
        ProjectDetailQuery projectDetailQuery = new ProjectDetailQuery();
        Project project = new Project();
        project.setProjectCode("e495idnx");
        projectDetailQuery.setProject(project);
        WorkerGroup workergroup = new WorkerGroup();
        workergroup.setTeamSysNo(43573847);
        workergroup.setTeamName("fdk");
        projectDetailQuery.setWorkerGroup(workergroup);
        Person person = new Person();
        person.setPersonName("personName");
        person.setSubordinateCompany("dffkd");
        person.setCorpCode("23");
        person.setWorkRole(1);
        person.setWorkType("010");
        person.setNation("nation");
        person.setAddress("address");
        projectDetailQuery.setPerson(person);
        JSONObject jsonObject = NationalUtils.uploadPerson(projectDetailQuery);
        if (jsonObject.getBoolean("error")) {
            logger.error("上传班组信息出现异常");
            return ResultVo.failure(jsonObject.getString("data"));
        }
        return ResultVo.success(jsonObject);
    }


}
