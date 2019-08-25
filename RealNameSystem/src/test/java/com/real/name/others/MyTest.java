package com.real.name.others;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.auth.entity.Role;
import com.real.name.auth.entity.User;
import com.real.name.common.utils.TimeUtil;
import com.real.name.common.utils.XSSFExcelUtils;
import com.real.name.device.netty.model.AccessResponse;
import com.real.name.device.netty.utils.ConvertUtils;
import com.real.name.project.entity.Project;
import com.real.name.record.query.ProjectWorkRecord;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.query.GroupPeople;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyTest {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        getGroupPeopleJSON();
    }

    private static void getGroupPeopleJSON() {
        List<GroupPeople> groupPeopleList = new ArrayList<>();
        GroupPeople groupPeople = new GroupPeople();
        groupPeople.setTeamSysNo(1562649690);
        List<Integer> personIds = new ArrayList<>();
        personIds.add(1234);
        groupPeople.setPersonIds(personIds);
        groupPeopleList.add(groupPeople);

        GroupPeople groupPeople1 = new GroupPeople();
        groupPeople1.setTeamSysNo(1562649811);
        List<Integer> personIds1 = new ArrayList<>();
        personIds1.add(1235);
        groupPeople1.setPersonIds(personIds1);
        groupPeopleList.add(groupPeople1);
        System.out.println(JSON.toJSONString(groupPeopleList));
    }

    public static void contractorTOJSONString() {
        SubContractor subContractor = new SubContractor();
        subContractor.setSubContractorId(2);
        subContractor.setCorpCode("100dfdf");
        subContractor.setCorpName("dfdf");
        subContractor.setBankCode("dfd");
        subContractor.setBankName("bankName");
        subContractor.setBankLinkNumber("dfdf");
        subContractor.setBankNumber("dfd");
        subContractor.setCreateTime(new Date());
        subContractor.setEntryTime(new Date());
        subContractor.setExitTime(new Date());
        subContractor.setPmIdCardNumber("df");
        subContractor.setPmName("dere");
        subContractor.setPmPhone("dfd");
        Project project = new Project();
        project.setProjectCode("44010620190510008");
        subContractor.setProject(project);
        subContractor.setUploadStatus(1);
        String s = JSONObject.toJSONString(subContractor);
        System.out.println(s);
    }

    public static void testdateBeginEnd() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2019-08-06 12:23:44");
        Date dateBegin = TimeUtil.getDateBegin(date);
        Date dateEnd = TimeUtil.getDateEnd(date);
        System.out.println(dateFormat.format(dateBegin));
        System.out.println(dateFormat.format(dateEnd));
    }

    public static void exportFileTest() throws ParseException, IOException {
        List<String> headList = new ArrayList<>();
        headList.add("姓名");
        headList.add("身份证号码");
        headList.add("性别");
        headList.add("所属公司");
        headList.add("班组");
        headList.add("工种");
        headList.add("出勤天数");
        headList.add("考勤小时");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dateFormat.parse("2019-07-01 21:23:41");
        Date end = dateFormat.parse("2019-07-31 02:43:22");
        List<String> dayBetweenFormat = TimeUtil.getDayBetweenFormat(begin, end);
        for (String head : headList) {
            System.out.println(head);
        }
        ProjectWorkRecord projectWorkRecord = new ProjectWorkRecord();
        projectWorkRecord.setCreateTime(new Date());
        projectWorkRecord.setEndDate(end);
        projectWorkRecord.setStartDate(begin);
        projectWorkRecord.setProjectName("广东工业大学");
        projectWorkRecord.setProjectCode("dfdfdf");
        XSSFExcelUtils.exportXSSFExcel(projectWorkRecord, headList, dayBetweenFormat);
    }

    public static void testYesterday() {
        Date yesterdayBegin = TimeUtil.getYesterdayBegin();
        Date todayBegin = TimeUtil.getTodayBegin();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateFormat.format(yesterdayBegin));
        System.out.println(dateFormat.format(todayBegin));
    }

    public static void getDayBetween() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = dateFormat.parse("2019-07-01 21:23:41");
        Date end = dateFormat.parse("2019-07-31 02:43:22");
        List<Date> dayBetween = TimeUtil.getDayBetween(begin, end);
        for (Date date : dayBetween) {
            System.out.println(date.getTime());
            System.out.println(dateFormat.format(date));
        }
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
