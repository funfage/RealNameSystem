package com.real.name.device;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.face.entity.Device;
import com.real.name.face.service.implement.DeviceImp;
import com.real.name.face.service.repository.DeviceRepository;
import com.real.name.netty.dao.DeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备添加、关联到项目管理
 * 设备管理页面
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DeviceImp deviceImp;
    @Autowired
    DeviceRepository deviceRepository;

    /**
     * 查询设备信息
     * @param startTime
     * @param endTime
     * @param deviceId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @PostMapping("/finddevice")
    public ResultVo finddevice(Long startTime,
                               Long endTime,
                               String deviceId,
                               String projectCode,
                               @RequestParam("pageNumber")Integer pageNumber,
                               @RequestParam("pageSize") Integer pageSize){
        Map<String,Object> map = new HashMap<>();
        if (startTime != null){
            Date startDate = new Date(startTime);
            map.put("startTime",startDate);
        }
        if (deviceId != null){
            map.put("deviceId",deviceId);
        }
        if (projectCode != null){
            map.put("projectCode",projectCode);
        }
        if (endTime != null){
            Date endDate = new Date(endTime);
            map.put("endTime",endDate);
        }
        map.put("start",pageNumber*pageSize);
        map.put("end",pageSize);
        List<Device> result = deviceDao.getDevice(map);
        Integer allNumber = deviceDao.countDevice(map);
        Map<String,Object> finalRes = new HashMap<>();
        if (result != null && allNumber != null){

            finalRes.put("totalCount",allNumber);
            finalRes.put("rows",result);
            return ResultVo.success(finalRes);
        }else {
            finalRes.put("totalCount",0);
            finalRes.put("rows",null);
            return ResultVo.success(finalRes);
        }
        //System.out.println(result);
       // return ResultVo.success(result);
    }

    @PostMapping("/updatedevice")
    public ResultVo updatedevice(String factory,
                                 Integer deviceType,
                                 @RequestParam("deviceId")String deviceId,
                                 String ip,
                                 Integer direction,
                                 Integer channel,
                                 Long installTime,
                                 Integer outPort,
                                 @RequestParam("projectCode")String projectCode,
                                 String phone,
                                 String pass,
                                 String remark){
        //Map<String,Object> map = new HashMap<>();
        //不能直接new实体，必须通过hibernate获得实体，否则deviceRepository.save方法会把没有更新的字段变为null
        Device device = deviceRepository.findDeviceByDeviceId(deviceId);
        //map.put("projectCode",projectCode);
        //map.put("deviceId",deviceId);
        device.setProjectCode(projectCode);
        //device.setDeviceId(deviceId);
        if ( factory != null) {
            //map.put("factory",factory);
            device.setFactory(factory);
        }
        if ( deviceType!= null) {
           // map.put("deviceType",deviceType);
            device.setDeviceType(deviceType);
        }
        if ( ip!= null) {
            //map.put("ip",ip);
            device.setIp(ip);
        }
        if ( direction!= null) {
            //map.put("direction",direction);
            device.setDirection(direction);
        }
        if ( channel!= null) {
            //map.put("channel",channel);
            device.setChannel(channel);
        }
        if ( installTime!= null) {
           // map.put("installTime",new Date(installTime));
            device.setInstallTime(new Date(installTime));
        }
        if ( outPort!= null) {
           // map.put("outPort",outPort);
            device.setOutPort(outPort);
        }
        if ( phone!= null) {
            //map.put("phone",phone);
            device.setPhone(phone);
        }

        if(pass != null){
            //map.put("pass",pass);
            device.setPass(pass);
        }
        if(remark != null){
            //map.put("remark",remark);
            device.setRemark(remark);
        }
        System.out.println("updateTT:" + device);
        Device flag = deviceRepository.save(device);
        System.out.println("falg:" +flag);
        if (flag != null){
            return ResultVo.success();
        }else return ResultVo.failure(1,"error");

    }

    @PostMapping("/adddevice")
    public ResultVo adddevice(@RequestParam("factory")String factory,
                                 @RequestParam("deviceType")Integer deviceType,
                                 @RequestParam("deviceId")String deviceId,
                                 @RequestParam("ip")String ip,
                                 @RequestParam("direction")Integer direction,
                                 @RequestParam("channel")Integer channel,
                                 @RequestParam("installTime")long installTime,
                                 @RequestParam("outPort")Integer outPort,
                                 String projectCode,
                                 String phone,
                                 String pass,
                                 String remark){
        Device device =new Device();
        device.setDeviceId(deviceId);
        device.setDeviceType(deviceType);
        device.setFactory(factory);
        device.setIp(ip);
        device.setChannel(channel);
        device.setDirection(direction);
        device.setInstallTime(new Date(installTime));
        device.setOutPort(outPort);
        if (phone != null) {
            device.setPhone(phone);
        }
        if (remark != null) {
            device.setRemark(remark);
        }
        if (projectCode != null) {
            device.setProjectCode(projectCode);
        }
        if (pass != null) {
            device.setPass(pass);
        }
        boolean flag = deviceImp.existsDeviceByDeviceId(deviceId);
        if (!flag){
            Device flag2 = deviceImp.save(device);
            System.out.println("flag:" +flag2);
            if (flag2 != null) {
                return  ResultVo.success();
            }else return  ResultVo.failure(1,"error");
        }else throw new AttendanceException(ResultError.DEVICE_EXIST);
    }
}
