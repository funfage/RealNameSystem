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

    Logger logger = LoggerFactory.getLogger(ControllerContainer.class);

    public static ControllerContainer instance;
    public   Map<String,Controller> clientMap;
    //private Map<String,String> authSuccessMap;

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
     * @param equipmentId
     * @param ip
     * @param ctx
     * @param cardNo
     * @return
     */
    public Boolean addOnlineEquipment(String equipmentId, String ip, ChannelHandlerContext ctx, String cardNo) {
        //获取设备信息
        Controller dutou = clientMap.get(equipmentId);
        if( dutou != null) {
            System.out.println("dutou not null");
            dutou.setIp(ip);
            dutou.setCtx(ctx);
            dutou.setCardNo(cardNo);
            //deviceType为禁止读头
           return dutou.getDeviceType() == 1;
        }else {
            ApplicationContext appCtx = SpringUtil.getApplicationContext();
            DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
            Controller newDutou = equipmentDao.getDutouByID(equipmentId);
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
     * 从clientMap中得到设备信息
     */
    public Controller getController(String equipmentID){
        logger.info("clientMap中的devices所有deviceId", clientMap);
        return  clientMap.get(equipmentID);
    }

    private void init(){
        System.out.println("ttt:"+DeviceDao.class);
        ApplicationContext appCtx = SpringUtil.getApplicationContext();
        DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
        //获取所有读头设备信息
        List<Controller> devices = equipmentDao.findAll();
        System.out.println("devices size:" + devices.size());
        //将设备信息放入clientMap
        for (Controller device: devices) {
            System.out.println("getDutou:" +device.getDeviceId());
            String deviceID = device.getDeviceId();
            clientMap.put(deviceID, device);
        }
    }
}
