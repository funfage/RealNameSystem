package com.real.name.group.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.group.service.repository.GroupQueryMapper;
import com.real.name.group.service.repository.GroupRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.PersonQueryMapper;
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
    private GroupQueryMapper groupQueryMapper;

    @Autowired
    private PersonService personService;

    @Transactional
    @Override
    public WorkerGroup create(WorkerGroup group) {
        return groupRepository.save(group);
    }

    @Override
    public Optional<WorkerGroup> findById(Integer id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<WorkerGroup> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public WorkerGroup updateByTeamSysNo(WorkerGroup workerGroup) {
        return groupRepository.save(workerGroup);
    }

    @Override
    public void deleteByTeamSysNo(Integer teamSysNo) {
        int i = groupQueryMapper.deleteByTeamSysNo(teamSysNo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
    }

    @Override
    public void removeGroupInProject(Integer teamSysNo, String projectCode) {
        //查询该班组下所有的人员
        List<Person> personList = personService.findRemovePersonInGroup(teamSysNo, projectCode);
        //将班组下的人员移除项目
        for (Person person : personList) {
            personService.removePersonInProject(person, projectCode);
        }
        //设置班组移除标识
        groupQueryMapper.setProGroupRemoveStatus(teamSysNo);
    }

    @Override
    public List<WorkerGroup> findRemoveGroupInContract(Integer subContractorId) {
        return groupQueryMapper.findRemoveGroupInContract(subContractorId);
    }

    @Override
    public boolean judgeExistByTeamNameAndSubContractor(String teamName, Integer subContractorId) {
        Integer integer = groupQueryMapper.judgeExistByTeamNameAndSubContractor(teamName, subContractorId);
        return integer != null && integer >= 0;
    }

    @Override
    public List<GroupQuery> findByProjectCode(String projectCode) {
        return groupQueryMapper.findByProjectCode(projectCode);
    }

    @Override
    public boolean judgeExistAdminGroupByProjectCode(String projectCode) {
        Integer integer = groupQueryMapper.judgeExistAdminGroupByProjectCode(projectCode);
        return integer != null && integer >= 0;
    }


    @Override
    public List<WorkerGroup> searchGroup(GroupQuery groupQuery) {
        return groupQueryMapper.searchGroup(groupQuery);
    }

}
