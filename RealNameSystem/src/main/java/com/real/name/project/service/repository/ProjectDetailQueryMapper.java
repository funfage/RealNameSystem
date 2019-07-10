package com.real.name.project.service.repository;

import com.real.name.project.entity.ProjectDetailQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectDetailQueryMapper {

    /**
     * 获取其他项目的管理人员信息
     */
    List<ProjectDetailQuery> findOtherAdmins(String projectCode);

    /**
     * 获取未参加项目的建筑工人信息
     */
    List<ProjectDetailQuery> findOtherWorker();

    List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode);

    /**
     * 查询用户所在的项目的所有id
     */
    List<String> getProjectIdsByPersonId(Integer personId);

}
