package com.real.name.group.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.NationalUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectPersonDetailService;
import com.real.name.project.service.ProjectService;
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
@RequestMapping("/group")
public class GroupController {
    private Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectPersonDetailService projectPersonDetailService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;


    /**
     * 创建班组
     */
    @PostMapping("create")
    public Object create(WorkerGroup group) {
        if (!StringUtils.hasText(group.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        } else if (!StringUtils.hasText(group.getTeamName())) {
            throw AttendanceException.emptyMessage("班组名称");
        } else if (groupService.findByTeamName(group.getTeamName()).isPresent()) {
            throw new AttendanceException(ResultError.GROUP_EXIST);
        } else if (StringUtils.hasText(group.getResponsiblePersonPhone())) {
            // 如果有手机号，判断手机号是否正确
            if (!CommonUtils.isRightPhone(group.getResponsiblePersonPhone())) {
                throw new AttendanceException(ResultError.PHONE_ERROR);
            }
        }
        // 判断项目是否存在
        Optional<Project> projectOptional = projectService.findByProjectCode(group.getProjectCode());
        if (!projectOptional.isPresent()) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        //判断是否选择的是管理员班组
        if (group.getIsAdminGroup() != null && group.getIsAdminGroup() == 1) {
            //从数据库中查询是否存在管理员班组
            if (groupService.findByIsAdminGroupAndProjectCode(group.getIsAdminGroup(), group.getProjectCode()).isPresent()) {
                throw new AttendanceException(ResultError.ADMIN_GROUP_ERROR);
            }
        }
        Project project = projectOptional.get();
        group.setCorpCode(project.getContractorCorpCode());
        group.setCorpName(project.getContractorCorpName());
        // 判断是否需要上传到全国平台
        if (project.getIsUpload() != null && project.getIsUpload() == 1) {
            JSONObject gr = NationalUtils.uploadGroup(group);
            //判断是否上传成功
            if(gr.getBoolean("error")){
                return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + ":" + gr.getString("message"));
            }
            JSONObject data = gr.getJSONObject("data");
            if (data.toString().contains("teamSysNo")) {
                JSONObject result = data.getJSONObject("result");
                Integer teamSysNo = result.getInteger("teamSysNo");
                if (teamSysNo != null && teamSysNo > 0) {
                    group.setTeamSysNo(teamSysNo);
                    groupService.create(group);
                    return ResultVo.success(group);
                } else {
                    logger.error("uploadGroup error 没有班组编号信息");
                    return ResultVo.failure(ResultError.NATIONAL_ERROR);
                }
            } else {
                return ResultVo.failure(ResultError.NATIONAL_ERROR);
            }
        } else {//否则只存到本地数据库
            group.setTeamSysNo((int)(System.currentTimeMillis() / 1000));
            groupService.create(group);
            return ResultVo.success(group);
        }
    }

    /**
     * 查询班组
     */
    @GetMapping("find")
    public Object find(@RequestParam(name = "teamSysNo", required = false) Integer teamSysNo,
                       @RequestParam(name = "projectCode", required = false) String  projectCode) {

        if (teamSysNo == null && projectCode == null) {
            return ResultVo.success(groupService.findAll());
        } else if (teamSysNo != null) {
            return ResultVo.success(groupService.findById(teamSysNo));
        } else if (StringUtils.hasText(projectCode)) {
            return ResultVo.success(groupService.findByProjectCode(projectCode));
        }
        return ResultVo.failure(ResultError.PARAM_ERROR);
    }

