package com.real.name.project.service.implement;

import com.real.name.auth.entity.Role;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectQueryMapper;
import com.real.name.project.service.repository.ProjectRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
       /* if (user != null) {
            if (AuthUtils.isOnlyProjectRole(user)) {
                //只查询本项目相关信息
                return projectQueryMapper.findAllInProjectCode(user.getProjectSet());
            } else {
                return projectQueryMapper.findAll();
            }
        } else {
            return new ArrayList<>();
        }*/
        return projectQueryMapper.findAll();
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
}
