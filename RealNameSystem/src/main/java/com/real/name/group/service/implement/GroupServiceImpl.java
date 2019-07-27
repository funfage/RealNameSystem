package com.real.name.group.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.group.service.repository.GroupQueryMapper;
import com.real.name.group.service.repository.GroupRepository;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupQueryMapper groupQueryMapper;

    @Transactional
    @Override
    public WorkerGroup create(WorkerGroup group) {

        Project project = projectService.findByProjectCode(group.getProjectCode());
        if (project == null) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        return groupRepository.save(group);
    }

    @Override
    public Optional<WorkerGroup> findById(Integer id) {
        return groupRepository.findById(id);
    }

    @Override
    public Optional<WorkerGroup> findByTeamName(String name) {
        return groupRepository.findByTeamName(name);
    }

    @Override
    public List<WorkerGroup> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public List<WorkerGroup> findByProjectCode(String projectCode) {
        return groupRepository.findByProjectCode(projectCode);
    }

    @Override
    public WorkerGroup updateByTeamSysNo(WorkerGroup workerGroup) {
        return groupRepository.save(workerGroup);
    }

    @Override
    public int deleteByTeamSysNo(Integer teamSysNo) {
        return groupRepository.deleteByTeamSysNo(teamSysNo);
    }

    @Override
    public Optional<WorkerGroup> findByIsAdminGroupAndProjectCode(Integer status, String projectCode) {
        return groupRepository.findByIsAdminGroupAndProjectCode(status, projectCode);
    }

    @Override
    public List<WorkerGroup> searchGroup(GroupQuery groupQuery) {
        return groupQueryMapper.searchGroup(groupQuery);
    }

}
