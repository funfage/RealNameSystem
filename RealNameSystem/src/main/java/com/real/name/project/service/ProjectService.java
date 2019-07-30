package com.real.name.project.service;

import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {

    /**
     * 创建项目
     */
    Project createProject(Project project);

    /**
     * 分页查询
     */
    List<Project> findAll();

    /**
     * 查找项目
     * @param projectCode 项目id
     */
    Project findByProjectCode(String projectCode);

    /**
     * 修改项目信息
     */
    Project updateByProjectCode(Project project);

    /**
     * 删除项目信息
     */
    int deleteByProjectCode(String Project);

    /**
     * 根据项目名查询
     */
    Project findByName(String projectName);

    /**
     * 根据projectCode查询项目名称
     */
    String findProjectName(String projectName);

    /**
     *  查询项目名和所属公司
     */
    Project findProNameAndCorp(String projectCode);

    /**
     * 搜索项目
     */
    List<Project> searchProject(ProjectQuery projectQuery);

    /**
     * 查询所有的projectCode和name
     */
    List<Map<String, String>> findAllProjectCodeAndName();


}