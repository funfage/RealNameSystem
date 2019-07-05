package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.device.entity.Device;
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
        issueDetail.setIssuePersonStatus(0);
        issueDetail.setIssueImageStatus(0);
        issueDetail.setProjectCode("44010620190510008");
        Device device = new Device();
        device.setDeviceId("6667");
        issueDetail.setDevice(device);
        repository.save(issueDetail);
    }

    @Test
    public void findIssueDetailTest() {
        List<IssueDetail> issueDetails = repository.findAll();
        System.out.println(issueDetails);
    }

}
