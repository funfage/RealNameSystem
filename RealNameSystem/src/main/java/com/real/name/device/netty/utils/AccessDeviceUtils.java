package com.real.name.device.netty.utils;

import com.real.name.device.entity.Device;
import com.real.name.device.service.AccessService;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.IssueFaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class AccessDeviceUtils {

    private static Logger logger = LoggerFactory.getLogger(AccessDeviceUtils.class);

    @Autowired
    private AccessService accessService;

    private static AccessDeviceUtils accessDeviceUtils;

    @PostConstruct
    public void init() {
        accessDeviceUtils = this;
    }

    /**
     * 下发身份证索引号到多台控制器
     * @param deviceList
     * @param idCardIndex
     */
    public static void issueIdCardIndexToDevices(List<Device> deviceList, String idCardIndex) {
        for (Device device : deviceList) {
            issueIdCardIndexToOneDevice(device, idCardIndex);

        }
    }

    /**
     * 下发单个身份证索引号到单台设备
     * @param device
     * @param idCardIndex
     */
    public static void issueIdCardIndexToOneDevice(Device device, String idCardIndex) {
        try {
            accessDeviceUtils.accessService.addAuthority(device.getDeviceId(), idCardIndex);
            //修改标识
            IssueFace issueFace = new IssueFace();
            issueFace.setDevice(device);

        } catch (Exception e) {
            logger.error("控制器下发出现异常, e:{}", e);
        }
    }


}
