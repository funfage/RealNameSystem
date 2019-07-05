package com.real.name.dutouWeb;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.UDPTool;
import com.real.name.device.entity.Device;
import com.real.name.device.service.implement.DeviceImp;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.netty.DutouCmdSend;
import com.real.name.netty.Entity.Controller;
import com.real.name.netty.ControllerContainer;
import com.real.name.netty.dao.DeviceDao;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//向所有读头注册身份证信息
            /*Iterator DutouIterator = ControllerContainer.getInstance().clientMap.entrySet().iterator();
            while (DutouIterator.hasNext()){
                try {
                    Map.Entry entry = (Map.Entry) DutouIterator.next();
                    String deviceID = (String) entry.getKey();
                    Controller controller = (Controller) entry.getValue();
                    System.out.println("hhh:" +controller);
                    ChannelHandlerContext ctx = controller.getCtx();
                    String ip = controller.getIp();
                    byte[] data = DutouCmdSend.addAuthority(deviceID,cardNo);
                    UDPTool.sendData(data,ip,ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // System.out.println("de:" +deviceID);
                //System.out.println("dd:" +controller);
            }*/
@RestController
@RequestMapping("/dutou")
public class DutouController {

    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DeviceImp deviceImp;
    @Autowired
    DeviceRepository deviceRepository;

    /**
     *
     * 把身份证卡片ID给前端
     * @param equipmentID 读头控制器ID
     * @return
     */
    @PostMapping("/create")
    public ResultVo openGate(@RequestParam("equipmentID") String equipmentID){
        //得到设备信息
        Controller dutouController = ControllerContainer.getInstance().getController(equipmentID);
        if (dutouController != null) {
           String cardNo = dutouController.getCardNo();
           if(cardNo != null){
               Map<String,String> result = new HashMap<>();
               result.put("number",cardNo);
               return ResultVo.success(result);
           }else{
               return ResultVo.failure(1,"idcard is null");
           }
        }else {
            return ResultVo.failure(1,"设备不存在");
        }
    }

    @PostMapping("/getController")
    public ResultVo getController(@RequestParam("projectCode")String  projectCode){
        List<String> dutou = deviceDao.getController(projectCode);
        return  ResultVo.success(dutou);
    }

    /**
     * 将身份证索引号注册到项目的门禁读头
     * @param projectCode 项目编码
     * @param idCardIndex 身份证索引号
     * @return
     */
    @PostMapping("/registerDutou")
    public ResultVo registerDutou(@RequestParam("projectCode")String  projectCode,@RequestParam("idCardIndex")String idCardIndex){
        //获取该项目下的所有绑定的设备
        List<Device> devices = deviceImp.findDutouOfmenjin(projectCode,1);
        if (devices != null && devices.size() != 0) {
            for (Device device : devices) {
                String equipmentID = device.getDeviceId();
                //获取设备信息
                Controller controller = ControllerContainer.getInstance().clientMap.get(equipmentID);
                ChannelHandlerContext ctx = controller.getCtx();
                //添加权限帧
                byte[] order = DutouCmdSend.addAuthority(equipmentID, idCardIndex);
                //控制器没有心跳，就不能保证有控制机IP，故用同一项目人脸设备的IP
                List<Device> deviceList = deviceRepository.findByProjectCodeAndDeviceType(projectCode, 3);
                String hostname = deviceList.get(0).getIp();
                int outPort = controller.getOutPort();
                UDPTool.sendData(order, hostname, ctx, outPort);
            }
            return ResultVo.success();
        } else {
            throw new AttendanceException(ResultError.DEVICE_NOT_EXIST);
        }

    }
}
