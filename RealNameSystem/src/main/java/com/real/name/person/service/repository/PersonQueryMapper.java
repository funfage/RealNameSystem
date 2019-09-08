package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.search.PersonSearch;
import com.real.name.person.entity.search.PersonSearchInPro;
import com.real.name.project.entity.ProjectDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
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
     * 获取添加到项目的人员
     * 人员过滤的条件是：与参建单位的CorpCode一致，并且没有参加其它项目（或者被移出项目的人员）的普通人员。
     */
    List<Person> getNormalPersonToAttendProject(@Param("ContractCorpCode") String ContractCorpCode);

    /**
     * 获取添加到项目的人员
     * 人员过滤的条件是：与参建单位的CorpCode一致，并且不在本项目下（或者在该项目下但是已经被移除）的管理人员。
     */
    List<Person> getAdminPersonToAttendProject(@Param("projectCode") String projectCode);

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
     * 查询包括头像信息的多个人员信息
     */
    List<Person> findAllByPersonIdIn(@Param("personIds") List<Integer> personIds);

    /**
     * 查询需要下发的多个人员信息
     */
    List<Person> findIssueInfoByPersonIdIn(@Param("personIds") List<Integer> personIds);

    /**
     * 根据身份证索引号搜索
     */
    Person findByIdCardIndex(@Param("idCardIndex") String idCardIndex);

    /**
     * 查询用户名
     */
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
     * 查询多个人员信息和照片信息
     */
    List<Person> findIssuePeopleImagesInfo(@Param("personIds") List<Integer> personIds);

    /**
     * 根据人员查询身份证索引号
     */
    String getIdCardIndexByPersonId(@Param("personId") Integer personId);

    /**
     * 获取班组下的人员
     */
    List<Person> getPersonInGroupByStatus(@Param("teamSysNo") Integer teamSysNo,
                                  @Param("projectCode") String projectCode,
                                  @Param("personStatus") Integer personStatus);

    /**
     * 搜索人员
     */
    List<Person> searchPerson(@Param("personSearch") PersonSearch personSearch);

    /**
     * 获取总的人员数
     */
    Integer countPersonNum();

    /**
     * 获取当日注册的人员数量
     */
    Integer countTodayPersonNum();

    /**
     * 获取当日新增人员id,后缀名和姓名
     */
    List<Map<String, Object>> getTodayPersonInfo();

    /**
     * 获取已经填写过的CorpCode
     */
    List<Person> findExistCorpCode();

    /**
     * 搜索项目中的人员
     */
    List<ProjectDetailQuery> searchPersonInPro(@Param("personSearchInPro") PersonSearchInPro personSearchInPro);

    /**
     * 查询人员上传到全国平台需要的信息
     */
    List<ProjectDetailQuery> findPeopleUploadInfo(@Param("projectCode") String projectCode,
                                                  @Param("teamSysNo") Integer teamSysNo,
                                                  @Param("personIds") List<Integer> personIds);

    /**
     * 判断人员是否加入了其他项目，不包括从项目中移除的人员
     */
    Integer judgePersonJoinOtherProject(@Param("personId") Integer personId,
                                   @Param("projectCode") String projectCode);

}
