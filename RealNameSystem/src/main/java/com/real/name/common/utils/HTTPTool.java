package com.real.name.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.result.ResultVo;
import com.real.name.face.entity.Device;
import com.real.name.face.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class HTTPTool {

    @Autowired
    private DeviceService deviceService;

    private static HTTPTool httpTool;

    //private static final String ports[] = {":8090/", ":8091/", ":8092/", ":8093/"};

    @PostConstruct
    public void init() {
        httpTool = this;
    }

    /**
     * 给硬件设备发数据
     *
     * @param deviceId 设备id
     * @param url      请求路径
     * @param param    请求参数
     */
    public static ResultVo sendDataTo(String deviceId, String url, Map<String, String> param) {

        if (StringUtils.hasText(deviceId)) {
            Optional<Device> device = httpTool.deviceService.findByDeviceId("E0F2D4B761E53DF9F0");
            if (device.isPresent() && StringUtils.hasText(device.get().getIp())) {
                return postUrlForParam("http://" + device.get().getIp() + ":" + device.get().getOutPort() + "/" + url, param);
            }
        }
        return ResultVo.success();
    }

    /**
     * 给所有硬件设备发数据
     *
     * @param url   请求路径
     * @param param 请求参数
     */
    public static ResultVo sendDataTo(String url, Map<String, String> param) {

        if (!StringUtils.hasText(url)) return null;

        List<Device> devices = httpTool.deviceService.findAll();
        for (Device device : devices) {
            if (!StringUtils.hasText(device.getIp()) || device.getOutPort() == null || device.getOutPort() < 1) continue;
            ResultVo resultVo = postUrlForParam("http://" + device.getIp() + ":" + device.getOutPort() + "/" + url, param);
            System.out.println(resultVo);
        }
        return ResultVo.success();
    }

    /**
     * 发送POST请求
     *
     * @param url   URL
     * @param param 参数
     * @return POST请求的结果，Json格式
     */
    public static ResultVo postUrlForParam(String url, Map<String, String> param) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        for (Map.Entry<String, String> entry : param.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params);
        String response = restTemplate.postForObject(url, entity, String.class);
        return JSONObject.parseObject(response, ResultVo.class);
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