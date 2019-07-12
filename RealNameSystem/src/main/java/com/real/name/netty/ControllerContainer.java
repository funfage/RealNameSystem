package com.real.name.netty;

import com.real.name.common.utils.SpringUtil;
import com.real.name.netty.Entity.Controller;
import com.real.name.netty.dao.DeviceDao;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerContainer {

    private Logger logger = LoggerFactory.getLogger(ControllerContainer.class);

    private static ControllerContainer instance;

    public   Map<String,Controller> clientMap;

    private  ControllerContainer(){
        clientMap = new HashMap<>();
        init();
    }

    public static ControllerContainer getInstance() {
        if(instance == null) {
            synchronized (ControllerContainer.class) {
                if(instance == null) {
                    instance = new ControllerContainer();
                }
            }
        }
        return instance;
    }

    /**
     * 添加在线设备
     * @param deviceId 设备id
     * @param ip 设备ip地址
     * @param cardNo 身份证索引号
     * @return
     */
    Boolean addOnlineEquipment(String deviceId, String ip, ChannelHandlerContext ctx, String cardNo) {
        //获取设备信息
        Controller dutou = clientMap.get(deviceId);
        if(dutou != null) {
            dutou.setIp(ip);
            dutou.setCtx(ctx);
            dutou.setCardNo(cardNo);
            //deviceType为门禁读头
           return dutou.getDeviceType() == 1;
        }else {//如果从clientMap中没有获取到设备则从数据库中查询并放入clientMap
            ApplicationContext appCtx = SpringUtil.getApplicationContext();
            DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
            Controller newDutou = equipmentDao.getDutouByID(deviceId);
            if (newDutou != null){
                newDutou.setIp(ip);
                newDutou.setCtx(ctx);
                newDutou.setCardNo(cardNo);
                clientMap.put(newDutou.getDeviceId(), newDutou);
                return newDutou.getDeviceType() == 1;
            }
        }
        return false;
    }

    /**
     * 通过deviceId从clientMap中得到指定的设备信息
     */
    public Controller getController(String deviceId){
        logger.info("clientMap中的devices所有deviceId", clientMap);
        return  clientMap.get(deviceId);
    }

    private void init(){
        logger.info("将读头控制器信息放入clientMap中");
        ApplicationContext appCtx = SpringUtil.getApplicationContext();
        DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
        //获取所有读头设备信息
        List<Controller> devices = equipmentDao.findDutouAll();
        //将设备信息放入clientMap
        for (Controller device: devices) {
            String deviceID = device.getDeviceId();
            clientMap.put(deviceID, device);
        }
    }
}
