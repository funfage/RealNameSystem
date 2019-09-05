package com.real.name.pay.service;

import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.query.PayInfoQuery;

import java.util.List;

public interface PayInfoService {

    /**
     * 保存薪资记录
     */
    void savePayInfo(PayInfo payInfo, String projectCode, Integer teamSysNo, Integer personId);

    /**
     * 更新薪资
     */
    void updatePayInfo(PayInfo payInfo);

    /**
     * 删除薪资
     */
    void deletePayInfo(Integer payId);

    /**
     * 查询所有薪资记录
     */
    List<PayInfo> getAllPayInfo();

    /**
     * 薪资搜索
     */
    List<PayInfo> searchPayInfo(PayInfoQuery payInfoQuery);

}
