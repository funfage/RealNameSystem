package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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


}
