package com.real.name.project.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.CommonUtils;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

@Service
public class ProjectImp implements ProjectService {
    Logger logger = LoggerFactory.getLogger(ProjectImp.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project) {

        if (!StringUtils.hasText(project.getName())) {
            throw  AttendanceException.emptyMessage("项目名称");
        }
        // 判断项目名称是否唯一
        Optional<Project> optionalProject = projectRepository.findById(project.getProjectCode());
        if (optionalProject.isPresent()) {
            throw new AttendanceException(ResultError.PROJECT_EXIST);
        }
        //为项目生成一个唯一的projectCode
        project.setProjectCode(CommonUtils.getUniqueString(32));
        //此处日期可能要修改
        project.setStartDate(new Date(25));
        project.setCompleteDate(new Date(100000));
        return projectRepository.save(project);
    }

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Optional<Project> findByProjectCode(String projectCode) {
        return projectRepository.findById(projectCode);
    }

    @Override
    public Project updateByProjectCode(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public int deleteByProjectCode(String projectCode) {
        return projectRepository.deleteByProjectCode(projectCode);
    }
}
