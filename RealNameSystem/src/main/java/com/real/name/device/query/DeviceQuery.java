package com.real.name.device.query;

import com.real.name.device.entity.Device;
import com.real.name.project.entity.Project;
import lombok.Data;


@Data
public class DeviceQuery extends Device {

    private Project project;

    private Integer pageNum = 0;

    private Integer pageSize = 10;

}
