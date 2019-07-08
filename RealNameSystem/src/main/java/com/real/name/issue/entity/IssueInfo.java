package com.real.name.issue.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IssueInfo {

    private List<IssuePersonStatus> issuePersonStatusList;

    private String deviceId;



    public IssueInfo() {
    }

    public IssueInfo(List<IssuePersonStatus> issuePersonStatus, String deviceId) {
        this.issuePersonStatusList = issuePersonStatus;
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "IssueInfo{" +
                "issuePersonStatusList=" + issuePersonStatusList +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
