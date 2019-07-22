package com.real.name.Project;

import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectQueryMapper;
import com.real.name.project.service.repository.ProjectRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ProjectTest extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectQueryMapper projectQueryMapper;

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
        String projectName = projectRepository.findProjectName("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(projectName);
    }

    @Test
    public void findProNameAndCorp() {
        Project proNameAndCorp = projectRepository.findProNameAndCorp("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(proNameAndCorp);
    }

    @Test
    public void findAllProjectCode() {
        List<String> allProjectCode = projectRepository.findAllProjectCode();
        System.out.println(allProjectCode);
    }

    @Test
    public void searchProject() {
        ProjectQuery projectQuery = new ProjectQuery();
        projectQuery.setName("公司");
       /* projectQuery.setPrjStatus(1);
        projectQuery.setAddress("1");
        projectQuery.setCategory("01");*/
        List<Project> projects = projectQueryMapper.searchProject(projectQuery);
        System.out.println(projects);
    }



}
