package com.real.name.pay;

import com.alibaba.fastjson.JSONObject;
import com.real.name.others.BaseTest;
import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.query.PayInfoQuery;
import com.real.name.pay.service.repository.PayInfoMapper;
import com.real.name.project.entity.ProjectDetailQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class PayInfoTest extends BaseTest {

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Test
    public void savePayInfo() {
        PayInfo payInfo = new PayInfo();
        payInfo.setProjectDetailQuery(new ProjectDetailQuery(104));
        payInfo.setPayBankCardNumber("5443438434978");
        payInfo.setPayBankName("建设银行");
        payInfo.setPayBankCode("101");
        payInfo.setTotalPayAmount(4500.0f);
        payInfo.setActualAmount(4500.00f);
        payInfo.setSuffixName(".jpg");
        payInfo.setBalanceDate(new Date());
        int i = payInfoMapper.savePayInfo(payInfo);
        System.out.println(i);
        System.out.println(payInfo);
    }

    @Test
    public void getAllPayInfo() {
        List<PayInfo> allPayInfo = payInfoMapper.getAllPayInfo();
        PayInfo payInfo = allPayInfo.get(0);
        String jsonString = JSONObject.toJSONString(payInfo);
        System.out.println(jsonString);
    }

    @Test
    public void updatePayInfoById() {
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(9L);
        payInfo.setProjectDetailQuery(new ProjectDetailQuery(104));
        payInfo.setPayBankCardNumber("3943948");
        payInfo.setPayBankName("工商银行");
        payInfo.setPayBankCode("10000341");
        payInfo.setTotalPayAmount(5500.0f);
        payInfo.setActualAmount(5500.00f);
        payInfo.setSuffixName(".jpg");
        payInfo.setBalanceDate(new Date());
        int i = payInfoMapper.updatePayInfoById(payInfo);
        System.out.println(i);
    }

    @Test
    public void deletePayInfoById() {
        int i = payInfoMapper.deletePayInfoById(8);
        System.out.println(i);
    }

    @Test
    public void searchPayInfo() {
        PayInfoQuery query = new PayInfoQuery();
        query.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        query.setNameOrIdCardNumber("勇");
        List<PayInfo> payInfos = payInfoMapper.searchPayInfo(query);
        System.out.println(payInfos);
    }


}











