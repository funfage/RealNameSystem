package com.real.name.common.utils;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.face.entity.Device;
import com.real.name.face.service.DeviceService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Component
public class HTTPTool {

    private static Logger logger = LoggerFactory.getLogger(HTTPTool.class);

    //编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

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
     * @param projectCode 项目id
     */
    public static Map<String, Object> sendDataToFaceDeviceByProjectCode(String url, Map<String, Object> param, String projectCode, Integer method) {
        Map<String, Object> modelMap = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        List<Device> devices = httpTool.deviceService.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.faceDeviceType);
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                if (!StringUtils.hasText(device.getIp()) || device.getOutPort() == null || device.getOutPort() < 1) {
                    continue;
                }
                deviceIds.add(device.getDeviceId());
                String response = (method == DeviceConstant.postMethod ? postToDevice(device, url, param) : getToDevce(device, url, param));
                modelMap.put(device.getDeviceId(), response);
            }
            //将所查询的所有设备id放入集合
            modelMap.put("deviceIds", deviceIds);
            return modelMap;
        }else{
            throw new AttendanceException("该项目未绑定设备");
        }
    }

    /**
     * 发送数据给某个项目中的设备
     *
     * @param url         请求路径
     * @param param       请求参数
     * @param deviceId 项目id
     */
    public static Map<String, Object> sendDataToFaceDeviceByDeviceId(String url, Map<String, Object> param, String deviceId, Integer method) {
        Map<String, Object> modelMap = new HashMap<>();
        Optional<Device> optionalDevice = httpTool.deviceService.findByDeviceId(deviceId);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();
            if (StringUtils.hasText(device.getIp()) && device.getOutPort() != null && device.getOutPort() > 0 && device.getOutPort() < 65536) {
                String response = ((method == DeviceConstant.postMethod) ? postToDevice(device, url, param) : getToDevce(device, url, param));
                modelMap.put(device.getDeviceId(), response);
                return modelMap;
            }else{
                throw new AttendanceException("设备id为:" + deviceId + "的设备的ip地址或端口不合法");
            }
        }else{
            throw new AttendanceException("设备id为:" + deviceId + "的设备未添加");
        }
    }


    /**
     * 给所有硬件设备发数据
     *
     * @param url   请求路径
     * @param param 请求参数
     */
    public static Map<String, Object> sendDataToFaceDevice(String url, Map<String, Object> param, Integer method) {
        Map<String, Object> modelMap = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        //查询出所有读头设备
        List<Device> devices = httpTool.deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
        if (devices != null && devices.size() > 0) {
            for (Device device : devices) {
                if (!StringUtils.hasText(device.getIp()) || device.getOutPort() == null || device.getOutPort() < 1) {
                    continue;
                }
                deviceIds.add(device.getDeviceId());
                String response = ((method == DeviceConstant.postMethod) ? postToDevice(device, url, param) : getToDevce(device, url, param));
                modelMap.put(device.getDeviceId(), response);
            }
            //将所查询的所有设备id放入集合
            modelMap.put("deviceIds", deviceIds);
            return modelMap;
        }else{
            throw new AttendanceException("没有上线的设备");
        }
    }

    private static String postToDevice(Device device, String url, Map<String, Object> param) {
        if (param == null) param = new HashMap<>();
        param.put("pass", device.getPass());
        return postUrlForParam("http://" + device.getIp() + ":" + device.getOutPort() + "/" + url, param);
    }

    private static String getToDevce(Device device, String url, Map<String, Object> param) {
        if (param == null) param = new HashMap<>();
        param.put("pass", device.getPass());
        return getUrlForParam("http://" + device.getIp() + ":" + device.getOutPort() + "/" + url, param);
    }

    /**
     * 发送POST请求
     *
     * @param url   URL
     * @param param 参数
     * @return POST请求的结果，Json格式
     */
    public static String postUrlForParam(String url, Map<String, Object> param) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);// 设置超时
        requestFactory.setReadTimeout(10000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                params.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params);
        try {
            //获取响应的内容
            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            logger.error("postUrlForParam error e:{}", e);
            throw new AttendanceException("POST请求" + ResultError.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 基于HttpClient 4.5的通用GET方法
     *
     * @param url       提交的URL
     * @return 提交响应
     */
    public static String getUrlForParam(String url, Map<String, Object> param) throws RuntimeException {
        try {
            url += "?";
            String data = "";
            for (String key : param.keySet()) {
                data += key + "=" + param.get(key) + "&";
            }
            //将参数拼接到URL
            url += data.substring(0, data.length() - 1);
            CloseableHttpClient client = HttpClients.createDefault();
            String responseText = "";
            CloseableHttpResponse response = null;
            try {
                HttpGet get = new HttpGet(url);
                response = client.execute(get);
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseText = EntityUtils.toString(entity, ENCODING);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (response != null)
                        response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return responseText;
        } catch (RuntimeException e) {
            logger.error("getUrlForParam error e:{}", e);
            throw new AttendanceException("GET请求" + ResultError.NETWORK_ERROR.getMessage());
        }
    }

    public static void main(String[] args) {
        Map<String , String >m = new HashMap<>();
        m.put("username", "guest");
        m.put("password", "123456");
        try {
//            ResultVo resultVo = postUrlForParam("http://139.9.47.19:9901/attendance/login", m);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        System.out.println(resultVo);
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