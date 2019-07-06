package com.real.name.others;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.device.entity.Device;
import com.real.name.issue.entity.IssueDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {

    public static void main(String[] args) {
        testC();
    }

    public static void testMap() {
        Map<String, Object> modelMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        modelMap.put("list", list);
        List<String> ml = (List<String>) modelMap.get("list");
        System.out.println(ml);
    }

    public static void testJSON() {
        String response = "";
    }

    public static void testUrl() {
        String url = "http://localhost:8080/person/find";
        url += "?";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", "20");
        map.put("size", 2);
        map.put("number", 3);
        String data = "";
        for (String key : map.keySet()) {
            data += key + "=" + map.get(key) + "&";
        }
        url += data.substring(0, data.length() - 1);
        System.out.println(url);
    }

    public static void testC() {
        List<IssueDetail> list = new ArrayList<>();
        IssueDetail issue1 = new IssueDetail();
        issue1.setPersonId(23);
        issue1.setProjectCode("sfdfdfd");
        issue1.setIssueId(34L);
        issue1.setIssueImageStatus(1);
        issue1.setIssuePersonStatus(0);
        Device device = new Device();
        device.setDeviceId("dfdf");
        device.setOutPort(3);
        device.setIp("34.34.344");
        issue1.setDevice(device);
        list.add(issue1);
//        String s = JSON.toJSONString(list);
        String s = JSON.toJSON(list).toString();
        List<IssueDetail> issueDetails = JSONObject.parseArray("{\"projectCode\":\"\",\"ip\":\"113.101.245.2\",\"outPort\":8092,\"deviceId\":\"E0F28CF710E2812AF8\"}", IssueDetail.class);
        System.out.println(issueDetails);
    }
}
