package com.real.name.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.utils.JedisService;
import com.real.name.device.DeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.entity.Record;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.RecordRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.WebSocket;
import com.real.name.issue.entity.IssueDetail;
import com.real.name.issue.service.IssueDetailService;
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
    private RecordRepository faceRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private IssueDetailService issueDetailService;

    @Autowired
    private PersonService personService;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private JedisService.JedisStrings jedisStrings;


    @Autowired
    private WebSocket webSocket;

    @PostMapping("attendance")
    public Map<String, Object> attendance(@RequestParam("ip") String ip, @RequestParam("deviceKey") String deviceKey,
                                          @RequestParam("personId") String personId, @RequestParam("time") long time,
                                          @RequestParam("type") String type, @RequestParam("path") String path, HttpServletRequest request) {

        String IP = HTTPTool.getIpAddr(request);

      /*  String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personId: " + personId +
                ", time: " + time + ", type: " + type + ", path: " + path + ", IP: " + IP + "\n";
        logger.info("设备识别回调信息:{}", result);*/

        // 保存刷脸记录
        Record faceRecord = new Record(ip, deviceKey, Integer.valueOf(personId), new Date(time), type, path);
        faceRepository.save(faceRecord);

        Map<String, Object> map = new HashMap<>();

        Optional<Device> device = deviceService.findByDeviceId(deviceKey);
        if (device.isPresent()) {
            map.put("device", device.get());
            faceRecord.setDirection(device.get().getDirection());
        }

        // 获取刷脸人员信息
        Optional<Person> person = personService.findById(Integer.valueOf(personId));
        if (person.isPresent()) {
            person.get().setHeadImage(null);
            map.put("person", person.get());
        }

        String s = JSON.toJSONString(map);
        webSocket.sendMessage(s);
//        System.out.println(s);
        //Optional<Project> project = projectDetailService.getProjectFromPersonId(Integer.valueOf(personId));

//        if (person.isPresent()) {
//            person.get().setHeadImage(null);
//            String personStr = JSON.toJSONString(person);
//            webSocket.sendMessage(personStr);
//        }

        // 开门
//        Map<String, Object> m = new HashMap<>();
//        m.put("pass", "12345678");
//        ResultVo resultVo = HTTPTool.postUrlForParam(HTTPTool.baseURL + "device/openDoorControl", m);
//        System.out.println(resultVo);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("result", 1);
        map1.put("success", true);
        return map1;
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

    /**
     * 重发
     * @param issueDetails 需要重发的信息
     */
    private void resend(List<IssueDetail> issueDetails) {
        for (IssueDetail issueDetail : issueDetails) {
            Integer personStatus = issueDetail.getIssuePersonStatus();
            Integer imageStatus = issueDetail.getIssueImageStatus();
            Integer personId = issueDetail.getPersonId();
            Device device = issueDetail.getDevice();
            //判断是下发人员信息还是照片或者都下发
            if (personStatus == 0 && imageStatus == 0) {    //下发人员信息和照片
                //查询下发的人员信息
                Person issuePersonImageInfo = personService.findIssuePersonImageInfo(personId);
                Boolean isSuccess = DeviceUtils.issuePersonByDeviceId(device, issuePersonImageInfo);
                if (isSuccess) {
                    //设置人员信息下发成功标识
                    issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                    //下发照片信息
                    isSuccess = DeviceUtils.issueImageByDeviceId(device, issuePersonImageInfo);
                    if (isSuccess) {
                        //设置照片信息下发成功标识
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                    } else {
                        //设置照片信息下发失败标识
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    }
                } else {
                    //设置下发失败标识
                    issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                }
                if (issueDetail.getIssuePersonStatus() != 0 || issueDetail.getIssueImageStatus() != 0) {
                    //修改数据库信息
                    int effectNum = issueDetailService.updateIssueStatus(issueDetail.getIssuePersonStatus(), issueDetail.getIssueImageStatus(), issueDetail.getIssueId());
                    if (effectNum > 0) {
                        //删除存在redis中下发失败的信息
                        //jedisKeys.del(device.getDeviceId() + device.getProjectCode());
                    }
                }
            } else if (personStatus == 1 && imageStatus == 0) {     //只下发照片
                Person issueImageInfo = personService.findIssueImageInfo(personId);
                Boolean isSuccess = DeviceUtils.issueImageByDeviceId(device, issueImageInfo);
                if (isSuccess) {
                    //设置下发照片成功标识
                    issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                } else {
                    //设置下发照片失败标识
                    issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                }
                if (issueDetail.getIssueImageStatus() == 1) {
                    int effectNum = issueDetailService.updateIssueStatus(1, 1, issueDetail.getIssueId());
                    if (effectNum > 0) {
                        //jedisKeys.del(device.getDeviceId() + device.getProjectCode());
                    }
                }
            } else if (personStatus == 0 && imageStatus == 1) {     //只下发人员信息
                Person issuePersonInfo = personService.findIssuePersonInfo(personId);
                Boolean isSuccess = DeviceUtils.issuePersonByDeviceId(device, issuePersonInfo);
                if (isSuccess) {
                    //设置下发人员成功标识
                    issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                } else {
                    //设置下发人员失败标识
                    issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                }
                if (issueDetail.getIssuePersonStatus() == 1) {
                    int effectNum = issueDetailService.updateIssueStatus(1, 1, issueDetail.getIssueId());
                    if (effectNum > 0) {
                        //jedisKeys.del(device.getDeviceId() + device.getProjectCode());
                    }
                }
            }
        }
    }

}
