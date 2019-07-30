package com.real.name.issue.service.implement;

import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.entity.IssuePersonStatus;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.issue.service.repository.IssueFaceMapper;
import com.real.name.person.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueFaceServiceImpl implements IssueFaceService {

    private Logger logger = LoggerFactory.getLogger(IssueFaceServiceImpl.class);

    @Autowired
    private IssueFaceMapper mapper;

    @Override
    public void resend(int issuePersonStatus, int issueImageStatus, Person person, Device device) {
        if (person != null) {
            if (issuePersonStatus == 0) {
                //表示下发人员失败
                FaceDeviceUtils.issuePersonToOneDevice(device, person, 1);
            } else if (issuePersonStatus == 1 && issueImageStatus == 0) {
                //表示下发照片失败
                FaceDeviceUtils.issueImageToOneDevice(device, person, 1);
            }
        }
    }

    @Override
    public int updateByPersonIdAndDeviceId(IssueFace issueFace) {
        try {
            return mapper.updateByPersonIdAndDeviceId(issueFace);
        } catch (Exception e) {
            logger.error("issueFaceServiceImpl updateByPersonId error e:", e.getMessage());
            return -1;
        }
    }

    @Override
    public int insertInitIssue(IssueFace issueFace) {
        try {
            return mapper.insertInitIssue(issueFace);
        } catch (Exception e) {
            logger.error("insertInitIssue error, e:", e.getMessage());
            return -1;
        }
    }

    @Override
    public List<String> findAllFailDeviceIds() {
        List<String> failDeviceIds = mapper.findAllFailDeviceIds();
        //去除集合中重复的元素
        return failDeviceIds.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> findAllFailDeviceIdsByProjectCode(String projectCode) {
        List<String> failProjectDeviceIds = mapper.findAllFailDeviceIdsByProjectCode(projectCode);
        return failProjectDeviceIds.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<IssueFace> findIssueFailPersonInfoByProjectCode(String projectCode) {
        return mapper.findIssueFailPersonInfoByProjectCode(projectCode);
    }

    @Override
    public List<IssueFace> findIssueFailPersonInfoByDeviceId(String deviceId) {
        return mapper.findIssueFailPersonInfoByDeviceId(deviceId);
    }

    @Override
    public List<IssueFace> findIssueFailPersonByPersonId(Integer personId) {
        return mapper.findIssueFailPersonByPersonId(personId);
    }

    @Override
    public List<IssueFace> findUpdateFailPersonByWorkRole(Integer workRole) {
        return mapper.findUpdateFailPersonByWorkRole(workRole);
    }
}
