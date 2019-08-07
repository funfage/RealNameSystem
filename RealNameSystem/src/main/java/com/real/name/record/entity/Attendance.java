package com.real.name.record.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Attendance {

    private Long attendanceId;

    private Integer projectDetailId;

    private Double workHours;

    private Date workTime;

    private Date startTime;

    private Date endTime;

    private Integer status;


    public Attendance() {
    }

}


