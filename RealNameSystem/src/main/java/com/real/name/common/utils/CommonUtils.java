package com.real.name.common.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isRightPhone(String phone) {
        return  StringUtils.hasText(phone) && phone.length() == 11 && phone.startsWith("1");
    }

    /**
     * 将日期转换为yyyy-MM-dd字符串格式
     * @param date
     * @return
     */
    public static String DateToString(Date date) {
        if (date != null) {
            return format.format(date);
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(DateToString(new Date()));
    }
}
