package com.real.name.contract.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.PageUtils;
import com.real.name.contract.entity.ContractFile;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.query.ContractInfoQuery;
import com.real.name.contract.service.ContractInfoService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    private Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractInfoService contractInfoService;

    /**
     * 添加合同信息
     */
    @PostMapping("/saveContractInfo")
    public ResultVo saveContractInfo(@RequestParam("contractInfo") String contractInfoStr,
                                     @RequestParam("projectCode") String projectCode,
                                     @RequestParam("personId") Integer personId,
                                     @RequestParam("contractFiles") MultipartFile [] contractFiles) {
        ContractInfo contractInfo = null;
        try {
            contractInfo = JSONObject.parseObject(contractInfoStr, ContractInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将payInfoStr转换为json字符串出现异常");
            throw AttendanceException.errorMessage(ResultError.OPERATOR_ERROR);
        }
        if (contractFiles.length <= 0) {
            throw AttendanceException.emptyMessage("文件");
        }
        //校验合同信息
        verifyContractInfo(contractInfo);
        //保存合同信息
        contractInfoService.saveContractInfo(contractInfo, projectCode, personId);
        Integer contractId = contractInfo.getContractId();
        //保存文件并保存合同信息和文件的一对多关联
        contractInfoService.saveContractFileList(contractId, contractFiles);
        return ResultVo.success();
    }

    /**
     * 更新合同
     */
    @PostMapping("/updateContractInfo")
    public ResultVo updateContractInfo(@RequestParam("contractInfo") String contractInfoStr,
                                       @RequestParam(name = "deleteFileList", required = false) List<String> deleteFileList,
                                       @RequestParam(name = "contractFiles", required = false) MultipartFile[] addFileList) {
        ContractInfo contractInfo = null;
        try {
            contractInfo = JSONObject.parseObject(contractInfoStr, ContractInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将payInfoStr转换为json字符串出现异常");
            throw AttendanceException.errorMessage(ResultError.OPERATOR_ERROR);
        }
        Integer contractId = contractInfo.getContractId();
        //删除文件
        contractInfoService.deleteContractFileList(contractId, deleteFileList);
        //保存文件
        contractInfoService.saveContractFileList(contractId, addFileList);
        //修改合同信息
        contractInfoService.updateContractInfo(contractInfo);
        return ResultVo.success();
    }

    /**
     * 删除合同
     */
    @GetMapping("/deleteContractInfo")
    public ResultVo deleteContractInfo(@RequestParam("contractId") Integer contractId) {
        contractInfoService.deleteContractInfo(contractId);
        return ResultVo.success();
    }

    /**
     * ======================================以下只与查询有关====================================
     */
    /**
     * 查询所有合同信息
     */
    @JSONS({
            @JSON(type = ProjectDetailQuery.class, filter = "createTime,attendanceList,projectCode,teamSysNo"),
            @JSON(type = Person.class,
                    include = "personId,personName,corpCode,subordinateCompany,idCardType,idCardNumber"),
            @JSON(type = WorkerGroup.class, include = "teamSysNo,teamName"),
            @JSON(type = Project.class, include = "projectCode,name"),
            @JSON(type = ContractFile.class, filter = "contractId")
    })
    @GetMapping("/getAllContractInfo")
    public ResultVo getAllContractInfo(@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                       @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        PageHelper.startPage(   pageNum + 1, pageSize);
        List<ContractInfo> allContractInfo = contractInfoService.getAllContractInfo();
        PageInfo<ContractInfo> pageInfo = new PageInfo<>(allContractInfo);
        return PageUtils.pageResult(pageInfo, allContractInfo);
    }

    /**
     * 搜索合同信息
     */
    @JSONS({
            @JSON(type = ProjectDetailQuery.class, filter = "createTime,attendanceList,projectCode,teamSysNo"),
            @JSON(type = Person.class,
                    include = "personId,personName,corpCode,subordinateCompany,idCardType,idCardNumber"),
            @JSON(type = WorkerGroup.class, include = "teamSysNo,teamName"),
            @JSON(type = Project.class, include = "projectCode,name"),
            @JSON(type = ContractFile.class, filter = "contractId")
    })
    @GetMapping("/searchContractInfo")
    public ResultVo searchContractInfo(ContractInfoQuery contractInfoQuery) {
        PageHelper.startPage(contractInfoQuery.getPageNum() + 1, contractInfoQuery.getPageSize());
        List<ContractInfo> contractInfoList = contractInfoService.searchContractInfo(contractInfoQuery);
        PageInfo<ContractInfo> pageInfo = new PageInfo<>(contractInfoList);
        return PageUtils.pageResult(pageInfo, contractInfoList);
    }


    private void verifyContractInfo(ContractInfo contractInfo) {
        if (contractInfo.getContractPeriodType() == null) {
            throw AttendanceException.emptyMessage("合同期限类型");
        } else if (contractInfo.getContractPeriodType() != 1 && contractInfo.getContractPeriodType() != 0) {
            throw AttendanceException.errorMessage("合同期限类型");
        } else if (contractInfo.getStartDate() == null) {
            throw AttendanceException.emptyMessage("生效日期");
        } else if (contractInfo.getEndDate() == null) {
            throw AttendanceException.emptyMessage("失效日期");
        }
    }

}




















