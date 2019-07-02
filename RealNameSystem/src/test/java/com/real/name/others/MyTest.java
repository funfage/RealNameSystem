package com.real.name.others;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {

    public static void main(String[] args) {
        testUrl();
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
}
