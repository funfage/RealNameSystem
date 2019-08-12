package com.real.name.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.constant.CommConstant;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.utils.JedisService;
import com.real.name.common.utils.TimeUtil;
import com.real.name.device.entity.Device;
import com.real.name.record.entity.Record;
import com.real.name.device.service.DeviceService;
import com.real.name.record.service.RecordService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.common.websocket.WebSocket;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


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
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Autowired
    private WebSocket webSocket;

    @PostMapping("attendance")
    public Map<String, Object> attendance(@RequestParam("ip") String ip, @RequestParam("deviceKey") String deviceKey,
                                          @RequestParam("personId") String personId, @RequestParam("time") long time,
                                          @RequestParam("type") String type, @RequestParam("path") String path, HttpServletRequest request) {
        //获取设备路由器的ip地址
        String routerIP = HTTPTool.getIpAddr(request);
        String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personId: " + personId +", time: " + time + ", type: " + type + ", path: " + path + ", IP: " + routerIP + "\n";
        logger.debug("设备识别回调信息:{}", result);
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
            //识别模式，0：刷脸，1：卡&人脸双重认证，2：人证比对，3：刷卡
            if (type.equals("face_0")) {
                record.setType(0);
            } else if(type.equals("faceAndcard_0")) {
                record.setType(1);
            } else if (type.equals("idcard_0")) {
                record.setType(2);
            } else if (type.equals("card_0")) {
                record.setType(3);
            }
            //获取用户信息
            Person person = personService.findPersonNameByPersonId(Integer.valueOf(personId));
            //设置用户名
            if (person != null) {
                record.setPersonName(person.getPersonName());
                record.setPersonId(person.getPersonId());
                //获取设备信息
                Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceKey);
                if (deviceOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    record.setDeviceId(device.getDeviceId());
                    record.setDeviceType(device.getDeviceType());
                    record.setChannel(device.getChannel());
                    record.setDirection(device.getDirection());
                    record.setPath(path);
                    record.setTimeNumber(time);
                    record.setDetailTime(new Date(time));
                    //保存考勤记录
                    recordService.saveRecord(record);
                    String key = device.getProjectCode() + CommConstant.PRESENT + person.getPersonId();
                    if (device.getDirection() == 1 && !jedisKeys.hasKey(key)) {
                        ProjectDetailQuery sendInfo = projectDetailQueryMapper.getSendInfo(person.getPersonId(), device.getProjectCode());
                        //将信息推送到远程
                        String presentInfo = sendToClient(sendInfo, device, time);
                        //设置有效时间为第二天凌晨12点
                        jedisStrings.set(key, presentInfo, TimeUtil.getTomorrowBeginMilliSecond(), TimeUnit.MILLISECONDS);
                        logger.warn("在场的key:{}, value:{}, time:{}", device.getProjectCode() + person.getPersonId(), presentInfo, TimeUtil.getTomorrowBeginMilliSecond());
                    } else if (device.getDirection() == 2 && jedisKeys.hasKey(key)) {
                        //删除键
                        ProjectDetailQuery sendInfo = projectDetailQueryMapper.getSendInfo(person.getPersonId(), device.getProjectCode());
                        //将信息推送到远程
                        sendToClient(sendInfo, device, time);
                        jedisKeys.del(key);
                    }
                    logger.info("成功添加了一条人员考勤记录, record:{}", record.toString());
                }
            }
        }
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
        logger.debug("设备心跳信息:{}", result);
        //将设备id存入redis
        jedisStrings.set(deviceKey, routerIP);
        //设备在线的标识,设置过期时间
        jedisStrings.set(DeviceConstant.OnlineDevice + deviceKey, true, 90, TimeUnit.SECONDS);
        //判断数据库中是否有设备的信息
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceKey);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            //如过存在该设备信息则判断设备的ip是否发生改变
            if (device.getIp() == null || !device.getIp().equals(routerIP)) {
                //如果ip发生改变则修改数据库中的ip值
                /*device.setIp(routerIP);
                deviceService.save(device);*/
                deviceService.updateDeviceIPByProjectCode(routerIP, device.getProjectCode());
            }
        }
        //返回成功信息给设备
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        map.put("success", true);
        return map;
    }

    /**
     * 将需要推送的在场信息封装并推送
     */
    private String sendToClient(ProjectDetailQuery sendInfo, Device device, long time) {
        JSONObject map = new JSONObject();
        Person person = sendInfo.getPerson();
        map.put("personId", person.getPersonId());
        map.put("personName", person.getPersonName());
        map.put("projectCode", device.getProjectCode());
        map.put("suffixName", person.getSuffixName());
        map.put("teamName", sendInfo.getWorkerGroup().getTeamName());
        map.put("direction", device.getDirection());
        map.put("time", time);
        map.put("type", CommConstant.PRESENT_TYPE);
        webSocket.sendMessageToAll(map.toJSONString());
        return map.toJSONString();
    }

}
