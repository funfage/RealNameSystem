package com.real.name.device;

import com.real.name.others.BaseTest;
import com.real.name.device.entity.Device;
import com.real.name.device.service.repository.DeviceRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeviceTest extends BaseTest {
    @Autowired
    private DeviceRepository repository;

    @Test
    public void findAllByDeviceTypeTest() {
        List<Device> allByDeviceType = repository.findAllByDeviceType(3);
        System.out.println(allByDeviceType.size());
    }

    @Test
    public void findByProjectCodeAndDeviceTypeTest() {
        List<Device> byProjectCodeAndDeviceType = repository.findByProjectCodeAndDeviceType("44010620190510009", 1);
        System.out.println(byProjectCodeAndDeviceType);
    }

    @Test
    public void getDeviceIdList() {
        List<String> deviceIdList = repository.getDeviceIdList();
        System.out.println(deviceIdList);
    }

    @Test
    public void findByIpAndOutPortTest() {
        Optional<Device> device = repository.findByIpAndOutPort("113.101.245.45", 8092);
        System.out.println(device.get());
    }

    @Test
    public void findByProjectCodeIn() {
        List<String> projectCodes = new ArrayList<>();
        projectCodes.add("067R9HQR0dmw178890C4BKubNU2d9gG7");
        projectCodes.add("84z2Mj71Vq95VgnFMo7Ca9LpAK81986s");
        List<Device> deviceList = repository.findByProjectCodeIn(projectCodes);
        System.out.println(deviceList);
    }


    @Test
    public void findByProjectCodeInAndDeviceType() {
        List<String> projectCodes = new ArrayList<>();
        projectCodes.add("067R9HQR0dmw178890C4BKubNU2d9gG7");
        repository.findByProjectCodeInAndDeviceType(projectCodes, 3);
    }


}
