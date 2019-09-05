package com.real.name.record.query;

import com.real.name.record.entity.Attendance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class ProjectAttendanceQuery extends Attendance {

    private Integer pageNum = 0;

    private Integer pageSize = 20;

    private String projectCode;

    private Integer subContractorId;

    private Date searchDate;

    private String personName;


}
