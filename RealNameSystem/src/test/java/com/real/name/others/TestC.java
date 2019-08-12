package com.real.name.others;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.utils.PathUtil;
import com.real.name.common.utils.TimeUtil;
import com.real.name.record.query.PeriodTime;
import org.junit.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestC extends BaseTest {

    @Test
    public void testA() {
        System.out.println(PathUtil.getPayFileBasePath());
    }

    @Test
    public void testB() {
        System.out.println(PathUtil.getContractFilePath());
    }

    @Test
    public void testC() {
        System.out.println(PathUtil.getExcelFilePath());
    }

    @Test
    public void testD() {

    }


}
