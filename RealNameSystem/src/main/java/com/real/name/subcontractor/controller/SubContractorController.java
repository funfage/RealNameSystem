package com.real.name.subcontractor.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.annotion.JSON;
import com.real.name.common.annotion.JSONS;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.PageUtils;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectService;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.query.GroupPeople;
import com.real.name.subcontractor.query.SubContractorQuery;
import com.real.name.subcontractor.service.SubContractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.RequestWrapper;
import java.util.List;

@RestController
@RequestMapping("subContractor")
public class SubContractorController {

    private Logger logger = LoggerFactory.getLogger(SubContractor.class);

    @Autowired
    private SubContractorService subContractorService;

    @Autowired
    private ProjectService projectService;

    /**
     * 添加参建单位
     */
    @PostMapping("/saveSubContractor")
    public ResultVo saveSubContractor(@RequestBody SubContractor subContractor) {
        //校验是否填写
        verify(subContractor);
        //保存参建单位信息
        subContractorService.saveSubContractor(subContractor);
        return ResultVo.success();
    }

    /**
     * 修改项目参建单位的信息
     */
    @PostMapping("/updateSubContractor")
    public ResultVo updateSubContractor(@RequestBody SubContractor subContractor) {
        subContractorService.updateSubContractorById(subContractor);
        return ResultVo.success();
    }

    /**
     * 将参建单位移出项目
     */
    @GetMapping("removeSubContractorInProject")
    public ResultVo removeSubContractorInProject(@RequestParam("subContractorId") Integer subContractorId,
                                                 @RequestParam("projectCode") String projectCode) {
        subContractorService.removeSubContractorInProject(subContractorId, projectCode);
        return ResultVo.success();
    }

    /**
     * 将参建单位重新加入项目
     */
    @PostMapping("/contractorReJoinToProject")
    public ResultVo contractorReJoinToProject(@RequestParam("projectCode") String projectCode,
                                              @RequestParam("subContractorId") Integer subContractorId,
                                              @RequestParam("groupPeopleList") String groupPeopleListStr) {
        List<GroupPeople> groupPeopleList;
        try {
            groupPeopleList = JSONArray.parseArray(groupPeopleListStr, GroupPeople.class);
        } catch (Exception e) {
            logger.error("json字符串groupPeopleListStr转换错误");
            return ResultVo.failure();
        }
        subContractorService.contractorReJoinToProject(projectCode, subContractorId, groupPeopleList);
        return ResultVo.success();
    }

    /**
     * 查询参建单位的企业名称
     * @param status 1为查询所有 否则为查询未被移除的参建单位
     */
    @GetMapping("/findCorpName")
    @JSON(type = SubContractorQuery.class, include = "subContractorId,corpCode,corpName")
    public ResultVo findCorpName(@RequestParam("projectCode") String projectCode,
                                 @RequestParam(name = "status", defaultValue = "1") Integer status) {
        if (status == 1) {
            List<SubContractorQuery> subContractorList = subContractorService.findCorpName(projectCode, 1);
            return ResultVo.success(subContractorList);
        } else {
            List<SubContractorQuery> subContractorList = subContractorService.findCorpName(projectCode, 0);
            return ResultVo.success(subContractorList);
        }
    }

    /**
     * 查询项目中的参建单位
     * @param status 为1时查询所有 否则为分页查询
     */
    @GetMapping("/findSubContractorInPro")
    @JSONS({
            @JSON(type = SubContractorQuery.class, filter = "uploadStatus,createTime"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    public ResultVo findSubContractor(@RequestParam("projectCode") String  projectCode,
                                      @RequestParam(name = "status", defaultValue = "1") Integer status,
                                      @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        if (status == 1) {
            List<SubContractorQuery> subContractorList = subContractorService.findByProjectCode(projectCode);
            return ResultVo.success(subContractorList);
        } else {
            PageHelper.startPage(pageNum + 1, pageSize);
            List<SubContractorQuery> subContractorList = subContractorService.findByProjectCode(projectCode);
            PageInfo<SubContractorQuery> pageInfo = new PageInfo<>(subContractorList);
            return PageUtils.pageResult(pageInfo, subContractorList);
        }
    }

    /**
     * 查未被移除的参建单位信息
     * @param status 为1时查询所有，否则为分页查询
     */
    @GetMapping("/findUnRmvContractInPro")
    @JSONS({
            @JSON(type = SubContractorQuery.class, filter = "uploadStatus,createTime"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    public ResultVo findUnRmvContractInPro(@RequestParam("projectCode") String projectCode,
                                           @RequestParam(value = "status", defaultValue = "1") Integer status,
                                           @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                           @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        if (status == 1) {
            List<SubContractorQuery> subContractorList = subContractorService.findUnRemoveInProject(projectCode);
            return ResultVo.success(subContractorList);
        } else {
            PageHelper.startPage(pageNum + 1, pageSize);
            List<SubContractorQuery> subContractorList = subContractorService.findUnRemoveInProject(projectCode);
            PageInfo<SubContractorQuery> pageInfo = new PageInfo<>(subContractorList);
            return PageUtils.pageResult(pageInfo, subContractorList);
        }
    }

    /**
     * 搜索项目下的参建单位
     */
    @JSONS({
            @JSON(type = SubContractorQuery.class, filter = "uploadStatus,createTime"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    @GetMapping("/searchContractorInPro")
    public ResultVo searchContractorInPro(SubContractorQuery subContractorQuery) {
        if (StringUtils.isEmpty(subContractorQuery.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        }
        PageHelper.startPage(subContractorQuery.getPageNum() + 1, subContractorQuery.getPageSize());
        List<SubContractorQuery> subContractorQueryList = subContractorService.searchContractorInPro(subContractorQuery);
        PageInfo<SubContractorQuery> pageInfo = new PageInfo<>(subContractorQueryList);
        return PageUtils.pageResult(pageInfo, subContractorQueryList);
    }

    private void verify(SubContractor subContractor) {
        if (subContractor.getProject() == null || StringUtils.isEmpty(subContractor.getProject().getProjectCode())) {
            throw AttendanceException.emptyMessage("项目");
        } else if (projectService.judgeEmptyByProjectCode(subContractor.getProject().getProjectCode())) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        } else if (StringUtils.isEmpty(subContractor.getCorpCode())) {
            throw AttendanceException.emptyMessage("信用代码");
        } else if (StringUtils.isEmpty(subContractor.getCorpName())) {
            throw AttendanceException.emptyMessage("企业名称");
        } else if (StringUtils.isEmpty(subContractor.getCorpType())) {
            throw AttendanceException.emptyMessage("参建类型");
        } else if (StringUtils.isEmpty(subContractor.getBankCode())) {
            throw AttendanceException.emptyMessage("银行代码");
        } else if (StringUtils.isEmpty(subContractor.getBankName())) {
            throw AttendanceException.emptyMessage("银行支行名称");
        } else if (StringUtils.isEmpty(subContractor.getBankNumber())) {
            throw AttendanceException.emptyMessage("银行卡号");
        }
    }

}
