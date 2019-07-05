package com.real.name.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.NationalUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.service.IssueDetailService;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectPersonDetailService;
import com.real.name.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//import com.real.name.person.entity.Person2;


@RestController
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PersonService personService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private ProjectPersonDetailService projectPersonDetailService;

    @Autowired
    private IssueDetailService issueDetailService;

    @Autowired
    private DeviceService deviceService;

    /**
     * 本地创建项目
     */
    @PostMapping("/createProject")
    public ResultVo createProject(@RequestBody Project project) {
        if (!StringUtils.hasText(project.getName())) {
            throw  AttendanceException.emptyMessage("项目名称");
        }
        try {
            // 判断项目名称是否唯一
            Optional<Project> optionalProject = projectService.findByName(project.getName());
            if (optionalProject.isPresent()) {
                throw new AttendanceException(ResultError.PROJECT_EXIST);
            }
            //为项目生成一个唯一的projectCode
            project.setProjectCode(CommonUtils.getUniqueString(32));
            //此处日期可能要修改
            Project insertProject = projectService.createProject(project);
            if (insertProject == null) {
                logger.error("项目添加失败");
                throw new AttendanceException(ResultError.INSERT_ERROR);
            }
            return ResultVo.success();
        } catch (Exception e) {
            logger.error("项目添加异常", e);
            return ResultVo.failure(ResultError.INSERT_ERROR, e.getMessage());
        }
    }

    /**
     * 从全国平台查询并创建项目
     */
    @GetMapping("/queryProject")
    public ResultVo queryProject(@RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        // 从全国对接平台查询项目
        JSONObject jsonObject = NationalUtils.queryProject(pageIndex, pageSize);
        //判断是否查询成功
        if(!jsonObject.getBoolean("error")){
            JSONObject queryResult = jsonObject.getJSONObject("data");
            //如果不为空则将项目信息插入数据库
            if (queryResult != null) {
                JSONArray rows = queryResult.getJSONArray("rows");
                List<Project> projects = JSONObject.parseArray(rows.toJSONString(), Project.class);
                for (Project project : projects) {
                    try {
                        projectService.createProject(project);
                    } catch (Exception e) {
                        //无需处理
                    }
                }
            }
        }
        //从数据库查询信息
        Page<Project> queryList = projectService.findAll(PageRequest.of(pageIndex, pageSize));
        if (queryList.isEmpty()) {
            return ResultVo.failure(ResultError.QUERY_EMPTY);
        }
        return ResultVo.success(queryList);
    }

    /**
     * 修改项目
     */
    @PostMapping(value = "updateProject")
    public ResultVo updateProject(@RequestBody Project project){
        if(project.getProjectCode() == null){
            throw AttendanceException.emptyMessage("projectCode");
        }
        //查询该项目是否存在
        Optional<Project> projectOptional = projectService.findByProjectCode(project.getProjectCode());
        if(!projectOptional.isPresent()){
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        Project selectProject = projectOptional.get();
        //信息的合并
        mergeProject(selectProject, project);
        try {
            //修改数据库中的信息
            Project updateProject = projectService.updateByProjectCode(selectProject);
            if (updateProject == null) {
                throw new AttendanceException(ResultError.UPDATE_ERROR);
            }
            return ResultVo.success();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateProject error e:{}", e);
            return ResultVo.failure();
        }
    }

    /**
     * 删除项目
     */
    @GetMapping("deleteProject")
    public ResultVo deleteProject(@RequestParam("projectCode") String projectCode) {
        if (!StringUtils.hasText(projectCode)) {
            throw AttendanceException.emptyMessage("项目编号");
        }
        try {
            //删除project_detail相关的信息
            projectPersonDetailService.deleteByProject(new Project(projectCode));
            int effectNum = projectService.deleteByProjectCode(projectCode);
            //判断是否删除成功
            if (effectNum <= 0) {
                throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "项目");
            }else{
                return ResultVo.success("删除项目成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure();
        }
    }


    /**
     * 项目添加人员
     */
    @PostMapping("/addPerson")
    public ResultVo addPerson(@RequestParam(value = "persons[]") Integer[] persons,
                              @RequestParam("projectCode") String projectCode,
                              @RequestParam("teamSysNo") Integer teamSysNo) {
        // 判断是否存在该项目
        Optional<Project> project = projectService.findByProjectCode(projectCode);
        if (!project.isPresent()) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        // 判断是否存在该班组
        Optional<WorkerGroup> group = groupService.findById(teamSysNo);
        if (!group.isPresent()) {
            throw new AttendanceException(ResultError.GROUP_NOT_EXIST);
        }
        //判断是否有设备存在
        List<Device> allDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
        if (allDevices == null || allDevices.size() <= 0) {
            logger.warn("查询不到添加的人脸设备");
            throw new AttendanceException(ResultError.FACE_EMPTY);
        }
        //判断该项目是否绑定设备
        List<Device> projectDevices = deviceService.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.faceDeviceType);
        if (projectDevices != null && projectDevices.size() <= 0) {
            logger.warn("该项目未绑定人脸设备");
            throw new AttendanceException(ResultError.PROJECT_NO_BIND_FACE);
        }
       /* // 判断该班组中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetail1 = projectDetailService.findByTeamSysNoAndPersonId(teamSysNo, personId);
        // 判断该项目中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetailOp = projectDetailService.findByProjectCodeAndPersonId(projectCode, personId);*/
        // 添加人员到项目中
        for (Integer personId : persons) {
            // 判断是否有该人员
            Optional<Person> personOptional = personService.findById(personId);
            if (!personOptional.isPresent() || !StringUtils.hasText(personOptional.get().getHeadImage()) || !StringUtils.hasText(personOptional.get().getPersonName())) {
                //若人员不存在则跳过
                continue;
            }
            Optional<ProjectDetail> detailOptional = projectDetailService.findByProjectCodeAndPersonIdAndTeamSysNo(projectCode, personId, teamSysNo);
            //如果为空则添加项目人员信息关联
            if (!detailOptional.isPresent()) {
                ProjectDetail save = projectDetailService.save(new ProjectDetail(projectCode, personId, teamSysNo));
                if (save == null) {
                    throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "添加人员到项目");
                }
            }
            //下发人员信息到设备
            projectDetailService.addPersonToDevice(projectCode, personOptional.get(), projectDevices, allDevices);
        }
        //从数据库中查询下发信息
        /*List<Integer> issueSuccessId = issueDetailService.findIdByIssueStatus(1, 1);
        List<Integer> issuePersonFailId = issueDetailService.findIdByIssuePersonStatus(0);
        List<Integer> issueImageFailId = issueDetailService.findIdByIssueImageStatus(0);
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("issueSuccessId", issueSuccessId);
        map.put("issuePersonFailId", issuePersonFailId);
        map.put("issueImageFailId", issueImageFailId);*/
        return ResultVo.success();
    }

    /**
     * 获取项目中的人员
     */
    @GetMapping("/personInProject")
    public ResultVo personInProject(@RequestParam("projectCode") String  projectCode,
                                    @RequestParam(name = "pageIndex", defaultValue = "0") Integer page,
                                    @RequestParam(name = "pageSize", defaultValue = "20") Integer size) {

        Map<String, Object> personList = projectDetailService.getPersonInProject(projectCode, PageRequest.of(page, size));
        return ResultVo.success(personList);
    }


    /**
     * 合并项目信息
     * @param selectProject
     * @param project
     * @return
     */
    private void mergeProject(Project selectProject, Project project){
        if(StringUtils.hasText(project.getAddress())){
            selectProject.setAddress(project.getAddress());
        }
        if(project.getApprovalLevelNum() != null){
            selectProject.setApprovalLevelNum(project.getApprovalLevelNum());
        }
        if(StringUtils.hasText(project.getApprovalNum())){
            selectProject.setApprovalNum(project.getApprovalNum());
        }
        if(StringUtils.hasText(project.getAreaCode())){
            selectProject.setAreaCode(project.getAreaCode());
        }
        if(StringUtils.hasText(project.getBuildCorpCode())){
            selectProject.setBuildCorpCode(project.getBuildCorpCode());
        }
        if(StringUtils.hasText(project.getBuildCorpName())){
            selectProject.setBuildCorpName(project.getBuildCorpName());
        }
        if(StringUtils.hasText(project.getBuilderLicenses())){
            selectProject.setBuilderLicenses(project.getBuilderLicenses());
        }
        if(StringUtils.hasText(project.getBuilderLicenses())){
            selectProject.setBuilderLicenses(project.getBuilderLicenses());
        }
        if(project.getBuildingArea() != null){
            if(project.getBuildingArea() < 0){
                throw AttendanceException.errorMessage("总面积");
            }
            selectProject.setBuildingArea(project.getBuildingArea());
        }
        if(project.getBuildingLength() != null){
            if(project.getBuildingLength() < 0){
                throw AttendanceException.errorMessage("总长度");
            }
            selectProject.setBuildingLength(project.getBuildingLength());
        }
        if(StringUtils.hasText(project.getBuildPlanNum())){
            selectProject.setBuildPlanNum(project.getBuildPlanNum());
        }
        if(StringUtils.hasText(project.getCategory())){
            selectProject.setCategory(project.getCategory());
        }
        //竣工日期
        if(project.getCompleteDate() != null){
            selectProject.setCompleteDate(project.getCompleteDate());
        }
        if(StringUtils.hasText(project.getContractorCorpCode())){
            selectProject.setContractorCorpCode(project.getContractorCorpCode());
        }
        if(StringUtils.hasText(project.getContractorCorpName())){
            selectProject.setContractorCorpName(project.getContractorCorpName());
        }
        if(StringUtils.hasText(project.getDescription())){
            selectProject.setDescription(project.getDescription());
        }
        if(StringUtils.hasText(project.getFunctionNum())){
            selectProject.setFunctionNum(project.getFunctionNum());
        }
        if(project.getInvest() != null){
            if(project.getInvest() <= 0){
                throw AttendanceException.errorMessage("总投资");
            }
            selectProject.setInvest(project.getInvest());
        }
        if(project.getLat() != null){
            if(project.getLat() < 0  || project.getLat() > 180){
                throw AttendanceException.errorMessage("经度");
            }
            selectProject.setLat(project.getLat());
        }
        if(StringUtils.hasText(project.getLinkMan())){
            selectProject.setLinkMan(project.getLinkMan());
        }
        if(StringUtils.hasText(project.getLinkPhone())){
            selectProject.setLinkPhone(project.getLinkPhone());
        }
        if(project.getLng() != null){
            if (project.getLng() < 0 || project.getLng() > 90) {
                throw AttendanceException.errorMessage("纬度");
            }
            selectProject.setLng(project.getLng());
        }
        if(StringUtils.hasText(project.getName())){
            selectProject.setName(project.getName());
        }
        if(project.getNationNum() != null){
            selectProject.setNationNum(project.getNationNum());
        }
        if(StringUtils.hasText(project.getPrjPlanNum())){
            selectProject.setPrjPlanNum(project.getPrjPlanNum());
        }
        if(StringUtils.hasText(project.getPrjSize())){
            selectProject.setPrjSize(project.getPrjSize());
        }
        if(project.getPrjStatus() != null){
            Integer status = project.getPrjStatus();
            if(status != 1 && status != 2 && status != 3 && status != 4 && status != 5){
                throw AttendanceException.errorMessage("项目状态");
            }
            selectProject.setPrjStatus(project.getPrjStatus());
        }
        if(StringUtils.hasText(project.getPropertyNum())){
            selectProject.setPropertyNum(project.getPropertyNum());
        }
        if(project.getStartDate() != null){
            if (project.getCompleteDate() == null || project.getStartDate().getTime() > project.getCompleteDate().getTime()) {
                throw AttendanceException.errorMessage("开工日期大于竣工日期,");
            }
            selectProject.setStartDate(project.getStartDate());
        }
    }


//    /**
//     * 查询项目考勤记录
//     */
//    @GetMapping("/attendancePerson")
//    public ResultVo attendancePerson(@RequestParam("projectId") Integer projectId,
//                                     @RequestParam("beginTime") Long beginTime,
//                                     @RequestParam("endTime") Long endTime /*,
//                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                     @RequestParam(name = "size", defaultValue = "20") Integer size*/) {
//        // 获取项目中全部人员
//        Map<String, Object> personMap = projectDetailService.getPersonInProject(projectId, PageRequest.of(0, 999));
//        List<Person2> personList = (List<Person2>) personMap.get("personList");
//
//        List<Object> recordList = new ArrayList<>();
//
//        // 遍历人员，在时间范围内是否有考勤记录
//        for (Person2 person : personList) {
//            List<Record> records = recordService.findByPersonIdAndTimeBetween(person.getPersonId(), new Date(beginTime), new Date(endTime));
//            if (records != null && records.size() > 0) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("person", person);
//                map.put("records", records);
//                recordList.add(map);
//            }
//        }
//        return ResultVo.success(recordList);
//    }
}