    /**
     * 修改班组信息
     */
    @PostMapping("updateWorkerGroup")
    public ResultVo updateWorkerGroup(@RequestBody WorkerGroup workerGroup) {
        if (workerGroup.getTeamSysNo() == null) {
            throw AttendanceException.emptyMessage("班组编号");
        }
        //判断是否选择的是管理员班组
        if (workerGroup.getIsAdminGroup() != null && workerGroup.getIsAdminGroup() == 1) {
            //从数据库中查询是否存在管理员班组
            if (groupService.findByIsAdminGroupAndProjectCode(workerGroup.getIsAdminGroup(), workerGroup.getProjectCode()).isPresent()) {
                throw new AttendanceException(ResultError.ADMIN_GROUP_ERROR);
            }
        }
        //查询该班组是否存在
        Optional<WorkerGroup> groupOptional = groupService.findById(workerGroup.getTeamSysNo());
        if (!groupOptional.isPresent()) {
            throw new AttendanceException(ResultError.GROUP_NOT_EXIST);
        }
        WorkerGroup selectWorkerGroup = groupOptional.get();
        //合并信息
        mergeWorkerGroup(selectWorkerGroup, workerGroup);
        try {
            //修改全国平台班组信息
            JSONObject jsonObject = NationalUtils.updateGroup(selectWorkerGroup);
            if(jsonObject.getBoolean("error")){
                return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + jsonObject.getString("message"));
            }
            //修改本地班组信息
            WorkerGroup updateGroup = groupService.updateByTeamSysNo(selectWorkerGroup);
            if (updateGroup == null) {
                throw new AttendanceException(ResultError.UPDATE_ERROR);
            }
            return ResultVo.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }
    }

    /**
     * 删除班组信息
     */
    @GetMapping("deleteWorkerGroup")
    public ResultVo deleteWorkerGroup(@RequestParam("teamSysNo") Integer teamSysNo) {
        if (teamSysNo <= 0) {
            throw AttendanceException.errorMessage("班组编号");
        }
        //获取该班组的项目id
        String projectCode = projectDetailQueryService.findProjectCodeByTeamSysNo(teamSysNo);
        //获取该项目绑定的设备信息
        List<Device> deviceList = deviceService.findByProjectCode(projectCode);
        //获取该班组下的人员信息
        List<Person> personList = projectDetailQueryService.findPersonByTeamSyNo(teamSysNo);
        //删除每个人员在该班组绑定设备的信息
        for (Person person : personList) {
            if (person.getWorkRole() == 20) {//不是管理员的人才需要把设备上的信息删除
                deviceService.deletePersonInDeviceList(deviceList, person);
            }
        }
        //删除project_detail相关的信息
        projectPersonDetailService.deleteByWorkerGroup(new WorkerGroup(teamSysNo));
        //删除班组信息
        int effectNum = groupService.deleteByTeamSysNo(teamSysNo);
        if (effectNum <= 0) {
            throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "班组");
        } else {
            return ResultVo.success("删除班组成功");
        }
    }

    /**
     * 搜索班组
     */
    @PostMapping("/searchGroup")
    public ResultVo searchGroup(GroupQuery groupQuery) {
        PageHelper.startPage(groupQuery.getPageNum() + 1, groupQuery.getPageSize());
        List<WorkerGroup> workerGroups = groupService.searchGroup(groupQuery);
        PageInfo<WorkerGroup> pageInfo = new PageInfo<>(workerGroups);
        Map<String, Object> map = new HashMap<>();
        map.put("workerGroups", workerGroups);
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        return ResultVo.success(map);
    }

    private void mergeWorkerGroup(WorkerGroup selectWorkerGroup, WorkerGroup workerGroup) {
        if (StringUtils.hasText(workerGroup.getResponsiblePersonName())) {
            selectWorkerGroup.setResponsiblePersonName(workerGroup.getResponsiblePersonName());
        }
        if (StringUtils.hasText(workerGroup.getResponsiblePersonPhone())) {
            if (!CommonUtils.isRightPhone(workerGroup.getResponsiblePersonPhone())) {
                throw AttendanceException.errorMessage("电话号码");
            }
            selectWorkerGroup.setResponsiblePersonPhone(workerGroup.getResponsiblePersonPhone());
        }
        if (workerGroup.getResponsiblePersonIdCardType() != null) {
            if (workerGroup.getResponsiblePersonIdCardType() != 99 && (workerGroup.getResponsiblePersonIdCardType() <= 0 || workerGroup.getResponsiblePersonIdCardType() >= 26)) {
                throw AttendanceException.errorMessage("责任人证件类型");
            }
            selectWorkerGroup.setResponsiblePersonIdCardType(workerGroup.getResponsiblePersonIdCardType());
        }
        if (StringUtils.hasText(workerGroup.getResponsiblePersonIdNumber())) {
            selectWorkerGroup.setResponsiblePersonIdNumber(workerGroup.getResponsiblePersonIdNumber());
        }
        if (StringUtils.hasText(workerGroup.getRemark())) {
            selectWorkerGroup.setRemark(workerGroup.getRemark());
        }
        if (StringUtils.hasText(workerGroup.getCorpName())) {
            selectWorkerGroup.setCorpName(workerGroup.getCorpName());
        }
        if (StringUtils.hasText(workerGroup.getCorpCode())) {
            selectWorkerGroup.setCorpCode(workerGroup.getCorpCode());
        }
        if (workerGroup.getIsAdminGroup() != null) {
            selectWorkerGroup.setIsAdminGroup(workerGroup.getIsAdminGroup());
        }
    }
}