package com.real.name.others;

import com.real.name.common.utils.PathUtil;
import org.junit.Test;

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


}
