package com.real.name.project.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.info.CommConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.utils.JedisService;
import com.real.name.common.utils.PageUtils;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.GroupPersonNum;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projectDetailQuery")
public class ProjectDetailQueryController {

    private Logger logger = LoggerFactory.getLogger(ProjectDetailQueryController.class);

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    /**
     * 获取将人员添加到项目的人员
     */
    @GetMapping("getPersonByWorkRole")
    public ResultVo getPersonByWorkRole(@RequestParam("projectCode") String projectCode,
                                        @RequestParam("isAdminGroup") Integer isAdminGroup,
                                        @RequestParam("groupCorpCode") String groupCorpCode,
                                        @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            //查询该项目是否存在
            Project project = projectService.findByProjectCode(projectCode);
            if (project == null) {
                return ResultVo.failure(ResultError.PROJECT_NOT_EXIST);
            }
            if (isAdminGroup == 1) {
                //查询除这个项目下的其他所有管理员
                PageHelper.startPage(pageNum + 1, pageSize);
                List<ProjectDetailQuery> detailQueries = projectDetailQueryService.findOtherAdmins(projectCode, groupCorpCode);
                PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(detailQueries);
                return PageUtils.pageResult(pageInfo, detailQueries);
            } else {
                //查询未被分配项目的普通工人信息
                PageHelper.startPage(pageNum + 1, pageSize);
                List<ProjectDetailQuery> detailQueries = projectDetailQueryService.findOtherWorker(groupCorpCode);
                PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(detailQueries);
                return PageUtils.pageResult(pageInfo , detailQueries);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultVo.failure();
        }
    }

    /**
     * 获取某个项目下每个班组的人员数目
     */
    @GetMapping("/getPersonNumInProject")
    public ResultVo getPersonNumInProject(String projectCode) {
        //查询项目名和公司名
        Project proNameAndCorp = projectService.findProNameAndCorp(projectCode);
        List<GroupPersonNum> workGroupPersonNum = projectDetailQueryService.getWorkGroupPersonNum(projectCode);
        //获取天气信息
        String weather = HTTPTool.getWeather();
        //获取在场信息
        List<Object> presentPerson = jedisStrings.multiGet(jedisKeys.keys(projectCode + CommConstant.PRESENT + "*"));
        //获取出场信息
        List<Object> outPerson = jedisStrings.multiGet(jedisKeys.keys(projectCode + CommConstant.ABSENT + "*"));
        Map<String, Object> map = new HashMap<>();
        map.put("weather", weather);
        map.put("projectName", proNameAndCorp.getName());
        map.put("corpName", proNameAndCorp.getContractorCorpName());
        map.put("workGroupPersonNum", workGroupPersonNum);
        map.put("presentPerson", presentPerson);
        map.put("outPerson", outPerson);
        return ResultVo.success(map);
    }

    /**
     * 获取项目下所有人员个数
     */
    @GetMapping("countPersonNumByProjectCode")
    public ResultVo countPersonNumByProjectCode(String projectCode) {
        Integer countNum = projectDetailQueryService.countPersonNumByProjectCode(projectCode);
        if (countNum == null) {
            countNum = 0;
        }
        return ResultVo.success(countNum);
    }

}
