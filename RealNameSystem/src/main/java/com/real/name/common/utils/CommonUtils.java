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

    /**
     * 获取第二天凌晨12点的时间戳
     */
    public static long getTomorrowBegin() {
        long now = System.currentTimeMillis() / 1000l;
        long daySecond = 60 * 60 * 24;
        return now - (now + 8 * 3600) % daySecond + daySecond;
    }

    /**
     * 获取当天凌晨时间
     */
    public static long getTodayBegin() {
        long now = System.currentTimeMillis() / 1000l;
        long daySecond = 60 * 60 * 24;
        return now - (now + 8 * 3600) % daySecond;
    }

    /**
     * 将毫秒转换为小时
     * @param milliseconds 毫秒数
     * @return 小时
     */
    public static double getHours(long milliseconds) {
        return milliseconds / (1000 * 3600.0);
    }

    /**
     * 获得当月1号零时零分零秒
     * @return
     */
    public static Date initDateByMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        long tomorrowBegin = CommonUtils.getTomorrowBegin();
        Date date = new Date(tomorrowBegin * 1000);
        System.out.println(date);
    }
}
