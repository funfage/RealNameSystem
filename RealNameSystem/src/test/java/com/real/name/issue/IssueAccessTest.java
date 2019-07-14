package com.real.name.issue;

import com.real.name.device.entity.Device;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.repository.IssueAccessMapper;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IssueAccessTest extends BaseTest {

    @Autowired
    private IssueAccessMapper issueAccessMapper;

    @Test
    public void saveIssueAccess() {
        IssueAccess issueAccess = new IssueAccess();
        issueAccess.setDevice(new Device("223000123"));
        issueAccess.setPerson(new Person(1131));
        int i = issueAccessMapper.saveIssueAccess(issueAccess);
        System.out.println(i);
    }
}
