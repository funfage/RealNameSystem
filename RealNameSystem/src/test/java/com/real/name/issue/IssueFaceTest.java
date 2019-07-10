package com.real.name.issue;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.device.entity.Device;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.issue.service.repository.IssueFaceMapper;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.notification.Failure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryConfigurationUtils;

import java.util.List;

public class IssueFaceTest extends BaseTest {

    @Autowired
    IssueFaceMapper repository;

    @Autowired
    IssueFaceService issueFaceService;


    @Test
    public void findAllFaceIssueImageFailure() {
        List<IssueFace> allFaceIssueImageFailure = repository.findAllFaceIssueImageFailure();
        System.out.println(allFaceIssueImageFailure);
    }

    @Test
    public void updateByPersonId() {
        IssueFace issueFace = new IssueFace();
        Person person = new Person();
        Device device = new Device();
        device.setDeviceId("6669");
        person.setPersonId(100);
        issueFace.setPerson(person);
        issueFace.setIssueImageStatus(1);
        issueFace.setIssuePersonStatus(1);
        issueFace.setDevice(device);
        int effectNum = repository.updateByPersonIdAndDeviceId(issueFace);
        Assert.assertEquals(effectNum, 1);
    }

    @Test
    public void updateByPersonIdService() {
        IssueFace issueFace = new IssueFace();
        Person person = new Person();
        Device device = new Device();
        device.setDeviceId("6669");
        person.setPersonId(100);
        issueFace.setPerson(person);
        issueFace.setIssueImageStatus(0);
        issueFace.setIssuePersonStatus(1);
        issueFace.setDevice(device);
        issueFaceService.updateByPersonIdAndDeviceId(issueFace);
    }

    @Test
    public void insertInitIssueTest() {
        IssueFace issueFace = new IssueFace();
        Person person = new Person();
        person.setPersonId(100);
        issueFace.setPerson(person);
        Device device = new Device();
        device.setDeviceId("6669");
        issueFace.setDevice(device);
        issueFace.setIssuePersonStatus(1);
        issueFace.setIssueImageStatus(1);
        int i = repository.insertInitIssue(issueFace);
        System.out.println(i);
    }

    @Test
    public void findAllFailDeviceIdsTest() {
        List<String> allFailDeviceIds = repository.findAllFailDeviceIds();
        System.out.println(allFailDeviceIds);
    }

    @Test
    public void findIssueFailPersonInfoByProjectCode() {
        Page<IssueFace> pageIssue = PageHelper.startPage(1, 2);
        List<IssueFace> failure = repository.findIssueFailPersonInfoByProjectCode("44010620190510008");
        PageInfo<IssueFace> issueInfo = new PageInfo<>(failure);
        for(IssueFace issueFace : failure)
            System.out.println(issueFace);
        int pageNum = issueInfo.getPageNum();
        long total = issueInfo.getTotal();
        int nextPage = issueInfo.getNextPage();
        System.out.println(failure);
    }

    //
    @Test
    public void findIssueFailPersonInfoByDeviceId() {
        PageHelper.startPage(1, 3);
        List<IssueFace> failure = repository.findIssueFailPersonInfoByDeviceId("6670");
        PageInfo<IssueFace> issueInfo = new PageInfo<>(failure);
        int pageNum = issueInfo.getPageNum();
        long total = issueInfo.getTotal();
        System.out.println(failure);
    }

    @Test
    public void findAllFailDeviceIdsByProjectCode() {
        List<String> fail = repository.findAllFailDeviceIdsByProjectCode("44010620190510008");
        System.out.println(fail);
    }

}