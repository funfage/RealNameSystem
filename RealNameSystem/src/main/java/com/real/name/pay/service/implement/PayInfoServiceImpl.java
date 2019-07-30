package com.real.name.pay.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.service.PayInfoService;
import com.real.name.pay.service.repository.PayInfoMapper;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void savePayInfo(PayInfo payInfo, String projectCode, Integer personId) {
        Integer projectDetailId = projectDetailQueryMapper.getIdByProjectCodeAndPersonId(projectCode, personId);
        if (projectDetailId == null) {
            throw new AttendanceException(ResultError.NO_FIND_PROJECT_DETAIL);
        }
        payInfo.setProjectDetailQuery(new ProjectDetailQuery(projectDetailId));
        //格式化日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            Date payMonth = dateFormat.parse(dateFormat.format(payInfo.getPayMonth()));
            payInfo.setPayMonth(payMonth);
            if (payInfo.getIsBackPay() == 1) {
                Date backPayMonth = dateFormat.parse(dateFormat.format(payInfo.getBackPayMonth()));
                payInfo.setBackPayMonth(backPayMonth);
            }
        } catch (ParseException e) {
            logger.error("添加薪资日期格式化错误, e:{}", e);
        }
        int i = payInfoMapper.savePayInfo(payInfo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Override
    public void updatePayInfo(PayInfo payInfo) {
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
}
