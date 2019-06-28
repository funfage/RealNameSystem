package com.real.name.project.entity;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "project_detail")
@Entity
@Data
public class ProjectPersonDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "personId")
    private Person person;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "projectCode")
    private Project project;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "teamSysNo")
    private WorkerGroup workerGroup;

    private Date createTime;

    /**
     * 人员状态，0:不在该项目, 1:在该项目
     */
    private Integer personStatus;

    @Override
    public String toString() {
        return "ProjectPersonDetail{" +
                "id=" + id +
                ", person=" + person +
                ", project=" + project +
                ", workerGroup=" + workerGroup +
                ", createTime=" + createTime +
                ", personStatus=" + personStatus +
                '}';
    }
}
