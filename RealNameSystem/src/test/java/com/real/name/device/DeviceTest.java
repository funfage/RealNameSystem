package com.real.name.device;

import com.real.name.BaseTest;
import com.real.name.device.entity.Device;
import com.real.name.device.service.repository.DeviceRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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


}
