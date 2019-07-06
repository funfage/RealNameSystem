package com.real.name.common.utils;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.device.DeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Component
public class HTTPTool {

    private static Logger logger = LoggerFactory.getLogger(HTTPTool.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DeviceService deviceService;

    private static HTTPTool httpTool;

    @PostConstruct
    public void init() {
        httpTool = this;
    }


    /**
     * 发送数据给某个项目中的设备
     *
     * @param url         请求路径
     * @param param       请求参数
     */
    public static Map<String, Object> sendDataToFaceDeviceByProjectCode(String url, Map<String, Object> param, Integer method, List<Device> projectDevices){
        Map<String, Object> modelMap = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        for (Device device : projectDevices) {
            if (StringUtils.hasText(device.getIp()) && device.getOutPort() != null && device.getOutPort() > 0 && device.getOutPort() < 65536) {
                deviceIds.add(device.getDeviceId());
                String response = (method == DeviceConstant.postMethod ? postToDevice(device, url, param) : getToDevce(device, url, param));
                logger.info("设备返回的信息返回的信息:{}", response);
                modelMap.put(device.getDeviceId(), response);
            } else {
                modelMap.put(device.getDeviceId(), null);
                logger.warn("设备id为{}的ip地址为{}设备的ip地址或端口不合法", device.getDeviceId(), device.getIp());
            }
        }
        //将所查询的所有设备id放入集合
        modelMap.put("deviceIds", deviceIds);
        return modelMap;
    }

    /**
     * 发送数据给某个项目中的设备
     *
     * @param url         请求路径
     * @param param       请求参数
     * @param device 项目id
     */
    public static Map<String, Object> sendDataToFaceDeviceByDeviceId(String url, Map<String, Object> param, Integer method, Device device)  throws AttendanceException{
        Map<String, Object> modelMap = new HashMap<>();
        if (StringUtils.hasText(device.getIp()) && device.getOutPort() != null && device.getOutPort() > 0 && device.getOutPort() < 65536) {
            String response = ((method == DeviceConstant.postMethod) ? postToDevice(device, url, param) : getToDevce(device, url, param));
            logger.info("设备返回的信息返回的信息:{}", response);
            modelMap.put(device.getDeviceId(), response);
        }else{
            logger.warn("设备id为{}的ip地址为{}设备的ip地址或端口不合法", device.getDeviceId(), device.getIp());
            modelMap.put(device.getDeviceId(), null);
        }
        return modelMap;
    }


    /**
     * 给所有硬件设备发数据
     *
     * @param url   请求路径
     * @param param 请求参数
     */
    public static Map<String, Object> sendDataToFaceDevice(String url, Map<String, Object> param, Integer method, List<Device> allDevices){
        Map<String, Object> modelMap = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        //查询出所有读头设备
        for (Device device : allDevices) {
            if (StringUtils.hasText(device.getIp()) && device.getOutPort() != null && device.getOutPort() > 0 && device.getOutPort() < 65536) {
                deviceIds.add(device.getDeviceId());
                String response = ((method == DeviceConstant.postMethod) ? postToDevice(device, url, param) : getToDevce(device, url, param));
                logger.info("设备返回的信息返回的信息:{}", response);
                modelMap.put(device.getDeviceId(), response);
            } else {
                logger.warn("设备id为{}的ip地址为{}设备的ip地址或端口不合法", device.getDeviceId(), device.getIp());
                modelMap.put(device.getDeviceId(), null);
            }
        }
        //将所查询的所有设备id放入集合
        modelMap.put("deviceIds", deviceIds);
        return modelMap;
    }

    private static String postToDevice(Device device, String url, Map<String, Object> param) {
        if (param == null) param = new HashMap<>();
        param.put("pass", device.getPass());
        return postUrlForParam(DeviceUtils.getWholeUrl(url, device), param);
    }

    private static String getToDevce(Device device, String url, Map<String, Object> param) {
        if (param == null) param = new HashMap<>();
        param.put("pass", device.getPass());
        return getUrlForParam(DeviceUtils.getWholeUrl(url, device), param);
    }

    /**
     * 发送POST请求
     *
     * @param url   URL
     * @param param 参数
     * @return POST请求的结果，Json格式
     */
    public static String postUrlForParam(String url, Map<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                params.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);
        try {
            //获取响应的内容
            return httpTool.restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            logger.error("postUrlForParam error e:{}", e.getMessage());
            return DeviceConstant.connectTimeOut;
        }
    }

    /**
     * @param baseUrl       提交的URL
     * @return 提交响应
     */
    public static String getUrlForParam(String baseUrl, Map<String, Object> params) throws RuntimeException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity entity = new HttpEntity(headers);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        try {
            ResponseEntity<String> exchange = httpTool.restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class, params);
            logger.info("GetRequest Body:{}", exchange.getBody());
            return exchange.getBody();
        } catch (RestClientException e) {
            logger.error("postUrlForParam error e:{}", e.getMessage());
            return DeviceConstant.connectTimeOut;
        }
    }

    /**
     * 获取IP地址
     *
     * @param request 请求
     * @return IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    if (inet != null) {
                        ipAddress = inet.getHostAddress();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();
        return ipAddress;
    }
}