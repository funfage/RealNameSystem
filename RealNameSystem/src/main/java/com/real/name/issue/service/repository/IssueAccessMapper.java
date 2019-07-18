package com.real.name.issue.service.repository;

import com.real.name.issue.entity.IssueAccess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IssueAccessMapper {

    int saveIssueAccess(IssueAccess issueAccess);

    int updateIssueAccess(IssueAccess issueAccess);

    List<IssueAccess> findIssueFailAccess();

}
