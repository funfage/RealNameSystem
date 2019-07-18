package com.real.name.project.service;

import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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


    /**
     *获取在某个项目下每个班组的人员数
     */
    List<GroupPersonNum> getWorkGroupPersonNum(String projectCode);

    /**
     * 查询某个人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<ProjectDetailQuery> findPersonWorkHoursInfo(Date startDate, Date endDate);

}
