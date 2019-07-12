package com.real.name.Project;

import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class ProjectTest extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Test
    public void deleteByProjectCodeTestRep() {
        int i = projectRepository.deleteByProjectCode("55010620190510008");
        Assert.assertEquals(1, i);
    }


    @Test
    public void updateByProjectCodeTestService() {
        Project project = new Project();
        project.setProjectCode("45010620190510008");

        Project updateProject = projectService.updateByProjectCode(project);
        Assert.assertEquals(null, updateProject);
    }

    @Test
    public void findAll() {
        PageRequest pageRequest = PageRequest.of(2, 5);
        Page<Project> all = projectRepository.findAll(pageRequest);
        /*for (Project project : all) {
            System.out.println(project);
        }*/
        System.out.println(all.get());
    }

    @Test
    public void findProjectName() {
        String projectName = projectRepository.findProjectName("067R9HQR0dmw178890C4BKubNU2d9gG7");
        System.out.println(projectName);
    }



}
