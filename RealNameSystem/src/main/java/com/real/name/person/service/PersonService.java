package com.real.name.person.service;

import com.real.name.common.result.ResultVo;
import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface PersonService {

    /**
     * 将人员添加到项目
     */
    void addPeopleToProject(String projectCode, Integer teamSysNo, List<Person> personList, List<Device> allIssueDevice, List<Device> allProjectIssueDevice);

    /**
     * 删除设备人员信息
     */
    void deleteDevicesPersonInfo(Person person);

    /**
     * 将人员从项目中移除
     */
    void removePersonInProject(Person person, String projectCode, Integer teamSysNo);

    /**
     *查询移除人员的信息
     */
    Person findRemovePerson(Integer personId);

    /**
     * 查询班组下的需要移除的人员信息
     */
    List<Person> findRemovePersonInGroup(Integer teamSysNo, String projectCode);

    /**
     * 获取添加到项目的人员
     * 人员过滤的条件是：与参建单位的CorpCode一致并且没有参见其它项目的人员（或者被移出项目的人员）。
     */
    List<Person> getPersonToAttendProject(String projectCode, String ContractCorpCode, Integer isAdminGroup);


    /**
     * 更新设备人员信息
     */
    void updateDevicesPersonInfo(Person person, String oldName, Integer oldWorkRole);

    /**
     * 将照片信息下发到人脸设备
     */
    void updateDevicesImage(Person person);

    /**
     * 判断人员信息是否成功下发到设备
     */
    String judgeIssueSuccessToDevices(Person person);

    /**
     * 新建人员
     */
    Person createPerson(Person person);

    /**
     * 查询主页人员
     */
    ResultVo findMainPagePerson(Integer personId, Integer pageNum, Integer pageSize, Integer workRole);

    /**
     * 查询全部人员
     */
    Page<Person> findAll(Pageable pageable);

    /**
     * 查询该集合id下所有人员信息
     */
    List<Person> findByPersonIdIn(List<Integer> personIds);

    /**
     * 根据id查询人员
     */
    Optional<Person> findById(Integer personId);

    /**
     * 查询项目中的人员
     * @param personIds 人员id
     */
    List<Person> findPersons(List<Integer> personIds);

    /**
     * 查询人员
     * @param idCardNumber 身份证号码
     */
    Person findByIdCardNumber(String idCardNumber);

    /**
     * 根据身份证索引号查询人员
     * @param idcardIndex 身份证索引号
     */
    Person findByIdCardIndex(String idcardIndex);

    /**
     * 根据workRole查询出所有人员
     */
    List<Person> findByWorkRole(Integer workRole);

    /**
     * 根据员工ID删除所有员工
     */
    int deleteByPersonId(Integer personId);

    /**
     * 更新员工信息
     */
    Person updatePerson(Person person);

    /**
     * 查询人员需要下发的信息
     */
    Person findIssuePersonImageInfo(Integer personId);

    /**
     * 查询需要下发的多个人员信息
     */
    List<Person> findIssueInfoByPersonIdIn(List<Integer> personIds);

    /**
     * 查询多个人员信息和照片信息
     */
    List<Person> findIssuePeopleImagesInfo(List<Integer> personIds);

    /**
     * 查询用户名
     */
    Person findPersonNameByPersonId(Integer personId);

    /**
     * 查询所有人员id
     */
    List<Integer> findAllPersonId();

    /**
     * 查询所有人员id和workRole
     */
    List<Person> findAllPersonRole();

    /**
     * 搜索人员信息
     */
    List<Person> searchPerson(PersonQuery personQuery);

    /**
     * 获取人员首页数据
     */
    Map<String, Object> getPersonMainPageInfo();

    /**
     * 获取班组下的人员
     */
    List<Person> getPersonInGroup(Integer teamSysNo, String projectCode, Integer status);


    /**
     * 获取已经填写过的CorpCode
     */
    List<String> findExistCorpCode();


}
