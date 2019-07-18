package com.real.name.project.service.implement;

import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectImp implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project) {
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

    @Override
    public Optional<Project> findByName(String projectName) {
        return projectRepository.findByName(projectName);
    }

    @Override
    public String findProjectName(String projectName) {
        return projectRepository.findProjectName(projectName);
    }

    @Override
    public Project findProNameAndCorp(String projectCode) {
        return projectRepository.findProNameAndCorp(projectCode);
    }
}
