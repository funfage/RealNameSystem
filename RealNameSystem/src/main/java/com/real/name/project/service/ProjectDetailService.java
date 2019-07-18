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
     * 往项目中添加人员
     * @param projectCode 项目id
     */
    void addPersonToFaceDevice(String projectCode, Person person, List<Device> projectFaceDevices, List<Device> allFaceDevices, String teamName);

    void addPersonToAccessDevice(String projectCode, Person person, List<Device> projectAccessDevices, List<Device> allAccessDevices, String teamName);

    /**
     * 获取项目中的人员
     * @param projectCode 项目id
     */
    Map<String, Object> getPersonInProject(String  projectCode, Pageable pageable);

    /**
     * 根据人员id 获取项目信息
     * @param personId 人员id
     * @return 项目信息
     */
    Project getProjectFromPersonId(Integer personId);

    Optional<ProjectDetail> findByTeamSysNoAndPersonId(Integer teamSysNo, Integer personId);

    Optional<ProjectDetail> findByProjectCodeAndPersonId(String projectId, Integer personId);

    Optional<ProjectDetail> findByProjectCodeAndPersonIdAndTeamSysNo(String projectId, Integer personId, Integer teamSysNo);

    List<ProjectDetail> findAll();


}
