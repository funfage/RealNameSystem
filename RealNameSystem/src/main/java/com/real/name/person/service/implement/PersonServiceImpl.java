package com.real.name.person.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.constant.CommConstant;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.*;
import com.real.name.common.websocket.WebSocket;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceQueryMapper;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.DeleteInfoService;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.issue.service.repository.IssueFaceMapper;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.PersonQueryMapper;
import com.real.name.person.service.repository.PersonRepository;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private PersonQueryMapper personQueryMapper;

    @Autowired
    private IssueFaceMapper issueFaceMapper;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private GroupService groupService;

    @Autowired
    private IssueFaceService issueFaceService;

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private DeleteInfoService deleteInfoService;

    @Transactional
    @Override
    public void addPeopleToProject(String projectCode, Integer teamSysNo, List<Person> personList, List<Device> allIssueDevice, List<Device> allProjectIssueDevice) {
        for (Person person : personList) {
            if (!StringUtils.isEmpty(person.getPersonName()) || !StringUtils.isEmpty(person.getHeadImage())) {
                Integer personId = person.getPersonId();
                //判断人员是否在这个项目班组中
                boolean exists = projectDetailQueryService.judgePersonInProGroup(projectCode, teamSysNo, personId);
                if (!exists) {
                    //添加项目班组人员信息关联
                    projectDetailQueryService.insertProjectDetail(new ProjectDetailQuery(projectCode, teamSysNo, personId));
                } else {
                    //修改移除标识
                    projectDetailQueryService.setProGroupPersonUnRemove(personId, projectCode, teamSysNo);
                }
                //查询人员所在的班组名
                String teamName = groupService.findTeamNameByTeamSysNo(teamSysNo);
                //将人员进场推送到远程
                if (teamName != null) {
                    JSONObject map = new JSONObject();
                    map.put("projectCode", projectCode);
                    map.put("teamName", teamName);
                    map.put("type", CommConstant.ENTER_TYPE);
                    webSocket.sendMessageToAll(map.toJSONString());
                }
                if (person.getWorkRole() == 10) { //下发到所有设备
                    addPersonToDevices(projectCode, person, allIssueDevice);
                } else {  //下发到项目绑定的设备
                    addPersonToDevices(projectCode, person, allProjectIssueDevice);
                }
            }
        }
    }

    private void addPersonToDevices(String projectCode, Person person, List<Device> deviceList) {
        Integer personId = person.getPersonId();
        //保存人脸下发标识
        for (Device device : deviceList) {
            if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
                //将该设备下原有的下发标识删除
                issueFaceService.deleteStatusByPersonInDevice(personId, device.getDeviceId());
                //保存为下发成功的标识
                issueFaceService.insertInitIssue(new IssueFace(person, device));
            } else if(device.getDeviceType() == DeviceConstant.AccessDeviceType) {//保存控制器下发标识
                issueAccessService.deleteStatusByPersonInDevice(personId, device.getDeviceId());
                issueAccessService.insertIssueAccess(new IssueAccess(person, device));
            }
            //将原有的删除下发记录删除
            deleteInfoService.deleteByCondition(personId, device.getDeviceId());
        }
        //下发人员信息
        deviceService.addPersonToDevices(projectCode, person, deviceList);
    }

    @Transactional
    @Override
    public void deleteDevicesPersonInfo(Person person) {
        if (person.getWorkRole() == 10) {
            //获取所有设备信息
            List<Device> allDeviceList = deviceService.findAll();
            //删除设备的人员信息
            deviceService.deletePersonInDeviceList(allDeviceList, person);
        } else if (person.getWorkRole() == 20) {
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
            //获取项目绑定的设备
            List<Device> deviceList = deviceService.findByProjectCodeIn(projectCodes);
            deviceService.deletePersonInDeviceList(deviceList, person);
        }
    }

    @Transactional
    @Override
    public void removePersonInProject(Person person, String projectCode, Integer teamSysNo) {
        //设置移出标识
        int i = projectDetailQueryService.setProGroupPersonRemove(person.getPersonId(), projectCode, teamSysNo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.REMOVE_FROM_PROJECT_FAILURE);
        }
        //查询该项目所绑定的设备
        List<Device> projectDevices = deviceService.findByProjectCode(projectCode);
        //删除人员在设备上的信息
        deviceService.deletePersonInDeviceList(projectDevices, person);
        //查被删除人员所在的班组，并将出场人数加1
        String teamName = groupService.findTeamNameByTeamSysNo(teamSysNo);
        logger.info("出场的人员姓名为：{}", person.getPersonName());
        logger.info("出场的班组名为：{}", teamName);
        String key = projectCode + CommConstant.ABSENT + teamName;
        if (jedisKeys.hasKey(key)) {
            String value = (String) jedisStrings.get(key);
            int number = Integer.parseInt(value.substring(value.lastIndexOf(",") + 1)) + 1;
            jedisStrings.set(key, teamName + "," + number, TimeUtil.getTomorrowBeginMilliSecond(), TimeUnit.MILLISECONDS);
        } else {
            jedisStrings.set(key, teamName + "," + 1, TimeUtil.getTomorrowBeginMilliSecond(), TimeUnit.MILLISECONDS);
        }
        //推送出场消息给前端
        JSONObject map = new JSONObject();
        map.put("teamName", teamName);
        map.put("projectCode", projectCode);
        map.put("type", CommConstant.ABSENT_TYPE);
        webSocket.sendMessageToAll(map.toJSONString());
    }

    @Override
    public Person findRemovePerson(Integer personId) {
        return personQueryMapper.findRemovePerson(personId);
    }

    @Override
    public List<Person> findRemovePersonInGroup(Integer teamSysNo, String projectCode) {
        return personQueryMapper.findRemovePersonInGroup(teamSysNo, projectCode);
    }

    @Override
    public List<Person> getPersonToAttendProject(String projectCode, String ContractCorpCode, Integer isAdminGroup) {
        if (isAdminGroup == 1) { // 管理员班组
            return personQueryMapper.getAdminPersonToAttendProject(projectCode, ContractCorpCode);
        } else {
            return personQueryMapper.getNormalPersonToAttendProject(ContractCorpCode);
        }
    }

    @Override
    public void updateDevicesPersonInfo(Person person, String oldName, Integer oldWorkRole) {
        //判断人员类型是否由管理员类型变为其他类型
        if (oldWorkRole == 10 && person.getWorkRole() == 20) {
            //删除该人员在所有人脸设备的信息
            List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
            deviceService.deletePersonInDeviceList(allDevices, person);
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
            //获取该项目所绑定所有人脸设备
            List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
            //更新人员信息
            FaceDeviceUtils.updatePersonToDevices(faceDeviceList, person, 3);
        } else if (!person.getPersonName().equals(oldName)) { //如果人员姓名发生了改变
            if (person.getWorkRole() == 10) { //如果是管理人员则修改所有设备的信息
                List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
                FaceDeviceUtils.updatePersonToDevices(allDevices, person, 3);
            } else if (person.getWorkRole() == 20) { //否则修改绑定设备的信息
                //查询用户所在的项目
                List<String> projectCodes = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
                //获取该项目所绑定所有人脸设备
                List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
                //更新人员信息
                FaceDeviceUtils.updatePersonToDevices(faceDeviceList, person, 3);
            }
        }
    }

    @Override
    public void updateDevicesImage(Person person) {
        if (person.getWorkRole() == 10) {//更新所有设备的照片信息
            //判断是否有设备存在
            List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
            //更新照片信息
            FaceDeviceUtils.updateImageToDevices(allDevices, person, 3);
        } else if (person.getWorkRole() == 20) {//更新给人员所绑定人脸设备的照片信息
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
            //获取该项目所绑定所有人脸设备
            List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
            //更新照片信息
            FaceDeviceUtils.updateImageToDevices(faceDeviceList, person, 3);
        }
    }

    @Override
    public String judgeIssueSuccessToDevices(Person person) {
        //查询人员是否下发到人脸, 如果已经下发判断是否下发成功
        List<IssueFace> issueFailPerson = issueFaceMapper.findIssueFailPersonByPersonId(person.getPersonId());
        if (issueFailPerson.size() > 0) {
            for (IssueFace issueFace : issueFailPerson) {
                //下发失败, 不允许用户修改信息
                if (issueFace.getIssuePersonStatus() == DeviceConstant.issuePersonFailure
                        || issueFace.getIssueImageStatus() == DeviceConstant.issueImageFailure) {
                    //查询出对应的项目信息
                    return projectService.findProjectName(issueFace.getDevice().getProjectCode());
                }
            }
        }
        return null;
    }

    @Transactional
    @Override
    public Person createPerson(Person person) {
        Person selectPerson;
        try {
            selectPerson = personRepository.save(person);
        } catch (Exception e) {
            logger.error("添加人员出现异常", e);
            throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "人员");
        }
        return selectPerson;
    }

    @Override
    public ResultVo findMainPagePerson(Integer personId, Integer pageNum, Integer pageSize, Integer workRole) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        boolean containProjectRole = AuthUtils.isContainProjectRole(user);
        boolean containFuncRole = AuthUtils.isContainFuncRole(user);
        if (personId == -1 && workRole != 0) { //如果id为-1 则根据workRole查询
            if (workRole == -1) {//查询所有人员信息
                PageHelper.startPage(pageNum + 1, pageSize);
                List<Person> allPeople = personQueryMapper.findAll();
                PageInfo<Person> pageInfo = new PageInfo<>(allPeople);
                return PageUtils.pageResult(pageInfo, allPeople);
            } else {//根据workRole查询
                if (containProjectRole && !containFuncRole) {//如果只是项目管理员,则根据workRole查询该项目下或未被分配项目的人员信息
                    PageHelper.startPage(pageNum + 1, pageSize);
                    List<Person> personList = personQueryMapper.findByWorkRoleInUnionNotAttendProject(workRole, user.getProjectSet());
                    PageInfo<Person> pageInfo = new PageInfo<>(personList);
                    return PageUtils.pageResult(pageInfo, personList);
                } else { //否则根据workRole查出所有
                    PageHelper.startPage(pageNum + 1, pageSize);
                    List<Person> personList = personQueryMapper.findByWorkRole(workRole);
                    PageInfo<Person> pageInfo = new PageInfo<>(personList);
                    return PageUtils.pageResult(pageInfo, personList);
                }
            }
        } else { //如果id不为-1, 则根据id查询
            Person person = personQueryMapper.findByPersonId(personId);
            if (person != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("data", person);
                return ResultVo.success(map);
            } else {
                return ResultVo.failure("查询信息为空");
            }
        }
    }

    @Override
    public Person updatePerson(Person person) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        boolean containAdminRole = AuthUtils.isContainAdminRole(user);
        boolean containProjectRole = AuthUtils.isContainProjectRole(user);
        Person updatePerson = null;
        try {
            if (containAdminRole) {
                updatePerson = personRepository.save(person);
            } else if (containProjectRole) {
                //判断该人员是否属于该项目管理员所在的项目下或者是否是没有参见项目的人员
                List<String> ids = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
                if (ids.size() == 0) { //
                    updatePerson = personRepository.save(person);
                } else {
                    for (String id : user.getProjectSet()) {
                        if (ids.contains(id)) {
                            updatePerson = personRepository.save(person);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("修改人员发生异常", e);
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
        if (updatePerson == null) {
            throw new AttendanceException(ResultError.USER_UN_AUTHORIZED);
        }
        return updatePerson;
    }

    @Override
    public Map<String, Object> getPersonMainPageInfo() {
        //获取当日新增的人员
        Integer todayPersonNum = personQueryMapper.countTodayPersonNum();
        //获取总的人员
        Integer personNum = personQueryMapper.countPersonNum();
        //获取当日新增人员的姓名和头像地址
        List<Map<String, Object>> todayPersonInfo = personQueryMapper.getTodayPersonInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("todayPersonNum", todayPersonNum);
        map.put("personNum", personNum);
        map.put("todayPersonInfo", todayPersonInfo);
        return map;
    }

    @Override
    public List<Person> getPersonInGroup(Integer teamSysNo, String projectCode, Integer status) {
        return personQueryMapper.getPersonInGroupByStatus(teamSysNo, projectCode, status);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public List<Person> findByPersonIdIn(List<Integer> personIds) {
        return personQueryMapper.findByPersonIdIn(personIds);
    }

    @Override
    public Optional<Person> findById(Integer personId) {
        return personRepository.findById(personId);
    }

    @Override
    public List<Person> findPersons(List<Integer> personIds) {
        return personQueryMapper.findByPersonIdIn(personIds);
    }

    @Override
    public Person findByIdCardNumber(String idCardNumber) {
        return personQueryMapper.findByIdCardNumber(idCardNumber);
    }

    @Override
    public Person findByIdCardIndex(String idCardIndex) {
        return personQueryMapper.findByIdCardIndex(idCardIndex);
    }

    @Override
    public List<Person> findByWorkRole(Integer workRole) {
        return personQueryMapper.findByWorkRole(workRole);
    }

    @Override
    public int deleteByPersonId(Integer personId) {
        return personRepository.deleteByPersonId(personId);
    }

    @Override
    public Person findIssuePersonImageInfo(Integer personId) {
        return personQueryMapper.findIssuePersonImageInfo(personId);
    }

    @Override
    public List<Person> findIssueInfoByPersonIdIn(List<Integer> personIds) {
        return personQueryMapper.findIssueInfoByPersonIdIn(personIds);
    }

    @Override
    public List<Person> findIssuePeopleImagesInfo(List<Integer> personIds) {
        return personQueryMapper.findIssuePeopleImagesInfo(personIds);
    }

    @Override
    public Person findPersonNameByPersonId(Integer personId) {
        return personQueryMapper.findPersonNameByPersonId(personId);
    }

    @Override
    public List<Integer> findAllPersonId() {
        return personQueryMapper.findAllPersonId();
    }

    @Override
    public List<Person> findAllPersonRole() {
        return personQueryMapper.findAllPersonRole();
    }

    @Override
    public List<Person> searchPerson(PersonQuery personQuery) {
        return personQueryMapper.searchPerson(personQuery);
    }

    @Override
    public List<String> findExistCorpCode() {
        return personQueryMapper.findExistCorpCode();
    }


}
