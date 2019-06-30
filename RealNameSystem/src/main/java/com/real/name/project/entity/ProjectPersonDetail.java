package com.real.name.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "project_detail")
@Entity
@Getter
@Setter
public class ProjectPersonDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personId")
    @JsonIgnore
    private Person person;

    @ManyToOne
    @JoinColumn(name = "projectCode")
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "teamSysNo")
    @JsonIgnore
    private WorkerGroup workerGroup;

    private Date createTime;

    /**
     * 人员状态，0:不在该项目, 1:在该项目
     */
    private Integer personStatus;

    public ProjectPersonDetail() {
    }

    public ProjectPersonDetail(Person person, Project project, WorkerGroup workerGroup, Date createTime, Integer personStatus) {
        this.person = person;
        this.project = project;
        this.workerGroup = workerGroup;
        this.createTime = createTime;
        this.personStatus = personStatus;
    }

    @Override
    public String toString() {
        return "ProjectPersonDetail{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", personStatus=" + personStatus +
                '}';
    }
}
