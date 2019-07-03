package com.real.name.project.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.HTTPTool;
import com.real.name.face.entity.Device;
import com.real.name.face.service.repository.DeviceRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.*;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.repository.IssueDetailRepository;
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
    public void addPersonToDevice(String projectCode, Integer personId, Person person, List<Device> projectDevice, List<Device> allDevices) {
        // 给设备发送人员信息
        String url = "person/create";
        Map<String, Object> map = new HashMap<>();
        map.put("person", person.toJSON());
        // 根据工人类型，发送人员信息给不同的设备
        Integer workRole = person.getWorkRole();
        //根据员工类型下发到设备
        Map<String, Object> personMap = sendPersonInfo(workRole, url, map, projectDevice, allDevices);
        //给下发成功的设备添加人员照片,否则查询设备是否有该人员的信息,若无则设置下发失败的标识
        //获取所有下发设备的id
        List<String> deviceIds = (List<String>) personMap.get("deviceIds");
        //查询每个id是否下发成功
        for (String deviceId : deviceIds) {
            Boolean isSuccess;
            //创建下发信息
            IssueDetail issueDetail = new IssueDetail(personId, new Device(deviceId), projectCode);
            //获取指定设备的返回信息
            String response = (String) personMap.get(deviceId);
            //接收参数为空或者请求超时
            if (!StringUtils.hasText(response) || response.equals(DeviceConstant.connectTimeOut)) {
                logger.error("获取设备响应信息：{}失败", response);
                //查询设备信息
                Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
                if (optionalDevice.isPresent()) {
                    //查询设备信息是否有人员信息
                    Map<String, Object> queryMap = sendQueryPerson(person.getPersonId().toString(), 1, 0, optionalDevice.get());
                    String queryResponse = (String) queryMap.get(deviceId);
                    //接收参数为空或者请求超时
                    if (!StringUtils.hasText(queryResponse) || queryResponse.equals(DeviceConstant.connectTimeOut)) {
                        logger.error("获取设备响应queryResponse信息：{}失败", response);
                        //人员下发失败,设置失败标识
                        issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                        issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
                    } else {
                        FaceResult queryResult = JSONObject.parseObject(queryResponse, FaceResult.class);
                        isSuccess = queryResult.getSuccess();
                        if (isSuccess != null && isSuccess) {
                            //人员下发成功,设置成功标识
                            issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                            //下发照片
                            issueImage(workRole, projectCode, deviceId, person, issueDetail, projectDevice, allDevices);
                        } else {
                            //人员下发失败,设置失败标识
                            issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
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
                    //下发照片
                    issueImage(workRole, projectCode, deviceId, person, issueDetail, projectDevice, allDevices);
                } else {  //下发失败
                    //查询设备信息
                    Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
                    if (optionalDevice.isPresent()) {
                        //查询设备信息是否有人员信息
                        Map<String, Object> queryMap = sendQueryPerson(person.getPersonId().toString(), 1, 0, optionalDevice.get());
                        String queryResponse = (String) queryMap.get(deviceId);
                        //接收参数为空或者请求超时
                        if (!StringUtils.hasText(queryResponse) || queryResponse.equals(DeviceConstant.connectTimeOut)) {
                            logger.error("获取设备响应queryResponse信息：{}失败", response);
                            //人员下发失败,设置失败标识
                            issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
                        } else {
                            FaceResult queryResult = JSONObject.parseObject(queryResponse, FaceResult.class);
                            isSuccess = queryResult.getSuccess();
                            if (isSuccess != null && isSuccess) {
                                //人员下发成功,设置成功标识
                                issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonSuccess);
                                //下发照片
                                issueImage(workRole, projectCode, deviceId, person, issueDetail, projectDevice, allDevices);
                            } else {
                                //人员下发失败,设置失败标识
                                issueDetail.setIssuePersonStatus(DeviceConstant.issuePersonFailure);
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
            //保存下发信息到数据库
            //查询数据库中是否已经存有下发信息
            List<IssueDetail> issueDetailList = issueDetailRepository.findByCondition(personId, deviceId, projectCode);
            List<Long> issueDeIntegers = new ArrayList<>();
            if (issueDetailList.size() > 0) {
                for (int i = 0; i < issueDetailList.size(); i++) {
                    //若有则更新第一条信息
                    if (i == 0) {
                        IssueDetail select = issueDetailList.get(0);
                        select.setIssueImageStatus(issueDetail.getIssueImageStatus());
                        select.setPersonId(issueDetail.getPersonId());
                        select.setProjectCode(issueDetail.getProjectCode());
                        select.setDevice(issueDetail.getDevice());
                        select.setIssuePersonStatus(issueDetail.getIssuePersonStatus());
                        issueDetailRepository.save(select);
                    } else {
                        issueDeIntegers.add(issueDetailList.get(i).getIssueId());
                    }
                }
                if (issueDeIntegers.size() > 0) {
                    //删除其他多余的信息
                    issueDetailRepository.deleteByIssueIdIn(issueDeIntegers);
                }
            } else {
                //保存信息
                issueDetailRepository.save(issueDetail);
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

    /**
     * 下发照片
     * @param workRole
     * @param projectCode
     * @param deviceId
     * @param person
     * @param issueDetail
     * @return
     */
    private Boolean issueImage(Integer workRole, String projectCode, String deviceId, Person person, IssueDetail issueDetail, List<Device> projectDevice, List<Device> allDevices) {
        Boolean isSuccess;
        //下发人员照片
        Map<String, Object> imgMap = sendHeadImg(workRole, person.getPersonId().toString(), person.getHeadImage(), projectDevice, allDevices);
        String imgResponse = (String) imgMap.get(deviceId);
        //接收参数为空或者请求超时
        if (!StringUtils.hasText(imgResponse) || imgResponse.equals(imgResponse)) {
            //照片下发失败,设置失败标识
            issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
            isSuccess = false;
        }else {
            FaceResult imgResult = JSONObject.parseObject(imgResponse, FaceResult.class);
            isSuccess = imgResult.getSuccess();
            if(isSuccess != null && isSuccess){
                //照片下发成功,设置成功标识
                issueDetail.setIssueImageStatus(DeviceConstant.issueImageSuccess);
            }else{
                //照片下发失败,设置失败标识
                issueDetail.setIssueImageStatus(DeviceConstant.issueImageFailure);
            }
        }
        return isSuccess;
    }



    /**
     * 向人脸识别设备发送信息
     * @param workRole 人员类型
     * @param url 下发地址
     * @param map 参数集合
     */
    private Map<String, Object> sendPersonInfo(Integer workRole, String url, Map<String, Object> map, List<Device> projectDevice, List<Device> allDevices) {
        Map<String, Object> resultMap;
        if (workRole == 20) { // 如果是建筑工人，发送给该工人所属项目的设备
            resultMap = HTTPTool.sendDataToFaceDeviceByProjectCode(url, map, DeviceConstant.postMethod, projectDevice);
        } else if (workRole == 10){ // 如果是管理工人，发送给全部设备
            resultMap = HTTPTool.sendDataToFaceDevice(url, map, DeviceConstant.postMethod, allDevices);
        } else {
            throw new AttendanceException(ResultError.PERSON_EMPTY);
        }
        return resultMap;
    }

    /**
     * 下发人员照片
     * @param workRole 人员类型
     * @param personId 人员id
     * @param headImg 图片base64编码
     */
    private Map<String, Object> sendHeadImg(Integer workRole, String personId, String headImg, List<Device> projectDevice, List<Device> allDevices) {
        String url = "face/create";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        map.put("imgBase64", headImg);
        return sendPersonInfo(workRole, url, map, projectDevice, allDevices);
    }

    /**
     * 查询指定 id 的人员信息
     * 若peronId 传入-1 表示不局限于 personId 查询人员
     * @param personId 人员id
     * @param length 每页最大数量
     * @param index 页码
     */
    private Map<String, Object> sendQueryPerson(String personId, Integer length, Integer index, Device device) {
        String url = "/person/findByPage?pass={pass}&personId={personId}&length={length}&index={index}";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        map.put("length", length);
        map.put("index", index);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, DeviceConstant.getMethod, device);
    }

}
