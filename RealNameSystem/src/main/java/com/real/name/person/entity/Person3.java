package com.real.name.person.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "person")
public class Person3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 身份证
     */
    private String idCardNumber;


    /**
     * 班组
     */
    private Integer groupNo;

    /**
     * 工种
     */
    private String workType;
    /**
     * 出勤天数
     */
    @Transient
    private Integer days;

    /**
     * 每天出勤小时
     */
    @Transient
    private List<Double> attendanceHours;

    public Person3(String personName, String idCardNumber,  Integer groupNo, String workType) {
        this.personName = personName;
        this.idCardNumber = idCardNumber;
        this.groupNo = groupNo;
        this.workType = workType;
        //this.days = days;
    }


}