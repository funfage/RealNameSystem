package com.real.name.project.service;

import com.real.name.project.entity.Project;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ProjectDetailService {

    /**
     * 往项目中添加人员
     * @param projectCode 项目id
     * @param personId 人员id
     */
    void addPersonToProject(String projectCode, Integer teamSysNo, Integer personId);

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
    Optional<Project> getProjectFromPersonId(Integer personId);
}
