package com.real.name.issue.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.device.entity.Device;
import com.real.name.device.service.AccessService;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.repository.IssueAccessMapper;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueAccessServiceImpl implements IssueAccessService {

    @Autowired
    private IssueAccessMapper issueAccessMapper;

    @Autowired
    private AccessService accessService;

    @Override
    public void resend(Person person, Device device) {
        accessService.addAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
    }

    @Override
    public void insertIssueAccess(IssueAccess issueAccess) {
        int i = issueAccessMapper.saveIssueAccess(issueAccess);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Override
    public int deleteStatusByPersonInDevice(Integer personId, String deviceId) {
        return issueAccessMapper.deleteStatusByPersonInDevice(personId, deviceId);
    }

    @Override
    public int updateIssueAccess(IssueAccess issueAccess) {
        return issueAccessMapper.updateIssueAccess(issueAccess);
    }

    @Override
    public List<IssueAccess> findIssueFailAccess() {
        return issueAccessMapper.findIssueFailAccess();
    }
}
