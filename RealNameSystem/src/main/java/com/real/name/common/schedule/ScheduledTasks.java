package com.real.name.common.schedule;

import com.real.name.common.constant.CommConstant;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.schedule.entity.FaceRecordData;
import com.real.name.common.schedule.entity.Records;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.PathUtil;
import com.real.name.common.utils.TimeUtil;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.issue.entity.DeleteInfo;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.repository.DeleteInfoMapper;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import com.real.name.project.service.repository.ProjectQueryMapper;
import com.real.name.record.entity.*;
import com.real.name.record.service.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private DeleteInfoMapper deleteInfoMapper;

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

    @Autowired
    private ProjectQueryMapper projectQueryMapper;

    @Autowired
    private PersonAttendMapper personAttendMapper;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 每天11点的时候触发每隔5分钟执行一次 共执行6次
     * springboot的cron表达式只值支持6个域的表达式，也就是不能设定年
     * 核对每个员工的考勤情况
     */

    @Scheduled(cron = "0 0,5,10,15,20,25 23 * * ?")
    public void checkFaceAttendance() {
        logger.warn("总召定时任务开始");
        try {
            //获取所有人员
            List<Person> personList = personService.findAllPersonRole();
            //获取所有的人脸设备
            List<Device> allFaceDevice = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
            for (Person person : personList) {
                //查询用户所在的项目
                List<String> projectCodes = projectDetailQueryService.getProjectCodeListByPersonId(person.getPersonId());
                if (person.getWorkRole() == null) {
                    //什么都不做
                } else if (person.getWorkRole() == 10) {//如果是管理人员则查询所有设备的识别信息
                    queryFaceRecord(allFaceDevice, person);
                } else if (person.getWorkRole() == 20) {//如果是普通人员则查询项目所绑定的设备的识别信息
                    //获取该项目所绑定所有人脸设备
                    List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
                    queryFaceRecord(faceDeviceList, person);
                }
            }
        } catch (Exception e) {
            logger.warn("定时总召任务出现异常，e{}", e);
        }
        logger.warn("总召定时任务结束");

    }

    /**
     * 查询指定设备一天内某个人员的识别记录
     *
     * @param deviceList 设备集合
     * @param person     人员信息
     */

    private void queryFaceRecord(List<Device> deviceList, Person person) {
        Date startTime = new Date(System.currentTimeMillis() - CommConstant.DAY_MILLISECOND);
        Date endTime = new Date(System.currentTimeMillis());
        for (Device device : deviceList) {
            //查询设备的人脸识别记录
            FaceRecordData faceRecordData = FaceDeviceUtils.getPersonRecords(device, person.getPersonId(), -1, 0, dateFormat.format(startTime), dateFormat.format(endTime));
            if (faceRecordData != null) {
                //获取设备返回的识别信息
                List<Records> recordsList = faceRecordData.getRecords();
                for (Records faceRecord : recordsList) {
                    //只有在时间段内识别成功的信息才有效,0：时间段内，1：时间段外，2：陌生人/识别失败
                    if (faceRecord.getType() != null && faceRecord.getType() == 0) {
                        //根据设备序列号,人员id和识别时间查询数据库中的识别记录
                        Record selectRecord = recordMapper.findAttendancePerson(person.getPersonId(), device.getDeviceId(), faceRecord.getTime());
                        //若在数据库中查询不到记录则存入数据库
                        if (selectRecord == null) {
                            Person selectPerson = personService.findPersonNameByPersonId(person.getPersonId());
                            if (selectPerson != null) {
                                //若查询记录为空则将从设备查询的记录插入数据库
                                logger.warn("保存了一条未添加的考勤识别记录");
                                recordMapper.saveRecord(new Record(device.getDeviceId(), device.getDeviceType(), selectPerson.getPersonId(),
                                        selectPerson.getPersonName(), faceRecord.getTime(), new Date(faceRecord.getTime()), faceRecord.getType(),
                                        faceRecord.getPath(), device.getDirection(), device.getChannel()));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 从第5秒开始每隔15秒查询控制器下发失败的信息
     */

    @Scheduled(cron = "5/60 * * * * ?")
    public void queryFailAccess() {
        List<IssueAccess> issueFailAccess = issueAccessService.findIssueFailAccess();
        for (IssueAccess issueAccess : issueFailAccess) {
            Device device = issueAccess.getDevice();
            Person person = issueAccess.getPerson();
            //发送查询报文给控制器
            accessService.queryAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
        }
    }

    /**
     * 从第30秒开始每隔60秒重发控制器下发失败的信息
     */

    @Scheduled(cron = "30/60 * * * * ?")
    public void resendFailAccess() {
        List<IssueAccess> issueAccessList = issueAccessService.findIssueFailAccess();
        for (IssueAccess issueAccess : issueAccessList) {
            Device device = issueAccess.getDevice();
            Person person = issueAccess.getPerson();
            accessService.addAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
        }
    }

    /**
     * 每天晚上11点半统计工人的工时
     */

    @Scheduled(cron = "0 30 23 * * ?")
    public void countWorkTime() {
        logger.warn("统计工时定时任务开始");
        try {
            List<ProjectDetail> projectDetailList = projectDetailService.findAll();
            for (ProjectDetail projectDetail : projectDetailList) {
                Attendance attendance = new Attendance();
                attendance.setProjectDetailId(projectDetail.getId());
                Integer personId = projectDetail.getPersonId();
                long todayBegin = TimeUtil.getTodayBeginMilliSecond();
                long tomorrowBegin = TimeUtil.getTomorrowBeginMilliSecond();
                //查询该员工的当天的所有进出记录
                List<Record> todayRecord = recordMapper.getTodayRecord(personId, todayBegin, tomorrowBegin);
                if (todayRecord.size() > 0) {
                    long totalTime = 0;
                    //判断人员的进出记录是否异常
                    //1,员工只出不进
                    if (todayRecord.get(0).getDirection() == 2) {
                        attendance.setEndTime(new Date(todayRecord.get(0).getTimeNumber()));
                        attendance.setStatus(0);
                    } else if (todayRecord.get(todayRecord.size() - 1).getDirection() == 1) { // 2,只进不出
                        attendance.setStartTime(new Date(todayRecord.get(todayRecord.size() - 1).getTimeNumber()));
                        attendance.setStatus(-1);
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
                                long time2;
                                if (i == todayRecord.size()) {
                                    time2 = todayRecord.get(i - 1).getTimeNumber();
                                    totalTime += time2 - time1;
                                } else {
                                    time2 = todayRecord.get(--i).getTimeNumber();
                                    totalTime += time2 - time1;
                                }
                                //保存人员完整的一次进的记录
                                personAttendMapper.savePersonAttend(new PersonAttend(new Person(personId), new Date(time1), direction1));
                                personAttendMapper.savePersonAttend(new PersonAttend(new Person(personId), new Date(time2), direction2));
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
            logger.warn("统计工时出现异常, e:{}", e);
        }
        logger.warn("统计工时定时任务结束");
    }

    /**
     * 每天晚上11点10分统计班组和项目每日的总工时,考勤次数和异常次数
     */

    @Scheduled(cron = "0 40 23 * * ?")
    public void countGroupAndProjectAttend() {
        logger.warn("统计班组和项目每日的总工时定时任务开始");
        try {
            Date todayBegin = TimeUtil.getTodayBegin();
            Date todayEnd = TimeUtil.getTodayEnd();
            //查询所有的projectCode
            List<String> allProjectCode = projectQueryMapper.findAllProjectCode();
            for (String projectCode : allProjectCode) {
                //查询该项目下所有班组信息
                List<ProjectDetailQuery> groupList = projectDetailQueryMapper.getWorkerGroupInProject(projectCode);
                //统计班组相关考勤数据
                for (ProjectDetailQuery projectDetailQuery : groupList) {
                    WorkerGroup workerGroup = projectDetailQuery.getWorkerGroup();
                    //获取该班组所有的project_detail_id
                    List<Integer> projectDetailIds = projectDetailQueryMapper.getProjectDetailIdByGroup(workerGroup.getTeamSysNo());
                    //获取当天班组考勤的总工时, 总考勤次数, 不统计异常数据
                    Double workHours = attendanceMapper.getPeriodWorkHoursInPIds(projectDetailIds, todayBegin, todayEnd);
                    Integer groupAttendNum = attendanceMapper.getPeriodAttendNumInPIds(projectDetailIds, todayBegin, todayEnd);
                    //获取班组当天考勤异常的次数
                    Integer groupAttendErrNum = attendanceMapper.getPeriodAttendErrNumInPIds(projectDetailIds, todayBegin, todayEnd);
                    //保存班组当天考勤信息
                    GroupAttend groupAttend = new GroupAttend();
                    groupAttend.setWorkerGroup(workerGroup);
                    groupAttend.setProject(new Project(projectCode));
                    groupAttend.setWorkHours(workHours);
                    groupAttend.setGroupAttendNum(groupAttendNum);
                    groupAttend.setGroupAttendErrNum(groupAttendErrNum);
                    groupAttend.setWorkTime(new Date());
                    groupAttendMapper.saveGroupAttend(groupAttend);
                }
                //统计项目相关考勤数据
                List<Integer> projectDetailIds = projectDetailQueryMapper.getIdsByProjectCode(projectCode);
                //获取当天项目考勤的总工时, 总考勤次数, 不统计异常数据
                Double workHours = attendanceMapper.getPeriodWorkHoursInPIds(projectDetailIds, todayBegin, todayEnd);
                Integer projectAttendNum = attendanceMapper.getPeriodAttendNumInPIds(projectDetailIds, todayBegin, todayEnd);
                //获取项目当天考勤异常的次数
                Integer projectAttendErrNum = attendanceMapper.getPeriodAttendErrNumInPIds(projectDetailIds, todayBegin, todayEnd);
                //保存项目当天考勤信息
                ProjectAttend projectAttend = new ProjectAttend();
                projectAttend.setProjectCode(projectCode);
                projectAttend.setWorkHours(workHours);
                projectAttend.setProjectAttendNum(projectAttendNum);
                projectAttend.setProjectAttendErrNum(projectAttendErrNum);
                projectAttendMapper.saveProjectAttend(projectAttend);
            }
        } catch (Exception e) {
            logger.warn("统计班组工时出现异常, e:{}", e);
        }
        logger.warn("统计班组和项目每日的总工时定时任务结束");
    }

    /**
     * 第一次延迟1小时后执行，之后按fixedRate的规则30分钟执行一次
     * 查询某个人员在某个设备的权限,并删除已有的权限
     */

    @Scheduled(initialDelay = 1000 * 3600, fixedRate = 1000 * 1800)
    public void confirmDelAuthority() {
        logger.warn("查询权限定时任务开始");
        try {
            List<DeleteInfo> deleteInfoList = deleteInfoMapper.findAll();
            for (DeleteInfo deleteInfo : deleteInfoList) {
                Device device = deleteInfo.getDevice();
                Person person = deleteInfo.getPerson();
                if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
                    Boolean isSuccess = FaceDeviceUtils.queryPersonByDeviceId(device, person);
                    if (!isSuccess) {//若查询不到人员信息则说明该设备已将人员信息删除
                        //删除记录
                        int i = deleteInfoMapper.deleteById(deleteInfo.getDeleteInfoId());
                        if (i > 0) {
                            logger.warn("删除了一条设备上的人员信息，设备id：{}，人员id：{}，人员姓名：{}", device.getDeviceId(), person.getPersonId(), person.getPersonName());
                        }
                    }
                } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {
                    //发送查询报文
                    accessService.queryAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
                }
            }
        } catch (Exception e) {
            logger.warn("控制器权限出现异常, e:{}", e);
        }
        logger.warn("查询权限定时任务结束");
    }

    /**
     * 凌晨两点定时把生成的报表删除
     */

    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExcelFiles() {
        logger.warn("清除报表定时任务开始");
        FileTool.deleteFile(PathUtil.getExcelFilePath(), "");
        logger.warn("清除报表定时任务结束");
    }

}


















