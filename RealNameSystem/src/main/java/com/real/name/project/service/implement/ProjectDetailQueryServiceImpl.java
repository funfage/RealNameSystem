package com.real.name.project.service.implement;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.record.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectDetailQueryServiceImpl implements ProjectDetailQueryService {

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Override
    public List<ProjectDetailQuery> findOtherAdmins(String projectCode) {
        return projectDetailQueryMapper.findOtherAdmins(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> findOtherWorker() {
        return projectDetailQueryMapper.findOtherWorker();
    }

    @Override
    public List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode) {
        return projectDetailQueryMapper.getPersonAndWorkerGroupInfo(projectCode);
    }

    @Override
    public List<String> getProjectIdsByPersonId(Integer personId) {
        List<String> projectCodes = projectDetailQueryMapper.getProjectIdsByPersonId(personId);
        return projectCodes.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<GroupPersonNum> getWorkGroupPersonNum(String projectCode) {
        return projectDetailQueryMapper.getWorkGroupPersonNum(projectCode);
    }

    @Override
    public List<Map<String, Object>> findPersonWorkHoursInfo(Date startDate, Date endDate) {
        List<ProjectDetailQuery> personWorkHoursInfo = projectDetailQueryMapper.findPersonWorkHoursInfo(startDate, endDate);
        //整理考勤信息
        List<Map<String, Object>> attendanceInfo = new ArrayList<>();
        for (ProjectDetailQuery query : personWorkHoursInfo) {
            Map<String, Object> map = new HashMap<>();
            Person person = query.getPerson();
            WorkerGroup workerGroup = query.getWorkerGroup();
            List<Attendance> attendanceList = query.getAttendanceList();
            map.put("personId", person.getPersonId());
            map.put("personName", person.getPersonName());
            map.put("idCardNumber", person.getIdCardNumber());
            map.put("idCardType", person.getIdCardIndex());
            map.put("workType", person.getWorkType());
            map.put("workRole", person.getWorkRole());
            map.put("subordinateCompany", person.getSubordinateCompany());
            map.put("corpCode", person.getCorpCode());
            map.put("teamSysNo", workerGroup.getTeamSysNo());
            map.put("teamName", workerGroup.getTeamName());
            double allWorkHours = 0.0;
            int workDays = 0;
            for (Attendance attendance : attendanceList) {
                if (attendance.getWorkHours() != null && attendance.getWorkHours() > 0) {
                    allWorkHours += attendance.getWorkHours();
                    workDays++;
                }
            }
            map.put("allWorkHours", allWorkHours);
            map.put("workDays", workDays);
            map.put("attendanceList", attendanceList);
            attendanceInfo.add(map);
        }
        return attendanceInfo;
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
    public List<Integer> getProjectIdByGroup(Integer teamSysNo) {
        return projectDetailQueryMapper.getProjectIdByGroup(teamSysNo);
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
        if (queryId != null && queryId >= 0) {
            return false;
        }
        return true;
    }

}















