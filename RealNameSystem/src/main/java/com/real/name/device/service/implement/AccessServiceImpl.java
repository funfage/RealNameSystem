package com.real.name.device.service.implement;

import com.real.name.common.utils.TimeUtil;
import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.device.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private UDPClient udpClient;

    /**
     * 给用户添加权限
     * @param deviceId 设备id
     * @param idCardIndex 身份证索引号
     */
    @Override
    public void addAuthority(String deviceId, String idCardIndex) {
        AccessEvent event = new AccessEvent();
        //设置设备id
        event.setDeviceId(Integer.parseInt(deviceId));
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        byte[] idCardBytes = ConvertUtils.intToByte4(Integer.parseInt(idCardIndex));
        idCardBytes = ConvertUtils.reverse(idCardBytes);
        //设置身份证索引号
        dataBuffer.put(idCardBytes);
        //设置始末时间
        byte[] startTime = TimeUtil.getBCDTime();
        byte[] endTime = TimeUtil.getBCDTime2();
        dataBuffer.put(startTime);
        dataBuffer.put(endTime);
        //设置控制时段
        dataBuffer.put((byte)0x01);
        dataBuffer.put((byte)0x01);
        dataBuffer.put((byte)0x01);
        dataBuffer.put((byte)0x01);
        event.setData(dataBuffer.array());
        //发送数据给控制器
        udpClient.sendMessage(event);
        udpClient.destroy();
    }


}














