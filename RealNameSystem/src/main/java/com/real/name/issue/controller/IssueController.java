package com.real.name.issue.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.service.DeviceService;
import com.real.name.issue.entity.FaceIssueUpdate;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.entity.IssueInfo;
import com.real.name.issue.entity.IssuePersonStatus;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/issueFace")
public class IssueController {

    private Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueFaceService issueFaceService;

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    /**
     * 重发设备信息
     */
    @PostMapping("resend")
    public ResultVo resend(@RequestBody IssueInfo issueInfo) {
        String deviceId = issueInfo.getDeviceId();
        if (!StringUtils.hasText(deviceId)) {
            throw AttendanceException.emptyMessage("设备id");
        }
        //查询该设备是否存在
        Optional<Device> optionalDevice = deviceService.findByDeviceId(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_NOT_EXIST);
        }
        Device device = optionalDevice.get();
        //判断是那种下发失败情况
        List<IssuePersonStatus> issuePersonStatusList = issueInfo.getIssuePersonStatusList();
        for (IssuePersonStatus issue : issuePersonStatusList) {
            Person person = personService.findIssuePersonImageInfo(issue.getPersonId());
            if (person != null) {
                if (device.getDeviceType() == DeviceConstant.faceDeviceType) {//重发人脸设备
                    issueFaceService.resend(issue.getIssuePersonStatus(), issue.getIssueImageStatus(), person, device);
                } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {//重发控制器
                    issueAccessService.resend(person, device);
                }
            }
        }
        return ResultVo.success();
    }

    /**
     * 重发需要更新的人员信息
     */
    @PostMapping("resendUpdate")
    public ResultVo resendUpdate(@RequestParam("faceIssueUpdateList") String faceIssueUpdateListStr) {
        List<FaceIssueUpdate> faceIssueUpdateList = null;
        try {
            faceIssueUpdateList = JSONArray.parseArray(faceIssueUpdateListStr, FaceIssueUpdate.class);
        } catch (Exception e) {
            return ResultVo.failure();
        }
        for (FaceIssueUpdate faceIssueUpdate : faceIssueUpdateList) {
            //获取人员信息
            Person personInfo = personService.findIssuePersonImageInfo(faceIssueUpdate.getPersonId());
            //获取设备信息
            Optional<Device> deviceOptional = deviceService.findByDeviceId(faceIssueUpdate.getDeviceId());
            if (deviceOptional.isPresent() && personInfo != null) {
                Device device = deviceOptional.get();
                if (faceIssueUpdate.getIssuePersonStatus() == -1) {//更新设备人员信息失败
                    FaceDeviceUtils.updatePersonToOneDevice(device, personInfo, 3);
                }
                if (faceIssueUpdate.getIssueImageStatus() == -1) {//更新设备照片信息失败
                    FaceDeviceUtils.updateImageToOneDevice(device, personInfo, 3);
                }
            }
        }
        return ResultVo.success();
    }

    /**
     * ========================================以下只与查询有关===============================================
     */

    /**
     * 获取所有失败设备的id
     */
    @GetMapping("getIssueFailDeviceIds")
    public ResultVo getIssueFailDeviceIds() {
        List<String> failDeviceIds = issueFaceService.findAllFailDeviceIds();
        return ResultVo.success(failDeviceIds);
    }

    /**
     * 获取某个项目下所有下发失败设备的id
     */
    @GetMapping("getIssueFailDeviceIdsInProject")
    public ResultVo getIssueFailDeviceIdsInProject(@RequestParam("projectCode")String projectCode) {
        List<String> failDeviceIdList = issueFaceService.findAllFailDeviceIdsByProjectCode(projectCode);
        return ResultVo.success(failDeviceIdList);
    }

    /**
     * 获取某个项目下所有下发失败的人员信息
     *
     * @param projectCode 项目编码
     * @param pageNum     每页数量
     * @param pageSize    页码
     */
    @GetMapping("getProjectIssueFail")
    public ResultVo getProjectIssueFail(@RequestParam("projectCode") String projectCode,
                                        @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (pageNum < 0) {
            throw AttendanceException.errorMessage("页码");
        }
        if (pageSize < 0) {
            throw AttendanceException.errorMessage("页数");
        }
        try {
            PageHelper.startPage(pageNum + 1, pageSize);
            List<IssueFace> failList = issueFaceService.findIssueFailPersonInfoByProjectCode(projectCode);
            PageInfo<IssueFace> issueInfo = new PageInfo<>(failList);
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("failList", failList);
            modelMap.put("pageNum", issueInfo.getPageNum());
            modelMap.put("pageSize", issueInfo.getPageSize());
            modelMap.put("total", issueInfo.getTotal());
            return ResultVo.success(modelMap);
        } catch (Exception e) {
            logger.error("获取项目下设备下发失败人员信息为空, e:{}", e.getMessage());
            return ResultVo.failure();
        }
    }

    /**
     * 获取某个设下发失败的信息
     */
    @GetMapping("getDeviceIssueFail")
    public ResultVo getDeviceIssueFail(@RequestParam("deviceId") String deviceId,
                                       @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (pageNum < 0) {
            throw AttendanceException.errorMessage("页码");
        }
        if (pageSize < 0) {
            throw AttendanceException.errorMessage("页数");
        }
        PageHelper.startPage(pageNum + 1, pageSize);
        try {
            List<IssueFace> failList = issueFaceService.findIssueFailPersonInfoByDeviceId(deviceId);
            PageInfo<IssueFace> issueInfo = new PageInfo<>(failList);
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("failList", failList);
            modelMap.put("pageNum", issueInfo.getPageNum());
            modelMap.put("total", issueInfo.getTotal());
            return ResultVo.success(modelMap);
        } catch (Exception e) {
            logger.error("获取某个设备下发失败人员信息为空, e:{}", e.getMessage());
            return ResultVo.failure();
        }
    }

    /**
     * 获取更新到设备失败的人员信息
     */
    @GetMapping("getUpdateFaceFail")
    public ResultVo getUpdateFaceFail(@RequestParam("workRole") Integer workRole) {
        List<IssueFace> issueFaceList = issueFaceService.findUpdateFailPersonByWorkRole(workRole);
        return ResultVo.success(issueFaceList);
    }


}