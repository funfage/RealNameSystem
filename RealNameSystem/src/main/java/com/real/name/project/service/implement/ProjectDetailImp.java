package com.real.name.project.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.HTTPTool;
import com.real.name.face.entity.Device;
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


    @SuppressWarnings("unchecked")
    @Override
    public void addPersonToProject(String projectCode, Integer teamSysNo, Integer personId) {
        // 判断是否有该人员
        Optional<Person> personOptional = personService.findById(personId);
        if (!personOptional.isPresent()) return;
        // 判断该班组中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetail1 = projectDetailRepository.findByTeamSysNoAndPersonId(teamSysNo, personId);
        if (projectDetail1.isPresent()) return;
        // 判断该项目中，是否已经添加了该人员
        Optional<ProjectDetail> projectDetailOp = projectDetailRepository.findByProjectCodeAndPersonId(projectCode, personId);
        if (projectDetailOp.isPresent()) return;
        Person person = personOptional.get();

        // 给设备发送人员信息
        String url = "person/create";
        Map<String, Object> map = new HashMap<>();
        map.put("person", personOptional.get().toJSON());
        // 根据工人类型，发送人员信息给不同的设备
        Integer workRole = person.getWorkRole();
        Map<String, Object> personMap = sendPersonInfo(workRole, url, map, projectCode);
        //给下发成功的设备添加人员照片,否则查询设备是否有该人员的信息,若无则设置下发失败的标识
        //获取所有下发设备的id
        List<String> deviceIds = (List<String>) personMap.get("deviceIds");
        //查询每个id是否下发成功
        for (String deviceId : deviceIds) {
            Device device = new Device(deviceId);
            IssueDetail issueDetail = new IssueDetail(personId, device);
            //获取该设备的返回信息
            String response = (String) personMap.get(deviceId);
            FaceResult personResult = JSONObject.parseObject(response, FaceResult.class);
            Boolean isSuccess = personResult.getSuccess();
            if (isSuccess != null && isSuccess) {//下发成功
                //下发人员照片
                Map<String, Object> imgMap = sendHeadImg(workRole, person.getPersonId().toString(), person.getHeadImage(), projectCode);
                String imgResponse = (String) imgMap.get(deviceId);
                FaceResult imgResult = JSONObject.parseObject(imgResponse, FaceResult.class);
                isSuccess = imgResult.getSuccess();
                if(isSuccess != null && isSuccess){
                    //下发失败,设置失败标识
                    issueDetail.setIssueStatus(DeviceConstant.issueSuccess);
                }else{
                    //下发成功,设置成功标识
                    issueDetail.setIssueStatus(DeviceConstant.issueFailure);
                }
                issueDetailRepository.save(issueDetail);
            }else{//下发失败
                //查询设备信息是否有人员信息
                Map<String, Object> queryMap = sendQuery(person.getPersonId().toString(), 1, 0, deviceId);
                String queryResponse = (String) queryMap.get(deviceId);
                FaceResult queryResult = JSONObject.parseObject(queryResponse, FaceResult.class);
                isSuccess = queryResult.getSuccess();
                if(isSuccess != null && isSuccess){
                    //下发成功,设置成功标识
                    issueDetail.setIssueStatus(DeviceConstant.issueSuccess);
                }else{
                    //下发失败,设置失败标识
                    issueDetail.setIssueStatus(DeviceConstant.issueFailure);
                }
                issueDetailRepository.save(issueDetail);
            }
        }
        //将项目,人员,班组信息绑定并保存
        ProjectDetail projectDetail = new ProjectDetail(projectCode, personId, teamSysNo);
        try {
            projectDetailRepository.save(projectDetail);
        } catch (Exception e) {
            logger.error("添加人员到项目失败, e:{}", e);
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

    /**
     * 向人脸识别设备发送信息
     * @param workRole 人员类型
     * @param url 下发地址
     * @param map 参数集合
     * @param projectCode 项目代码
     */
    private Map<String, Object> sendPersonInfo(Integer workRole, String url, Map<String, Object> map, String projectCode) {
        Map<String, Object> resultMap;
        if (workRole == 20) { // 如果是建筑工人，发送给该工人所属项目的设备
            resultMap = HTTPTool.sendDataToFaceDeviceByProjectCode(url, map, projectCode, DeviceConstant.postMethod);
        } else if (workRole == 10){ // 如果是管理工人，发送给全部设备
            resultMap = HTTPTool.sendDataToFaceDevice(url, map, DeviceConstant.postMethod);
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
     * @param projectCode 项目代码
     */
    private Map<String, Object> sendHeadImg(Integer workRole, String personId, String headImg, String projectCode) {
        String url = "face/create";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        map.put("imgBase64", headImg);
        return sendPersonInfo(workRole, url, map, projectCode);
    }

    /**
     * 查询指定 id 的人员信息
     * 若peronId 传入-1 表示不局限于 personId 查询人员
     * @param personId 人员id
     * @param size 每页最大数量
     * @param index 页码
     */
    private Map<String, Object> sendQuery(String personId, Integer size, Integer index, String deviceId) {
        String url = "/person/findByPage";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        map.put("length", size);
        map.put("index", index);
        return HTTPTool.sendDataToFaceDeviceByDeviceId(url, map, deviceId, DeviceConstant.getMethod);
    }

}
