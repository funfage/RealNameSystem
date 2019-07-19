package com.real.name.issue.entity;

import com.real.name.device.entity.Device;
import com.real.name.person.entity.Person;
import lombok.Data;

@Data
public class DeleteInfo {

    private Long deleteInfoId;

    private Person person;

    private Device device;

    private Integer status;


}
