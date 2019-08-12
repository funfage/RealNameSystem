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
    List<ProjectDetailQuery> findOtherAdmins(String projectCode, String groupCorpCode);

    /**
     * 获取未参加项目的建筑工人信息
     */
    List<ProjectDetailQuery> findOtherWorker(String groupCorpCode);

    /**
     * 获取项目中的人员信息和班组信息
     */
    List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode);

    /**
     * 获取所在的所有项目id
     */
    List<String> getProjectCodeListByPersonId(Integer personId);


    /**
     * 获取在某个项目下每个班组的人员数
     */
    List<GroupPersonNum> getWorkGroupPersonNum(String projectCode);

    /**
     * 将人员从项目中移除
     */
    int deletePersonInProject(@Param("projectCode") String projectCode, @Param("personId") Integer personId);

    /**
     * 查询班组名称
     */
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

    /**
     * 获取一个项目下所有人员数目
     */
    Integer countPersonNumByProjectCode( String projectCode);

    /**
     * 判断记录是否存在
     *
     * @return true 不存在
     * false 记录存在
     */
    boolean judgeEmptyById(Integer id);

    /**
     * 设置项目人员移除标识
     */
    int setProPersonRemoveStatus(Integer personId, String projectCode);

}
