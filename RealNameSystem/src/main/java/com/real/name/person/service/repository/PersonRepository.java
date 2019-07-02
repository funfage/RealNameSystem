package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person3;
import com.real.name.person.entity.PersonQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByIdCardNumber(String idcardNem);

    List<Person> findByPersonIdIn(List<Integer> personIds);

    //根据身份证索引号搜索
    Optional<Person> findByIdCardIndex(String idcardIndex);

    //根据PersonId
    Person3 findByPersonId(Integer id);

    //根据工作类型查询所有人员
    Page<Person> findByWorkRole(Pageable pageable, Integer workRole);

    @Modifying
    @Transactional
    //根据员工ID删除人员
    int deleteByPersonId(Integer personId);

    //根据条件查询人员信息
    @Query("from Person p where p.personName like %:#{#pq.nameOrIDCard}%")
    List<Person> findPeopleByCondition(@Param("pq") PersonQuery personQuery);

}
