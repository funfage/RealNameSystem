package com.real.name.project.service.implement;

import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.utils.TimeUtil;
import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectQueryMapper;
import com.real.name.project.service.repository.ProjectRepository;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectQueryMapper projectQueryMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * 如果是项目管理员只查询该项目管理员所在的项目信息
     * 其他角色可以查看所有项目信息
     */
    @Override
    public List<Project> findAll() {
        //从session获取用户
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        boolean containProjectRole = AuthUtils.isContainProjectRole(user);
        boolean containFuncRole = AuthUtils.isContainFuncRole(user);
        if (containProjectRole && ! containFuncRole) { //如果只是项目管理员
            if (user.getProjectSet() != null && user.getProjectSet().size() > 0) {
                //只查询本项目相关信息
                return projectQueryMapper.findAllInProjectCode(user.getProjectSet());
            } else {
                //返回空信息
                return new ArrayList<>();
            }
        } else {
            return projectQueryMapper.findAll();
        }
    }

    @Override
    public Project findByProjectCode(String projectCode) {
        return projectQueryMapper.findByProjectCode(projectCode);
    }

    @Override
    public Project updateByProjectCode(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public int deleteByProjectCode(String projectCode) {
        return projectQueryMapper.deleteByProjectCode(projectCode);
    }

    @Override
    public Project findByName(String projectName) {
        return projectQueryMapper.findByName(projectName);
    }

    @Override
    public String findProjectName(String projectName) {
        return projectQueryMapper.findProjectName(projectName);
    }

    @Override
    public Project findProNameAndCorp(String projectCode) {
        return projectQueryMapper.findProNameAndCorp(projectCode);
    }

    @Override
    public List<Project> searchProject(ProjectQuery projectQuery) {
        return projectQueryMapper.searchProject(projectQuery);
    }

    @Override
    public List<Map<String, String>> findAllProjectCodeAndName() {
        return projectQueryMapper.findAllProjectCodeAndName();
    }

    @Override
    public Map<String, Object> getMainPageProjectInfo() {
        Integer projectNumber = projectQueryMapper.getProjectNumber();
        Integer projectAttendNumber = projectQueryMapper.getProjectAttendNumber();
        Integer projectYedAttendErrNumber = projectQueryMapper.getProjectYedAttendErrNumber(TimeUtil.getYesterdayBegin(), TimeUtil.getTodayBegin());
        Map<String, Object> map = new HashMap<>();
        map.put("projectNumber", projectNumber);
        map.put("projectAttendNumber", projectAttendNumber);
        map.put("projectYedAttendErrNumber", projectYedAttendErrNumber);
        return map;
    }

    @Override
    public List<Map<String, String>> getProjectCodeAndNameByUserRole() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        boolean containProjectRole = AuthUtils.isContainProjectRole(user);
        boolean containFuncRole = AuthUtils.isContainFuncRole(user);
        if (containProjectRole && !containFuncRole) { //如果只是项目管理员
            if (user.getProjectSet() != null && user.getProjectSet().size() > 0) {
                return projectQueryMapper.findProjectCodeAndName(user.getProjectSet());
            } else {
                //返回空信息
                return new ArrayList<>();
            }
        } else {
            return projectQueryMapper.findAllProjectCodeAndName();
        }
    }

}
