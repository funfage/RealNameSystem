package com.real.name.issue.service;

import com.real.name.device.entity.Device;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.person.entity.Person;

import java.util.List;

public interface IssueAccessService {

    /**
     * 重发
     */
    void resend(Person person, Device device);

    /**
     * 插入下发标识
     */
    int insertIssueAccess(IssueAccess issueAccess);

    /**
     * 更新下发标识
     */
    int updateIssueAccess(IssueAccess issueAccess);

    /**
     * 获取所有下发失败的信息
     */
    List<IssueAccess> findIssueFailAccess();

}

