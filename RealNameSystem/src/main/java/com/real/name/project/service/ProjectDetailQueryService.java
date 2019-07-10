package com.real.name.project.service;

import com.real.name.project.entity.ProjectDetailQuery;

import java.util.List;

public interface ProjectDetailQueryService {

    /**
     * 获取其他项目的管理人员信息
     */
    List<ProjectDetailQuery> findOtherAdmins(String projectCode);

    /**
     * 获取未参加项目的建筑工人信息
     */
    List<ProjectDetailQuery> findOtherWorker();

    /**
     * 获取项目中的人员信息和班组信息
     */
    List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode);

    /**
     * 获取所在的所有项目id
     */
    List<String> getProjectIdsByPersonId(Integer personId);
}
