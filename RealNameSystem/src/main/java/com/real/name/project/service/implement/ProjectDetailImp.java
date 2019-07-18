package com.real.name.project.service.implement;

import com.real.name.device.netty.utils.AccessDeviceUtils;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectPersonDetail;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.repository.ProjectDetailRepository;
import com.real.name.project.service.repository.ProjectPersonDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectDetailImp implements ProjectDetailService {
    private Logger logger = LoggerFactory.getLogger(ProjectDetailImp.class);

    @Autowired
    private ProjectDetailRepository projectDetailRepository;

    @Autowired
    private ProjectPersonDetailRepository projectPersonDetailRepository;

    @Autowired
    private PersonService personService;


    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AccessService accessService;


    @Override
    public ProjectDetail save(ProjectDetail projectDetail) {
        return projectDetailRepository.save(projectDetail);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addPersonToFaceDevice(String projectCode, Person person, List<Device> projectFaceDevices, List<Device> allFaceDevices, String teamName) {
        if (person.getWorkRole() != null && person.getWorkRole() == 10) {//下发管理员信息
            FaceDeviceUtils.issuePersonToDevices(allFaceDevices, person, 3, teamName);
        } else {//下发人员信息
            FaceDeviceUtils.issuePersonToDevices(projectFaceDevices, person, 3, teamName);
        }
    }

    @Override
    public void addPersonToAccessDevice(String projectCode, Person person, List<Device> projectAccessDevices, List<Device> allAccessDevices, String teamName) {
        if (person.getWorkRole() != null && person.getWorkRole() == 10) {//下发管理员信息
            AccessDeviceUtils.issueIdCardIndexToDevices(allAccessDevices, person.getIdCardIndex(), teamName);
        } else {//下发人员信息
            AccessDeviceUtils.issueIdCardIndexToDevices(projectAccessDevices, person.getIdCardIndex(), teamName);
        }
    }

    @Override
    public Map<String, Object> getPersonInProject(String  projectCode, Pageable pageable) {
        Page<ProjectDetail> projectDetails = projectDetailRepository.findByProjectCode(projectCode, pageable);
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
    public Project getProjectFromPersonId(Integer personId) {
        Optional<ProjectPersonDetail> detail = projectPersonDetailRepository.findByPerson_PersonId(personId);
        if (!detail.isPresent()) {
            return null;
        }
        return detail.get().getProject();
    }

    @Override
    public Optional<ProjectDetail> findByTeamSysNoAndPersonId(Integer teamSysNo, Integer personId) {
        return projectDetailRepository.findByTeamSysNoAndPersonId(teamSysNo, personId);
    }

    @Override
    public Optional<ProjectDetail> findByProjectCodeAndPersonId(String projectId, Integer personId) {
        return projectDetailRepository.findByProjectCodeAndPersonId(projectId, personId);
    }

    @Override
    public Optional<ProjectDetail> findByProjectCodeAndPersonIdAndTeamSysNo(String projectId, Integer personId, Integer teamSysNo) {
        return projectDetailRepository.findByProjectCodeAndPersonIdAndTeamSysNo(projectId, personId, teamSysNo);
    }

    @Override
    public List<ProjectDetail> findAll() {
        return projectDetailRepository.findAll();
    }

}
