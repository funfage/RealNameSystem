package com.real.name.face.controller;

import com.alibaba.fastjson.JSON;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.HTTPTool;
import com.real.name.face.entity.Device;
import com.real.name.face.entity.Record;
import com.real.name.face.service.DeviceService;
import com.real.name.face.service.repository.RecordRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.WebSocket;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
public class CallBackController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RecordRepository faceRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private WebSocket webSocket;

    /**
     * 人脸设备回调地址
     * @param callbackUrl
     * @param pass
     * @return
     */
    @PostMapping("setIdentifyCallBack")
    public ResultVo setIdentifyCallBack(@RequestParam("callbackUrl") String callbackUrl, @RequestParam("pass") String pass) {

        if (!StringUtils.hasText(pass)) {
            throw AttendanceException.emptyMessage("密码 ");
        } else if (!StringUtils.hasText(callbackUrl)) {
            throw AttendanceException.emptyMessage("URL");
        }

        String url = "setIdentifyCallBack";

        Map<String, String> map = new HashMap<>();
        map.put("pass", pass);
        map.put("callbackUrl", callbackUrl);
        return HTTPTool.sendDataTo(url, map);
    }

    /**
     * 设置设备心跳回调
     * @param url 回调地址
     * @param pass 设备密码
     * @return
     */
    @PostMapping("setDeviceHeartBeat")
    public ResultVo setDeviceHeartBeat(@RequestParam("url") String url, @RequestParam("pass") String pass) {

        if (!StringUtils.hasText(pass)) {
            throw AttendanceException.emptyMessage("密码 ");
        } else if (!StringUtils.hasText(url)) {
            throw AttendanceException.emptyMessage("URL ");
        }

        String baseURL = "setDeviceHeartBeat";

        Map<String, String> map = new HashMap<>();
        map.put("pass", pass);
        map.put("url", url);
        return HTTPTool.sendDataTo(baseURL, map);
    }

    @PostMapping("attendance")
    public Map<String, Object> attendance(@RequestParam("ip") String ip, @RequestParam("deviceKey") String deviceKey,
                                          @RequestParam("personId") String personId, @RequestParam("time") long time,
                                          @RequestParam("type") String type, @RequestParam("path") String path, HttpServletRequest request) {

        String IP = HTTPTool.getIpAddr(request);

        String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personId: " + personId +
                ", time: " + time + ", type: " + type + ", path: " + path + ", IP: " + IP + "\n";
        System.out.println("receive face info: " + result);

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
    public Map<String, Object> heartbeat(@RequestParam(value = "ip", required = false) String ip, @RequestParam("deviceKey") String deviceKey,
                                         @RequestParam("personCount") String personCount, @RequestParam("time") long time,
                                         @RequestParam("faceCount") String faceCount, @RequestParam("version") String version, HttpServletRequest request) {

        String IP = HTTPTool.getIpAddr(request);

        String result = "ip: " + ip + ", deviceKey: " + deviceKey + ", personCount: " + personCount +
                ", time: " + time + ", faceCount: " + faceCount + ", version: " + version + ", IP: " + IP + "\n";
        System.out.println("receive heartbeat info: " + result);

        Optional<Device> device = deviceService.findByDeviceId(deviceKey);
        if (device.isPresent()) {
//            device.get().setIp(IP);
//            deviceService.save(device.get());
        } else {
            deviceService.save(new Device(deviceKey, IP));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        map.put("success", true);
        return map;
    }

    @GetMapping("/test")
    public ResultVo test() {

        redisTemplate.opsForValue().set("abc", "99999");
        Map<String, String> m = new HashMap<>();
        m.put("pass", "12345678");
        m.put("id", "-1");

        return ResultVo.success(m);
    }
}
