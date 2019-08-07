package com.real.name.project.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectDetailQueryMapper {

    /**
     * 获取其他项目的管理人员信息
     */
    List<ProjectDetailQuery> findOtherAdmins(@Param("projectCode") String projectCode,
                                             @Param("groupCorpCode") String groupCorpCode);

    /**
     * 获取未参加项目的建筑工人信息
     */
    List<ProjectDetailQuery> findOtherWorker(@Param("groupCorpCode") String groupCorpCode);

    /**
     * 查询项目下的人员信息和班组信息
     */
    List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode);

    /**
     * 查询用户所在的项目的所有id
     */
    List<String> getProjectIdsByPersonId(Integer personId);

    /**
     * 获取推送给浏览器的数据
     */
    ProjectDetailQuery getSendInfo(@Param("personId") Integer personId,@Param("projectCode") String projectCode);

    /**
     * 查询某个项目下所有班组的人员个数
     */
    List<GroupPersonNum> getWorkGroupPersonNum(@Param("projectCode") String projectCode);

    /**
     * 根据projectCode和personId删除
     */
    int deletePersonInProject(@Param("projectCode") String projectCode, @Param("personId") Integer personId);

    /**
     * 查询某个人员在某个项目下的班组名
     */
    String findTeamName(@Param("projectCode") String projectCode, @Param("personId") Integer personId);

    /**
     * 查询班组所在的项目id
     */
    String findProjectCodeByTeamSysNo(Integer teamSysNo);

    /**
     * 获取班组下的人员信息
     */
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
    List<Integer> getProjectDetailIdByGroup(Integer teamSysNo);

    /**
     * 获取某个班组下的人数
     */
    Integer getPersonNumInGroup(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 查询某个项目下所有人员信息和工作天数等
     */
    List<ProjectDetailQuery> findPersonWorkDayInfoInProject(@Param("projectCode") String projectCode,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate);

    /**
     * 查询项目下所有project_detail_id和人员信息
     */
    List<ProjectDetailQuery> findIdAndPersonInProject(String projectCode);

    /**
     * 查询项目下的人员信息
     */
    List<ProjectDetailQuery> findDelPersonInDeviceByProject(@Param("projectCode") String projectCode);

    /**
     * 获取一个项目下所有人员数目
     */
    Integer countPersonNumByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据id判断记录是否存在
     */
    Integer getIdWhileExists(Integer id);

    /**
     * 通过projectCode和personId查询id
     */
    Integer getIdByProjectCodeAndPersonId(@Param("projectCode") String projectCode,
                                          @Param("personId") Integer personId);

    /**
     * 通过项目id获取所有project_detail_id
     */
    List<Integer> getIdsByProjectCode(@Param("projectCode") String projectCode);

}