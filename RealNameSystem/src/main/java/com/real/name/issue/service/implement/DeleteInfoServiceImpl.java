package com.real.name.issue.service.implement;

import com.real.name.issue.entity.DeleteInfo;
import com.real.name.issue.service.DeleteInfoService;
import com.real.name.issue.service.repository.DeleteInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteInfoServiceImpl implements DeleteInfoService {

    @Autowired
    private DeleteInfoMapper deleteInfoMapper;

    @Override
    public int saveDeleteInfo(DeleteInfo deleteInfo) {
        return deleteInfoMapper.saveDeleteInfo(deleteInfo);
    }

    @Override
    public int deleteByCondition(Integer personId, String deviceId) {
        return deleteInfoMapper.deleteByCondition(personId, deviceId);
    }
}
