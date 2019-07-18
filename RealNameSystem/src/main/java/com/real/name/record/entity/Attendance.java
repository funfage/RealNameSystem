package com.real.name.record.entity;

import com.real.name.project.entity.ProjectDetailQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Attendance {

    private Long attendanceId;

    private String projectDetailId;

    private double workHours;

    private Date workTime;


    public Attendance() {
    }

}

