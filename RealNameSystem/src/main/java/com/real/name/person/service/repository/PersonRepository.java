package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByIdCardNumber(String idcardNem);

    List<Person> findByPersonIdIn(List<Integer> personIds);

    //根据身份证索引号搜索
    Optional<Person> findByIdCardIndex(String idcardIndex);

    //根据PersonId
    Person3 findByPersonId(Integer id);

}
