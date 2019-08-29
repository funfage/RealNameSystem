package com.real.name.group.service;

import com.real.name.device.entity.Device;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.person.entity.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupService {

    /**
     * 创建班组
     */
    void create(WorkerGroup group);

    /**
     * 查询所有班组信息
     */
    List<GroupQuery> findAll();

    /**
     * 根据id查询
     */
    GroupQuery findById(@Param("teamSysNo") Integer teamSysNo);

    /**
     * 修改班组信息
     */
    void updateByTeamSysNo(WorkerGroup workerGroup);

    /**
     * 获取某个参见单位下的班组
     */
    List<WorkerGroup> getUnRemoveGroupInContractor(Integer subContractorId);

    /**
     * 删除班组信息
     */
    void deleteByTeamSysNo(Integer teamSysNo);

    /**
     * 从项目中移除班组
     */
    void removeGroupInProject(Integer teamSysNo, String projectCode);

    /**
     * 班组重新加入项目
     */
    void groupReJoinToProject(String projectCode, Integer subContractorId, Integer teamSysNo, List<Person> personList, List<Device> allIssueDevice, List<Device> allProjectIssueDevice);

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
     * 根据projectCode和personId查询出对应的班组
     */
    WorkerGroup findByProjectCodeAndPersonId(String projectCode, Integer personId);

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

    /**
     * 根据班组编号查询班组名称
     */
    String findTeamNameByTeamSysNo(Integer teamSysNo);

    /**
     * 搜索项目中的班组
     */
    List<GroupQuery> searchGroupInPro(GroupQuery groupQuery);

}
