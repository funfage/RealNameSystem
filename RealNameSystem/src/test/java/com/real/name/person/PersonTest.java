package com.real.name.person;

import com.alibaba.fastjson.JSONObject;
import com.real.name.auth.entity.User;
import com.real.name.others.BaseTest;
import com.real.name.common.utils.FileTool;
import com.real.name.httptest.RunService;
import com.real.name.person.controller.PersonController;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.PersonQueryMapper;
import com.real.name.person.service.repository.PersonRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonTest extends BaseTest {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private RunService runService;

    @Autowired
    private PersonQueryMapper personQueryMapper;

    @Test
    public void testGetPersonAll(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Person> personPage = personRepository.findAll(pageRequest);
        System.out.println(personPage.getContent());
    }


    @Test
    public void testGetPersonAllByWorkRole(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Integer workRole = 20;
        /*Page<Person> allByWorkRole = personRepository.findByWorkRole(pageRequest, workRole);
        System.out.println(allByWorkRole.getContent());*/
    }

    @Test
    public void testDeletePersonById(){
        personRepository.deleteById(71);
    }

    @Test
    public void updatePersonByIdTest(){
        Optional<Person> optional = personRepository.findById(72);
        Person person = optional.get();
        person.setPersonName("aaa");
        Person updatePerson = personRepository.save(person);
        System.out.println(updatePerson);
    }

    @Test
    public void testA(){
        String str1 = null;
        String str2 = "";
        System.out.println(StringUtils.hasText(str1));
        System.out.println(StringUtils.hasText(str2));
    }

    @Test
    public void findPersonTest() {
        PageRequest p = PageRequest.of(0, 10);
        /*Page<Person> work = personRepository.findByWorkRole(p, 10);
        System.out.println(work);*/
    }

    @Test
    public void createPersonTest() {
        Person person = new Person();
        person.setPersonName("1111");
        person.setIdCardNumber("437438478");
        person.setWorkType("10");
        person.setIssueCardDate(new Date());
        person.setNation("han");
        person.setGrantOrg("aaa");
        person.setAddress("address");
        person.setHeadImage("dfjdk");
        PersonController personController = new PersonController();
    }

    @Test
    public void createPerson() {
        Map<String, Object> map = new HashMap<>();
       /* map.put("personName", "1111");
        map.put("idCardNumber", "1111");
        map.put("workType", "1111");
        map.put("issueCardDate","1111" );
        map.put("nation", "1111");
        map.put("grantOrg", "1111");
        map.put("address", "1111");
        map.put("headImage", "1111");*/
       /* File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\99.jpg");
        map.put("imageFile", file);*/
        Person person = new Person();
        person.setPersonName("1111");
        person.setIdCardNumber("437438478");
        person.setWorkType("10");
        person.setIssueCardDate(new Date());
        person.setNation("han");
        person.setGrantOrg("aaa");
        person.setAddress("address");
        person.setHeadImage("dfjdk");
        String pstr = JSONObject.toJSONString(person);
        File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\99.jpg");
        map.put("person", pstr);
        map.put("imageFile", file);
        runService.testPost(map, BaseTest.baseUrl + "person/savePerson");
    }

    @Test
    public void generateImgTest() {
        File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\99.jpg");
    }


    /*@Test
    public void findIssuePersonImageInfoTest() {
        Person issuePersonInfo = personRepository.findIssuePersonImageInfo(100);
        System.out.println(issuePersonInfo);
    }

    @Test
    public void findByIdCardIndex() {
        Optional<Person> personOptional = personRepository.findByIdCardIndex("123456");
        System.out.println(personOptional.get());
    }

    @Test
    public void findPersonNameByPersonId() {
        Optional<Person> personOptional = personRepository.findPersonNameByPersonId(102);
        System.out.println(personOptional.get());
    }

    @Test
    public void findAllPersonRole() {
        List<Person> allPersonRole = personRepository.findAllPersonRole();
        System.out.println(allPersonRole);
    }

    @Test
    public void getIdCardIndexByPersonId() {
        String idCardIndex = personRepository.getIdCardIndexByPersonId(1100);
        System.out.println(idCardIndex);
    }*/

    /**
     * ==============================================================
     */

    @Test
    public void findAll() {
        List<Person> all = personQueryMapper.findAll();
        User user = new User();
        System.out.println(all);
    }

    @Test
    public void findAllPersonInProjects() {
        Set<String> projectList = new HashSet<>();
        projectList.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        List<Person> allPersonInProjects = personQueryMapper.findAllPersonInProjects(projectList);
        System.out.println(allPersonInProjects);
    }

    @Test
    public void findAllPersonNotAttendProject() {
        List<Person> allPersonNotAttendProject = personQueryMapper.findAllPersonNotAttendProject();
        System.out.println(allPersonNotAttendProject);
    }

    @Test
    public void findByWorkRole() {
        List<Person> personList = personQueryMapper.findByWorkRole(10);
        System.out.println(personList);
    }

    @Test
    public void findByWorkRoleInProject() {
        Set<String> projectCOdeList = new HashSet<>();
        projectCOdeList.add("36bj84W235Zgc8O78yuS32510ppMkHfe");
        List<Person> byWorkRoleInProject = personQueryMapper.findByWorkRoleInProject(10, projectCOdeList);
        System.out.println(byWorkRoleInProject);
    }

    @Test
    public void findPersonById() {
        Person byPersonId = personQueryMapper.findByPersonId(89);
        String s = JSONObject.toJSONString(byPersonId);
        System.out.println(s);
    }



}