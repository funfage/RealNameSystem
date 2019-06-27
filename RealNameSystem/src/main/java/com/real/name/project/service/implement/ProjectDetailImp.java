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

        ProjectDetail projectD = new ProjectDetail(projectCode, personId, teamSysNo);
        projectDetailRepository.save(projectD);




        // 给设备发送人员信息
        String url = "person/create";
        Map<String, String> map = new HashMap<>();
        map.put("pass", Device.PASS);
        map.put("person", person.get().toJSON());
        System.out.println(map);
        ResultVo rvo = HTTPTool.sendDataTo(url, map);
        System.out.println("add person: " + rvo);

        // 给设备添加人员照片
        if (rvo != null && rvo.getSuccess()) {
            url = "face/create";
            map = new HashMap<>();
            map.put("pass", Device.PASS);
            map.put("personId", person.get().getPersonId().toString());
            map.put("imgBase64", person.get().getHeadImage());
            HTTPTool.sendDataTo(url, map);
        }
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
}
