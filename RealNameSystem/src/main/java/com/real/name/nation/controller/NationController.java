package com.real.name.nation.controller;

import com.real.name.common.result.ResultVo;
import com.real.name.nation.service.NationService;
import com.real.name.subcontractor.entity.SubContractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController("nation")
public class NationController {

    @Autowired
    private NationService nationService;

    /**
     * 上传项目信息/修改上传项目信息
     */
    @PostMapping("/uploadProject")
    public ResultVo uploadProject(@RequestParam("projectCodeList") Set<String> projectCodeList) {
        nationService.uploadProject(projectCodeList);
        return ResultVo.success();
    }

    /**
     * 上传参建单位信息/修改上传参建单位信息
     */
    @PostMapping("/uploadSubContractor")
    public ResultVo uploadSubContractor(@RequestParam("subContractorIdList") List<Integer> subContractorIdList,
                                        @RequestParam("projectCode") String projectCode) {
        nationService.uploadSubContractor(subContractorIdList, projectCode);
        return ResultVo.success();
    }

    /**
     * 上传班组信息
     */
    @PostMapping("/uploadWorkerGroup")
    public ResultVo uploadWorkerGroup() {
        return ResultVo.success();
    }

    /**
     * 修改全国平台班组信息
     */
    @PostMapping("/updateWorkerGroup")
    public ResultVo updateWorkerGroup(@RequestParam("subContractorId") Integer subContractorId,
                                      @RequestParam("groupIdList") List<Integer> groupIdList) {
        nationService.uploadWorkerGroup(groupIdList, subContractorId);
        return ResultVo.success();
    }

    /**
     * 上传人员信息/修改上传人员信息
     */
    @PostMapping("/uploadPerson")
    public ResultVo uploadPerson(@RequestParam("projectCode") String projectCode,
                                 @RequestParam("teamSysNo") Integer teamSysNo,
                                 @RequestParam("personIdList") List<Integer> personIdList) {
        nationService.uploadPerson(personIdList, teamSysNo, projectCode);
        //查询用户是否绑定项目
        return ResultVo.success();
    }

    /**
     * 上传合同信息/修改上传合同信息
     */
    @GetMapping("/uploadContractor")
    public ResultVo uploadContractor(@RequestParam("contractIdList") List<Integer> contractIdList) {

        return ResultVo.success();
    }


}
