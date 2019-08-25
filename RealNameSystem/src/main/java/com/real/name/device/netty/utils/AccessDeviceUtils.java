package com.real.name.device.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.constant.CommConstant;
import com.real.name.common.utils.JedisService;
import com.real.name.common.websocket.WebSocket;
import com.real.name.device.entity.Device;
import com.real.name.device.service.AccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AccessDeviceUtils {

    private static Logger logger = LoggerFactory.getLogger(AccessDeviceUtils.class);

    @Autowired
    private AccessService accessService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    private static AccessDeviceUtils accessDeviceUtils;

    @Autowired
    private WebSocket webSocket;

    @PostConstruct
    public void init() {
        accessDeviceUtils = this;
    }

    /**
     * 下发身份证索引号到多台控制器
     */
    public static void issueIdCardIndexToDevices(List<Device> deviceList, String idCardIndex) {
        for (Device device : deviceList) {
            issueIdCardIndexToOneDevice(device, idCardIndex);
        }
    }

    /**
     * 下发单个身份证索引号到单台设备
     */
    public static void issueIdCardIndexToOneDevice(Device device, String idCardIndex) {
        try {
            accessDeviceUtils.accessService.addAuthority(device.getDeviceId(), idCardIndex, device.getIp(), device.getOutPort());
        } catch (Exception e) {
            logger.error("控制器下发出现异常, e:{}", e);
        }
    }

}
