package com.real.name.project.service.implement;

import com.real.name.project.entity.IssueDetail;
import com.real.name.project.service.IssueDetailService;
import com.real.name.project.service.repository.IssueDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IssueDetailServiceImpl implements IssueDetailService {

    @Autowired
    private IssueDetailRepository issueDetailRepository;

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
}
