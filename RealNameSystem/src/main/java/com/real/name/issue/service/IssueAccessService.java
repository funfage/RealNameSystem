package com.real.name.issue.service;

import com.real.name.issue.entity.IssueAccess;

import java.util.List;

public interface IssueAccessService {

    int insertIssueAccess(IssueAccess issueAccess);

    int updateIssueAccess(IssueAccess issueAccess);

    List<IssueAccess> findIssueFailAccess();

}

