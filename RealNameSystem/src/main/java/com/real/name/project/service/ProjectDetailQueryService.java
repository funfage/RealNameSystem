package com.real.name.project.service;

import com.real.name.person.entity.Person;
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

    /**
     * 将人员从项目中移除
     */
    int deletePersonInProject(@Param("projectCode") String projectCode, @Param("personId") Integer personId);

    String findTeamName(String projectCode, Integer personId);

    /**
     * 查询班组所在的项目id
     */
    String findProjectCodeByTeamSysNo(Integer teamSysNo);

    List<Person> findPersonByTeamSyNo(Integer teamSysNo);

    /**
     * 获取项目下的所有人员信息和班组信息
     */
    List<ProjectDetailQuery> findProjectDetail(String projectCode);

    /**
     * 获取项目下需要下发到人脸的人员信息
     */
    List<ProjectDetailQuery> getProjectFaceIssueDetail(String projectCode);

    /**
     * 获取需要下发到控制器的人员信息
     */
    List<ProjectDetailQuery> getProjectAccessIssueDetail(String projectCode);

    /**
     * 查询某个项目下所有的班组信息
     */
    List<ProjectDetailQuery> getWorkerGroupInProject(String projectCode);

    /**
     * 获取某个班组下所有的project_detail_id
     */
    List<Integer> getProjectIdByGroup(Integer teamSysNo);

    /**
     * 获取某个班组下的人数
     */
    Integer getPersonNumInGroup(Integer teamSysNo);

    List<ProjectDetailQuery> findPersonWorkDayInfoInProject(String projectCode, Date startDate, Date endDate);

    /**
     * 查询项目下所有project_detail_id和人员信息
     */
    List<ProjectDetailQuery> findIdAndPersonInProject(String projectCode);

    /**
     * 查询项目下的人员信息
     */
    List<ProjectDetailQuery> findDelPersonInDeviceByProject(String projectCode);

}
