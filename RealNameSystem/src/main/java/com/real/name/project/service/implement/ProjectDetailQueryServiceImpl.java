package com.real.name.project.service.implement;

import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectDetailQueryServiceImpl implements ProjectDetailQueryService {

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Override
    public void insertProjectDetail(ProjectDetailQuery projectDetailQuery) {
        projectDetailQueryMapper.insertProjectDetail(projectDetailQuery);
    }

    @Override
    public List<ProjectDetailQuery> findOtherAdmins(String projectCode, String groupCorpCode) {
        return projectDetailQueryMapper.findOtherAdmins(projectCode, groupCorpCode);
    }

    @Override
    public List<ProjectDetailQuery> findOtherWorker(String groupCorpCode) {
        return projectDetailQueryMapper.findOtherWorker(groupCorpCode);
    }

    @Override
    public List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode) {
        return projectDetailQueryMapper.getPersonAndWorkerGroupInfo(projectCode);
    }

    @Override
    public List<String> getProjectCodeListByPersonId(Integer personId) {
        List<String> projectCodes = projectDetailQueryMapper.getProjectCodeListByPersonId(personId);
        return projectCodes.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<GroupPersonNum> getWorkGroupPersonNum(String projectCode) {
        return projectDetailQueryMapper.getWorkGroupPersonNum(projectCode);
    }

    @Override
    public int deletePersonInProject(String projectCode, Integer personId) {
        return projectDetailQueryMapper.deletePersonInProject(projectCode, personId);
    }

    @Override
    public String findTeamName(String projectCode, Integer personId) {
        return projectDetailQueryMapper.findTeamName(projectCode, personId);
    }

    @Override
    public String findProjectCodeByTeamSysNo(Integer teamSysNo) {
        return projectDetailQueryMapper.findProjectCodeByTeamSysNo(teamSysNo);
    }

    @Override
    public List<Person> findPersonByTeamSyNo(Integer teamSysNo) {
        return projectDetailQueryMapper.findPersonByTeamSyNo(teamSysNo);
    }

    @Override
    public List<ProjectDetailQuery> findProjectDetail(String projectCode) {
        return projectDetailQueryMapper.findProjectDetail(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> getProjectFaceIssueDetail(String projectCode) {
        return projectDetailQueryMapper.getProjectFaceIssueDetail(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> getProjectAccessIssueDetail(String projectCode) {
        return projectDetailQueryMapper.getProjectAccessIssueDetail(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> getWorkerGroupInProject(String projectCode) {
        return projectDetailQueryMapper.getWorkerGroupInProject(projectCode);
    }

    @Override
    public Integer getPersonNumInGroup(Integer teamSysNo) {
        return projectDetailQueryMapper.getPersonNumInGroup(teamSysNo);
    }

    @Override
    public List<ProjectDetailQuery> findPersonWorkDayInfoInProject(String projectCode, Date startDate, Date endDate) {
        return projectDetailQueryMapper.findPersonWorkDayInfoInProject(projectCode, startDate, endDate);
    }

    @Override
    public List<ProjectDetailQuery> findIdAndPersonInProject(String projectCode) {
        return projectDetailQueryMapper.findIdAndPersonInProject(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> findDelPersonInDeviceByProject(String projectCode) {
        return projectDetailQueryMapper.findDelPersonInDeviceByProject(projectCode);
    }

    @Override
    public Integer countPersonNumByProjectCode(String projectCode) {
        return projectDetailQueryMapper.countPersonNumByProjectCode(projectCode);
    }

    @Override
    public boolean judgeEmptyById(Integer id) {
        Integer queryId = projectDetailQueryMapper.getIdWhileExists(id);
        return queryId == null || queryId <= 0;
    }

    @Override
    public int setProGroupPersonRemove(Integer personId, String projectCode, Integer teamSysNo) {
        return projectDetailQueryMapper.setProPersonStatus(personId, 0, projectCode);
    }

    @Override
    public int setProGroupPersonUnRemove(Integer personId, String projectCode, Integer teamSysNo) {
        return projectDetailQueryMapper.setProPersonStatus(personId, 1, projectCode);
    }

    @Override
    public ProjectDetail findUnRemByProAndPersonId(String projectCode, Integer personId) {
        return projectDetailQueryMapper.findUnRemByProAndPersonId(projectCode, personId);
    }

    @Override
    public boolean judgePersonInProGroup(String projectCode, Integer teamSysNo, Integer personId) {
        Integer integer = projectDetailQueryMapper.judgePersonInProGroup(projectCode, teamSysNo, personId);
        return integer != null && integer > 0;
    }


}















