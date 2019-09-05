package com.real.name.group.service.repository;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.entity.query.GroupQuery;
import com.real.name.group.entity.search.GroupSearch;
import com.real.name.group.entity.search.GroupSearchInPro;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 根据id集合查询
     */
    List<GroupQuery> findByIdList(@Param("groupIdList") List<Integer> groupIdList);

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
    List<WorkerGroup> searchGroup(@Param("groupSearch") GroupSearch groupSearch);

    /**
     * 根据teamSysNo删除
     */
    int deleteByTeamSysNo(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 设置项目班组移除标识
     */
    int setProGroupRemoveStatus(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 查询某个参建单位下的班组信息
     */
    List<WorkerGroup> getGroupInContractor(@Param("subContractorId") Integer subContractorId,
                                           @Param("groupStatus") Integer groupStatus);

    /**
     * 查询某个参建单位下所有班组名
     */
    List<WorkerGroup> getGroupNameInContractor(@Param("subContractorId") Integer subContractorId,
                                               @Param("groupStatus") Integer groupStatus);

    /**
     * 根据班组编号查询班组名称
     */
    String findTeamNameByTeamSysNo(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 搜索项目中的班组
     */
    List<GroupQuery> searchGroupInPro(@Param("groupSearchInPro") GroupSearchInPro groupSearchInPro);

    /**
     * 查询班组上传标识
     */
    Integer findUploadStatusById(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 修改班组的teamSysNo
     */
    Integer updateTeamSysNo(@Param("oldTeamSysNo") Integer oldTeamSysNo,
                            @Param("newTeamSysNo") Integer newTeamSysNo);

}

























