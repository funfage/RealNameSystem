package com.real.name.record.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProjectAttend {

    private Integer projectAttendId;

    private String projectCode;

    private Double workHours;

    private Integer projectAttendNum;

    private Integer projectAttendErrNum;

    private Date workTime;

}
