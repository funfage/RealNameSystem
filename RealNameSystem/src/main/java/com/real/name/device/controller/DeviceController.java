package com.real.name.device.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.PageUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.query.DeviceQuery;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceDao;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.project.service.repository.ProjectQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 设备添加、关联到项目管理
 * 设备管理页面
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProjectQueryMapper projectQueryMapper;

    /**
     * 添加设备
     */
    @PostMapping("/addDevice")
    @Transactional
    public ResultVo addDevice(@RequestParam("factory") String factory,
                              @RequestParam("deviceType") Integer deviceType,
                              @RequestParam("deviceId") String deviceId,
                              @RequestParam("ip") String ip,
                              @RequestParam("direction") Integer direction,
                              @RequestParam("channel") Integer channel,
                              @RequestParam("installTime") Long installTime,
                              @RequestParam("outPort") Integer outPort,
                              String projectCode,
                              String phone,
                              String pass,
                              String remark) {
        if (!StringUtils.hasText(deviceId)) {
            throw AttendanceException.emptyMessage("设备id");
        } else if (deviceType == null) {
            throw AttendanceException.emptyMessage("设备类型");
        } else if (!StringUtils.hasText(factory)) {
            throw AttendanceException.emptyMessage("厂商");
        } else if (!StringUtils.hasText(ip)) {
            throw AttendanceException.emptyMessage("ip地址");
        } else if (direction == null) {
            throw AttendanceException.emptyMessage("方向");
        } else if (installTime == null) {
            throw AttendanceException.emptyMessage("安装时间");
        } else if (channel == null) {
            throw AttendanceException.emptyMessage("通道号");
        } else if (outPort == null) {
            throw AttendanceException.emptyMessage("外部端口");
        }
        //查询设备是否存在
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId);
        if (deviceOptional.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_EXIST);
        }
        //查询设备的IP和端口是否重复
        if (deviceService.findByIpAndOutPort(ip, outPort).isPresent()) {
            throw new AttendanceException(ResultError.IP_PORT_REPEAT);
        }
        Device device = new Device();
        device.setDeviceId(deviceId);
        //校验输入的参数是否正确
        verifyParam(factory, deviceType, ip, direction, channel, installTime, outPort, phone, remark, projectCode, pass, device);
        if (device.getDeviceType() == 3) {
            //添加人脸设备
            deviceService.addFaceDevice(device);
        } else if (device.getDeviceType() == 1) {
            //添加控制器
            deviceService.addAccessDevice(device);
        } else if (device.getDeviceType() == 2) {
            //添加录用读头
            deviceService.save(device);
        }
        return ResultVo.success();
    }

    /**
     * 更新设备
     */
    @PostMapping("/updateDevice")
    public ResultVo updateDevice(String factory,
                                 Integer deviceType,
                                 @RequestParam("deviceId") String deviceId,
                                 String ip,
                                 Integer direction,
                                 Integer channel,
                                 Long installTime,
                                 Integer outPort,
                                 @RequestParam("projectCode") String projectCode,
                                 String phone,
                                 String pass,
                                 String remark) {
        if (!StringUtils.hasText(deviceId)) {
            throw AttendanceException.emptyMessage("设备id");
        }
        if (projectQueryMapper.findByProjectCode(projectCode) == null) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        //查询设备是否存在
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId);
        if (!deviceOptional.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_NOT_EXIST);
        }
        //查询设备的IP和端口是否重复
        if (deviceService.findByIpAndOutPort(ip, outPort).isPresent()) {
            throw new AttendanceException(ResultError.IP_PORT_REPEAT);
        }
        Device selectDevice = deviceOptional.get();
        //校验参数并设置
        verifyParam(factory, deviceType, ip, direction, channel, installTime, outPort, phone, remark, projectCode, pass, selectDevice);
        //判断设备是否绑定项目,如果绑定项目则修改设备ip
        if (projectCode != null) {
            Set<String> ipSet = deviceService.findIPByProjectCode(projectCode);
            selectDevice.setIp(ipSet.iterator().next());
        }
        //更新人脸设备
        if (selectDevice.getDeviceType() == 3) {
            //人脸设备
            deviceService.updateFaceDevice(selectDevice);
        } else if (selectDevice.getDeviceType() == 1) {
            //控制器
            deviceService.updateAccessDevice(selectDevice);
        } else {
            /**
             * TODO 更新其他设备信息
             */
            Device device = deviceService.save(selectDevice);
            if (device == null) {
                throw new AttendanceException(ResultError.UPDATE_ERROR);
            }
        }
        return ResultVo.success();
    }

    /**
     * 删除设备
     */
    @GetMapping("/deleteDevice")
    public ResultVo deleteDevice(@RequestParam("deviceId") String deviceId) {
        //查询该设备信息
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId);
        if (!deviceOptional.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_NOT_EXIST);
        }
        deviceService.deleteDevice(deviceOptional.get());
        return ResultVo.success();
    }

    /**
     * ========================================以下只与查询有关===============================================
     */

    /**
     * 查询某个项目下设备信息
     */
    @PostMapping("/finddevice")
    public ResultVo finddevice(Long startTime,
                               Long endTime,
                               String deviceId,
                               String projectCode,
                               @RequestParam("pageNumber") Integer pageNumber,
                               @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (startTime != null) {
            Date startDate = new Date(startTime);
            map.put("startTime", startDate);
        }
        if (deviceId != null) {
            map.put("deviceId", deviceId);
        }
        if (projectCode != null) {
            map.put("projectCode", projectCode);
        }
        if (endTime != null) {
            Date endDate = new Date(endTime);
            map.put("endTime", endDate);
        }
        map.put("start", pageNumber * pageSize);
        map.put("end", pageSize);
        List<Device> result = deviceDao.getDevice(map);
        Integer allNumber = deviceDao.countDevice(map);
        Map<String, Object> finalRes = new HashMap<>();
        if (result != null && allNumber != null) {
            finalRes.put("totalCount", allNumber);
            finalRes.put("rows", result);
            return ResultVo.success(finalRes);
        } else {
            finalRes.put("totalCount", 0);
            finalRes.put("rows", null);
            return ResultVo.success(finalRes);
        }
    }

    /**
     * 获取所有设备id
     */
    @GetMapping("getDeviceIdList")
    public ResultVo getDeviceIdList() {
        List<String> deviceIdList = deviceService.getDeviceIdList();
        return ResultVo.success(deviceIdList);
    }

    /**
     * 搜索设备
     */
    @PostMapping("/searchDevice")
    public ResultVo searchDevice(DeviceQuery deviceQuery) {

        PageHelper.startPage(deviceQuery.getPageNum() + 1, deviceQuery.getPageSize());
        List<Device> devices = deviceService.searchDevice(deviceQuery);
        PageInfo<Device> pageInfo = new PageInfo<>(devices);
        Map<String, Object> map = new HashMap<>();
        map.put("devices", devices);
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        return ResultVo.success(map);
    }

    /**
     * 搜索项目下设备信息
     */
    @GetMapping("searchDeviceInPro")
    public ResultVo searchDeviceInPro(DeviceQuery deviceQuery) {
        if (StringUtils.isEmpty(deviceQuery.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        }
        PageHelper.startPage(deviceQuery.getPageNum() + 1, deviceQuery.getPageSize());
        List<Device> deviceList = deviceService.searchDeviceInPro(deviceQuery);
        PageInfo<Device> pageInfo = new PageInfo<>(deviceList);
        return PageUtils.pageResult(pageInfo, deviceList);
    }

    /**
     * 获取首页信息
     */
    @GetMapping("/getMainPageDeviceInfo")
    public ResultVo getMainPageDeviceInfo() {
        return ResultVo.success(deviceService.getMainPageDeviceInfo());
    }

    private void verifyParam(String factory, Integer deviceType, String ip, Integer direction, Integer channel,
                             Long installTime, Integer outPort, String phone, String remark, String projectCode,
                             String pass, Device device) {
        if (StringUtils.hasText(factory)) {
            device.setFactory(factory);
        }
        if (deviceType != null) {
            if (deviceType != 1 && deviceType != 2 && deviceType != 3) {
                throw AttendanceException.errorMessage("设备类型");
            }
            device.setDeviceType(deviceType);
        }
        if (StringUtils.hasText(ip)) {
            if (!CommonUtils.isRightIp(ip)) {
                throw AttendanceException.errorMessage("ip地址");
            }
            device.setIp(ip);
        }
        if (direction != null) {
            if (direction != 1 && direction != 2) {
                throw AttendanceException.errorMessage("进出方向");
            }
            device.setDirection(direction);
        }
        if (channel != null) {
            device.setChannel(channel);
        }
        if (installTime != null) {
            System.out.println(System.currentTimeMillis());
            System.out.println(installTime > System.currentTimeMillis());
            if (installTime < 0 || installTime > System.currentTimeMillis()) {
                throw AttendanceException.errorMessage("设备安装时间");
            }
            device.setInstallTime(new Date(installTime));
        }
        if (outPort != null) {
            if (outPort < 0 && outPort > 65536) {
                throw AttendanceException.errorMessage("端口号");
            }
            device.setOutPort(outPort);
        }
        if (StringUtils.hasText(phone)) {
            if (!CommonUtils.isRightPhone(phone)) {
                throw AttendanceException.errorMessage("电话号码");
            }
            device.setPhone(phone);
        }
        if (StringUtils.hasText(remark)) {
            device.setRemark(remark);
        }
        if (StringUtils.hasText(projectCode)) {
            //从数据库中查询该项目编码是否存在
            Project project = projectService.findByProjectCode(projectCode);
            if (project == null) {
                throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
            }
            device.setProjectCode(projectCode);
        }
        if (StringUtils.hasText(pass)) {
            device.setPass(pass);
        }
    }


}
