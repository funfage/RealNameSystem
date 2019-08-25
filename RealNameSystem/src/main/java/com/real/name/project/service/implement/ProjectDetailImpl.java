package com.real.name.project.service.implement;

import com.real.name.device.netty.utils.AccessDeviceUtils;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.repository.ProjectDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectDetailImpl implements ProjectDetailService {

    private Logger logger = LoggerFactory.getLogger(ProjectDetailImpl.class);

    @Autowired
    private ProjectDetailRepository projectDetailRepository;

    @Override
    public ProjectDetail save(ProjectDetail projectDetail) {
        return projectDetailRepository.save(projectDetail);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addPersonToFaceDevice(String projectCode, Person person, List<Device> projectFaceDevices, List<Device> allFaceDevices) {
        if (person.getWorkRole() != null && person.getWorkRole() == 10) {//下发管理员信息
            FaceDeviceUtils.issuePersonToDevices(allFaceDevices, person, 3);
        } else {
            //下发人员信息
            FaceDeviceUtils.issuePersonToDevices(projectFaceDevices, person, 3);
        }
    }

    @Override
    public void addPersonToAccessDevice(String projectCode, Person person, List<Device> projectAccessDevices, List<Device> allAccessDevices) {
        if (person.getWorkRole() != null && person.getWorkRole() == 10) {//下发管理员信息
            AccessDeviceUtils.issueIdCardIndexToDevices(allAccessDevices, person.getIdCardIndex());
        } else {//下发人员信息
            AccessDeviceUtils.issueIdCardIndexToDevices(projectAccessDevices, person.getIdCardIndex());
        }
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
