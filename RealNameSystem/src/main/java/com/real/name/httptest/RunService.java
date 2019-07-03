package com.real.name.httptest;

import com.real.name.common.info.DeviceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RunService {

    private static String GET_URL = "http://localhost:9901/attendance/testGet?name={name}&password={password}&gender={gender}";
    private static String POST_URL = "http://localhost:9901/attendance/testPost";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("getTestGet")
    public void testGet(Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity entity = new HttpEntity(headers);
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(GET_URL, HttpMethod.GET, entity, String.class, params);
            System.out.println(exchange.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    public void testPost(Map<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                params.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);
        try {
            //获取响应的内容
            String result = restTemplate.postForObject(POST_URL, entity, String.class);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
