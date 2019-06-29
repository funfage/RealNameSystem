package com.real.name.project.service;

import com.real.name.project.entity.Project;

import java.util.Optional;

public interface ProjectService {

    /**
     * 新建项目
     * @param projectName 项目名称
     * @param projectCompany 项目所属公司
     * @param projectLocal 项目地点
     * @param projectType 项目类型
     * @param personId 项目负责人id
     * @return 项目对象
     */
    Project create(String projectName, String projectCompany, String projectLocal,
                   String projectType, Integer personId);

    Project create(Project project);

    /**
     * 查找项目
     * @param projectCode 项目id
     */
    Optional<Project> findByProjectCode(String projectCode);

    /**
     * 修改项目信息
     */
    Project updateByProjectCode(Project project);

    /**
     * 删除项目信息
     */
    int deleteByProjectCode(String Project);
}
