package com.real.name.person;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.repository.PersonQueryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

}
