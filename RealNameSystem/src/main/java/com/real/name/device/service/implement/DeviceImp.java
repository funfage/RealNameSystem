package com.real.name.device.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.JedisService;
import com.real.name.device.netty.utils.FaceDeviceUtils;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceRepository;
import com.real.name.issue.entity.FaceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceImp implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private JedisService.JedisKeys jedisKeys;

    /**
     * 添加人脸设备
     * @param device
     * @return
     */
    @Transactional
    @Override
    public Device addFaceDevice(Device device) {
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
        return newDevice;
    }

    /**
     * 更新人脸设备
     * @param device
     * @return
     */
    @Transactional
    @Override
    public Device updateFaceDevice(Device device) {
        //从redis中判断是否收到设备的心跳
        if (!jedisKeys.hasKey(device.getDeviceId())) {
            throw new AttendanceException(ResultError.NO_HEARTBEAT);
        }
        Device newDevice = deviceRepository.save(device);
        if (newDevice == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
        return newDevice;
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
}
