package com.real.name.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.PageUtils;
import com.real.name.common.utils.PathUtil;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.query.PayInfoQuery;
import com.real.name.pay.service.PayInfoService;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pay")
public class PayInfoController {

    private Logger logger = LoggerFactory.getLogger(PayInfoController.class);

    @Autowired
    private PayInfoService payInfoService;

    /**
     * 保存工资信息
     */
    @PostMapping("/savePayInfo")
    public ResultVo savePayInfo(@RequestParam("payInfo") String payInfoStr,
                                @RequestParam("projectCode") String projectCode,
                                @RequestParam("teamSysNo") Integer teamSysNo,
                                @RequestParam("personId") Integer personId,
                                @RequestParam("payFile") MultipartFile payFile) {
        PayInfo payInfo;
        try {
            payInfo = JSONObject.parseObject(payInfoStr, PayInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将payInfoStr转换为json字符串出现异常");
            throw AttendanceException.errorMessage(ResultError.OPERATOR_ERROR);
        }
        if (payFile.isEmpty()) {
            throw AttendanceException.emptyMessage("文件");
        }
        //校验薪资信息是否正确
        verifyPayInfo(payInfo, payFile);
        //将薪资信息保存
        payInfoService.savePayInfo(payInfo, projectCode, teamSysNo, personId);
        //保存文件信息
        FileTool.generateFile(payFile, PathUtil.getPayFileBasePath(), payInfo.getPayId() + payInfo.getSuffixName());
        return ResultVo.success();
    }

    /**
     * 更新薪资信息
     */
    @PostMapping("/updatePayInfo")
    public ResultVo updatePayInfo(@RequestParam("payInfo") String payInfoStr,
                                  @RequestParam(name = "payFile", required = false) MultipartFile payFile) {
        PayInfo payInfo;
        try {
            payInfo = JSONObject.parseObject(payInfoStr, PayInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将payInfoStr转换为json字符串出现异常");
            throw AttendanceException.errorMessage(ResultError.OPERATOR_ERROR);
        }
        if (!payFile.isEmpty()) {
            String WholeFileName = payFile.getOriginalFilename();
            if (!StringUtils.hasText(WholeFileName)) {
                throw new AttendanceException(ResultError.EMPTY_NAME);
            }
            String suffixName = WholeFileName.substring(WholeFileName.lastIndexOf("."));
            if (StringUtils.isEmpty(suffixName)) {
                throw AttendanceException.emptyMessage("文件后缀名");
            }
            //删除原来的文件信息并重新生成新的文件
            String basePath = PathUtil.getPayFileBasePath();
            String fileName = payInfo.getPayId() + payInfo.getSuffixName();
            FileTool.deleteFile(basePath, fileName);
            FileTool.generateFile(payFile, basePath, fileName);
            payInfo.setSuffixName(suffixName);
        }
        //更新薪资信息
        payInfoService.updatePayInfo(payInfo);
        return ResultVo.success();
    }

    /**
     * 删除薪资记录并将详情文件也删除
     */
    @GetMapping("/deletePayInfo")
    public ResultVo deletePayInfo(@RequestParam("payId") Integer payId,
                                  @RequestParam("suffixName") String suffixName) {
        //删除详情文件
        FileTool.deleteFile(PathUtil.getPayFileBasePath(), payId + suffixName);
        //删除数据库中的薪资详情信息
        payInfoService.deletePayInfo(payId);
        return ResultVo.success();
    }


    /**
     * =====================================以下只与查询有关==============================
     */

    /**
     * 查询所有薪资记录
     */
    @JSONS({
            @JSON(type = ProjectDetailQuery.class, filter = "createTime,attendanceList,projectCode,teamSysNo"),
            @JSON(type = Person.class, include = "personId,personName,corpCode,subordinateCompany,idCardType,idCardNumber," +
                    "payRollBankCardNumber,payRollBankName,bankLinkNumber,payRollTopBankCode"),
            @JSON(type = WorkerGroup.class, include = "teamSysNo,teamName"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    @GetMapping("/getAllPayInfo")
    public ResultVo getAllPayInfo(@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        PageHelper.startPage(pageNum + 1, pageSize);
        List<PayInfo> allPayInfo = payInfoService.getAllPayInfo();
        PageInfo<PayInfo> pageInfo = new PageInfo<>(allPayInfo);
        return PageUtils.pageResult(pageInfo, allPayInfo);
    }

    /**
     * 薪资搜索
     */
    @JSONS({
            @JSON(type = ProjectDetailQuery.class, filter = "createTime,attendanceList,projectCode,teamSysNo"),
            @JSON(type = Person.class, include = "personId,personName,corpCode,subordinateCompany,idCardType,idCardNumber," +
                    "payRollBankCardNumber,payRollBankName,bankLinkNumber,payRollTopBankCode"),
            @JSON(type = WorkerGroup.class, include = "teamSysNo,teamName"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    @GetMapping("/searchPayInfo")
    public ResultVo searchPayInfo(PayInfoQuery payInfoQuery) {
        PageHelper.startPage(payInfoQuery.getPageNum(), payInfoQuery.getPageSize());
        List<PayInfo> infoList = payInfoService.searchPayInfo(payInfoQuery);
        PageInfo<PayInfo> pageInfo = new PageInfo<>(infoList);
        return PageUtils.pageResult(pageInfo, infoList);
    }


    /**
     * 校验用户输入的信息是否合法
     */
    private void verifyPayInfo(PayInfo payInfo, MultipartFile payFile) {
        if (payInfo.getBalanceDate() == null) {
            throw AttendanceException.emptyMessage("发放日期");
        } else if (payInfo.getIsBackPay() == null) {
            throw AttendanceException.emptyMessage("是否补发");
        } else if (payInfo.getIsBackPay() == 1 && payInfo.getBackPayMonth() == null) {
            throw AttendanceException.emptyMessage("补发月份");
        } else if (payInfo.getTotalPayAmount() == null) {
            throw AttendanceException.emptyMessage("应付工资");
        } else if (payInfo.getTotalPayAmount() < 0.0) {
            throw AttendanceException.errorMessage("应付工资");
        } else if (payInfo.getActualAmount() == null) {
            throw AttendanceException.emptyMessage("实付工资");
        } else if (payInfo.getActualAmount() < 0.0) {
            throw AttendanceException.errorMessage("实付工资");
        } else if (StringUtils.isEmpty(payInfo.getThirdPayRollCode())) {
            throw AttendanceException.emptyMessage("第三方工资单编号");
        } else if (!payFile.isEmpty()) {
            String WholeFileName = payFile.getOriginalFilename();
            if (!StringUtils.hasText(WholeFileName)) {
                throw new AttendanceException(ResultError.EMPTY_NAME);
            }
            String suffixName = WholeFileName.substring(WholeFileName.lastIndexOf("."));
            if (StringUtils.isEmpty(suffixName)) {
                throw AttendanceException.emptyMessage("文件后缀名");
            }
            payInfo.setSuffixName(suffixName);
        }
    }

}
