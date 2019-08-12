package com.real.name.record.query;

import com.real.name.record.entity.Attendance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PersonWorkRecord {

    /**
     * 人员项目详情id
     */
    private Integer projectDetailId;

    /**
     * 人员id
     */
    private Integer personId;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 身份证号码
     */
    private String idCardNumber;

    /**
     * 身份证类型
     */
    private Integer idCardType;

    /**
     * 当前工种
     */
    private String workType;

    /**
     * 工人类型;10:管理人员，20：建筑工人
     */
    private Integer workRole;

    /**
     * 所属公司
     */
    private String subordinateCompany;

    /**
     * 所在企业统一社会信用代码
     */
    private String corpCode;

    /**
     * 班组编号
     */
    private Integer teamSysNo;

    /**
     * 班组名
     */
    private String teamName;

    /**
     * 出勤天数
     */
    private Integer workDay;

    /**
     * 出勤小时
     */
    private Double workHours;

    /**
     * 出勤
     */
    private List<Attendance> attendanceList;

}
