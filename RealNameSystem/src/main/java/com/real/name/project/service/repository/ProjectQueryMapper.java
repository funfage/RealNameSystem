package com.real.name.project.service.repository;

import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface ProjectQueryMapper {

    /**
     * 保存项目信息
     */
    int saveProject(@Param("project") Project project);

    /**
     * 根据项目名查询
     */
    Project findByName(@Param("projectName") String projectName);

    /**
     * 根据项目id查询
     */
    Project findByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 查询所有项目信息(项目管理员只查询该项目下的信息)
     */
    List<Project> findAll();
    List<Project> findAllInProjectCode(@Param("projectCodeList") Set<String> projectCodeList);

    /**
     * 根据项目id删除
     */
    int deleteByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据projectCode查询出项目名称
     */
    String findProjectName(@Param("projectCode") String projectCode);

    /**
     * 查询项目名和公司名
     */
    @Query(value = "select new com.real.name.project.entity.Project(p.name, p.contractorCorpName) from Project p where p.projectCode = ?1")
    Project findProNameAndCorp(String projectCode);

    /**
     * 获取所有项目的id
     */
    List<String> findAllProjectCode();

    /**
     * 搜索项目信息
     */
    List<Project> searchProject(@Param("projectQuery") ProjectQuery projectQuery);

    /**
     * 查询所有的projectCode和name
     */
    List<Map<String, String>> findAllProjectCodeAndName();


}
