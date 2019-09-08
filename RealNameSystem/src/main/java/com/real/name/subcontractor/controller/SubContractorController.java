package com.real.name.subcontractor.controller;

import com.alibaba.fastjson.JSONArray;
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
import com.real.name.subcontractor.entity.query.GroupPeople;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.entity.search.SubContractorSearchInPro;
import com.real.name.subcontractor.service.SubContractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
        Integer status = subContractorService.findUploadStatusById(subContractor.getSubContractorId());
        if (status != null && status == 1) {
            //设置参建单位修改未上传
            subContractor.setUploadStatus(status);
        }
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
        //重建单位重新加入项目
        List<String> failNameList = subContractorService.contractorReJoinToProject(projectCode, subContractorId, groupPeopleList);
        if (failNameList.size() > 0) {
            return ResultVo.failure(failNameList, ResultError.PERSON_REJOIN_PROJECT_FAILURE);
        }
        return ResultVo.success();
    }

    /**
     * 查询项目中的参建单位
     * @param status 若不传则查询项目下所有参建单位，为1时查询项目下未被移除的参建单位 为0查询项目下被移除的参建单位
     */
    @GetMapping("/findSubContractorInPro")
    @JSONS({
            @JSON(type = SubContractorQuery.class, filter = "uploadStatus,createTime"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    public ResultVo findSubContractorInPro(@RequestParam("projectCode") String  projectCode,
                                      @RequestParam(name = "status", required = false) Integer status,
                                      @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        PageHelper.startPage(pageNum + 1, pageSize);
        List<SubContractorQuery> subContractorList = subContractorService.findContractInPro(projectCode, status);
        PageInfo<SubContractorQuery> pageInfo = new PageInfo<>(subContractorList);
        return PageUtils.pageResult(pageInfo, subContractorList);
    }

    /**
     * 查询项目中未被移除的参建单位名和id
     * @param status 不传为查询所有参建单位信息，为1为查询未被移除的参建单位名, 为0位查询被移除的参建单位名
     */
    @GetMapping("/findContractCorpNameInPro")
    @JSONS({
            @JSON(type = SubContractorQuery.class, include = "subContractorId,corpCode,corpName")
    })
    public ResultVo findContractCorpNameInPro(@RequestParam("projectCode") String projectCode,
                                              @RequestParam(name = "status", required = false) Integer status) {
        List<SubContractorQuery> subContractorQueryList = subContractorService.findContractCorpNameInPro(projectCode, status);
        return ResultVo.success(subContractorQueryList);
    }

    /**
     * 搜索项目下的参建单位
     */
    @JSONS({
            @JSON(type = SubContractorQuery.class, filter = "uploadStatus,createTime"),
            @JSON(type = Project.class, include = "projectCode,name")
    })
    @GetMapping("/searchContractorInPro")
    public ResultVo searchContractorInPro(SubContractorSearchInPro subContractorSearchInPro) {
        if (StringUtils.isEmpty(subContractorSearchInPro.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        }
        PageHelper.startPage(subContractorSearchInPro.getPageNum() + 1, subContractorSearchInPro.getPageSize());
        List<SubContractorQuery> subContractorQueryList = subContractorService.searchContractorInPro(subContractorSearchInPro);
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
