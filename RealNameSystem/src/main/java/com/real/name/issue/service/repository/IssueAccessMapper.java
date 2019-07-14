package com.real.name.issue.service.repository;

import com.real.name.issue.entity.IssueAccess;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueAccessMapper {

    public int saveIssueAccess(IssueAccess issueAccess);

}
