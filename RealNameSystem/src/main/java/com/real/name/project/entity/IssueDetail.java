package com.real.name.project.entity;

import com.real.name.face.entity.Device;

import javax.persistence.*;

@Entity
public class IssueDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    private Integer personId;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    private Device device;

    //下发成功标识 0失败 1成功
    private Integer issueStatus;

    public IssueDetail() {

    }

    public IssueDetail(Integer personId, Device device, Integer issueStatus) {
        this.personId = personId;
        this.device = device;
        this.issueStatus = issueStatus;
    }

    public IssueDetail(Integer personId, Device device) {
        this.personId = personId;
        this.device = device;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Integer getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }
}
