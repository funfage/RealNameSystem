package com.real.name.project.service.implement;

import com.real.name.project.entity.IssueDetail;
import com.real.name.project.service.IssueDetailService;
import com.real.name.project.service.repository.IssueDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class IssueDetailServiceImpl implements IssueDetailService {

    @Autowired
    private IssueDetailRepository issueDetailRepository;

    @Override
    public IssueDetail save(IssueDetail issueDetail) {
        return issueDetailRepository.save(issueDetail);
    }
}
