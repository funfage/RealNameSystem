package com.real.name.device.service.implement;

import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.device.service.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceImp implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

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
}
