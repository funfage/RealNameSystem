package com.real.name.common.schedule;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.schedule.entity.FaceRecordData;
import com.real.name.common.schedule.entity.Records;
import com.real.name.common.utils.CommonUtils;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.issue.entity.DeleteInfo;
import com.real.name.issue.entity.FaceResult;
import com.real.name.issue.service.DeleteInfoService;
import com.real.name.issue.service.repository.DeleteInfoMapper;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.Record;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.DeviceService;
import com.real.name.record.service.repository.AttendanceMapper;
import com.real.name.record.service.repository.RecordMapper;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.service.ProjectDetailQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 每天凌晨零点的时候触发每隔10分钟执行一次 共执行六次
     * springboot的cron表达式只值支持6个域的表达式，也就是不能设定年
     * 核对每个员工的考勤情况
     */
    @Scheduled(cron = "0 0,10,20,30,40,50 0 * * ?")
    public void checkFaceAttendance() {
        logger.warn("总召定时任务开始");
        //获取所有人员
        List<Person> personList = personService.findAllPersonRole();
        //获取所有的人脸设备
        List<Device> allFaceDevice = deviceService.findAllByDeviceType(DeviceConstant.faceDeviceType);
        for (Person person : personList) {
            //查询用户所在的项目
            List<String> projectCodes = projectDetailQueryService.getProjectIdsByPersonId(person.getPersonId());
            if (person.getWorkRole() == null) {
                //什么都不做
                continue;
            } else if (person.getWorkRole() == 10) {//如果是管理人员则查询所有设备的识别信息
                queryFaceRecord(allFaceDevice, person);
            } else if (person.getWorkRole() == 20) {//如果是普通人员则查询项目所绑定的设备的识别信息
                //获取该项目所绑定所有人脸设备
                List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
                queryFaceRecord(faceDeviceList, person);
            }
        }
    }

    /**
     * 每天凌晨一点的时候开始触发,每隔10分钟执行一次,共执行六次
     */
    //@Scheduled(cron = "0 0,10,20,30,40,50 1 * * ?")
    public void checkAccessAttendance() {
        //获取所有人员
        List<Person> personList = personService.findAllPersonRole();
        //获取所有的控制器设备
        List<Device> allAccessDevice = deviceService.findAllByDeviceType(DeviceConstant.AccessDeviceType);
        for (Person person : personList) {
            if (person.getWorkRole() == null) {
                continue;
            } else if (person.getWorkRole() == 10) {
                
            } else if (person.getWorkRole() == 20) {

            }
        }
    }

    /**
     *
     */

    /**
     * 查询指定设备一天内某个人员的识别记录
     * @param deviceList 设备集合
     * @param person 人员信息
     */
    private void queryFaceRecord(List<Device> deviceList, Person person) {
        Date startTime = new Date(System.currentTimeMillis() - 86400000);
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
                            Optional<Person> personOptional = personService.findPersonNameByPersonId(person.getPersonId());
                            if (personOptional.isPresent()) {
                                //若查询记录为空则将从设备查询的记录插入数据库
                                recordMapper.saveRecord(new Record(device.getDeviceId(), device.getDeviceType(), personOptional.get().getPersonId(),
                                        personOptional.get().getPersonName(), faceRecord.getTime(), new Date(faceRecord.getTime()) ,faceRecord.getType(),
                                        faceRecord.getPath(), device.getDirection(), device.getChannel()));
                            }
                        }
                    }
                }
            }
        }
    }

    private void queryAccessRecord(List<Device> deviceList, Person person) {

    }

    /**
     * 从第5秒开始每隔15秒查询控制器下发失败的信息
     */
    @Scheduled(cron = "0/30 * * * * ?")
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
     * 从第30秒开始每隔30秒重发控制器下发失败的信息
     */
    @Scheduled(cron = "30/30 * * * * ?")
    public void resendFailAccess() {
        List<IssueAccess> issueAccessList = issueAccessService.findIssueFailAccess();
        for (IssueAccess issueAccess : issueAccessList) {
            Device device = issueAccess.getDevice();
            Person person = issueAccess.getPerson();
            accessService.addAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
        }
    }

    /**
     * 每天晚上11点统计工人的工时
     */
    @Scheduled(cron = "0 0 21 * * ?")
    public void countWorkTime() {
        logger.warn("统计工时定时任务开始");
        List<ProjectDetail> projectDetailList = projectDetailService.findAll();
        for (ProjectDetail projectDetail : projectDetailList) {
            Integer personId = projectDetail.getPersonId();
            long todayBegin = CommonUtils.getTodayBegin() * 1000;
            long tomorrowBegin = CommonUtils.getTomorrowBegin() * 1000;
            //查询该员工的当天的所有进出记录
            List<Record> todayRecord = recordMapper.getTodayRecord(personId, todayBegin, tomorrowBegin);
            long totalTime = 0;
            for (int i = 0; i < todayRecord.size() - 1; i++) {
                int direction1 = todayRecord.get(i).getDirection();
                long time1 = todayRecord.get(i).getTimeNumber();
                int direction2 = todayRecord.get(i + 1).getDirection();
                if (direction1 == 1 && direction2 == 2) {
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
            //保存员工当天的工作时长
            Attendance attendance = new Attendance();
            attendance.setWorkHours(CommonUtils.getHours(totalTime));
            attendance.setWorkTime(new Date());
            attendance.setProjectDetailId(projectDetail.getProjectCode());
            attendanceMapper.saveAttendance(attendance);
        }
    }

    /**
     * 每天晚上十点开始执行,每隔10分钟执行一次,共六次
     * 查询某个人员在某个设备的权限,并删除已有的权限
     */
    @Scheduled(cron = "0 0,10,20,30,40,50 10 * * ? ")
    public void confirmDelAuthority() {
        logger.warn("查询权限定时任务开始");
        List<DeleteInfo> deleteInfoList = deleteInfoMapper.findAll();
        for (DeleteInfo deleteInfo : deleteInfoList) {
            Device device = deleteInfo.getDevice();
            Person person = deleteInfo.getPerson();
            if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
                Boolean isSuccess = FaceDeviceUtils.queryPersonByDeviceId(device, person);
                if (!isSuccess) {//若查询不到人员信息则说明该设备已将人员信息删除
                    //删除记录
                    deleteInfoMapper.deleteById(deleteInfo.getDeleteInfoId());
                }
            } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {
                //发送查询报文
                accessService.queryAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
            }
        }
    }




}


















