package com.real.name.person;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.repository.PersonQueryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonQueryTest extends BaseTest {

    @Autowired
    private PersonQueryMapper mapper;

    @Test
    public void find() {
        PersonQuery personQuery = new PersonQuery();
      /*  personQuery.setAgeBegin(27);
        personQuery.setAgeEnd(40);
        personQuery.setWorkRole(10);*/
        //personQuery.setNameOrIdCard("黄");
        PageHelper.startPage(0, 20);
        List<Person> personQueries = mapper.searchPerson(personQuery);
        PageInfo<Person> pageInfo = new PageInfo<>(personQueries);
        System.out.println(pageInfo);
    }

    @Test
    public void findByIdCardNum() {
        Person person = mapper.findByIdCardNumber("440808199011011111");
        System.out.println(person);
    }

    @Test
    public void findByPersonIdIn() {
        List<Integer> personIds = new ArrayList<>();
        personIds.add(89);
        personIds.add(94);
        List<Person> byPersonIdIn = mapper.findByPersonIdIn(personIds);
        System.out.println(byPersonIdIn);
    }

    @Test
    public void findByIdCardIndex() {
        Person person = mapper.findByIdCardIndex("123");
        System.out.println(person);
    }

    @Test
    public void findAllPersonId() {
        List<Integer> allPersonId = mapper.findAllPersonId();
        System.out.println(allPersonId);
    }

    @Test
    public void findAllPersonRole() {
        List<Person> allPersonRole = mapper.findAllPersonRole();
        System.out.println(allPersonRole);
    }

    @Test
    public void findIssuePersonImageInfo() {
        Person person = mapper.findIssuePersonImageInfo(89);
        System.out.println(person);
    }

    @Test
    public void getIdCardIndexByPersonId() {
        String idCardIndexByPersonId = mapper.getIdCardIndexByPersonId(89);
        System.out.println(idCardIndexByPersonId);
    }

    @Test
    public void findPersonNameByPersonId() {
        Person personNameByPersonId = mapper.findPersonNameByPersonId(89);
        System.out.println(personNameByPersonId);
    }

    @Test
    public void findByWorkRoleInUnionNotAttendProject() {
        Set<String> projectList = new HashSet<>();
       /* projectList.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        projectList.add("44010620190510008");*/
        List<Person> byWorkRoleInUnionNotAttendProject = mapper.findByWorkRoleInUnionNotAttendProject(20, projectList);
        System.out.println(byWorkRoleInUnionNotAttendProject);
    }

    @Test
    public void findByPersonId() {
        Person person = mapper.findByPersonId(89);
        System.out.println(person);
    }

}
