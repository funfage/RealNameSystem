package com.real.name.person;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.repository.PersonQueryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        Person person = mapper.findByIdCardNumber("440808199011111");
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
        Person personNameByPersonId = mapper.findPersonNameByPersonId(Integer.valueOf("89"));
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
        Person person = mapper.findByPersonId(137);
        String personstr = JSON.toJSONString(person);
        System.out.println(personstr);
    }

    @Test
    public void countPersonNum() {
        Integer integer = mapper.countPersonNum();
        System.out.println(integer);
    }

    @Test
    public void countTodayPersonNum() {
        Integer integer = mapper.countTodayPersonNum();
        System.out.println(integer);
    }

    @Test
    public void findRemovePerson() {
        Person removePerson = mapper.findRemovePerson(89);
        System.out.println(removePerson);
    }

    @Test
    public void findRemovePersonInGroup() {
        List<Person> removePersonInGroup = mapper.findRemovePersonInGroup(1563331590, "36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(removePersonInGroup);
    }

    @Test
    public void getAdminPersonToAttendProject() {
        List<Person> adminPersonToAttendProject = mapper.getAdminPersonToAttendProject("44010620190510008", "100dfdfere");
        System.out.println(adminPersonToAttendProject);
    }

    @Test
    public void getTodayPersonInfo() {
        List<Map<String, Object>> todayPersonInfo = mapper.getTodayPersonInfo();
        System.out.println(todayPersonInfo);
    }

    @Test
    public void getNormalPersonToAttendProject() {
        List<Person> normalPersonToAttendProject = mapper.getNormalPersonToAttendProject("100dfdfere");
        System.out.println(normalPersonToAttendProject);
    }

    @Test
    public void findIssuePeopleImagesInfo() {
        List<Integer> personIds = new ArrayList<>();
        List<Person> personList = mapper.findIssuePeopleImagesInfo(personIds);
        System.out.println(personList);
    }

    @Test
    public void findIssueInfoByPersonIdIn() {
        List<Integer> personIds = new ArrayList<>();
        personIds.add(89);
        personIds.add(94);
        List<Person> personList = mapper.findIssueInfoByPersonIdIn(personIds);
        System.out.println(personList);
    }

    @Test
    public void getPersonInGroup() {
        List<Person> personInGroup = mapper.getPersonInGroupByStatus(1562649690, "36bj84W235Zgc8O78yuS32510ppMkHfe", 0);
        List<Person> personInGroup1 = mapper.getPersonInGroupByStatus(1562649690, "36bj84W235Zgc8O78yuS32510ppMkHfe", 1);
        List<Person> personInGroup2 = mapper.getPersonInGroupByStatus(1562649690, "36bj84W235Zgc8O78yuS32510ppMkHfe", null);
        System.out.println(personInGroup);
        System.out.println(personInGroup1);
        System.out.println(personInGroup2);
    }


}
