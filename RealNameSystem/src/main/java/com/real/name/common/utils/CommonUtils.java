package com.real.name.common.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isRightPhone(String phone) {
        return StringUtils.hasText(phone) && phone.length() == 11 && phone.startsWith("1");
    }

    public static boolean isRightIp(String ip) {
        String pattern = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        return ip.matches(pattern);
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

    /**
     * 生成指定长度的随机字符串
     * @param length
     * @return
     * @throws Exception
     */
    public static String getUniqueString(int length) {
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }

        }
        return sb.toString();
    }

    public static List<Integer> getIntegerList(List<Integer> list) {
        // 使用HashSet去掉重复
        Set<Integer> set = new HashSet<Integer>(list);
        // 得到去重后的新集合
        return new ArrayList<Integer>(set);
    }

    public static void main(String[] args) {
        System.out.println(CommonUtils.getUniqueString(32));
    }
}
