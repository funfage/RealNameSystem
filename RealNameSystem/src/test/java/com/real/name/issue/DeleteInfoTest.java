package com.real.name.issue;

import com.real.name.device.entity.Device;
import com.real.name.issue.entity.DeleteInfo;
import com.real.name.issue.service.repository.DeleteInfoMapper;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DeleteInfoTest extends BaseTest {

    @Autowired
    private DeleteInfoMapper mapper;

    @Test
    public void saveDeleteInfo() {
        DeleteInfo deleteInfo = new DeleteInfo();
        Person person = new Person();
        person.setPersonId(89);
        person.setIdCardIndex("1541211");
        Device device = new Device();
        device.setDeviceId("E0F28CF710E2596CF8");
        deleteInfo.setPerson(person);
        deleteInfo.setDevice(device);
        int i = mapper.saveDeleteInfo(deleteInfo);
        System.out.println(i);
    }

    @Test
    public void findAll() {
        List<DeleteInfo> deleteInfos = mapper.findAll();
        System.out.println(deleteInfos);
    }

    @Test
    public void deleteById() {
        int i = mapper.deleteById(4L);
        System.out.println(i);
    }

    @Test
    public void deleteByCondition() {
        int i = mapper.deleteByCondition(12, "E0F28CF710E2596CF8");
        System.out.println(i);
    }

}
