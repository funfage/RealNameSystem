package com.real.name.issue.entity;


import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class IssueAccess {

    private Integer issueAccessId;

    private Person person;

    private Device device;

    private Integer issueStatus;

    public IssueAccess() {
    }

    public IssueAccess(Person person, Device device) {
        this.person = person;
        this.device = device;
    }

    public IssueAccess(Person person, Device device, Integer issueStatus) {
        this.person = person;
        this.device = device;
        this.issueStatus = issueStatus;
    }
}
