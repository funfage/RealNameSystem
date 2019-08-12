package com.real.name.record;

import com.real.name.common.utils.TimeUtil;
import com.real.name.others.BaseTest;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.record.entity.Attendance;
import com.real.name.record.query.PersonWorkRecord;
import com.real.name.record.entity.Record;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.RecordMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceTest extends BaseTest {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Test
    public void saveAttendance() {
        Attendance attendance = new Attendance();
        ProjectDetailQuery query = new ProjectDetailQuery();
        query.setId(8);
        attendance.setWorkHours((double) 12349839L);
        attendance.setWorkTime(new Date());
        attendance.setStartTime(new Date());
        attendance.setEndTime(new Date());
        attendanceMapper.saveAttendance(attendance);
    }

    @Test
    public void findByAttendanceId() {
        Attendance byAttendanceId = attendanceMapper.findByAttendanceId(477L);
        System.out.println(byAttendanceId);
    }

    @Test
    public void updateAttendanceByProjectDetailId() {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(477L);
        attendance.setWorkHours(3.2);
        attendance.setWorkTime(new Date());
        attendance.setEndTime(new Date());
        attendance.setStartTime(new Date());
        int i = attendanceMapper.updateAttendanceByAttendanceId(attendance);
        System.out.println(i);
    }

    @Test
    public void countGroupTime() throws ParseException {
        try {
            List<ProjectDetail> projectDetailList = projectDetailService.findAll();
            for (ProjectDetail projectDetail : projectDetailList) {
                Attendance attendance = new Attendance();
                attendance.setProjectDetailId(projectDetail.getId());
                Integer personId = projectDetail.getPersonId();
                long todayBegin = TimeUtil.getTodayBeginMilliSecond() - 86400000 * 4;
                long tomorrowBegin = TimeUtil.getTomorrowBeginMilliSecond() - 86400000 * 4  ;
                //查询该员工的当天的所有进出记录
                List<Record> todayRecord = recordMapper.getTodayRecord(personId, todayBegin, tomorrowBegin);
                if (todayRecord.size() > 0) {
                    long totalTime = 0;
                    //判断人员的进出记录是否异常
                    //1,员工只出不进
                    if (todayRecord.get(0).getDirection() == 2) {
                        attendance.setStartTime(new Date(todayRecord.get(0).getTimeNumber()));
                        attendance.setStatus(-1);
                    } else if (todayRecord.get(todayRecord.size() - 1).getDirection() == 1) { // 2,只进不出
                        attendance.setEndTime(new Date(todayRecord.get(todayRecord.size() - 1).getTimeNumber()));
                        attendance.setStatus(0);
                    } else {
                        int index = 0;
                        for (int i = 0; i < todayRecord.size() - 1; i++) {
                            int direction1 = todayRecord.get(i).getDirection();
                            long time1 = todayRecord.get(i).getTimeNumber();
                            int direction2 = todayRecord.get(i + 1).getDirection();
                            if (direction1 == 1 && direction2 == 2) {
                                if (index == 0) {
                                    //设置开始工作的时间
                                    attendance.setStartTime(new Date(todayRecord.get(i).getTimeNumber()));
                                }
                                index++;
                                while (++i < todayRecord.size() && todayRecord.get(i).getDirection() == 2) {

                                }
                                if (i == todayRecord.size()) {
                                    long time2 = todayRecord.get(i - 1).getTimeNumber();
                                    totalTime += time2 - time1;
                                } else {
                                    long time2 = todayRecord.get(--i).getTimeNumber();
                                    totalTime += time2 - time1;
                                }
                            }
                        }
                        if (totalTime < 0) { // 算法异常
                            attendance.setStatus(-2);
                        }
                        //设置结束工作的时间
                        attendance.setEndTime(new Date(todayRecord.get(todayRecord.size() - 1).getTimeNumber()));
                    }
                    //保存员工当天的工作时长
                    attendance.setWorkHours(TimeUtil.getHours(totalTime));
                    attendance.setWorkTime(new Date());
                } else {
                    //否则今天员工没有来上班
                    attendance.setWorkHours(0.0);
                    attendance.setWorkTime(new Date());
                }
                attendanceMapper.saveAttendance(attendance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findPersonPeriodWorkInfoInProject() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        List<PersonWorkRecord> personPeriodWorkInfoInProject = attendanceMapper.findPersonPeriodWorkInfoInProject(start, end, "36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(personPeriodWorkInfoInProject);
    }

    @Test
    public void findAttendancesById() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        Map<String, Object> map = new HashMap<>();
        map.put("projectDetailId", 103);
        map.put("startTime", start);
        map.put("endTime", end);
        List<Attendance> attendancesById = attendanceMapper.findAttendances(map);
        System.out.println(attendancesById);
    }

    @Test
    public void findPagePersonPeriodWorkInfoInProject() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        System.out.println(dateFormat.format(start));
        System.out.println(dateFormat.format(end));
        List<PersonWorkRecord> pagePersonPeriodWorkInfoInProject = attendanceMapper.findPagePersonPeriodWorkInfoInProject(start, end, "36bj84W235Zgc8O78yuS32510ppMkHfe", 0, 5);
        System.out.println(pagePersonPeriodWorkInfoInProject);
    }

    @Test
    public void getPeriodAttendNumAndWorkHours() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        Map<String, Object> periodAttendNumAndWorkHours = attendanceMapper.getPeriodAttendNumAndWorkHours(null, end);
        Integer attendErrNum = attendanceMapper.getPeriodAttendErrNum(null, end);
        periodAttendNumAndWorkHours.put("attendErrNum", attendErrNum);
        System.out.println(periodAttendNumAndWorkHours);
    }

    @Test
    public void getPeriodAttendNumAndWorkHoursByProjectRole() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        Set<String> projectSet = new HashSet<>();
        projectSet.add("44010620190510008");
        Map<String, Object> periodAttendNumAndWorkHoursByProjectRole = attendanceMapper.getPeriodAttendNumAndWorkHoursByProjectSet(start, end, projectSet);
        Integer errNumByProjectRole = attendanceMapper.getPeriodAttendErrNumByProjectRole(null, end, projectSet);
        periodAttendNumAndWorkHoursByProjectRole.put("errNumByProjectRole", errNumByProjectRole);
        System.out.println(periodAttendNumAndWorkHoursByProjectRole);
    }

    @Test
    public void getPeriodWorkHoursInPIds() throws ParseException {
        List<Integer> projectDetailIds = projectDetailQueryMapper.getIdsByProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = dateFormat.parse("2019-07-01 00:00:00");
        Date end = new Date(System.currentTimeMillis());
        Double hours = attendanceMapper.getPeriodWorkHoursInPIds(projectDetailIds, start, end);
        Integer periodAttendNumInPIds = attendanceMapper.getPeriodAttendNumInPIds(projectDetailIds, start, end);
        Integer periodAttendErrNumInPIds = attendanceMapper.getPeriodAttendErrNumInPIds(projectDetailIds, start, end);
        System.out.println(hours);
        System.out.println(periodAttendNumInPIds);
        System.out.println(periodAttendErrNumInPIds);
    }



}