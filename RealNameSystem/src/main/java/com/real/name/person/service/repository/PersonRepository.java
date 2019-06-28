package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

    //根据员工ID删除人员
    void deleteById(Integer id);


    /*//修改员工信息
    @Modifying
    @Query("update Person p set p.personName=?1, p.idCardIndex=?2, p.cellPhone=?3, p.gender=?4, p.age=5, p.nation=?6, p.address=?7" +
            " where p.personId=?8 and " +
            "?1 is not null and " +
            "?2 is not null and " +
            "?3 is not null and " +
            "?4 is not null and " +
            "?5 is not null and " +
            "?6 is not null and " +
            "?7 is not null")
    int updatePerson(String personName, String idCardNumber, String cellPhone, String gender,
                         String age, String nation, String address, Integer id);*/

}
