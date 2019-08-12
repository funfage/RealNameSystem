package com.real.name.record.service.repository;

import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.ProjectAttend;
import com.real.name.record.query.ProjectAttendQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ProjectAttendMapper {

    /**
     * 累计某个时间段的除异常数据外的考勤次数和考勤工时
     */
    Map<String, Object> getPeriodAttendNumAndWorkHours(@Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 根据id集合查询某个时间段的除异常数据外的考勤次数和考勤工时
     */
    Map<String, Object> getPeriodAttendNumAndWorkHoursByProjectCodeSet(@Param("startTime") Date startTime,
                                                                   @Param("endTime") Date endTime,
                                                                   @Param("projectSet") Set<String> projectSet);

    /**
     * 查询某个时间段异常数据的总数
     */
    Integer getPeriodAttendErrNum(@Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime);

    /**
     * 根据id集合查询某个时间段异常数据的总数
     */
    Integer getPeriodAttendErrNumByProjectCodeSet(@Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("projectSet") Set<String> projectSet);

    /**
     * 保存项目考勤信息
     */
    int saveProjectAttend(@Param("projectAttend") ProjectAttend projectAttend);

    /**
     * 根据projectCode修改项目某日考勤信息
     */
    int updateByProjectCode(@Param("projectCode") String projectCode,
                            @Param("hours") Double hours,
                            @Param("attendNum") Integer attendNum,
                            @Param("errAttendNum") Integer errAttendNum,
                            @Param("startTime") Date startTime,
                            @Param("endTime") Date endTime);

    /**
     * 按照项目总人数升序
     */
    List<ProjectAttendQuery> rangeByPersonNum();

    /**
     * 按照项目考勤工时升序
     */
    List<ProjectAttendQuery> rangeBySumWorkHours(@Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);

    /**
     * 按照项目考勤次数升序
     */
    List<ProjectAttendQuery> rangeBySumAttendNum(@Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);

    /**
     * 按照项目考勤异常次数升序
     */
    List<ProjectAttendQuery> rangeBySumAttendErrNum(@Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime);

}
