package com.real.name.common.utils;

import org.springframework.util.StringUtils;

public class CommonUtils {

    public static boolean isRightPhone(String phone) {
        return  StringUtils.hasText(phone) && phone.length() == 11 && phone.startsWith("1");
    }
}
