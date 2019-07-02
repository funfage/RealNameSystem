package com.real.name.project.entity;

import com.real.name.face.entity.Device;
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
