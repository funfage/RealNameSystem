package com.real.name.device.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.device.entity.Device;
import com.real.name.project.entity.Project;
import lombok.Data;


@Data
public class DeviceQuery extends Device {

    @JsonIgnore
    private Project project;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;

}
