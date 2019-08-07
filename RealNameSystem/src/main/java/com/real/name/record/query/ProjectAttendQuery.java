package com.real.name.record.query;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectAttendQuery  {

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目总人数
     */
    private Integer sumPersonNum;

    /**
     * 项目考勤工时
     */
    private Double sumWorkHours;

    /**
     * 项目考勤次数
     */
    private Integer sumProjectAttendNum;

    /**
     * 项目考勤异常次数
     */
    private Integer sumProjectAttendErrNum;

}
