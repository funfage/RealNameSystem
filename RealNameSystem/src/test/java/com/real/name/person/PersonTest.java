package com.real.name.person;

import com.real.name.person.entity.Person;
import com.real.name.person.service.repository.PersonRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonTest {
    @Autowired
    private PersonRepository personRepository;

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
        Page<Person> allByWorkRole = personRepository.findByWorkRole(pageRequest, workRole);
        System.out.println(allByWorkRole.getContent());
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

}
