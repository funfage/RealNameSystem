package com.real.name.record.entity;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.project.entity.Project;
import lombok.Data;

import java.util.Date;

@Data
public class GroupAttend {

    private Integer groupAttendId;

    private WorkerGroup workerGroup;

    private Project project;

    private double workHours;

    private Date workTime;


    public GroupAttend() {
    }

    public GroupAttend(Integer groupAttendId, WorkerGroup workerGroup, Project project, double workHours, Date workTime) {
        this.groupAttendId = groupAttendId;
        this.workerGroup = workerGroup;
        this.project = project;
        this.workHours = workHours;
        this.workTime = workTime;
    }
}
