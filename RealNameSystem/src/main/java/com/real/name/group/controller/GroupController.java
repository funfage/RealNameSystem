package com.real.name.group.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.CommonUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.person.entity.Person;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.service.SubContractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    private Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private SubContractorService subContractorService;


    /**
     * 创建班组
     */
    @PostMapping("/createWorkerGroup")
    public Object createWorkerGroup(WorkerGroup group) {
        verify(group);
        //查询参建单位信息
        SubContractor subContractor = subContractorService.findById(group.getSubContractorId());
        //判断是否选择的是管理员班组
        if (group.getIsAdminGroup() != null && group.getIsAdminGroup() == 1) {
            //从数据库中查询是否存在管理员班组
            if (groupService.judgeExistAdminGroupByProjectCode(group.getProjectCode())) {
                throw new AttendanceException(ResultError.ADMIN_GROUP_ERROR);
            }
        }
        //设置临时的班组编码
        group.setTeamSysNo((int) (System.currentTimeMillis() / 1000));
        group.setCorpCode(subContractor.getCorpCode());
        group.setCorpName(subContractor.getCorpName());
        groupService.create(group);
        return ResultVo.success(group);
    }

    /**
     * 修改班组信息
     */
    @PostMapping("updateWorkerGroup")
    public ResultVo updateWorkerGroup(@RequestBody WorkerGroup workerGroup) {
        if (workerGroup.getTeamSysNo() == null) {
            throw AttendanceException.emptyMessage("班组编号");
        }
        //查询该班组是否存在
        Optional<WorkerGroup> groupOptional = groupService.findById(workerGroup.getTeamSysNo());
        if (!groupOptional.isPresent()) {
            throw new AttendanceException(ResultError.GROUP_NOT_EXIST);
        }
        //判断是否选择的是管理员班组
        if (workerGroup.getIsAdminGroup() != null && workerGroup.getIsAdminGroup() == 1) {
            //从数据库中查询是否存在管理员班组
            if (groupService.judgeExistAdminGroupByProjectCode(workerGroup.getProjectCode())) {
                throw new AttendanceException(ResultError.ADMIN_GROUP_ERROR);
            }
        }
        WorkerGroup selectWorkerGroup = groupOptional.get();
        //合并信息
        mergeWorkerGroup(selectWorkerGroup, workerGroup);
        //修改本地班组信息
        WorkerGroup updateGroup = groupService.updateByTeamSysNo(selectWorkerGroup);
        if (updateGroup == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
        return ResultVo.success();
    }

    /**
     * 删除班组信息
     */
    @GetMapping("deleteWorkerGroup")
    public ResultVo deleteWorkerGroup(@RequestParam("teamSysNo") Integer teamSysNo) {
        if (teamSysNo <= 0) {
            throw AttendanceException.errorMessage("班组编号");
        }
        //获取该班组的项目id
        String projectCode = projectDetailQueryService.findProjectCodeByTeamSysNo(teamSysNo);
        //获取该项目绑定的设备信息
        List<Device> deviceList = deviceService.findByProjectCode(projectCode);
        //获取该班组下的人员信息
        List<Person> personList = projectDetailQueryService.findPersonByTeamSyNo(teamSysNo);
        //删除每个人员在该班组绑定设备的信息
        for (Person person : personList) {
            if (person.getWorkRole() == 20) {//不是管理员的人才需要把设备上的信息删除
                deviceService.deletePersonInDeviceList(deviceList, person);
            }
        }
        return ResultVo.success("删除班组成功");
    }

    /**
     * 移除班组
     */
    @GetMapping("/removeGroupInProject")
    public ResultVo removeGroupInProject(@RequestParam("teamSysNo") Integer teamSysNo,
                                         @RequestParam("projectCode") String projectCode) {
        groupService.removeGroupInProject(teamSysNo, projectCode);
        return ResultVo.success();
    }

    /**
     * ========================================以下只与查询有关===============================================
     */

    /**
     * 查询班组
     */
    @GetMapping("find")
    public Object find(@RequestParam(name = "teamSysNo", required = false) Integer teamSysNo,
                       @RequestParam(name = "projectCode", required = false) String projectCode) {
        if (teamSysNo == null && projectCode == null) {
            return ResultVo.success(groupService.findAll());
        } else if (teamSysNo != null) {
            return ResultVo.success(groupService.findById(teamSysNo));
        } else if (StringUtils.hasText(projectCode)) {
            return ResultVo.success(groupService.findByProjectCode(projectCode));
        }
        return ResultVo.failure(ResultError.PARAM_ERROR);
    }

    /**
     * 搜索班组
     */
    @PostMapping("/searchGroup")
    public ResultVo searchGroup(GroupQuery groupQuery) {
        PageHelper.startPage(groupQuery.getPageNum() + 1, groupQuery.getPageSize());
        List<WorkerGroup> workerGroups = groupService.searchGroup(groupQuery);
        PageInfo<WorkerGroup> pageInfo = new PageInfo<>(workerGroups);
        Map<String, Object> map = new HashMap<>();
        map.put("workerGroups", workerGroups);
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        return ResultVo.success(map);
    }

    /**
     * 校验班组信息是否正确
     */
    private void verify(WorkerGroup workerGroup) {
        if (!StringUtils.hasText(workerGroup.getProjectCode())) {
            throw AttendanceException.emptyMessage("项目编码");
        } else if (projectService.judgeEmptyByProjectCode(workerGroup.getProjectCode())) {
            throw AttendanceException.errorMessage("项目编码");
        } else if (workerGroup.getSubContractorId() == null) {
            throw AttendanceException.emptyMessage("参建单位");
        } else if (subContractorService.judgeEmptyById(workerGroup.getSubContractorId())) {
            throw AttendanceException.errorMessage("参建单位");
        } else if (!StringUtils.hasText(workerGroup.getTeamName())) {
            throw AttendanceException.emptyMessage("班组名称");
        } else if (groupService.judgeExistByTeamNameAndSubContractor(workerGroup.getTeamName(), workerGroup.getSubContractorId())) {
            throw new AttendanceException(ResultError.GROUP_EXIST);
        } else if (StringUtils.hasText(workerGroup.getResponsiblePersonPhone())) {
            // 如果有手机号，判断手机号是否正确
            if (!CommonUtils.isRightPhone(workerGroup.getResponsiblePersonPhone())) {
                throw new AttendanceException(ResultError.PHONE_ERROR);
            }
        }
    }

    /**
     * 合并班组信息
     */
    private void mergeWorkerGroup(WorkerGroup selectWorkerGroup, WorkerGroup workerGroup) {
        if (workerGroup.getSubContractorId() != null) {
            //判断班组是否已经被移除,如果已经被移除则不能修改参建单位信息
            if (selectWorkerGroup.getGroupStatus() == 1) {
                throw new AttendanceException(ResultError.GROUP_REMOVE_UPDATE_ERROR);
            } else if (subContractorService.judgeEmptyById(workerGroup.getSubContractorId())) {
                throw AttendanceException.errorMessage("参建单位");
            } else {
                SubContractor subContractor = subContractorService.findById(workerGroup.getSubContractorId());
                selectWorkerGroup.setSubContractorId(workerGroup.getSubContractorId());
                selectWorkerGroup.setCorpCode(subContractor.getCorpCode());
                selectWorkerGroup.setCorpName(subContractor.getCorpName());
            }
        }
        if (StringUtils.hasText(workerGroup.getResponsiblePersonName())) {
            selectWorkerGroup.setResponsiblePersonName(workerGroup.getResponsiblePersonName());
        }
        if (StringUtils.hasText(workerGroup.getResponsiblePersonPhone())) {
            if (!CommonUtils.isRightPhone(workerGroup.getResponsiblePersonPhone())) {
                throw AttendanceException.errorMessage("电话号码");
            }
            selectWorkerGroup.setResponsiblePersonPhone(workerGroup.getResponsiblePersonPhone());
        }
        if (workerGroup.getResponsiblePersonIdCardType() != null) {
            if (workerGroup.getResponsiblePersonIdCardType() != 99 && (workerGroup.getResponsiblePersonIdCardType() <= 0 || workerGroup.getResponsiblePersonIdCardType() >= 26)) {
                throw AttendanceException.errorMessage("责任人证件类型");
            }
            selectWorkerGroup.setResponsiblePersonIdCardType(workerGroup.getResponsiblePersonIdCardType());
        }
        if (StringUtils.hasText(workerGroup.getResponsiblePersonIdNumber())) {
            selectWorkerGroup.setResponsiblePersonIdNumber(workerGroup.getResponsiblePersonIdNumber());
        }
        if (StringUtils.hasText(workerGroup.getRemark())) {
            selectWorkerGroup.setRemark(workerGroup.getRemark());
        }
        if (StringUtils.hasText(workerGroup.getCorpName())) {
            selectWorkerGroup.setCorpName(workerGroup.getCorpName());
        }
        if (StringUtils.hasText(workerGroup.getCorpCode())) {
            selectWorkerGroup.setCorpCode(workerGroup.getCorpCode());
        }
        if (workerGroup.getIsAdminGroup() != null) {
            selectWorkerGroup.setIsAdminGroup(workerGroup.getIsAdminGroup());
        }
    }
}