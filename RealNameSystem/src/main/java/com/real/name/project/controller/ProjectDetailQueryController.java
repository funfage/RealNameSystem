package com.real.name.project.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/projectDetailQuery")
public class ProjectDetailQueryController {

    private Logger logger = LoggerFactory.getLogger(ProjectDetailQueryController.class);

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("getPersonByWorkRole")
    public ResultVo getPersonByWorkRole(@RequestParam("projectCode") String projectCode,
                                        @RequestParam("isAdminGroup") Integer isAdminGroup,
                                        @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            //查询该项目是否存在
            Optional<Project> projectOptional = projectService.findByProjectCode(projectCode);
            if (!projectOptional.isPresent()) {
                return ResultVo.failure(ResultError.PROJECT_NOT_EXIST);
            }
            if (isAdminGroup != null && isAdminGroup == 1) {//查询除这个项目下的其他所有管理员
                PageHelper.startPage(pageNum + 1, pageSize);
                List<ProjectDetailQuery> detailQueries = projectDetailQueryService.findOtherAdmins(projectCode);
                PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(detailQueries);
                Map<String, Object> map = new HashMap<>();
                map.put("detailQueries", detailQueries);
                map.put("pageNum", pageInfo.getPageNum());
                map.put("pageSize", pageInfo.getPageSize());
                return ResultVo.success(map);
            } else {//查询为被分配项目的普通工人信息
                PageHelper.startPage(pageNum + 1, pageSize);
                List<ProjectDetailQuery> detailQueries = projectDetailQueryService.findOtherWorker();
                Map<String, Object> map = new HashMap<>();
                PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(detailQueries);
                map.put("detailQueries", detailQueries);
                map.put("pageNum", pageInfo.getPageNum());
                map.put("pageSize", pageInfo.getPageSize());
                return ResultVo.success(map);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultVo.failure();
        }
    }

}
