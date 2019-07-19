package com.real.name.issue.service;

import com.real.name.issue.entity.DeleteInfo;

public interface DeleteInfoService {

    int saveDeleteInfo(DeleteInfo deleteInfo);

    int deleteByCondition(Integer personId, String deviceId);



}
