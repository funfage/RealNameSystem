package com.real.name.project.service.repository;

import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

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
    Project findProNameAndCorp(@Param("projectCode") String projectCode);

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

    /**
     * 获取某个项目id集合的项目id和项目名
     */
    List<Map<String, String>> findProjectCodeAndName(@Param("projectCodeList") Set<String> projectCodeList);

    /**
     * 获取已创建的项目数量
     */
    Integer getProjectNumber();

    /**
     * 获取到当天为止已有考勤数据项目的数量
     */
    Integer getProjectAttendNumber();

    /**
     * 获取有异常考勤数据项目数量
     */
    Integer getProjectYedAttendErrNumber(@Param("startTime") Date startTime,
                                         @Param("endTime") Date endTime);



}
