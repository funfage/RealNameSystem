package com.real.name.project.service;

import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectDetailService {

    /**
     * 添加项目人员信息
     */
    ProjectDetail save(ProjectDetail projectDetail);

    /**
     * 将人员信息下发到人脸设备
     */
    void addPersonToFaceDevice(String projectCode, Person person, List<Device> projectFaceDevices, List<Device> allFaceDevices, String teamName);

    /**
     * 将人员下发到控制器
     */
    void addPersonToAccessDevice(String projectCode, Person person, List<Device> projectAccessDevices, List<Device> allAccessDevices, String teamName);


    /**
     * 根据projectCode和personId和teamSysNo查找
     */
    Optional<ProjectDetail> findByProjectCodeAndPersonIdAndTeamSysNo(String projectCode, Integer personId, Integer teamSysNo);

    /**
     * 查询所有
     */
    List<ProjectDetail> findAll();


}
