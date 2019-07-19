package com.real.name.device.service.implement;

import com.real.name.common.utils.TimeUtil;
import com.real.name.device.netty.UDPClient;
import com.real.name.device.netty.model.AccessConstant;
import com.real.name.device.netty.model.AccessEvent;
import com.real.name.device.netty.model.AccessFunction;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.device.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class AccessServiceImpl implements AccessService {

    private final int HexRadix = 16;

    @Autowired
    private UDPClient udpClient;

    /**
     * 给用户添加权限
     * @param deviceId 设备id
     * @param idCardIndex 身份证索引号
     */
    @Override
    public void addAuthority(String deviceId, String idCardIndex, String ip, int port) {
        AccessEvent event = new AccessEvent();
        //设置功能号
        event.setFunctionId(AccessFunction.ADD_AUTHORITY);
        //设置设备id
        event.setDeviceId(Integer.parseInt(deviceId));
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        byte[] idCardBytes = ConvertUtils.reverse(ConvertUtils.intToByte4(Integer.parseInt(idCardIndex)));
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
        //得到编码后的数据并发送
        udpClient.sendMessage(event, ip, port);
    }

    /**
     * 查询某个用户的权限
     * @param deviceId 设备id
     * @param idCardIndex 身份证索引号
     */
    @Override
    public void queryAuthority(String deviceId, String idCardIndex, String ip, int port) {
        AccessEvent event = new AccessEvent();
        //设置功能号
        event.setFunctionId(AccessFunction.QUERY_AUTHORITY);
        //设置设备id
        event.setDeviceId(Integer.parseInt(deviceId));
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        //设置需要查询的卡号
        byte[] cardIndexBytes = ConvertUtils.reverse(ConvertUtils.intToByte4(Integer.parseInt(idCardIndex)));
        dataBuffer.put(cardIndexBytes);
        event.setData(dataBuffer.array());
        //得到编码后的数据并发送
        udpClient.sendMessage(event, ip, port);
    }

    /**
     * 搜索控制器
     */
    @Override
    public void searchAccess(String deviceId, String ip, int port) {
        AccessEvent event = new AccessEvent();
        event.setDeviceId(0);
        event.setFunctionId(AccessFunction.SEARCH_ACCESS);
        udpClient.sendMessage(event, ip, port);
    }

    /**
     * 权限清空
     */
    @Override
    public void clearAccess(String deviceId, String ip, int port) {
        AccessEvent event = new AccessEvent();
        event.setFunctionId(AccessFunction.CLEAR_AUTHORITY);
        event.setDeviceId(Integer.parseInt(deviceId));
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        dataBuffer.put((byte) 0x55);
        dataBuffer.put((byte) 0xAA);
        dataBuffer.put((byte) 0xAA);
        dataBuffer.put((byte) 0x55);
        event.setData(dataBuffer.array());
        udpClient.sendMessage(event, ip, port);
    }

    /**
     * 清空权限
     */
    @Override
    public void deleteAuthority(String deviceId, String idCardIndex, String ip, int port) {
        AccessEvent event = new AccessEvent();
        event.setFunctionId(AccessFunction.DELETE_AUTHORITY);
        event.setDeviceId(Integer.parseInt(deviceId));
        ByteBuffer dataBuffer = ByteBuffer.allocate(AccessConstant.DATA_LENGTH);
        //设置需要查询的卡号
        byte[] cardIndexBytes = ConvertUtils.reverse(ConvertUtils.intToByte4(Integer.parseInt(idCardIndex)));
        dataBuffer.put(cardIndexBytes);
        event.setData(dataBuffer.array());
        udpClient.sendMessage(event, ip, port);
    }


}














