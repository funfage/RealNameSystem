package com.real.name.record.service.repository;

import com.real.name.record.entity.Attendance;
import com.real.name.record.query.PersonWorkRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface AttendanceMapper {

    /**
     * 保存考勤记录
     */
    int saveAttendance(Attendance attendance);

    /**
     * 通过projectDetailId修改考勤
     */
    int updateAttendanceByAttendanceId(@Param("attendance") Attendance attendance);

    /**
     * 根据id查询
     */
    Attendance findByAttendanceId(Long attendanceId);

    /**
     * 统计工时, 排除异常的考勤
     */
    Double getPeriodWorkHoursInPIds(@Param("projectDetailIds") List<Integer> projectDetailIds,
                                    @Param("startTime")Date startTime,
                                    @Param("endTime") Date endTime);

    /**
     * 统计考勤次数, 排除异常的考勤
     */
    Integer getPeriodAttendNumInPIds(@Param("projectDetailIds") List<Integer> projectDetailIds,
                                     @Param("startTime")Date startTime,
                                     @Param("endTime") Date endTime);

    /**
     * 统计考勤异常的数据
     */
    Integer getPeriodAttendErrNumInPIds(@Param("projectDetailIds") List<Integer> projectDetailIds,
                                        @Param("startTime")Date startTime,
                                        @Param("endTime") Date endTime);


    List<Attendance> findPeriodAttendByProjectDetailId(@Param("projectDetailId") Integer projectDetailId,
                                                       @Param("begin") Date begin,
                                                       @Param("end") Date end);

    /**
     * 查询所有人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<PersonWorkRecord> findPersonPeriodWorkInfoInProject(@Param("startTime") Date startTime,
                                                             @Param("endTime") Date endTime,
                                                             @Param("projectCode") String projectCode);

    /**
     * 分页查询人员的姓名,身份证,所属单位,班组名,工种 以及每天的工作时长
     */
    List<PersonWorkRecord> findPagePersonPeriodWorkInfoInProject(@Param("startTime") Date startTime,
                                                                 @Param("endTime") Date endTime,
                                                                 @Param("projectCode") String projectCode,
                                                                 @Param("offset") Integer offset,
                                                                 @Param("limit") Integer limit);

    /**
     * 累计某个时间段的除异常数据外的考勤次数和考勤工时
     */
    Map<String, Object> getPeriodAttendNumAndWorkHours(@Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 根据id集合查询某个时间段的除异常数据外的考勤次数和考勤工时
     */
    Map<String, Object> getPeriodAttendNumAndWorkHoursByProjectSet(@Param("startTime") Date startTime,
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
    Integer getPeriodAttendErrNumByProjectRole(@Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime,
                                  @Param("projectSet") Set<String> projectSet);

    List<Attendance> findAttendances(Map<String, Object> map);
}
