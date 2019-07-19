package com.real.name.person.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.service.DeviceService;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.Person2;
import com.real.name.person.entity.Person3;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.repository.Person2Rep;
import com.real.name.person.service.repository.PersonRepository;
import com.real.name.project.service.ProjectDetailQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public List<Person> findByPersonIdIn(List<Integer> personIds) {
        return personRepository.findByPersonIdIn(personIds);
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
        return personRepository.findByPersonIdIn(personIds);
    }

    @Override
    public List<Person2> findPersons2(List<Integer> personIds) {
        return person2Rep.findByPersonIdIn(personIds);
    }

    @Override
    public Optional<Person> findByIdCardNumber(String idCardNumber) {
        return personRepository.findByIdCardNumber(idCardNumber);
    }

    @Override
    public Optional<Person> findByIdCardIndex(String idCardIndex) {
        return personRepository.findByIdCardIndex(idCardIndex);
    }

    @Override
    public Person3 findByPersonId(Integer id) {
        return personRepository.findByPersonId(id);
    }

    @Override
    public Page<Person> findByWorkRole(PageRequest pageRequest, Integer workRole) {
        return personRepository.findByWorkRole(pageRequest, workRole);
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
        return personRepository.findIssuePersonImageInfo(personId);
    }

    @Override
    public Optional<Person> findPersonNameByPersonId(Integer personId) {
        return personRepository.findPersonNameByPersonId(personId);
    }

    @Override
    public List<Integer> findAllPersonId() {
        return personRepository.findAllPersonId();
    }

    @Override
    public List<Person> findAllPersonRole() {
        return personRepository.findAllPersonRole();
    }

    @Override
    public String getIdCardIndexByPersonId(Integer personId) {
        return personRepository.getIdCardIndexByPersonId(personId);
    }


}
