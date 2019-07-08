package com.real.name.http;

import com.real.name.others.BaseTest;
import com.real.name.httptest.RunService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class httptest extends BaseTest {

   @Autowired
    private RunService runService;

    @Test
    public void testGet() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "23");
        map.put("password", "34");
        map.put("gender", 1);
        runService.testGet(map);
    }

    @Test
    public void testPost() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "23");
        map.put("password", "34");
        map.put("gender", 1);
        runService.testPost(map, "");
    }

}
