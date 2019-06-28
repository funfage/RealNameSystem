package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectPersonDetail;
import com.real.name.project.service.repository.ProjectPersonDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ProjectPersonDetailRepositoryTest extends BaseTest {
    @Autowired
    private ProjectPersonDetailRepository repository;

    @Test
    public void findAllTest(){
        List<ProjectPersonDetail> personDetails = repository.findAll();
        System.out.println(personDetails.size());
    }

    @Test
    public void findByPersonTest(){
        Person person = new Person();
        person.setPersonId(72);
        Optional<ProjectPersonDetail> projectPersonDetail = repository.findByPerson(person);
        System.out.println(projectPersonDetail);
    }
}
