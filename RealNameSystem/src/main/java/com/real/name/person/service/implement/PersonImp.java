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

    @Override
    public Person create(Person person) {
        if (person.getIdCardNumber() == null || person.getIdCardNumber().trim().length() != 18) {
            throw new AttendanceException(ResultError.ID_CARD_ERROR);
        } else if (!StringUtils.hasText(person.getPersonName())) {
            throw new AttendanceException(ResultError.PERSON_NAME_ERROR);
        } else if (!StringUtils.hasText(person.getNation())) {
            throw AttendanceException.emptyMessage("名族");
        } else if (person.getGender() == null || person.getGender() > 3) {
            throw AttendanceException.errorMessage("性别");
        } else if (!StringUtils.hasText(person.getHeadImage())) {
            throw AttendanceException.emptyMessage("头像");
        } else if (!StringUtils.hasText(person.getGrantOrg())) {
            throw AttendanceException.emptyMessage("签发机关");
        } else if (personRepository.findByIdCardNumber(person.getIdCardNumber()).isPresent()) {
            throw new AttendanceException(ResultError.PERSON_EXIST);
        }
        return personRepository.save(person);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
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
    public Optional<Person> findByIdCardIndex(String idcardIndex) {
        return personRepository.findByIdCardIndex(idcardIndex);
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


}
