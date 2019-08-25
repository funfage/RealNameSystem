package com.real.name.issue.service.repository;

import com.real.name.issue.entity.IssueAccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IssueAccessMapper {

    int saveIssueAccess(IssueAccess issueAccess);

    int updateIssueAccess(IssueAccess issueAccess);

    int deleteStatusByPersonInDevice(@Param("personId") Integer personId,
                                     @Param("deviceId") String deviceId);

    List<IssueAccess> findIssueFailAccess();

}
