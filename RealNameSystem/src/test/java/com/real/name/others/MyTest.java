package com.real.name.others;

import com.real.name.auth.entity.Role;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.utils.PathUtil;
import com.real.name.common.utils.TimeUtil;
import com.real.name.device.netty.model.AccessResponse;
import com.real.name.device.netty.utils.ConvertUtils;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyTest {

    public static void main(String[] args) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String format = dateFormat.format(date);
        Date parse = dateFormat.parse(format);
        System.out.println(format);
        System.out.println(parse.getTime());
    }

    public static void testMap() {
        Map<String, Object> modelMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        modelMap.put("list", list);
        List<String> ml = (List<String>) modelMap.get("list");
        System.out.println(ml);
    }

    public static void testJSON() {
        String response = "";
    }

    public static void testUrl() {
        String url = "http://localhost:8080/person/find";
        url += "?";
        Map<String, Object> map = new HashMap<>();
        map.put("personId", "20");
        map.put("size", 2);
        map.put("number", 3);
        String data = "";
        for (String key : map.keySet()) {
            data += key + "=" + map.get(key) + "&";
        }
        url += data.substring(0, data.length() - 1);
        System.out.println(url);
    }

    public static void listTest() {
        List<String> excludeType = new ArrayList<>();
        excludeType.add("face_1");
        excludeType.add("face_2");
        excludeType.add("card_1");
        excludeType.add("faceAndcard_1");
        excludeType.add("faceAndcard_2");
        excludeType.add("idcard_2");
        if (excludeType.contains("face_0")) {
            System.out.println("true");
        }
    }

    public static void dateTest() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = new Date(1562754682832L);
        String start = format.format(startTime);
        Date endTime = new Date(System.currentTimeMillis());
        String end = format.format(endTime);
        System.out.println("start:" + start);
        System.out.println("end:" + end);
    }

    public static void testA() {

    }

    public static void testB() {
        ByteBuffer frame = ByteBuffer.allocate(64) ;
        frame.put((byte)0x17);
        frame.put((byte)0x50);
        frame.put((byte)0x00);
        frame.put((byte)0x00);
        byte[] startTime = TimeUtil.getBCDTime();
        byte[] endTime = TimeUtil.getBCDTime2();
        frame.put(startTime);
        frame.put(endTime);
        frame.put((byte)0x01);
        frame.put((byte)0x01);
        frame.put((byte)0x01);
        frame.put((byte)0x01);
        byte[] array = frame.array();
        System.out.println(Arrays.toString(array));
    }

    public static void testC() {
        String data = "abc";
        AccessResponse response = new AccessResponse();
        byte[] bytes = fillBytes(response.getData());
        System.out.println(Arrays.toString(bytes));
    }

    public static byte[] fillBytes(byte[] bytes) {
        byte[] fill = new byte[32];
        if (bytes.length < 32) {
            System.arraycopy(bytes, 0, fill, 0, bytes.length);
        }
        return fill;
    }

    public static void getBCDTime() {
        byte[] bcdTime = TimeUtil.getBCDTime2();
        String time = ConvertUtils.bytesToHex(bcdTime);
        System.out.println(time);
    }

    public static void reverse() {
        int number = 223000123;
        byte[] bytes = ConvertUtils.intToByte4(number);
        System.out.println(Arrays.toString(bytes));
        int toInt = ConvertUtils.byte4ToInt(bytes, 0);
        System.out.println(Integer.toHexString(toInt));
        ConvertUtils.reverse(bytes);
        int toInt1 = ConvertUtils.byte4ToInt(bytes, 0);
        System.out.println(Arrays.toString(bytes));
        System.out.println(Integer.toHexString(toInt1));
    }

    public static void bytetoHexTest() {
        byte[] bytes = new byte[32];
        bytes[12] = 0x20;
        bytes[13] = 0x19;
        bytes[14] = 0x07;
        bytes[15] = 0x14;
        bytes[16] = 0x08;
        bytes[17] = 0x04;
        bytes[18] = 0x30;
        String timeStr = ConvertUtils.bytesToHex(bytes, 12, 18);
        StringBuilder timeSb = new StringBuilder(timeStr);
        timeSb.insert(4, "-");
        timeSb.insert(7, "-");
        timeSb.insert(10, " ");
        timeSb.insert(13, ":");
        timeSb.insert(16, ":");
        System.out.println(timeSb);
    }

    public static void testD() {
        String str = "175A00001B7E4D0D9E015E0100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String str2 = "175A00001B7E4D0D0DD7370000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        System.out.println(str.length());
        System.out.println(str2.length());
    }

    public static void roleTest() {
        User user = new User();
        Role role = new Role();
        role.setRoleName("admin");
        Role role1 = new Role();
        role1.setRoleName("admin");
        Set<Role> roleList = new HashSet<>();
        roleList.add(role);
        roleList.add(role1);
        user.setRoles(roleList);
    }

}
