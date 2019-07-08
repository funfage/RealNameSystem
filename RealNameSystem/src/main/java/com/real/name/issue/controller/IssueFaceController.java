package com.real.name.issue.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.device.DeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.entity.IssueInfo;
import com.real.name.issue.entity.IssuePersonStatus;
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
public class IssueFaceController {

    private Logger logger = LoggerFactory.getLogger(IssueFaceController.class);

    @Autowired
    private IssueFaceService issueFaceService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    /**
     * 获取所有失败设备的id
     *
     * @return
     */
    @GetMapping("getIssueFailDeviceIds")
    public ResultVo getIssueFailDeviceIds() {
        List<String> failDeviceIds = issueFaceService.findAllFailDeviceIds();
        return ResultVo.success(failDeviceIds);
    }

    /**
     * 获取某个项目下所有下发失败设备的id
     * @param projectCode
     * @return
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
     * @return
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
            modelMap.put("total", issueInfo.getTotal());
            return ResultVo.success(modelMap);
        } catch (Exception e) {
            logger.error("获取项目下设备下发失败人员信息为空, e:{}", e.getMessage());
            return ResultVo.failure();
        }
    }

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

    @PostMapping("resend")
    public ResultVo resend(@RequestBody IssueInfo issueInfo) {
        String deviceId = issueInfo.getDeviceId();
        if (!StringUtils.hasText(deviceId)) {
            throw AttendanceException.emptyMessage("设备id");
        }
        //查询该设备是否存在
        Optional<Device> optionalDevice = deviceService.findByDeviceId(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_EXIST);
        }
        Device device = optionalDevice.get();
        //判断是那种下发失败情况
        List<IssuePersonStatus> issuePersonStatusList = issueInfo.getIssuePersonStatusList();
        for (IssuePersonStatus issuePersonStatus : issuePersonStatusList) {
            Person person = personService.findIssuePersonImageInfo(issuePersonStatus.getPersonId());
            if (person != null) {
                //表示下发人员失败
                if (issuePersonStatus.getIssuePersonStatus() == 0) {
                    DeviceUtils.issuePersonToOneDevice(device, person, 1);
                } else if (issuePersonStatus.getIssuePersonStatus() == 1 && issuePersonStatus.getIssueImageStatus() == 0) { //表示下发照片失败
                    DeviceUtils.issueImageToOneDevice(device, person, 1);
                }
            }
        }
        return ResultVo.success();
    }
}