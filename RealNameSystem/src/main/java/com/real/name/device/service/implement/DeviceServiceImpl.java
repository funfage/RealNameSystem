package com.real.name.device.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.constant.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.JedisService;
import com.real.name.device.entity.Device;
import com.real.name.device.netty.utils.AccessDeviceUtils;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.query.DeviceQuery;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceQueryMapper;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.issue.entity.DeleteInfo;
import com.real.name.issue.entity.FaceResult;
import com.real.name.issue.entity.IssueAccess;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.service.DeleteInfoService;
import com.real.name.issue.service.IssueAccessService;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private AccessService accessService;

    @Autowired
    private DeleteInfoService deleteInfoService;

    @Autowired
    private ProjectDetailQueryService projectDetailQueryService;

    @Autowired
    private IssueFaceService issueFaceService;

    @Autowired
    private IssueAccessService issueAccessService;

    @Autowired
    private DeviceQueryMapper deviceQueryMapper;

    @Override
    public void addPersonToDevices(String projectCode, Person person, List<Device> deviceList) {
        for (Device device : deviceList) {
            if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
                //下发到人脸设备
                FaceDeviceUtils.issuePersonToOneDevice(device, person, 3);
            } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType){
                //下发到控制器
                AccessDeviceUtils.issueIdCardIndexToOneDevice(device, person.getIdCardIndex());
            }
        }
    }

    /**
     * 添加人脸设备
     */
    @Transactional
    @Override
    public void addFaceDevice(Device device) {
        //获取设备的序列号
        FaceResult faceResult = FaceDeviceUtils.getDeviceKey(device);
        if (faceResult == null || !faceResult.getSuccess() || !faceResult.getMsg().equals(device.getDeviceId())) {
            throw new AttendanceException(ResultError.DEVICE_KEY_NOT_MATCH);
        }
        //从redis中判断是否收到设备的心跳
        if (!jedisKeys.hasKey(device.getDeviceId())) {
            throw new AttendanceException(ResultError.NO_HEARTBEAT);
        }
        //发送重置报文
        Boolean isSuccess = FaceDeviceUtils.issueResetDevice(device);
        if (!isSuccess) {
            throw new AttendanceException(ResultError.RESET_DEVICE_ERROR);
        }
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    /**
     * 添加控制器设备
     */
    @Override
    public void addAccessDevice(Device device) {
        //校验设备是否合法
        isValidAccess(device);
        //校验是否重置设备成功
        isResetAccessDevice(device);
        //保存设备信息
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    /**
     * 更新人脸设备
     */
    @Transactional
    @Override
    public void updateFaceDevice(Device device) {
        //从redis中判断是否收到设备的心跳
        if (!jedisKeys.hasKey(device.getDeviceId())) {
            throw new AttendanceException(ResultError.NO_HEARTBEAT);
        }
        if (device.getProjectCode() != null) {
            List<ProjectDetailQuery> projectIssueDetail = projectDetailQueryService.getProjectFaceIssueDetail(device.getProjectCode());
            //将设备绑定的项目下的人员信息下发到该设备
            for (ProjectDetailQuery projectDetailQuery : projectIssueDetail) {
                Person person = projectDetailQuery.getPerson();
                IssueFace issueFace = new IssueFace();
                issueFace.setPerson(new Person(person.getPersonId()));
                issueFace.setIssuePersonStatus(0);
                issueFace.setIssueImageStatus(0);
                //添加一条插入失败信息
                issueFaceService.insertInitIssue(issueFace);
                //下发到人脸设备
                FaceDeviceUtils.issuePersonToOneDevice(device, person, 3);
            }
        }
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    /**
     * 更新控制器
     */
    @Transactional
    @Override
    public void updateAccessDevice(Device device) {
        //校验设备是否合法
        isValidAccess(device);
        if (device.getProjectCode() != null) {
            List<ProjectDetailQuery> projectDetailQueryList = projectDetailQueryService.getProjectAccessIssueDetail(device.getProjectCode());
            for (ProjectDetailQuery projectDetailQuery : projectDetailQueryList) {
                Person person = projectDetailQuery.getPerson();
                IssueAccess issueAccess = new IssueAccess();
                issueAccess.setPerson(new Person(person.getPersonId()));
                issueAccess.setDevice(device);
                issueAccess.setIssueStatus(0);
                //为每一个设备人员添加一条下发失败信息
                issueAccessService.insertIssueAccess(issueAccess);
                //下发到控制器
                AccessDeviceUtils.issueIdCardIndexToOneDevice(device, person.getIdCardIndex());
            }
        }
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Transactional
    @Override
    public void deleteDevice(Device device) {
        //发送重置报文
        if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
            Boolean isSuccess = FaceDeviceUtils.issueResetDevice(device);
            if (!isSuccess) {
                throw new AttendanceException(ResultError.RESET_DEVICE_ERROR);
            }
        } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {
            isResetAccessDevice(device);
        }
        //删除数据库中的设备信息
        deviceRepository.delete(device);
    }

    /**
     * 删除设备中的人员信息
     */
    @Override
    public void deletePersonInDevice(Device device, Person person) {
        DeleteInfo deleteInfo = new DeleteInfo();
        deleteInfo.setPerson(person);
        deleteInfo.setDevice(device);
        deleteInfo.setStatus(0);
        deleteInfoService.saveDeleteInfo(deleteInfo);
        if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
            //删除人脸设备的人员信息
            FaceDeviceUtils.deleteDevicePersonInfo(device, person.getPersonId());
        } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {
            //删除控制器的人员信息
            accessService.deleteAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
        }
    }

    @Override
    public void deletePersonInDeviceList(List<Device> deviceList, Person person) {
        for (Device device : deviceList) {
            DeleteInfo deleteInfo = new DeleteInfo();
            deleteInfo.setPerson(person);
            deleteInfo.setDevice(device);
            deleteInfo.setStatus(0);
            //将原先的记录删除
            deleteInfoService.deleteByCondition(person.getPersonId(), device.getDeviceId());
            //保存新的记录
            deleteInfoService.saveDeleteInfo(deleteInfo);
            if (device.getDeviceType() == DeviceConstant.faceDeviceType) {
                //删除下发的记录
                issueFaceService.deleteStatusByPersonInDevice(person.getPersonId(), device.getDeviceId());
                //删除人脸设备的人员信息
                FaceDeviceUtils.deleteDevicePersonInfo(device, person.getPersonId());
            } else if (device.getDeviceType() == DeviceConstant.AccessDeviceType) {
                //删除下发的记录
                issueAccessService.deleteStatusByPersonInDevice(person.getPersonId(), device.getDeviceId());
                //删除控制器的人员信息
                accessService.deleteAuthority(device.getDeviceId(), person.getIdCardIndex(), device.getIp(), device.getOutPort());
            }
        }
    }

    @Override
    public Map<String, Object> getMainPageDeviceInfo() {
        //获取设备总数量
        //从redis获取在线设备数量
        int onDeviceNum = jedisKeys.keys(DeviceConstant.OnlineDevice + "*").size();
        Integer totalDeviceNum = deviceQueryMapper.getFaceDeviceNumber();
        Map<String, Object> map = new HashMap<>();
        map.put("onDeviceNum", onDeviceNum);
        map.put("totalDeviceNum", totalDeviceNum);
        return map;
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public List<Device> findByProjectCode(String projectCode) {
        return deviceRepository.findByProjectCode(projectCode);
    }

    @Override
    public Optional<Device> findByDeviceId(String deviceId) {
        return deviceRepository.findById(deviceId);
    }


    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> findAllIssueDevice() {
        List<Device> allIssueDevice = new ArrayList<>();
        allIssueDevice.addAll(deviceRepository.findAllByDeviceType(DeviceConstant.faceDeviceType));
        allIssueDevice.addAll(deviceRepository.findAllByDeviceType(DeviceConstant.AccessDeviceType));
        return allIssueDevice;
    }

    @Override
    public List<Device> findAllProjectIssueDevice(String projectCode) {
        List<Device> allProjectIssueDevice = new ArrayList<>();
        allProjectIssueDevice.addAll(deviceRepository.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.faceDeviceType));
        allProjectIssueDevice.addAll(deviceRepository.findByProjectCodeAndDeviceType(projectCode, DeviceConstant.AccessDeviceType));
        return allProjectIssueDevice;
    }

    @Override
    public List<Device> findAllByDeviceType(Integer deviceType) {
        return deviceRepository.findAllByDeviceType(deviceType);
    }

    @Override
    public List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deviceType) {
        return deviceRepository.findByProjectCodeAndDeviceType(projectId, deviceType);
    }

    @Override
    public List<String> getDeviceIdList() {
        return deviceRepository.getDeviceIdList();
    }

    @Override
    public Optional<Device> findByIpAndOutPort(String ip, Integer outPort) {
        return deviceRepository.findByIpAndOutPort(ip, outPort);
    }

    @Override
    public List<Device> findByProjectCodeIn(List<String> projectCodes) {
        return deviceRepository.findByProjectCodeIn(projectCodes);
    }

    @Override
    public List<Device> findByProjectCodeInAndDeviceType(List<String> projectCodes, Integer deviceType) {
        return deviceRepository.findByProjectCodeInAndDeviceType(projectCodes, deviceType);
    }

    @Override
    public void updateDeviceIPByProjectCode(String ip, String projectCode) {
        deviceRepository.updateDeviceIPByProjectCode(ip, projectCode);
    }

    @Override
    public List<String> findDeviceIdsByProjectCode(String projectCode) {
        return deviceRepository.findDeviceIdsByProjectCode(projectCode);
    }

    @Override
    public List<Device> searchDevice(DeviceQuery deviceQuery) {
        return deviceQueryMapper.searchDevice(deviceQuery);
    }

    @Override
    public Set<String> findIPByProjectCode(String projectCode) {
        return deviceQueryMapper.findIPByProjectCode(projectCode);
    }



    /**
     * 判断输入的控制器信息是否合法
     */
    private void isValidAccess(Device device) {
        //搜索控制器
        accessService.searchAccess(device.getDeviceId(), device.getIp(), device.getOutPort());
        long startTime = System.currentTimeMillis();
        //若三秒过后还没有获取到设备ip,则跳出循环
        while (true) {
            if (jedisKeys.hasKey(device.getDeviceId() + DeviceConstant.SEARCH_ACCESS)) {
                break;
            }
            if (System.currentTimeMillis() - startTime > 5000) {
                break;
            }
        }
        String ip = (String) jedisStrings.get(device.getDeviceId() + DeviceConstant.SEARCH_ACCESS);
        if (!StringUtils.hasText(ip)) {
            throw new AttendanceException(ResultError.DEVICE_SEARCH_EMPTY);
        }
        /*if (!ip.equals(device.getIp())) {
            throw new AttendanceException(ResultError.DEVICE_IP_NO_MATCH);
        }*/
    }

    /**
     * 判断是否重置控制器设备成功
     */
    private void isResetAccessDevice(Device device) {
        accessService.clearAccess(device.getDeviceId(), device.getIp(), device.getOutPort());
        long startTime = System.currentTimeMillis();
        while (true) {
            if (jedisKeys.hasKey(device.getDeviceId() + DeviceConstant.CLEAR_AUTHORITY)) {
                break;
            }
            if (System.currentTimeMillis() - startTime > 5000) {
                break;
            }
        }
        //是否成功
        Boolean isClear = (Boolean) jedisStrings.get(device.getDeviceId() + DeviceConstant.CLEAR_AUTHORITY);
        if (isClear == null || !isClear) {
            throw new AttendanceException(ResultError.RESET_DEVICE_ERROR);
        }
    }
}
