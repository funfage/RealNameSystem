package com.real.name.issue.service;

import com.real.name.issue.entity.IssueDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface IssueDetailService {

    Optional<IssueDetail> findByDevice_DeviceIdAndPersonId(String deviceId, Integer personId);

    IssueDetail save(IssueDetail issueDetail);

    List<Integer> findIdByIssuePersonStatus(Integer issuePersonStatus);

    List<Integer> findIdByIssueImageStatus(Integer issueImageStatus);

    List<Integer> findIdByIssueStatus(Integer issuePersonStatus, Integer issueImageStatus);

    List<IssueDetail> findByCondition(String deviceId, String projectCode, Integer issuePersonStatus, Integer issueImageStatus);

    int updateIssueStatus(Integer issuePersonStatus, Integer issueImageStatus, Long issueId);

    Page<Integer> findFailureIssue(String projectCode, String deviceId, Pageable pageable);

}
