package com.real.name.device.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.info.DeviceConstant;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.JedisService;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.AccessService;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.issue.entity.FaceResult;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImp implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    @Autowired
    private AccessService accessService;

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
        //搜索控制器
        accessService.searchAccess(device.getDeviceId(), device.getIp(), device.getOutPort());
        long startTime = System.currentTimeMillis();
        //若三秒过后还没有获取到设备ip,则跳出循环
        while (true) {
            if (jedisKeys.hasKey(device.getDeviceId() + DeviceConstant.SEARCH_ACCESS)) {
                break;
            }
            if (System.currentTimeMillis() - startTime > 3000) {
                break;
            }
        }
        String ip = (String) jedisStrings.get(device.getDeviceId() + DeviceConstant.SEARCH_ACCESS);
        if (!StringUtils.hasText(ip)) {
            throw new AttendanceException(ResultError.DEVICE_SEARCH_EMPTY);
        }
        if (!ip.equals(device.getIp())) {
            throw new AttendanceException(ResultError.DEVICE_IP_NO_MATCH);
        }
        startTime = System.currentTimeMillis();
        //重置设备
        accessService.clearAccess(device.getDeviceId(), device.getIp(), device.getOutPort());
        while (true) {
            if (jedisKeys.hasKey(device.getDeviceId() + DeviceConstant.CLEAR_AUTHORITY)) {
                break;
            }
            if (System.currentTimeMillis() - startTime > 3000) {
                break;
            }
        }
        //是否成功
        Boolean isClear = (Boolean) jedisStrings.get(device.getDeviceId() + DeviceConstant.CLEAR_AUTHORITY);
        if (isClear == null || !isClear) {
            throw new AttendanceException(ResultError.RESET_DEVICE_ERROR);
        }
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
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Override
    public List<Device> findByProjectCode(Integer projectId) {
        return deviceRepository.findByProjectCode(projectId);
    }

    @Override
    public Optional<Device> findByDeviceId(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> findAvailableDevice() {
        return deviceRepository.findByProjectCodeNotNull();
    }

    @Override
    public List<Device> findDutouOfmenjin(String projectId, Integer deciceType) {
        return deviceRepository.findByProjectCodeAndDeviceType(projectId, deciceType);
    }

    @Override
    public boolean existsDeviceByDeviceId(String deviceId) {
        return deviceRepository.existsDeviceByDeviceId(deviceId);
    }

    @Override
    public List<Device> findAllByDeviceType(Integer deviceType) {
        return deviceRepository.findAllByDeviceType(deviceType);
    }

    @Override
    public List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deciceType) {
        return deviceRepository.findByProjectCodeAndDeviceType(projectId, deciceType);
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
}
