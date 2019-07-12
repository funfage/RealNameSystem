package com.real.name.project.service;

import com.real.name.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectService {

    /**
     * 创建项目
     * @param project
     * @return
     */
    Project createProject(Project project);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Project> findAll(Pageable pageable);

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

    /**
     *根据项目名查询
     */
    Optional<Project> findByName(String projectName);

    /**
     * 根据projectCode查询项目名称
     */
    String findProjectName(String projectName);
}
