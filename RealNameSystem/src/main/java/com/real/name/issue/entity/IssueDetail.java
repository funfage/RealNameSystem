package com.real.name.issue.entity;

import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
public class IssueDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    private Integer personId;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    private Device device;

    private String projectCode;

    //人员下发成功标识 0失败 1成功
    private Integer issuePersonStatus;

    //照片下发成功标识 0失败 1成功
    private Integer issueImageStatus;

    public IssueDetail() {

    }

    public IssueDetail(Integer personId, Device device, String projectCode) {
        this.personId = personId;
        this.device = device;
        this.projectCode = projectCode;
    }

    public IssueDetail(Integer personId, Device device, Integer issuePersonStatus, Integer issueImageStatus) {
        this.personId = personId;
        this.device = device;
        this.issuePersonStatus = issuePersonStatus;
        this.issueImageStatus = issueImageStatus;
    }

    public IssueDetail(Integer personId, Device device, String projectCode, Integer issuePersonStatus, Integer issueImageStatus) {
        this.personId = personId;
        this.device = device;
        this.projectCode = projectCode;
        this.issuePersonStatus = issuePersonStatus;
        this.issueImageStatus = issueImageStatus;
    }

    @Override
    public String toString() {
        return "IssueDetail{" +
                "issueId=" + issueId +
                ", personId=" + personId +
                ", device=" + device +
                ", projectCode='" + projectCode + '\'' +
                ", issuePersonStatus=" + issuePersonStatus +
                ", issueImageStatus=" + issueImageStatus +
                '}';
    }
}
