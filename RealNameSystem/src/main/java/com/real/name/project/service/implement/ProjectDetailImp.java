package com.real.name.project.service.implement;

import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.HTTPTool;
import com.real.name.face.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ProjectDetailImp implements ProjectDetailService {

    @Autowired
    private ProjectDetailRepository projectDetailRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private ProjectService projectService;

    @Override
    public void addPersonToProject(String projectCode, Integer teamSysNo, Integer personId) {

        // 判断是否有该人员
        Optional<Person> person = personService.findById(personId);
        if (!person.isPresent()) return;

        // 判断该班组中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetail1 = projectDetailRepository.findByTeamSysNoAndPersonId(teamSysNo, personId);
        if (projectDetail1.isPresent()) return;

        // 判断该项目中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetail = projectDetailRepository.findByProjectCodeAndPersonId(projectCode, personId);
        if (projectDetail.isPresent()) return;

        // 把人员、班组、项目之间的关系存到数据库
        ProjectDetail projectD = new ProjectDetail(projectCode, personId, teamSysNo);
        projectDetailRepository.save(projectD);

        // 给设备发送人员信息
        String url = "person/create";
        Map<String, String> map = new HashMap<>();
        map.put("person", person.get().toJSON());

        // 根据工人类型，发送人员信息给不同的设备
        Integer workRole = person.get().getWorkRole();
        ResultVo rvo = sendData(workRole, url, map, projectCode);
        System.out.println("add person: " + rvo);

        String pId = person.get().getPersonId().toString();
        String headImage = person.get().getHeadImage();

        // 给设备添加人员照片
        if (rvo != null && rvo.getSuccess() && StringUtils.hasText(pId) && StringUtils.hasText(headImage)) {
            url = "face/create";
            map = new HashMap<>();
            map.put("personId", pId);
            map.put("imgBase64", headImage);
            sendData(workRole, url, map, projectCode);
        }
    }

    private ResultVo sendData(Integer workRole, String url, Map<String, String> map, String projectCode) {
        ResultVo rvo = null;
        if (workRole == 20) { // 如果是建筑工人，发送给该工人所属项目的设备
            rvo = HTTPTool.sendDataTo(url, map, projectCode);
        } else if (workRole == 10){ // 如果是管理工人，发送给全部设备
            rvo = HTTPTool.sendDataTo(url, map);
        }
        return rvo;
    }

    @Override
    public Map<String, Object> getPersonInProject(String  projectCode, Pageable pageable) {
        Page<ProjectDetail> projectDetails = projectDetailRepository.findByProjectCode(projectCode, pageable);
//        System.out.println(projectDetails);

        List<Integer> personIds = new ArrayList<>();

        for (ProjectDetail projectDetail : projectDetails) {
            personIds.add(projectDetail.getPersonId());
        }

        Map<String , Object>map = new HashMap<>();
        map.put("personList", personService.findPersons2(personIds));
        map.put("totalPages", projectDetails.getTotalPages());
        map.put("totalElements", projectDetails.getTotalElements());
        return map;
    }

    @Override
    public Optional<Project> getProjectFromPersonId(Integer personId) {
        Optional<ProjectDetail> detail = projectDetailRepository.findByPersonId(personId);
        if (!detail.isPresent()) return null;
        Optional<Project> project = projectService.findByProjectCode(detail.get().getProjectCode());

        return project;
    }

    @Override
    public int deleteByProjectCode(String projectCode) {
        return projectDetailRepository.deleteByProjectCode(projectCode);
    }

    @Override
    public int deleteByTeamSysNo(Integer teamSysNo) {
        return projectDetailRepository.deleteByTeamSysNo(teamSysNo);
    }

    @Override
    public int deleteByPersonId(Integer personId) {
        return projectDetailRepository.deleteByPersonId(personId);
    }
}
