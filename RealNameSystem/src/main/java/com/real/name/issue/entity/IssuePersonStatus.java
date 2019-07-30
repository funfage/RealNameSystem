package com.real.name.issue.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssuePersonStatus {

    private Integer personId;

    private Integer issuePersonStatus;

    private Integer issueImageStatus;

    public IssuePersonStatus() {

    }

    public IssuePersonStatus(Integer personId, Integer issuePersonStatus, Integer issueImageStatus) {
        this.personId = personId;
        this.issuePersonStatus = issuePersonStatus;
        this.issueImageStatus = issueImageStatus;
    }

    @Override
    public String toString() {
        return "IssuePersonStatus{" +
                "personId=" + personId +
                ", issuePersonStatus=" + issuePersonStatus +
                ", issueImageStatus=" + issueImageStatus +
                '}';
    }
}
