package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

@Mapper
public interface PersonQueryMapper {

    /**
     * 查询所有人员信息
     */
    List<Person> findAll();

    /**
     * 根据人员id查询
     */
    Person findByPersonId(Integer personId);

    /**
     *查询移除人员的信息
     */
    Person findRemovePerson(Integer personId);

    /**
     * 查询班组下的需要移除的人员信息
     */
    List<Person> findRemovePersonInGroup(@Param("teamSysNo") Integer teamSysNo,
                                         @Param("projectCode") String projectCode);


    /**
     * 查询所在项目的人员信息
     */
    List<Person> findAllPersonInProjects(@Param("projectCodeList") Set<String> projectCodeList);

    /**
     * 查询没有参加项目的人员信息
     */
    List<Person> findAllPersonNotAttendProject();

    /**
     * 根据人员类型查询
     */
    List<Person> findByWorkRole(@Param("workRole") Integer workRole);

    /**
     * 根据人员类型查询项目下的人员
     */
    List<Person> findByWorkRoleInProject(@Param("workRole") Integer workRole,
                                         @Param("projectCodeList") Set<String> projectCodeList);

    /**
     * 根据人员类型查询项目下的人员获取未参见项目的人员
     */
    List<Person> findByWorkRoleInUnionNotAttendProject(@Param("workRole") Integer workRole,
                                                       @Param("projectCodeList") Set<String> projectCodeList);

    /**
     * 根据idCardNum查询
     */
    Person findByIdCardNumber(@Param("idCardNum") String idCardNum);

    /**
     * 查询多个人员信息
     */
    List<Person> findByPersonIdIn(@Param("personIds") List<Integer> personIds);

    /**
     * 根据身份证索引号搜索
     */
    Person findByIdCardIndex(@Param("idCardIndex") String idCardIndex);

    /**
     * 查询用户名
     */
    @Query("select new com.real.name.person.entity.Person(p.personId, p.personName) from Person p where p.personId = :personId")
    Person findPersonNameByPersonId(@Param("personId") Integer personId);

    /**
     * 查询所有人员id
     */
    List<Integer> findAllPersonId();

    /**
     * 查询所有人员id和workRole
     */
    List<Person> findAllPersonRole();

    /**
     * 查询下发人员和照片所需要的字段
     */
    Person findIssuePersonImageInfo(@Param("personId") Integer personId);

    /**
     * 根据人员查询身份证索引号
     */
    String getIdCardIndexByPersonId(@Param("personId") Integer personId);

    /**
     * 搜索人员
     */
    List<Person> searchPerson(@Param("personQuery") PersonQuery personQuery);

    /**
     * 获取总的人员数
     */
    Integer countPersonNum();

    /**
     * 获取当日注册的人员数量
     */
    Integer countTodayPersonNum();



}
