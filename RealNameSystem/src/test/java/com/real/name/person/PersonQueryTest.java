package com.real.name.person;

import com.real.name.others.BaseTest;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.repository.PersonQueryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonQueryTest extends BaseTest {

    @Autowired
    private PersonQueryMapper mapper;

    @Test
    public void find() {
        PersonQuery query = mapper.find();
        System.out.println(query);
    }

}
