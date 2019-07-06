package com.real.name.issue.service.implement;

import com.real.name.issue.entity.IssueDetail;
import com.real.name.issue.service.IssueDetailService;
import com.real.name.issue.service.repository.IssueDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueDetailServiceImpl implements IssueDetailService {

    @Autowired
    private IssueDetailRepository issueDetailRepository;

    @Override
    public Optional<IssueDetail> findByDevice_DeviceIdAndPersonId(String deviceId, Integer personId) {
        return issueDetailRepository.findByDevice_DeviceIdAndPersonId(deviceId, personId);
    }

    @Override
    public IssueDetail save(IssueDetail issueDetail) {
        return issueDetailRepository.save(issueDetail);
    }

    @Override
    public List<Integer> findIdByIssuePersonStatus(Integer issuePersonStatus) {
        return issueDetailRepository.findIdByIssuePersonStatus(issuePersonStatus);
    }

    @Override
    public List<Integer> findIdByIssueImageStatus(Integer issueImageStatus) {
        return issueDetailRepository.findIdByIssueImageStatus(issueImageStatus);
    }

    @Override
    public List<Integer> findIdByIssueStatus(Integer issuePersonStatus, Integer issueImageStatus) {
        return issueDetailRepository.findIdByIssueStatus(issuePersonStatus, issueImageStatus);
    }

    @Override
    public List<IssueDetail> findByCondition(String deviceId, String projectCode, Integer issuePersonStatus, Integer issueImageStatus) {
        return issueDetailRepository.findByCondition(deviceId, projectCode, issuePersonStatus, issueImageStatus);
    }

    @Override
    public int updateIssueStatus(Integer issuePersonStatus, Integer issueImageStatus, Long issueId) {
        return issueDetailRepository.updateIssueStatus(issuePersonStatus, issueImageStatus, issueId);
    }

    @Override
    public Page<Integer> findFailureIssue(String projectCode, String deviceId, Pageable pageable) {
        return issueDetailRepository.findFailureIssue(projectCode, deviceId, pageable);
    }


}
