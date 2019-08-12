package com.real.name.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.constant.CommConstant;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.JedisService;
import com.real.name.common.utils.PageUtils;
import com.real.name.common.utils.TimeUtil;
import com.real.name.common.websocket.WebSocket;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.query.ProjectQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


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
    private IssueFaceService issueFaceService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private WebSocket webSocket;


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
            Project selectProject = projectService.findByName(project.getName());
            if (selectProject != null) {
                throw new AttendanceException(ResultError.PROJECT_EXIST);
            }
            //为项目生成一个唯一的projectCode
            project.setProjectCode(CommonUtils.getUniqueString(20));
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
        //从数据库查询信息
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Project> queryList = projectService.findAll();
        PageInfo<Project> pageInfo = new PageInfo<>(queryList);
        if (queryList.isEmpty()) {
            return ResultVo.failure(ResultError.QUERY_EMPTY);
        }
        return PageUtils.pageResult(pageInfo, queryList);
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
        Project selectProject = projectService.findByProjectCode(project.getProjectCode());
        if(selectProject == null){
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
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
    @Transactional
    @GetMapping("/deleteProject")
    public ResultVo deleteProject(@RequestParam("projectCode") String projectCode) {
        if (!StringUtils.hasText(projectCode)) {
            throw AttendanceException.emptyMessage("项目编号");
        }
        //查询项目下所有的人员信息和班组信息
        List<ProjectDetailQuery> projectDetailList = projectDetailQueryService.findProjectDetail(projectCode);
        //获取该项目绑定的所有设备
        List<Device> deviceList = deviceService.findByProjectCode(projectCode);
        for (ProjectDetailQuery projectDetailQuery : projectDetailList) {
            Person person = projectDetailQuery.getPerson();
            if (person.getWorkRole() == 20) {//只有普通人员才删除设备的信息
                deviceService.deletePersonInDeviceList(deviceList, person);
            }
            //删除该项目下班组的信息
            groupService.deleteByTeamSysNo(projectDetailQuery.getTeamSysNo());
        }
        int effectNum = projectService.deleteByProjectCode(projectCode);
        //判断是否删除成功
        if (effectNum <= 0) {
            throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "项目");
        }else{
            return ResultVo.success("删除项目成功");
        }
    }


    /**
     * 项目添加人员
     */
    @Transactional
    @PostMapping("/addPerson")
    public ResultVo addPerson(@RequestParam(value = "persons[]") Integer[] persons,
                              @RequestParam("projectCode") String projectCode,
                              @RequestParam("teamSysNo") Integer teamSysNo) {
        // 判断是否存在该项目
        Project project = projectService.findByProjectCode(projectCode);
        if (project == null) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }
        // 判断是否存在该班组
        Optional<WorkerGroup> group = groupService.findById(teamSysNo);
        if (!group.isPresent()) {
            throw new AttendanceException(ResultError.GROUP_NOT_EXIST);
        }
        //查询所有人脸设备
        List<Device> allFaceDevices = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
        //查询项目绑定的人脸设备
        List<Device> projectFaceDevices = deviceService.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.faceDeviceType);
        //查询所有的控制器设备
        List<Device> allAccessDevices = deviceService.findAllByDeviceType(DeviceConstant.AccessDeviceType);
        //查询项目绑定的控制器设备
        List<Device> projectAccessDevices = deviceService.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.AccessDeviceType);
        //判断该项目是否有绑定设备
        if ((projectFaceDevices.size() <= 0) && (projectAccessDevices.size() <= 0)) {
            throw new AttendanceException(ResultError.PROJECT_NO_BIND_DEVICE);
        }
        // 添加人员到项目中
        for (Integer personId : persons) {
            //判断是否有该人员
            Person personImageInfo = personService.findIssuePersonImageInfo(personId);
            if (personImageInfo == null || !StringUtils.hasText(personImageInfo.getPersonName()) || !StringUtils.hasText(personImageInfo.getPersonName())) {
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
            //若果是管理工人则下发到该项目绑定的设备
            if (personImageInfo.getWorkRole() == 10) {
                //为管理工人创建所有下发设备的标识
                for (Device device : allFaceDevices) {
                    int effect = issueFaceService.insertInitIssue(new IssueFace(personImageInfo, device, 0, 0));
                    if (effect <= 0) {
                        throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "添加人员到项目");
                    }
                }
                for (Device device : allAccessDevices) {
                    int effect = issueAccessService.insertIssueAccess(new IssueAccess(personImageInfo, device, 0));
                    if (effect <= 0) {
                        throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "添加人员到项目");
                    }
                }
            } else if (personImageInfo.getWorkRole() == 20) {//如果是普通工人则下发到所有设备
                //为普通工人添加一条项目绑定设备的标识
                for (Device device : projectFaceDevices) {
                    int effectNum = issueFaceService.insertInitIssue(new IssueFace(personImageInfo, device, 0, 0));
                    if (effectNum <= 0) {
                        throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "添加人员到项目");
                    }
                }
                for (Device device : projectAccessDevices) {
                    int effectNum = issueAccessService.insertIssueAccess(new IssueAccess(personImageInfo, device, 0));
                    if (effectNum <= 0) {
                        throw AttendanceException.errorMessage(ResultError.INSERT_ERROR, "添加人员到项目");
                    }
                }
            }
            //下发人员信息到人脸设备
            projectDetailService.addPersonToFaceDevice(projectCode, personImageInfo, projectFaceDevices, allFaceDevices, group.get().getTeamName());
            //下发信息到控制器
            personImageInfo.setHeadImage(null);
            projectDetailService.addPersonToAccessDevice(projectCode, personImageInfo, projectAccessDevices, allAccessDevices, group.get().getTeamName());
        }
        return ResultVo.success();
    }

    /**
     * 从项目中移除人员
     */
    @GetMapping("/deletePersonInProject")
    @Transactional
    public ResultVo deletePersonInProject(@RequestParam("projectCode") String projectCode,
                                          @RequestParam("personId") Integer personId,
                                          @RequestParam("idCardIndex") String idCardIndex,
                                          @RequestParam("workRole") Integer workRole) {
        //获取该项目绑定的设备信息
        List<Device> deviceList = deviceService.findByProjectCode(projectCode);
        Person person = new Person();
        person.setPersonId(personId);
        person.setWorkRole(workRole);
        person.setIdCardIndex(idCardIndex);
        //如果不是管理员则删除设备信息
        if (workRole != 10) {
            deviceService.deletePersonInDeviceList(deviceList, person);
        }
        //查被删除人员所在的班组
        String teamName = projectDetailQueryService.findTeamName(projectCode, personId);
        String key = projectCode + CommConstant.ABSENT + teamName;
        if (jedisKeys.hasKey(key)) {
            String value = (String) jedisStrings.get(key);
            int number = Integer.parseInt(value.substring(value.lastIndexOf(",") + 1)) + 1;
            jedisStrings.set(key, teamName + "," + number, TimeUtil.getTomorrowBeginMilliSecond(), TimeUnit.MILLISECONDS);
        } else {
            jedisStrings.set(key, teamName + "," + 1, TimeUtil.getTomorrowBeginMilliSecond(), TimeUnit.MILLISECONDS);
        }
        //删除人员与项目所关联的信息
        int i = projectDetailQueryService.deletePersonInProject(projectCode, personId);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
        //推送出场消息给前端
        JSONObject map = new JSONObject();
        map.put("teamName", teamName);
        map.put("projectCode", projectCode);
        map.put("type", CommConstant.ABSENT_TYPE);
        webSocket.sendMessageToAll(map.toJSONString());
        return ResultVo.success();
    }



    /**
     * 获取项目中的人员
     */
    @GetMapping("/personInProject")
    public ResultVo personInProject(@RequestParam("projectCode") String  projectCode,
                                    @RequestParam(name = "pageIndex", defaultValue = "0") Integer page,
                                    @RequestParam(name = "pageSize", defaultValue = "20") Integer size) {
        try {
            PageHelper.startPage(page + 1, size);
            List<ProjectDetailQuery> detailQueries = projectDetailQueryService.getPersonAndWorkerGroupInfo(projectCode);
            PageInfo<ProjectDetailQuery> pageInfo = new PageInfo<>(detailQueries);
            Map<String, Object> map = new HashMap<>();
            map.put("pageNum", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("total", pageInfo.getTotal());
            map.put("detailQueries", detailQueries);
            return ResultVo.success(map);
        } catch (Exception e) {
            logger.error("查询某个项目下的人员信息和班组信息失败, e:{}", e.getMessage());
            return ResultVo.failure();
        }
    }

    /**
     * 从项目中移除设备
     */
    @GetMapping("/uninstallDevice")
    public ResultVo uninstallDevice(String projectCode, String deviceId) {
        Optional<Device> deviceOptional = deviceService.findByDeviceId(deviceId);
        if (!deviceOptional.isPresent()) {
            throw new AttendanceException(ResultError.DEVICE_NOT_EXIST);
        }
        Device device = deviceOptional.get();
        //查询项目下所有人员所有信息
        List<ProjectDetailQuery> projectDetailList = projectDetailQueryService.findDelPersonInDeviceByProject(projectCode);
        for (ProjectDetailQuery projectDetailQuery : projectDetailList) {
            Person person = projectDetailQuery.getPerson();
            if (person.getWorkRole() != null && person.getWorkRole() == 20) {
                //删除设备的非管理人员信息
                deviceService.deletePersonInDevice(device, person);
            }
        }
        //删除设备与项目的关联
        device.setProjectCode(null);
        deviceService.save(device);
        return ResultVo.success();
    }

    /**
     * 根据角色用户项目id和项目名
     */
    @GetMapping("/getProjectCodeAndNameByUserRole")
    public ResultVo getProjectCodeAndNameByUserRole() {
        return ResultVo.success(projectService.getProjectCodeAndNameByUserRole());
    }

    /**
     * 考勤数据排序
     */

    /**
     * =====================================以下操作不涉及权限============================================
     */


    /**
     * 查询项目
     */
    @PostMapping("searchProject")
    public ResultVo searchProject(ProjectQuery projectQuery) {
        PageHelper.startPage(projectQuery.getPageNum() + 1, projectQuery.getPageSize());
        List<Project> projectList = projectService.searchProject(projectQuery);
        PageInfo<Project> pageInfo = new PageInfo<>(projectList);
        int presentNum = 0;
        //统计在场人数
        for (Project project : projectList) {
            //获取在场人数
            Set<String> keys = jedisKeys.keys(project.getProjectCode() + CommConstant.PRESENT + "*");
            presentNum += keys.size();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("projectList", projectList);
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        map.put("presentNum", presentNum);
        return ResultVo.success(map);
    }

    /**
     * 查询所有的项目id和名字,供用户注册使用
     */
    @GetMapping("getAllProjectCodeAndName")
    public ResultVo getAllProjectCodeAndName() {
        List<Map<String, String>> allProjectCodeAndName = projectService.findAllProjectCodeAndName();
        return ResultVo.success(allProjectCodeAndName);
    }

    /**
     * 获取首页信息
     */
    @GetMapping("/getMainPageProjectInfo")
    public ResultVo getMainPageProjectInfo() {
        return ResultVo.success(projectService.getMainPageProjectInfo());
    }
    /**
     * 合并项目信息
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



}