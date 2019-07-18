package com.real.name.project.entity;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
public class ProjectDetailQuery {

    private Integer id;

    private Person person;

    private Project project;

    private WorkerGroup workerGroup;

    private Date crateTime;

    private List<Attendance> attendanceList;

    public ProjectDetailQuery() {
    }

    public ProjectDetailQuery(Integer id) {
        this.id = id;
    }

    public ProjectDetailQuery(Integer id, Person person, Project project, WorkerGroup workerGroup, Date crateTime) {
        this.id = id;
        this.person = person;
        this.project = project;
        this.workerGroup = workerGroup;
        this.crateTime = crateTime;
    }

    @Override
    public String toString() {
        return "ProjectDetailQuery{" +
                "id=" + id +
                ", person=" + person +
                ", project=" + project +
                ", workerGroup=" + workerGroup +
                ", crateTime=" + crateTime +
                '}';
    }
}
