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
     * 根据teamName判断班组是否存在
     */
    Integer judgeExistByTeamNameAndSubContractor(@Param("teamName") String teamName,
                                                 @Param("subContractorId") Integer subContractorId);

    /**
     * 根据projectCode查询
     */
    List<GroupQuery> findByProjectCode(@Param("projectCode") String projectCode);

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
     * 查询参建单位下所有未移出项目的班组
     */
    List<WorkerGroup> findRemoveGroupInContract(@Param("subContractorId") Integer subContractorId);

}

