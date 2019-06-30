package com.real.name.project.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ProjectImp implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PersonService personService;

    @Override
    public Project create(String projectName, String projectCompany,
                          String projectLocal, String projectType, Integer personId) {

        // 判断项目名称是否唯一
        Optional<Project> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isPresent()) throw new AttendanceException(ResultError.PROJECT_EXIST);

        Optional<Person> person = personService.findById(personId);
        if (!person.isPresent()) throw new AttendanceException(ResultError.PERSON_NOT_EXIST);

        Project project = new Project(projectName, projectCompany, projectLocal, projectType, personId);
        return projectRepository.save(project);
    }

    @Override
    public Project createProject(Project project) {

        if (!StringUtils.hasText(project.getName())) {
            throw  AttendanceException.emptyMessage("项目名称");
        }
        // 判断项目名称是否唯一
        Optional<Project> optionalProject = projectRepository.findByName(project.getName());
        if (optionalProject.isPresent()) {
            throw new AttendanceException(ResultError.PROJECT_EXIST);
        }
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
