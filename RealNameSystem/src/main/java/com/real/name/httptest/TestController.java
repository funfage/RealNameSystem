package com.real.name.httptest;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.filter.CustomerJsonSerializer;
import com.real.name.common.result.ResultVo;
import com.real.name.common.schedule.entity.FaceRecordData;
import com.real.name.common.schedule.entity.Records;
import com.real.name.common.websocket.WebSocket;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.record.query.PeriodTime;
import com.real.name.subcontractor.entity.query.GroupPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {

    public class Article implements Serializable {
        private String id;
        private String title;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public class Tag {
        private String id;
        private String tagName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }

    @Autowired
    private ProjectDetailQueryMapper mapper;

    @Autowired
    private WebSocket webSocket;

    @GetMapping("/test1")
    public ResultVo test(@RequestParam("personIds") List<Integer> personIds) {
        return ResultVo.success(personIds);
    }

    @GetMapping("/test2")
    public ResultVo test2(@RequestBody List<GroupPeople> groupPeopleList) {
        return ResultVo.success(groupPeopleList);
    }

    /**
     * 测试时间段
     */
    @PostMapping("/testPeriodTime")
    public ResultVo testPeriodTime(@RequestParam("periodTimes") String periodTimesStr) {
        List<PeriodTime> periodTimes;
        try {
            periodTimes = JSONArray.parseArray(periodTimesStr, PeriodTime.class);
        } catch (Exception e) {
            return ResultVo.failure();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (PeriodTime periodTime : periodTimes) {
            System.out.println(dateFormat.format(new Date(periodTime.getStartTime())));
            System.out.println(dateFormat.format(new Date(periodTime.getEndTime())));
        }
        return ResultVo.success();
    }

    /**
     * get方法测试
     */
    @GetMapping("testGet")
    public ResultVo testGet(String name, String password, Integer gender) {
        System.out.println(name);
        System.out.println(password);
        System.out.println(gender);
        return ResultVo.success();
    }

    @PostMapping("testPost")
    public ResultVo testPost(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("gender") Integer gender) {
        System.out.println(name);
        System.out.println(password);
        System.out.println(gender);
        String data = "name: " + name + "password: " + password + "gender: " + gender;
        return ResultVo.success(data);
    }

    @GetMapping("testIssue")
    public ResultVo testIssue() throws ParseException {
        Person person = new Person();
        person.setPersonId(1020);
        person.setHeadImage("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");
        person.setPersonName("aaa");
        Device device = new Device();
        device.setIp("113.101.245.166");
        device.setOutPort(8090);
       /* //添加人员信息
        Map<String, Object> personMap = DeviceUtils.sendPersonByDeviceId(device, person);
        System.out.println(personMap.get(device.getDeviceId()));
        //查询人员信息
        Map<String, Object> queryPerson = DeviceUtils.sendQueryPerson(person.getPersonId().toString(), device);
        System.out.println(queryPerson.get(device.getDeviceId()));
        //添加照片信息
        Map<String, Object> headImgByDevice = DeviceUtils.sendHeadImgByDevice(device, person);
        System.out.println(headImgByDevice.get(device.getDeviceId()));
        //查询照片信息
        Map<String, Object> queryImage = DeviceUtils.sendQueryImage(person.getPersonId().toString(), device);
        System.out.println(queryImage.get(device.getDeviceId()));
        //获取设备id
        String url = "http://" + device.getIp() + ":" + device.getOutPort() + "/getDeviceKey";
        String result = HTTPTool.postUrlForParam(url, null);
        FaceResult faceResult = null;
        if (result != null) {
            faceResult = JSONObject.parseObject(result, FaceResult.class);
        }*/
        //修改人员信息
        //Map<String, Object> personMap = DeviceUtils.sendUpdatePersonInfoByDevice(device, person);
        //修改人员照片信息
        /*Map<String, Object> map = DeviceUtils.sendUpdateImageByDevice(device, person);
        System.out.println(map.get(device.getDeviceId()));*/
        testFindRecords(device, person);
        return ResultVo.success();
    }

    @GetMapping("testDelete")
    public ResultVo testDelete(@RequestParam("personId") Integer personId) {
        Device device = new Device();
        device.setIp("113.101.245.195");
        device.setOutPort(8090);
        FaceDeviceUtils.deleteDevicePersonInfo(device, personId);
        return ResultVo.success();
    }


    /*@GetMapping("webSocketTest")
    public void webSocketTest() {
        ProjectDetailQuery sendInfo = mapper.getSendInfo(1131, "067R9HQR0dmw178890C4BKubNU2d9gG7");
        JSONObject map = new JSONObject();
        Person person = sendInfo.getPerson();
        map.put("personId", person.getPersonId());
        map.put("personName", person.getPersonName());
        map.put("suffixName", person.getSuffixName());
        map.put("teamName", sendInfo.getWorkerGroup().getTeamName());
        map.put("direction", 2);
        map.put("time", 123456);
        webSocket.sendMessageToAll(map.toJSONString());
    }*/

    @GetMapping("jsonTest")
    public ResultVo jsonTest() throws JsonProcessingException {
        CustomerJsonSerializer cjs = new CustomerJsonSerializer();
        // 设置转换 Article 类时，只包含 id, content
        cjs.filter(Article.class, "id,content", null);
        String include = cjs.toJson(new Article());
        cjs = new CustomerJsonSerializer();
        // 设置转换 Article 类时，过滤掉 id, content
        cjs.filter(Article.class, null, "id,content");
        String filter = cjs.toJson(new Article());
        Map<String, Object> map = new HashMap<>();
        map.put("include", include);
        map.put("filter", filter);
        return ResultVo.success(map);
    }

    @GetMapping("jsonTest2")
    @JSON(type = Article.class, filter = "id,content")
    public Article jsonTest2() {
        Article article = new Article();
        article.setContent("content");
        article.setId("id");
        article.setTitle("title");
        return article;
    }

    @GetMapping("jsonTest3")
    @JSON(type = Article.class, include = "id,content")
    public Article jsonTest3() {
        Article article = new Article();
        article.setContent("content");
        article.setId("id");
        article.setTitle("title");
        return article;
    }

    @GetMapping("jsonTest4")
    @JSONS({
            @JSON(type = Article.class, include = "id"),
            @JSON(type = Tag.class, filter = "tagName")
    })
    public ResultVo jsonTest4() {
        Article article = new Article();
        article.setContent("content");
        article.setId("id");
        article.setTitle("title");
        Tag tag = new Tag();
        tag.setId("id");
        tag.setTagName("tagName");
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        map.put("tag", tag);
        return ResultVo.success(map);
    }

    @PostMapping("jsonTest5")
    public ResultVo jsonTest5(@RequestParam("contractInfos") List<ContractInfo> contractInfos) {
        return ResultVo.success(contractInfos);
    }

    private void testFindRecords(Device device, Person person) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = new Date(System.currentTimeMillis() - 86400000);
        Date endTime = new Date(System.currentTimeMillis());
        FaceRecordData faceRecordData = FaceDeviceUtils.getPersonRecords(device, -1, -1, 0, dateFormat.format(startTime), dateFormat.format(endTime));
        List<Records> recordsList = faceRecordData.getRecords();
        for (Records rd : recordsList) {
            System.out.println(rd.getPersonId());
            System.out.println(rd.getState());
            System.out.println(new Date(rd.getTime()));
        }
        System.out.println(faceRecordData);
    }


}
