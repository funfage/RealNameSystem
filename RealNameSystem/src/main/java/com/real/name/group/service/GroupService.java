package com.real.name.group.service;

import com.real.name.common.entity.forNational.Worker;
import com.real.name.group.entity.WorkerGroup;

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
     * 根据班组名查找班组
     * @param name 班组名称
     */
    Optional<WorkerGroup> findByTeamName(String  name);

    /**
     * 查找全部班组
     */
    List<WorkerGroup> findAll();

    /**
     * 根据项目id查找班组
     */
    List<WorkerGroup> findByProjectCode(String projectCode);
}
