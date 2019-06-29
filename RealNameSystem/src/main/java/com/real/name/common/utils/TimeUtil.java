package com.real.name.common.utils;

import java.util.Calendar;

public class TimeUtil {
    public static byte[] getBCDTime(){
        Calendar cal = Calendar.getInstance();
        /*int year = cal.get(Calendar.YEAR);//获取年份
        byte month=(byte) int2BCD(cal.get(Calendar.MONTH) + 1) ;//获取月份
        //byte tt =(byte) cal.get(Calendar.MONTH) ;
        System.out.println("month:" +(cal.get(Calendar.MONTH) + 1));
        byte day=(byte) int2BCD(cal.get(Calendar.DATE));//获取日
        byte hour=(byte) int2BCD(cal.get(Calendar.HOUR_OF_DAY));//小时
        byte minute=(byte) int2BCD(cal.get(Calendar.MINUTE));//分
        byte second=(byte) int2BCD(cal.get(Calendar.SECOND));//秒*/
        String str = String.valueOf(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH) +1;
        str += month>10?String.valueOf(month):"0" + String.valueOf(month);
        int day = cal.get(Calendar.DATE);
        str += day>10?String.valueOf(day):"0" + String.valueOf(day);
        /*int hour = cal.get(Calendar.HOUR_OF_DAY);
        str += hour>10?String.valueOf(hour):"0" + String.valueOf(hour);
        int minute = cal.get(Calendar.MINUTE);
        System.out.println("minute"+minute);
         str += minute > 10 ? String.valueOf(minute) :  "0" + String.valueOf(minute);
         int second = cal.get(Calendar.SECOND);
        System.out.println("second:" + second);
         str += second>10?String.valueOf(second):"0" + String.valueOf(second);
        System.out.println("TimeUtil str:" + str);*/
        byte[] time2= str2Bcd(str);
        //System.out.println("time2:" + time2.length);
        return time2;
    }


    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc =  asc+"0";
            len = asc.length();
        }
        System.out.println("str2Bcd len:" +asc);
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }
    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }
    //当前时间加10年
    public static byte[] getBCDTime2(){
        Calendar cal = Calendar.getInstance();
        /*int year = cal.get(Calendar.YEAR);//获取年份
        byte month=(byte) int2BCD(cal.get(Calendar.MONTH) + 1) ;//获取月份
        //byte tt =(byte) cal.get(Calendar.MONTH) ;
        System.out.println("month:" +(cal.get(Calendar.MONTH) + 1));
        byte day=(byte) int2BCD(cal.get(Calendar.DATE));//获取日
        byte hour=(byte) int2BCD(cal.get(Calendar.HOUR_OF_DAY));//小时
        byte minute=(byte) int2BCD(cal.get(Calendar.MINUTE));//分
        byte second=(byte) int2BCD(cal.get(Calendar.SECOND));//秒*/
        //add 10 years
        String str = String.valueOf(cal.get(Calendar.YEAR) +10);
        int month = cal.get(Calendar.MONTH) +1;
        str += month>10?String.valueOf(month):"0" + String.valueOf(month);
        int day = cal.get(Calendar.DATE);
        str += day>10?String.valueOf(day):"0" + String.valueOf(day);
        /*int hour = cal.get(Calendar.HOUR_OF_DAY);
        str += hour>10?String.valueOf(hour):"0" + String.valueOf(hour);
        int minute = cal.get(Calendar.MINUTE);
        System.out.println("minute"+minute);
        str += minute > 10 ? String.valueOf(minute) :  "0" + String.valueOf(minute);
        int second = cal.get(Calendar.SECOND);
        System.out.println("second:" + second);
        str += second>10?String.valueOf(second):"0" + String.valueOf(second);
        System.out.println("TimeUtil str:" + str);*/
        byte[] time2= str2Bcd(str);
        //System.out.println("time2:" + time2.length);
        return time2;
    }

}
