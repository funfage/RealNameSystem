package com.real.name.issue.entity;

import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Setter
@Getter
public class IssueFace {

    private Integer issueFaceId;

    private Person person;

    private Device device;

    private Integer issuePersonStatus;

    private Integer issueImageStatus;

    public IssueFace() {

    }



    public IssueFace(Person person, Device device, Integer issuePersonStatus, Integer issueImageStatus) {
        this.person = person;
        this.device = device;
        this.issuePersonStatus = issuePersonStatus;
        this.issueImageStatus = issueImageStatus;
    }

    public IssueFace(Person person, Device device) {
        this.person = person;
        this.device = device;
    }

    @Override
    public String toString() {
        return "IssueFace{" +
                "issueFaceId=" + issueFaceId +
                ", person=" + person +
                ", device=" + device +
                ", issuePersonStatus=" + issuePersonStatus +
                ", issueImageStatus=" + issueImageStatus +
                '}';
    }
}
