package com.real.name.project.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.info.DeviceConstant;
import com.real.name.device.DeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.issue.entity.IssueDetail;
import com.real.name.issue.entity.FaceResult;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.*;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.issue.service.repository.IssueDetailRepository;
import com.real.name.project.service.repository.ProjectDetailRepository;
import com.real.name.project.service.repository.ProjectPersonDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ProjectDetailImp implements ProjectDetailService {
    private Logger logger = LoggerFactory.getLogger(ProjectDetailImp.class);

    @Autowired
    private ProjectDetailRepository projectDetailRepository;

    @Autowired
    private ProjectPersonDetailRepository projectPersonDetailRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private IssueDetailRepository issueDetailRepository;

    @Autowired
    private DeviceRepository deviceRepository;


    @Override
    public ProjectDetail save(ProjectDetail projectDetail) {
        return projectDetailRepository.save(projectDetail);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addPersonToDevice(String projectCode, Person person, List<Device> projectDevice, List<Device> allDevices) {
        //根据员工类型下发到设备
        Map<String, Object> personMap = DeviceUtils.sendPersonInfo(person, projectDevice, allDevices);
        //给下发成功的设备添加人员照片,否则查询设备是否有该人员的信息,若无则设置下发失败的标识
        //获取所下发设备的id;集合
        List<String> deviceIds = (List<String>) personMap.get("deviceIds");
        //遍历每个设备,查询是否下发成功
        for (String deviceId : deviceIds) {
            Boolean isSuccess;
            //创建下发信息
            IssueDetail issueDetail = new IssueDetail();
            //查询设备信息
            Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
            //获取指定设备的返回信息
            String response = (String) personMap.get(deviceId);
            //接收参数为空或者请求超时
            if (!StringUtils.hasText(response) || response.equals(DeviceConstant.connectTimeOut)) {
                logger.error("获取设备响应信息失败：{}", response);
                if (optionalDevice.isPresent()) {
                    //查询设备信息是否有人员信息
                    Boolean querySuccess = DeviceUtils.queryPersonByDeviceId(optionalDevice.get(), person);
                    if (!querySuccess) {
                        //人员下发失败,设置失败标识
                        issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    } else {
                        //人员下发成功设置成功标识
                        issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                        //向指定的设备下发照片，issueImgSuccess为true则下发成功
                        boolean issueImgSuccess = DeviceUtils.issueAndQueryImageByDeviceId(optionalDevice.get(), person);
                        //设置照片下发成功与否标识
                        if (issueImgSuccess) {
                            issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                        }else{
                            issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                        }
                    }
                } else {
                    //设备不存在,设置下发失败标识
                    issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                    issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                }
            } else {
                FaceResult personResult = JSONObject.parseObject(response, FaceResult.class);
                isSuccess = personResult.getSuccess();
                if (isSuccess != null && isSuccess) { //下发成功
                    //人员下发成功,设置成功标识
                    issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                    //向指定的设备下发照片，issueImgSuccess为true则下发成功
                    boolean issueImgSuccess = DeviceUtils.issueAndQueryImageByDeviceId(optionalDevice.get(), person);
                    //设置照片下发成功与否标识
                    if (issueImgSuccess) {
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                    }else{
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    }
                } else {  //下发失败
                    if (optionalDevice.isPresent()) {
                        //查询设备信息是否有人员信息
                        Boolean querySuccess = DeviceUtils.queryPersonByDeviceId(optionalDevice.get(), person);
                        if (!querySuccess) {
                            //查询设备信息是否有人员信息
                            issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                            issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                        } else {
                            //人员下发成功设置成功标识
                            issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                            //向指定的设备下发照片，issueImgSuccess为true则下发成功
                            boolean issueImgSuccess = DeviceUtils.issueAndQueryImageByDeviceId(optionalDevice.get(), person);
                            if (issueImgSuccess) {
                                issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
                            }else{
                                issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                            }
                        }
                    } else {
                        //设备信息不存在设置下发失败标识
                        issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    }
                }
            }
            //修改数据库中的下发状态
            try {
                issueDetailRepository.updateByPersonIdAndDeviceId(issueDetail.getIssuePersonStatus(), issueDetail.getIssueImageStatus(), person.getPersonId(), deviceId);
            } catch (Exception e) {
                logger.error("修改数据库中的下发状态失败", e.getMessage());
            }
        }
    }

    @Override
    public Map<String, Object> getPersonInProject(String  projectCode, Pageable pageable) {
        Page<ProjectDetail> projectDetails = projectDetailRepository.findByProjectCode(projectCode, pageable);
        List<Integer> personIds = new ArrayList<>();

        for (ProjectDetail projectDetail : projectDetails) {
            personIds.add(projectDetail.getPersonId());
        }
        Map<String , Object>map = new HashMap<>();
        map.put("personList", personService.findPersons2(personIds));
        map.put("totalPages", projectDetails.getTotalPages());
        map.put("totalElements", projectDetails.getTotalElements());
        return map;
    }

    @Override
    public Project getProjectFromPersonId(Integer personId) {
        Optional<ProjectPersonDetail> detail = projectPersonDetailRepository.findByPerson_PersonId(personId);
        if (!detail.isPresent()) {
            return null;
        }
        return detail.get().getProject();
    }

    @Override
    public Optional<ProjectDetail> findByTeamSysNoAndPersonId(Integer teamSysNo, Integer personId) {
        return projectDetailRepository.findByTeamSysNoAndPersonId(teamSysNo, personId);
    }

    @Override
    public Optional<ProjectDetail> findByProjectCodeAndPersonId(String projectId, Integer personId) {
        return projectDetailRepository.findByProjectCodeAndPersonId(projectId, personId);
    }

    @Override
    public Optional<ProjectDetail> findByProjectCodeAndPersonIdAndTeamSysNo(String projectId, Integer personId, Integer teamSysNo) {
        return projectDetailRepository.findByProjectCodeAndPersonIdAndTeamSysNo(projectId, personId, teamSysNo);
    }

}
