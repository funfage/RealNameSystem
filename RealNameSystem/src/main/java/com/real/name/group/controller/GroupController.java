package com.real.name.group.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.NationalUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectPersonDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    private Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private ProjectPersonDetailService projectPersonDetailService;

    /**
     * 创建班组
     */
    @PostMapping("create")
    public Object create(WorkerGroup group) {

        if (!StringUtils.hasText(group.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        } else if (!StringUtils.hasText(group.getCorpName())) {
            throw AttendanceException.emptyMessage("班组所在企业名称");
        } else if (!StringUtils.hasText(group.getTeamName())) {
            throw AttendanceException.emptyMessage("班组名称");
        } else if (!StringUtils.hasText(group.getCorpCode())) {
            throw AttendanceException.emptyMessage("班组所在企业统一社会信用代码");
        } else if (groupService.findByTeamName(group.getTeamName()).isPresent()) {
            throw new AttendanceException(ResultError.GROUP_EXIST);
        }
        if (!CommonUtils.isRightPhone(group.getResponsiblePersonPhone())) {
            throw new AttendanceException(ResultError.PHONE_ERROR);
        }

        JSONObject gr = NationalUtils.uploadGroup(group);
        System.out.println(gr);
        JSONObject data = gr.getJSONObject("data");

        if (data.toString().contains("teamSysNo")) {
            JSONObject result = data.getJSONObject("result");
            System.out.println(result);
            Integer teamSysNo = result.getInteger("teamSysNo");
            System.out.println(teamSysNo);
            if (teamSysNo != null && teamSysNo > 0) {
                group.setTeamSysNo(teamSysNo);
                groupService.create(group);
                return ResultVo.success(group);
            }
        } else {
            String result = data.getString("result");
            return ResultVo.failure(8, result);
        }
        return ResultVo.failure(ResultError.NETWORK_ERROR);
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
            String code = jsonObject.getString("code");
            //判断是否修改成功
            if(!StringUtils.isEmpty(code) && !code.equals("0")){
                return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(),ResultError.NATIONAL_ERROR.getMessage() + jsonObject.getString("message"));
            }
            //修改本地班组信息
            groupService.updateByTeamSysNo(selectWorkerGroup);
            return ResultVo.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }
    }

    /**
     * 删除班组信息
     * @param teamSysNo
     * @return
     */
    @GetMapping("deleteWorkerGroup")
    public ResultVo deleteWorkerGroup(@RequestParam("teamSysNo") Integer teamSysNo) {
        if (teamSysNo <= 0) {
            throw AttendanceException.errorMessage("班组编号");
        }
        try {
            //删除project_detail相关的信息
            projectPersonDetailService.deleteByWorkerGroup(new WorkerGroup(teamSysNo));
            //删除班组信息
            int effectNum = groupService.deleteByTeamSysNo(teamSysNo);
            if (effectNum <= 0) {
                throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "班组");
            }else{
                return ResultVo.success("删除班组成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }
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
    }
}