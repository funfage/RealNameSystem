package com.real.name.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
    public static byte[] getBCDTime() {
        Calendar cal = Calendar.getInstance();
        String str = String.valueOf(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH) + 1;
        str += month > 10 ? String.valueOf(month) : "0" + String.valueOf(month);
        int day = cal.get(Calendar.DATE);
        str += day > 10 ? String.valueOf(day) : "0" + String.valueOf(day);
        byte[] time2 = str2Bcd(str);
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
            asc = asc + "0";
            len = asc.length();
        }
        System.out.println("str2Bcd len:" + asc);
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
    public static byte[] getBCDTime2() {
        Calendar cal = Calendar.getInstance();
        //add 10 years
        String str = String.valueOf(cal.get(Calendar.YEAR) + 10);
        int month = cal.get(Calendar.MONTH) + 1;
        str += month > 10 ? String.valueOf(month) : "0" + String.valueOf(month);
        int day = cal.get(Calendar.DATE);
        str += day > 10 ? String.valueOf(day) : "0" + String.valueOf(day);
        byte[] time2 = str2Bcd(str);
        //System.out.println("time2:" + time2.length);
        return time2;
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
     *
     * @param milliseconds 毫秒数
     * @return 小时
     */
    public static double getHours(long milliseconds) {
        return milliseconds / (1000 * 3600.0);
    }

    /**
     * 获得当月1号零时零分零秒
     *
     * @return
     */
    public static Date initDateByMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得本周一0点时间
     */
    public static Date getTimesWeekBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获得本周日24点时间
     */
    public static Date getTimesWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekBegin());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 得到本月的第一天
     *
     * @return
     */
    public static Date getMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND,0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 得到本月的最后一天
     */
    public static Date getMonthLastDay() {
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至0
        ca.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        ca.set(Calendar.MINUTE, 0);
        //将秒至0
        ca.set(Calendar.SECOND,0);
        //将毫秒至0
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

}
