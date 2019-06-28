package com.real.name;

import com.real.name.face.entity.Device;
import com.real.name.face.service.DeviceService;
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