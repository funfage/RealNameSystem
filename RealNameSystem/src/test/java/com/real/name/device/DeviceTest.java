package com.real.name.device;

import com.real.name.device.query.DeviceQuery;
import com.real.name.device.service.repository.DeviceQueryMapper;
import com.real.name.others.BaseTest;
import com.real.name.device.entity.Device;
import com.real.name.device.service.repository.DeviceRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DeviceTest extends BaseTest {
    @Autowired
    private DeviceRepository repository;

    @Autowired
    private DeviceQueryMapper deviceQueryMapper;

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

    @Test
    public void updateDeviceIPByProjectCode() {
        repository.updateDeviceIPByProjectCode("169.254.39.59", "067R9HQR0dmw178890C4BKubNU2d9gG7");
    }

    @Test
    public void findDeviceIdsByProjectCode() {
        List<String> deviceIdsByProjectCode = repository.findDeviceIdsByProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(deviceIdsByProjectCode);
    }

    @Test
    public void searchDevice() {
        DeviceQuery deviceQuery = new DeviceQuery();
        deviceQuery.setDeviceId("E28");
        List<Device> devices = deviceQueryMapper.searchDevice(deviceQuery);
        System.out.println(devices);
    }

    @Test
    public void delete() {
        Device device = new Device();
        device.setDeviceId("E0F28CF710E2596CF8");
        repository.delete(device);
    }

    @Test
    public void findIPByProjectCode() {
        Set<String> ips = deviceQueryMapper.findIPByProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(ips.iterator().next());
    }


    @Test
    public void getDeviceNumber() {
        Integer deviceNumber = deviceQueryMapper.getFaceDeviceNumber();
        System.out.println(deviceNumber);
    }

}
