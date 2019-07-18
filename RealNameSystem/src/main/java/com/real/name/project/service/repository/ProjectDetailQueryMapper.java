package com.real.name.project.service.repository;

import com.real.name.person.entity.Person;
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

    /**
     * 获取推送给浏览器的数据
     */
    ProjectDetailQuery getSendInfo(@Param("personId") Integer personId,@Param("projectCode") String projectCode);

    /**
     * 查询某个班组下的人员个数
     */
    List<GroupPersonNum> getWorkGroupPersonNum(@Param("projectCode") String projectCode);

    /**
     * 查询某个人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<ProjectDetailQuery> findPersonWorkHoursInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}