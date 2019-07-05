package com.real.name.project.service;

import com.real.name.project.entity.IssueDetail;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IssueDetailService {

    IssueDetail save(IssueDetail issueDetail);

    List<Integer> findIdByIssuePersonStatus(Integer issuePersonStatus);

    List<Integer> findIdByIssueImageStatus(Integer issueImageStatus);

    List<Integer> findIdByIssueStatus(Integer issuePersonStatus, Integer issueImageStatus);

    List<IssueDetail> findByCondition(String deviceId, String projectCode, Integer issuePersonStatus, Integer issueImageStatus);

    int updateIssueStatus(Integer issuePersonStatus, Integer issueImageStatus, Long issueId);

}
