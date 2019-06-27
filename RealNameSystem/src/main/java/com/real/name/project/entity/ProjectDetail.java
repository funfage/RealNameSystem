package com.real.name.project.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicInsert
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目id
     */
    private String projectCode;

    /**
     * 人员id
     */
    private Integer personId;

    /**
     * 班组id
     */
    private Integer teamSysNo;

    /**
     * 人员添加到项目中的时间
     */
    private Date createTime;

    /**
     * 人员状态，0:不在该项目, 1:在该项目
     */
    private Integer personStatus;

    public ProjectDetail(String projectId, Integer personId, Integer teamSysNo) {
        this.projectCode = projectId;
        this.personId = personId;
        this.teamSysNo = teamSysNo;
    }

    public ProjectDetail() {
    }
}
