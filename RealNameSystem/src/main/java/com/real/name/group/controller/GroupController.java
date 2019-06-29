package com.real.name.group.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.NationalUtils;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

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
     * 创建班组
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
}