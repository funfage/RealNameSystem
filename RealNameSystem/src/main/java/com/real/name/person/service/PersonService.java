package com.real.name.person.service;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person2;
import com.real.name.person.entity.Person3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PersonService {

    /**
     * 更新设备人员信息
     */
    void updateDevicesPersonInfo(Person person);

    /**
     * 将照片信息下发到人脸设备
     */
    void updateDevicesImage(Person person);

    /**
     * 新建人员
     */
    Person createPerson(Person person);

    /**
     * 查询全部人员
     */
    Page<Person> findAll(Pageable pageable);

    /**
     * 查询该集合id下所有人员信息
     * @param personIds
     * @return
     */
    List<Person> findByPersonIdIn(List<Integer> personIds);

    /**
     * 根据id查询人员
     */
    Optional<Person> findById(Integer personId);

    /**
     * 给人员添加照片
     * @param personId 人员id
     * @param img 照片的base64编码
     */
    Person saveImgBase64(Integer personId, String img);

    /**
     * 查询项目中的人员
     * @param personIds 人员id
     */
    List<Person> findPersons(List<Integer> personIds);

    /**
     * 查询项目中的人员
     * @param personIds 人员id
     */
    List<Person2> findPersons2(List<Integer> personIds);

    /**
     * 查询人员
     * @param idCardNumber 身份证号码
     */
    Optional<Person> findByIdCardNumber(String idCardNumber);

    /**
     * 根据身份证索引号查询人员
     * @param idcardIndex 身份证索引号
     * @return
     */
    Optional<Person> findByIdCardIndex(String idcardIndex);

    //根据PersonId
    Person3 findByPersonId(Integer id);

    //根据workRole查询出所有人员
    Page<Person> findByWorkRole(PageRequest pageRequest, Integer workRole);

    //根据员工ID删除所有员工
    int deleteByPersonId(Integer personId);

    //更新员工信息
    Person updateByPersonId(Person person);

    Person findIssuePersonImageInfo(Integer personId);

    //查询用户名
    Optional<Person> findPersonNameByPersonId(@Param("personId") Integer personId);

    //查询所有人员id
    List<Integer> findAllPersonId();

    /**
     * 查询所有人员id和workRole
     */
    List<Person> findAllPersonRole();

    /**
     * 查询人员的身份证索引号
     */
    String getIdCardIndexByPersonId(Integer personId);


}
