package com.real.name.common.schedule;

import com.real.name.common.info.DeviceConstant;
import com.real.name.common.schedule.entity.FaceRecordData;
import com.real.name.common.schedule.entity.Records;
import com.real.name.device.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.entity.Record;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.RecordMapper;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.service.ProjectDetailQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private RecordMapper recordMapper;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 每天凌晨零点和两点的时候触发：0 0 0,1 * * ?
     * springboot的cron表达式只值支持6个域的表达式，也就是不能设定年
     * 核对每个员工的考勤情况
     */
    @Scheduled(cron = "0 0 0,1 * * ? ")
    public void checkAttendance() {
        logger.warn("定时任务开始, 现在的时间为" + dateFormat.format(new Date()));
        System.out.println(dateFormat.format(new Date()));
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
                queryRecord(allFaceDevice, person);
            } else if (person.getWorkRole() == 20) {//如果是普通人员则查询项目所绑定的设备的识别信息
                //获取该项目所绑定所有人脸设备
                List<Device> faceDeviceList = deviceService.findByProjectCodeInAndDeviceType(projectCodes, DeviceConstant.faceDeviceType);
                queryRecord(faceDeviceList, person);
            }
        }
        logger.warn("定时任务结束, 现在的时间为" + dateFormat.format(new Date()));
    }

    /**
     * 查询指定设备一天内某个人员的识别记录
     * @param deviceList 设备集合
     * @param person 人员信息
     */
    private void queryRecord(List<Device> deviceList, Person person) {
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

    @Scheduled(cron = "0 0 17 * * ?")
    private void cronTest() {
        logger.warn("定时任务开始, 现在的时间为" + dateFormat.format(new Date()));
        System.out.println(dateFormat.format(new Date()));
    }

}
