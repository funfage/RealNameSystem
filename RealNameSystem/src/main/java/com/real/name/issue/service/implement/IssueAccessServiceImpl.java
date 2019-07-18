package com.real.name.issue.service.implement;

import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.repository.IssueAccessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueAccessServiceImpl implements IssueAccessService {

    @Autowired
    private IssueAccessMapper issueAccessMapper;

    @Override
    public int insertIssueAccess(IssueAccess issueAccess) {
        return issueAccessMapper.saveIssueAccess(issueAccess);
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