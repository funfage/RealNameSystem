package com.real.name.redis;

import com.alibaba.fastjson.JSONObject;
import com.real.name.BaseTest;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class RedisTest extends BaseTest {
    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Test
    public void jedisTest(){
        jedisStrings.set("name", "zhangsan", 10, TimeUnit.MINUTES);
        jedisStrings.set("password", "123", 1, TimeUnit.SECONDS);
        String name = (String) jedisStrings.get("name");
        System.out.println(name);
        jedisStrings.setIfAbsent("age", 12, 100, TimeUnit.SECONDS);
        jedisStrings.setIfAbsent("age", 18, 100, TimeUnit.SECONDS);
        jedisStrings.setIfPresent("age", 20, 100, TimeUnit.SECONDS);
        jedisKeys.del("age");
        jedisStrings.setIfPresent("age", 22, 100, TimeUnit.SECONDS);
        jedisStrings.set("gender", "man");
        jedisKeys.del("gender");
    }

    @Test
    public void jsonTest() {
        Device device = new Device();
        device.setIp("12.2323.4234");
        device.setOutPort(234849);
        device.setDeviceId("123");
        JSONObject deviceJSON = new JSONObject();
        deviceJSON.put("deviceId", device.getDeviceId());
        deviceJSON.put("deviceIp", device.getIp());
        deviceJSON.put("outPort", device.getOutPort());
        jedisStrings.set(device.getDeviceId(), deviceJSON.toJSONString());
        String deviceInfo = (String) jedisStrings.get(device.getDeviceId());
        Device selectDevice = JSONObject.parseObject(deviceInfo, Device.class);
        System.out.println(selectDevice);
    }


}
