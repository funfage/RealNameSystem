package com.real.name.Project;

import com.real.name.others.BaseTest;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person2;
import com.real.name.person.service.repository.Person2Rep;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectPersonDetail;
import com.real.name.project.service.repository.ProjectPersonDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectPersonDetailTest extends BaseTest {
    @Autowired
    private ProjectPersonDetailRepository repository;

    @Autowired
    private Person2Rep person2Rep;


    @Test
    public void findAllTest(){
        List<ProjectPersonDetail> personDetails = repository.findAll();
        System.out.println(personDetails.size());
    }

    @Test
    public void findByPersonTest(){
        Person person = new Person();
        person.setPersonId(76);
        Optional<ProjectPersonDetail> projectPersonDetail = repository.findByPerson(person);
        System.out.println(projectPersonDetail);
    }

    @Test
    public void findByPerson_PersonId() {
        Optional<ProjectPersonDetail> detail = repository.findByPerson_PersonId(76);
        System.out.println(detail.get().getPerson());
    }

    @Test
    public void findByPersonIdIn(){
        List<Integer> personIds = new ArrayList<>();
        personIds.add(76);
        List<Person2> person2s = person2Rep.findByPersonIdIn(personIds);
        System.out.println(person2s);
    }

    @Test
    public void deleteByPerson(){
        Person person = new Person();
        person.setPersonId(76);
        int eff = repository.deleteByPerson(person);
        System.out.println("eff" + eff);
    }

    @Test
    public void deleteByProject() {
        Project project = new Project();
        project.setProjectCode("44010620190510008");
        int eff = repository.deleteByProject(project);
        System.out.println("eff" + eff);
    }

    @Test
    public void deleteByWorkerGroup(){
        WorkerGroup workerGroup = new WorkerGroup();
        workerGroup.setTeamSysNo(1500162326);
        int eff = repository.deleteByWorkerGroup(workerGroup);
        System.out.println(eff);
    }


    @Test
    public void getWorkGroupPersonNum() {
    }



}
