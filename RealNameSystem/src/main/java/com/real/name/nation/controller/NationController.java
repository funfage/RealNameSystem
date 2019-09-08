package com.real.name.nation.controller;

import com.real.name.common.result.ResultError;
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
        List<String> failNameList = nationService.uploadProject(projectCodeList);
        return result(failNameList);
    }

    /**
     * 上传参建单位信息/修改上传参建单位信息
     */
    @PostMapping("/uploadSubContractor")
    public ResultVo uploadSubContractor(@RequestParam("subContractorIdList") List<Integer> subContractorIdList,
                                        @RequestParam("projectCode") String projectCode) {
        List<String> failNameList = nationService.uploadSubContractor(subContractorIdList, projectCode);
        return result(failNameList);
    }

    /**
     * 上传班组信息
     */
    @PostMapping("/uploadWorkerGroup")
    public ResultVo uploadWorkerGroup(List<Integer> groupIdList, Integer subContractorId) {
        List<String> failNameList = nationService.uploadWorkerGroup(groupIdList, subContractorId);
        return result(failNameList);
    }

    /**
     * 修改全国平台班组信息
     */
    @PostMapping("/updateWorkerGroup")
    public ResultVo updateWorkerGroup(@RequestParam("subContractorId") Integer subContractorId,
                                      @RequestParam("groupIdList") List<Integer> groupIdList) {
        List<String> failNameList = nationService.uploadWorkerGroup(groupIdList, subContractorId);
        return result(failNameList);
    }

    /**
     * 上传人员信息/修改上传人员信息
     */
    @PostMapping("/uploadPerson")
    public ResultVo uploadPerson(@RequestParam("projectCode") String projectCode,
                                 @RequestParam("teamSysNo") Integer teamSysNo,
                                 @RequestParam("personIdList") List<Integer> personIdList) {
        List<String> failNameList = nationService.uploadPerson(personIdList, teamSysNo, projectCode);
        return result(failNameList);
    }

    /**
     * 上传合同信息/修改上传合同信息
     */
    @GetMapping("/uploadContractor")
    public ResultVo uploadContractor(@RequestParam("contractIdList") List<Integer> contractIdList) {
        List<String> failNameList = nationService.uploadContractor(contractIdList);
        return result(failNameList);
    }

    private ResultVo result(List<String> failNameList) {
        if (failNameList.size() > 0) {
            return ResultVo.failure(failNameList, ResultError.NATION_UPLOAD_FAILURE);
        }
        return ResultVo.success();
    }


}
