package com.real.name.redis;

import com.alibaba.fastjson.JSONObject;
import com.real.name.others.BaseTest;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
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
        jedisStrings.set("123", true);
        String name = (String) jedisStrings.get("name");
        Boolean b = (Boolean) jedisStrings.get("123");
        if (b == null) {
            System.out.println("is null");
        }

        System.out.println(name);
        jedisStrings.setIfAbsent("age", 12);
        jedisStrings.setIfAbsent("age", 18);
        jedisStrings.setIfPresent("age", 20);
        jedisStrings.set("age", 3434);
        jedisKeys.del("age");
        jedisStrings.setIfPresent("age", 22, 100, TimeUnit.SECONDS);
        jedisStrings.set("gender", "man");
        jedisKeys.del("gender");
       jedisStrings.set("dfkjfdkj", null);
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

    @Test
    public void jedisTest2() {
        JSONObject map = new JSONObject();
        map.put("personId", 123);
        map.put("personName", "aaa");
        map.put("suffixName", ".jpg");
        map.put("teamName", "teamA");
        map.put("direction", 1);
        map.put("time", 123456);
        map.put("type", 3);
        jedisStrings.set("067R9HQR0dmw178890C4BKubNU2d9gG7", map.toJSONString(), 10, TimeUnit.MINUTES);

        JSONObject map1 = new JSONObject();
        map1.put("personId", 456);
        map1.put("personName", "bbb");
        map1.put("suffixName", ".jpg");
        map1.put("teamName", "teamB");
        map1.put("direction", 1);
        map1.put("time", 123456);
        map1.put("type", 3);
        jedisStrings.set("067R9HQR0dmw178890C4BKubNU2d9gG7", map1.toJSONString(), 10, TimeUnit.MINUTES);

        /*Set<String> keys = jedisKeys.keys("project*");
        List<Object> values = jedisStrings.multiGet(keys);
        System.out.println(values);*/
        //jedisKeys.delByPrex("project");
    }


    @Test
    public void test3() {
        String key = "36bj84W235Zgc8O78yuS32510ppMkHfeabsent杂工";
        String teamName = "杂工";
        for (int i = 0; i < 3; i++) {
            if (jedisKeys.hasKey(key)) {
                String value = (String) jedisStrings.get(key);
                int number = Integer.parseInt(value.substring(value.lastIndexOf(",") + 1)) + 1;
                jedisStrings.set(key, teamName + "," + number);
            } else {
                jedisStrings.set(key, teamName + "," + 1);
            }
        }
    }

}
