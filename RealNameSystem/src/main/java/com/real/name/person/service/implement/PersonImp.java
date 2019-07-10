package com.real.name.person.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person2;
import com.real.name.person.entity.Person3;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.Person2Rep;
import com.real.name.person.service.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonImp implements PersonService {

    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private Person2Rep person2Rep;

    @Transactional
    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public List<Person> findByPersonIdIn(List<Integer> personIds) {
        return personRepository.findByPersonIdIn(personIds);
    }

    @Override
    public Optional<Person> findById(Integer personId) {
        return personRepository.findById(personId);
    }

    @Override
    public Person saveImgBase64(Integer personId, String img) {
        Optional<Person> person = findById(personId);
        if (!person.isPresent()) throw new AttendanceException(ResultError.PERSON_NOT_EXIST);
        person.get().setHeadImage(img);
        return personRepository.save(person.get());
    }

    @Override
    public List<Person> findPersons(List<Integer> personIds) {
        return personRepository.findByPersonIdIn(personIds);
    }

    @Override
    public List<Person2> findPersons2(List<Integer> personIds) {
        return person2Rep.findByPersonIdIn(personIds);
    }

    @Override
    public Optional<Person> findByIdCardNumber(String idCardNumber) {
        return personRepository.findByIdCardNumber(idCardNumber);
    }

    @Override
    public Optional<Person> findByIdCardIndex(String idCardIndex) {
        return personRepository.findByIdCardIndex(idCardIndex);
    }

    @Override
    public Person3 findByPersonId(Integer id) {
        return personRepository.findByPersonId(id);
    }

    @Override
    public Page<Person> findByWorkRole(PageRequest pageRequest, Integer workRole) {
        return personRepository.findByWorkRole(pageRequest, workRole);
    }

    @Override
    public int deleteByPersonId(Integer personId) {
        return personRepository.deleteByPersonId(personId);
    }

    @Override
    public Person updateByPersonId(Person person) {
        return personRepository.save(person);
    }

    /*@Override
    public Person findIssuePersonInfo(Integer personId) {
        return personRepository.findIssuePersonInfo(personId);
    }

    @Override
    public Person findIssueImageInfo(Integer personId) {
        return personRepository.findIssueImageInfo(personId);
    }*/

    @Override
    public Person findIssuePersonImageInfo(Integer personId) {
        return personRepository.findIssuePersonImageInfo(personId);
    }

    @Override
    public Optional<Person> findPersonNameByPersonId(Integer personId) {
        return personRepository.findPersonNameByPersonId(personId);
    }

    @Override
    public List<Integer> findAllPersonId() {
        return personRepository.findAllPersonId();
    }

    @Override
    public List<Person> findAllPersonRole() {
        return personRepository.findAllPersonRole();
    }


}
