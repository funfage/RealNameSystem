package com.real.name.person.service.implement;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.PageUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.service.DeviceService;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person2;
import com.real.name.person.entity.Person3;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.Person2Rep;
import com.real.name.person.service.repository.PersonQueryMapper;
import com.real.name.person.service.repository.PersonRepository;
import com.real.name.project.service.ProjectDetailQueryService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private Person2Rep person2Rep;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private PersonQueryMapper personQueryMapper;

    @Override
    public void updateDevicesPersonInfo(Person person) {
        if (person.getWorkRole() == null) {
            return;
        } else if (person.getWorkRole() == 10) {
            //是否有设备存在
            List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
            FaceDeviceUtils.updatePersonToDevices(allDevices, person, 3);
        } else if (person.getWorkRole() == 20) {
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectIdsByPersonId(person.getPersonId());
            //获取该项目所绑定所有人脸设备
            List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
            //更新人员信息
            FaceDeviceUtils.updatePersonToDevices(faceDeviceList, person, 3);
        }
    }

    @Override
    public void updateDevicesImage(Person person) {
        if (person.getWorkRole() == null) {
            return;
        } else if (person.getWorkRole() == 10) {//更新所有设备的照片信息
            //判断是否有设备存在
            List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
            //更新照片信息
            FaceDeviceUtils.updateImageToDevices(allDevices, person, 3);
        } else if (person.getWorkRole() == 20) {//更新给人员所绑定人脸设备的照片信息
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectIdsByPersonId(person.getPersonId());
            //获取该项目所绑定所有人脸设备
            List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
            //更新照片信息
            FaceDeviceUtils.updateImageToDevices(faceDeviceList, person, 3);
        }
    }

    @Transactional
    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public ResultVo findMainPagePerson(Integer personId, Integer pageNum, Integer pageSize, Integer workRole) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        boolean onlyProjectRole = AuthUtils.isOnlyProjectRole(user);
        if (personId == -1 && workRole != 0) { //如果id为-1 则根据workRole查询
            if (workRole == -1) {//查询所有人员信息
                PageHelper.startPage(pageNum + 1, pageSize);
                List<Person> allPeople = personQueryMapper.findAll();
                PageInfo<Person> pageInfo = new PageInfo<>(allPeople);
                return PageUtils.pageResult(pageInfo, allPeople);
            } else {//根据workRole查询
                if (onlyProjectRole) {//如果是项目管理员,则根据workRole查询该项目下或未被分配项目的人员信息
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
    public Person saveImgBase64(Integer personId, String img) {
        Optional<Person> person = findById(personId);
        if (!person.isPresent()) throw new AttendanceException(ResultError.PERSON_NOT_EXIST);
        person.get().setHeadImage(img);
        return personRepository.save(person.get());
    }

    @Override
    public List<Person> findPersons(List<Integer> personIds) {
        return personQueryMapper.findByPersonIdIn(personIds);
    }

    @Override
    public List<Person2> findPersons2(List<Integer> personIds) {
        return person2Rep.findByPersonIdIn(personIds);
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
    public Person updateByPersonId(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person findIssuePersonImageInfo(Integer personId) {
        return personQueryMapper.findIssuePersonImageInfo(personId);
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
    public String getIdCardIndexByPersonId(Integer personId) {
        return personQueryMapper.getIdCardIndexByPersonId(personId);
    }

    @Override
    public List<Person> searchPerson(PersonQuery personQuery) {
        return personQueryMapper.searchPerson(personQuery);
    }


}
