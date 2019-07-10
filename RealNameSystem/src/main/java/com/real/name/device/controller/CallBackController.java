package com.real.name.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import com.real.name.device.entity.Record;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.RecordService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.WebSocket;
import com.real.name.person.service.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
public class CallBackController {

    private Logger logger = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    private DeviceService deviceService;


    @Autowired
    private PersonService personService;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private RecordService recordService;


    @Autowired
    private WebSocket webSocket;

    @PostMapping("attendance")
    public Map<String, Object> attendance(@RequestParam("ip") String ip, @RequestParam("deviceKey") String deviceKey,
                                          @RequestParam("personId") String personId, @RequestParam("time") long time,
                                          @RequestParam("type") String type, @RequestParam("path") String path, HttpServletRequest request) {
        //获取设备路由器的ip地址
        String routerIP = HTTPTool.getIpAddr(request);
        String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personId: " + personId +", time: " + time + ", type: " + type + ", path: " + path + ", IP: " + routerIP + "\n";
        logger.info("设备识别回调信息:{}", result);
        List<String> excludeType = new ArrayList<>();
        excludeType.add("face_1");
        excludeType.add("face_2");
        excludeType.add("card_1");
        excludeType.add("faceAndcard_1");
        excludeType.add("faceAndcard_2");
        excludeType.add("idcard_2");
        //排除识别失败的记录
        if (type != null && !excludeType.contains(type)) {
            Record record = new Record();
            //获取用户信息
            Optional<Person> optionalPerson = personService.findPersonNameByPersonId(Integer.valueOf(personId));
            //设置用户名
            optionalPerson.ifPresent(person -> record.setPersonName(person.getPersonName()));
            //获取设备信息
            Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceKey);
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();
                record.setDeviceId(device.getDeviceId());
                record.setDeviceType(device.getDeviceType());
                record.setChannel(device.getChannel());
                record.setDirection(device.getDirection());
            }
            record.setType(type);
            record.setPath(path);
            record.setTime(time);
            //保存考勤记录
            recordService.saveRecord(record);
        }
        /**
         * TODO
         * 将考勤信息推送到远程
         */

        //返回标记给设备
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        map.put("success", true);
        return map;
    }


    @PostMapping("heartbeat")
    public Map<String, Object> heartbeat(@RequestParam(value = "ip", required = false) String ip,
                                         @RequestParam("deviceKey") String deviceKey,
                                         @RequestParam("personCount") String personCount,
                                         @RequestParam("time") long time,
                                         @RequestParam("faceCount") String faceCount,
                                         @RequestParam("version") String version,
                                         HttpServletRequest request) {
        //获取设备所在路由器的外网ip
        String routerIP = HTTPTool.getIpAddr(request);
        String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personCount: " + personCount + ", time: " + time + ", faceCount: " + faceCount + ", version: " + version + ", routerIP: " + routerIP + "\n";
        logger.info("设备心跳信息:{}", result);
        //将设备id存入redis
        jedisStrings.set(deviceKey, routerIP);
        //判断数据库中是否有设备的信息
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceKey);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            //如过存在该设备信息则判断设备的ip是否发生改变
            if (device.getIp() == null || !device.getIp().equals(routerIP)) {
                //如果ip发生改变则修改数据库中的ip值
                device.setIp(routerIP);
                deviceService.save(device);
            }
        }
        //返回成功信息给设备
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        map.put("success", true);
        return map;
    }

    /**
     * 更新设备ip
     * @param deviceKey 设备id
     * @param routerIP 设备对应路由器的ip
     * @return 更新后的设备信息
     */
    private Device updateDeviceIp(String deviceKey, String routerIP) {
        try {
            Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceKey);
            if (deviceOptional.isPresent()) {
                Device selectDevice = deviceOptional.get();
                //判断ip地址是否相同
                if (deviceKey != null && !routerIP.equals(selectDevice.getIp())) {
                    //更新数据库中的ip地址
                    selectDevice.setIp(routerIP);
                    deviceService.save(selectDevice);
                }
                JSONObject deviceJSON = new JSONObject();
                deviceJSON.put("deviceId", selectDevice.getDeviceId());
                deviceJSON.put("outPort", selectDevice.getOutPort());
                deviceJSON.put("ip", selectDevice.getIp());
                deviceJSON.put("projectCode", selectDevice.getProjectCode());
                jedisStrings.set(deviceKey, deviceJSON.toJSONString());
                logger.info("更新后的设备信息:{}", deviceJSON.toJSONString());
                return selectDevice;
            }
        } catch (Exception e) {
            logger.error("updateDeviceIp error e:{}", e.getMessage());
            return null;
        }
        return null;
    }

}
