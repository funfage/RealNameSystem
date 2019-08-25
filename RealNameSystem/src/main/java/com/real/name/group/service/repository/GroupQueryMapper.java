package com.real.name.group.service.repository;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.text.ParsePosition;
import java.util.List;

@Mapper
public interface GroupQueryMapper {

    /**
     * 保存班组信息
     */
    int insertGroup(@Param("workerGroup") WorkerGroup workerGroup);

    /**
     * 修改班组信息
     */
    int updateWorkerGroup(@Param("workerGroup") WorkerGroup workerGroup);

    /**
     * 根据teamName判断班组是否存在
     */
    Integer judgeExistByTeamNameAndSubContractor(@Param("teamName") String teamName,
                                                 @Param("subContractorId") Integer subContractorId);

    /**
     * 查询所有班组信息
     */
    List<GroupQuery> findAll();

    /**
     * 根据id查询
     */
    GroupQuery findById(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 根据projectCode查询
     */
    List<GroupQuery> findByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据projectCode和personId查询出对应的班组
     */
    WorkerGroup findByProjectCodeAndPersonId(@Param("projectCode") String projectCode,
                                             @Param("personId") Integer personId);

    /**
     * 根据projectCode查询是否存在管理员班组
     */
    Integer judgeExistAdminGroupByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 搜索班组
     */
    List<WorkerGroup> searchGroup(@Param("groupQuery") GroupQuery groupQuery);

    /**
     * 根据teamSysNo删除
     */
    int deleteByTeamSysNo(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 设置项目班组移除标识
     */
    int setProGroupRemoveStatus(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 获取某个参见单位下的班组
     */
    List<WorkerGroup> getUnRemoveGroupInContractor(@Param("subContractorId") Integer subContractorId);

    /**
     * 查询参建单位下所有未移出项目的班组
     */
    List<WorkerGroup> findRemoveGroupInContract(@Param("subContractorId") Integer subContractorId);

    /**
     * 根据班组编号查询班组名称
     */
    String findTeamNameByTeamSysNo(@Param("teamSysNo") Integer teamSysNo);

}

























