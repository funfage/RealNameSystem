package com.real.name.project.service;

import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 查询在projectCodeList中所有项目的信息
     */
    List<Project> findAllInProjectCode(Set<String> projectCodeList);

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

    /**
     * 获取项目首页数据
     */
    Map<String, Object> getMainPageProjectInfo();

    /**
     * 根据用户角色获取项目id和项目名
     */
    List<Map<String, String>> getProjectCodeAndNameByUserRole();

    /**
     * 根据项目编码判断项目是否存在
     */
    boolean judgeEmptyByProjectCode(String projectCode);

    /**
     * 查询项目上传状态
     */
    Integer findUploadStatusByProjectCode(String projectCode);

    /**
     * 修改项目编码
     */
    Integer updateProjectCode(String oldProjectCode, String newProjectCode);

}