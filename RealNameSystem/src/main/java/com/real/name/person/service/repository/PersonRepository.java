package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person3;
import org.springframework.data.domain.Page;
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
    @Query("select new com.real.name.person.entity.Person(p.personId, p.personName, p.idCardNumber) from Person p where p.idCardIndex = :idCardIndex")
    Optional<Person> findByIdCardIndex(@Param("idCardIndex") String idCardIndex);

    //查询用户名
    @Query("select new com.real.name.person.entity.Person(p.personId, p.personName) from Person p where p.personId = :personId")
    Optional<Person> findPersonNameByPersonId(@Param("personId") Integer personId);

    //查询所有人员id
    @Query("select new com.real.name.person.entity.Person(p.personId) from Person p")
    List<Integer> findAllPersonId();

    //查询所有人员id和workRole
    @Query("select new com.real.name.person.entity.Person(p.personId, p.workRole) from Person p")
    List<Person> findAllPersonRole();

        //根据PersonId
    Person3 findByPersonId(Integer id);

    //根据工作类型查询所有人员
    Page<Person> findByWorkRole(Pageable pageable, Integer workRole);

    @Modifying
    @Transactional
    //根据员工ID删除人员
    int deleteByPersonId(Integer personId);

    //查询下发人员和照片所需要的字段
    @Query(value = "select new com.real.name.person.entity.Person(p.personId, p.personName, p.headImage, p.workRole, p.idCardIndex) from Person p where  p.personId = ?1")
    Person findIssuePersonImageInfo(Integer personId);

}