package com.real.name.record.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectWorkRecord {

    /**
     * 工程id
     */
    private String projectCode;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 报表生成日期
     */
    private Date createTime;

    /**
     * 统计开始时间
     */
    private Date startDate;

    /**
     * 统计结束时间
     */
    private Date endDate;

    /**
     * 项目下的人员考勤
     */
    private List<PersonWorkRecord> personWorkRecordList;

}
