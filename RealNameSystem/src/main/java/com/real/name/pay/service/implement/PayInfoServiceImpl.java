package com.real.name.pay.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.query.PayInfoQuery;
import com.real.name.pay.service.PayInfoService;
import com.real.name.pay.service.repository.PayInfoMapper;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PayInfoServiceImpl implements PayInfoService {

    private Logger logger = LoggerFactory.getLogger(PayInfoServiceImpl.class);

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Transactional
    @Override
    public void savePayInfo(PayInfo payInfo, String projectCode, Integer teamSysNo, Integer personId) {
        Integer integer = projectDetailQueryMapper.findPersonStatusByCondition(projectCode, teamSysNo, personId);
        if (integer == null || integer != 1) {
            throw new AttendanceException(ResultError.PERSON_NO_EXISTS_IN_PROJECT);
        }
        Integer projectDetailId = projectDetailQueryMapper.getProjectPersonDetailId(projectCode, teamSysNo, personId);
        if (projectDetailId == null) {
            throw new AttendanceException(ResultError.PERSON_NO_EXISTS_IN_PROJECT);
        }
        payInfo.setProjectDetailQuery(new ProjectDetailQuery(projectDetailId));
        int i = payInfoMapper.savePayInfo(payInfo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Override
    public void updatePayInfo(PayInfo payInfo) {
        if (payInfo.getProjectDetailQuery() == null || payInfo.getProjectDetailQuery().getId() == null) {
            logger.error("传入的projectDetailId为空");
            throw new AttendanceException(ResultError.OPERATOR_ERROR);
        }
        Integer integer = projectDetailQueryMapper.findPersonStatusById(payInfo.getProjectDetailQuery().getId());
        if (integer == null || integer != 1) {
            throw new AttendanceException(ResultError.PERSON_NO_EXISTS_IN_PROJECT);
        }
        int i = payInfoMapper.updatePayInfoById(payInfo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Override
    public void deletePayInfo(Integer payId) {
        int i = payInfoMapper.deletePayInfoById(payId);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
    }

    @Override
    public List<PayInfo> getAllPayInfo() {
        return payInfoMapper.getAllPayInfo();
    }

    @Override
    public List<PayInfo> searchPayInfo(PayInfoQuery payInfoQuery) {
        return payInfoMapper.searchPayInfo(payInfoQuery);
    }
}
