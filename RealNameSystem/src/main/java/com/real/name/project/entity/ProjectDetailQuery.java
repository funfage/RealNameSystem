package com.real.name.project.entity;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.Attendance;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailQuery {

    private Integer id;

    private String projectCode;

    private Integer teamSysNo;

    private Person person;

    private Project project;

    private WorkerGroup workerGroup;

    private Date createTime;

    private List<Attendance> attendanceList;

    public ProjectDetailQuery(Integer id) {
        this.id = id;
    }
}
