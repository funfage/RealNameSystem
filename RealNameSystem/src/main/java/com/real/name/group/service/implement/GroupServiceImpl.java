package com.real.name.group.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.group.service.repository.GroupQueryMapper;
import com.real.name.group.service.repository.GroupRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.PersonQueryMapper;
import com.real.name.record.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
    private DeviceService deviceService;

    private PersonService personService;

    @Autowired
    public GroupServiceImpl(@Lazy PersonService personService) {
        this.personService = personService;
    }

    @Transactional
    @Override
    public void create(WorkerGroup group) {
        int i = groupQueryMapper.insertGroup(group);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Override
    public GroupQuery findById(Integer id) {
        return groupQueryMapper.findById(id);
    }

    @Override
    public List<GroupQuery> findAll() {
        return groupQueryMapper.findAll();
    }

    @Override
    public void updateByTeamSysNo(WorkerGroup workerGroup) {
        int i = groupQueryMapper.updateWorkerGroup(workerGroup);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Override
    public List<WorkerGroup> getUnRemoveGroupInContractor(Integer subContractorId) {
        return groupQueryMapper.getUnRemoveGroupInContractor(subContractorId);
    }

    @Override
    public void deleteByTeamSysNo(Integer teamSysNo) {
        int i = groupQueryMapper.deleteByTeamSysNo(teamSysNo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
    }

    @Transactional
    @Override
    public void removeGroupInProject(Integer teamSysNo, String projectCode) {
        //设置班组移除标识
        int i = groupQueryMapper.setProGroupRemoveStatus(teamSysNo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.REMOVE_FROM_PROJECT_FAILURE);
        }
        //该班组下所有的未被移除项目的人员信息
        List<Person> personList = personService.findRemovePersonInGroup(teamSysNo, projectCode);
        //将班组下的人员移除项目
        for (Person person : personList) {
            personService.removePersonInProject(person, projectCode, teamSysNo);
        }
    }

    @Transactional
    @Override
    public void groupReJoinToProject(String projectCode, Integer subContractorId, Integer teamSysNo, List<Person> personList, List<Device> allIssueDevice, List<Device> allProjectIssueDevice) {
        //修改班组的移除标识和参建单位
        WorkerGroup group = new WorkerGroup();
        group.setTeamSysNo(teamSysNo);
        group.setProjectCode(projectCode);
        group.setSubContractorId(subContractorId);
        group.setGroupStatus(1);
        int i = groupQueryMapper.updateWorkerGroup(group);
        if (i <= 0) {
            throw new AttendanceException(ResultError.REJOIN_PROJECT_FAILURE);
        }
        //将人员加入项目中
        personService.addPeopleToProject(projectCode, teamSysNo, personList, allIssueDevice, allProjectIssueDevice);
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
    public WorkerGroup findByProjectCodeAndPersonId(String projectCode, Integer personId) {
        return groupQueryMapper.findByProjectCodeAndPersonId(projectCode,  personId);
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

    @Override
    public String findTeamNameByTeamSysNo(Integer teamSysNo) {
        return groupQueryMapper.findTeamNameByTeamSysNo(teamSysNo);
    }

}
