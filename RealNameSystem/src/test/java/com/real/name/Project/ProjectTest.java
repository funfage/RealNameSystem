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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectTest extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectQueryMapper projectQueryMapper;


    @Test
    public void findByName() {
        Project project = projectQueryMapper.findByName("新兴县四馆一中心工程");
        System.out.println(project);
    }


    @Test
    public void deleteByProjectCodeTestRep() {
//        int i = projectRepository.deleteByProjectCode("55010620190510008");
        int i = projectQueryMapper.deleteByProjectCode("7gU44Drw914wQe8aYK245ks8v32Iu50X");
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
       /* PageRequest pageRequest = PageRequest.of(2, 5);
        Page<Project> all = projectRepository.findAll(pageRequest);*/
        /*for (Project project : all) {
            System.out.println(project);
        }*/
        List<Project> all = projectQueryMapper.findAll();
        System.out.println(all);
    }

    @Test
    public void findAllInProjectCode() {
        Set<String> projectList = new HashSet<>();
        projectList.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        projectList.add("44010620190510008");
        List<Project> projects = projectQueryMapper.findAllInProjectCode(projectList);
        System.out.println(projects);
    }

    @Test
    public void findProjectName() {
//        String projectName = projectRepository.findProjectName("36bj84W235Zgc8O78yuS32510ppMkHfe");
        String projectName = projectQueryMapper.findProjectName("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(projectName);
    }

    @Test
    public void findProNameAndCorp() {
//        Project proNameAndCorp = projectRepository.findProNameAndCorp("36bj84W235Zgc8O78yuS32510ppMkHfe");
        Project proNameAndCorp = projectQueryMapper.findProNameAndCorp("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(proNameAndCorp);
    }

    @Test
    public void findAllProjectCode() {
//        List<String> allProjectCode = projectRepository.findAllProjectCode();
        List<String> allProjectCode = projectQueryMapper.findAllProjectCode();
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

    @Test
    public void saveProject() {
        Project project = new Project();
        project.setProjectCode("dfdfdff");
        project.setAddress("address");
        project.setCategory("01");
        project.setContractorCorpName("dgdg");
        project.setDescription("dfdf");
        project.setName("dgddfff");
        int i = projectQueryMapper.saveProject(project);
        System.out.println(i);
    }



}
