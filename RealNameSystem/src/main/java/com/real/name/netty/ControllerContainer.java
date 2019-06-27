package com.real.name.netty;

import com.real.name.common.utils.SpringUtil;
import com.real.name.netty.Entity.Controller;
import com.real.name.netty.dao.DeviceDao;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerContainer {
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
    public Boolean addOnlineEquipment(String equipmentId, String ip, ChannelHandlerContext ctx, String cardNo) {
        Controller dutou = clientMap.get(equipmentId);
        if( dutou != null) {
            System.out.println("dutou not null");
            //client.setCtx(ctx);
            dutou.setIp(ip);
            dutou.setCtx(ctx);
            dutou.setCardNo(cardNo);
           return dutou.getDeviceType()==1?true:false;
        }else {
            ApplicationContext appCtx = SpringUtil.getApplicationContext();
            DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
            Controller newDutou = equipmentDao.getDutouByID(equipmentId);
            if (newDutou != null){
                newDutou.setIp(ip);
                newDutou.setCtx(ctx);
                newDutou.setCardNo(cardNo);
                return newDutou.getDeviceType()==1?true:false;
            }
        }

        return false;
    }
    public Controller getController(String equipmentID){
        return  clientMap.get(equipmentID);
    }
    public void init(){
        System.out.println("ttt:"+DeviceDao.class);
        //System.out.println("container init============================");
        ApplicationContext appCtx = SpringUtil.getApplicationContext();
        DeviceDao equipmentDao = appCtx.getBean(DeviceDao.class);
        //String name = "DeviceDao";
       // DeviceDao equipmentDao = (DeviceDao) appCtx.getBean(name);
        List<Controller> devices = equipmentDao.findAll();
        //List<Device> devices = equipmentDao.findAllDutou();
         // List<Device> devices = deviceMapper.findAllDutou();
        System.out.println("devices size:" + devices.size());
        for (Controller device: devices) {
            System.out.println("tt:" +device.getDeviceId());
            String deviceID = device.getDeviceId();
            clientMap.put(deviceID,device);
        }
    }
}
