package com.real.name.others;

import java.text.SimpleDateFormat;
import java.util.*;

public class MyTest {

    public static void main(String[] args) {
        dateTest();
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

    public static void listTest() {
        List<String> excludeType = new ArrayList<>();
        excludeType.add("face_1");
        excludeType.add("face_2");
        excludeType.add("card_1");
        excludeType.add("faceAndcard_1");
        excludeType.add("faceAndcard_2");
        excludeType.add("idcard_2");
        if (excludeType.contains("face_0")) {
            System.out.println("true");
        }
    }

    public static void dateTest() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = new Date(1562754682832L);
        String start = format.format(startTime);
        Date endTime = new Date(System.currentTimeMillis());
        String end = format.format(endTime);
        System.out.println("start:" + start);
        System.out.println("end:" + end);
    }


}
