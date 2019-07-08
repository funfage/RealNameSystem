package com.real.name.others;

import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class NameApplicationTests extends BaseTest{

    @Autowired
    private DeviceService deviceService;


    @Test
    public void contextLoads() {
        List<Device> all = deviceService.findAll();
        System.out.println(all);
    }
}