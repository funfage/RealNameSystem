package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.face.entity.Device;
import com.real.name.project.entity.IssueDetail;
import com.real.name.project.service.repository.IssueDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IssueDetailTest extends BaseTest {

    @Autowired
    private IssueDetailRepository repository;

    @Test
    public void addIssueDetail() {
        IssueDetail issueDetail = new IssueDetail();
        issueDetail.setPersonId(77);
        issueDetail.setIssueStatus(0);
        Device device = new Device();
        device.setDeviceId("E0F2D4B761E53DF9F8");
        issueDetail.setDevice(device);
        repository.save(issueDetail);
    }

    @Test
    public void findIssueDetailTest() {
        List<IssueDetail> issueDetails = repository.findAll();
        System.out.println(issueDetails);
    }

}
