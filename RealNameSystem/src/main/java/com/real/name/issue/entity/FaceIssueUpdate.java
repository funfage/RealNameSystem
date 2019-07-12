package com.real.name.issue.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FaceIssueUpdate {

    private Integer personId;

    private String deviceId;

    private Integer issuePersonStatus;

    private Integer issueImageStatus;

    public FaceIssueUpdate() {
    }

    public FaceIssueUpdate(Integer personId, String deviceId, Integer issuePersonStatus, Integer issueImageStatus) {
        this.personId = personId;
        this.deviceId = deviceId;
        this.issuePersonStatus = issuePersonStatus;
        this.issueImageStatus = issueImageStatus;
    }

    @Override
    public String toString() {
        return "FaceIssueUpdate{" +
                "personId=" + personId +
                ", deviceId='" + deviceId + '\'' +
                ", issuePersonStatus=" + issuePersonStatus +
                ", issueImageStatus=" + issueImageStatus +
                '}';
    }
}
