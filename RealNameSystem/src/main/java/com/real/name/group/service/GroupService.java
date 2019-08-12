package com.real.name.group.service;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    /**
     * 创建班组
     */
    WorkerGroup create(WorkerGroup group);

    /**
     * 根据班组 id 查找班组
     * @param id teamSysNo
     */
    Optional<WorkerGroup> findById(Integer id);

    /**
     * 查找全部班组
     */
    List<WorkerGroup> findAll();

    /**
     * 修改班组信息
     */
    WorkerGroup updateByTeamSysNo(WorkerGroup workerGroup);

    /**
     * 删除班组信息
     */
    void deleteByTeamSysNo(Integer teamSysNo);

    /**
     * 从项目中移除班组
     */
    void removeGroupInProject(Integer teamSysNo, String projectCode);

    /**
     * 查询参建单位下所有未移出项目的班组
     */
    List<WorkerGroup> findRemoveGroupInContract(Integer subContractorId);

    /**
     * 根据teamName判断班组是否存在
     * true 存在
     * false 不存在
     */
    boolean judgeExistByTeamNameAndSubContractor(String teamName, Integer subContractorId);

    /**
     * 根据projectCode查询
     */
    List<GroupQuery> findByProjectCode(String projectCode);

    /**
     * 根据projectCode查询是否存在管理员班组
     * true 存在
     * false 不存在
     */
    boolean judgeExistAdminGroupByProjectCode(String projectCode);

    /**
     * 搜素班组
     */
    List<WorkerGroup> searchGroup(GroupQuery groupQuery);

}
