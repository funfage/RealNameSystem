package com.real.name.device.controller;

import com.real.name.common.result.ResultVo;
import com.real.name.device.entity.Record;
import com.real.name.netty.dao.RecordDao;
import com.real.name.person.entity.Person3;
import com.real.name.person.service.implement.PersonImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 考勤管理界面
 */
@RestController
@RequestMapping("/attendence")
public class AttendanceController {

    @Autowired
    RecordDao recordDao;
    @Autowired
    PersonImp personImp;

    /**
     * 每个人的出勤天数
     * @param startTime
     * @param endTime
     * @return
     */
     @PostMapping("/findAllAttendance")
    public ResultVo findAllAttendance(@RequestParam("startTime") long startTime,@RequestParam("endTime") long endTime,@RequestParam("projectCode") String projectCode){
         Date startDate = new Date(startTime);
         Date endDate = new Date(endTime);
         System.out.println("start:" +startDate);
         Map<String,Object> map = new HashMap<>();
         map.put("startDate",startDate);
         map.put("endDate",endDate);
         //得到时间范围内所有人的person_id
         List<Integer> personIds = null;
         //是查询所有项目还是查询某个项目
         if (projectCode == null) {
             personIds = recordDao.findDistinctPersons(map);
         }else {
             map.put("projectCode",projectCode);
             //获取该项目所有人员
             personIds = recordDao.personInproject(map);
         }
         System.out.println("records:" +personIds.size());
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
         Calendar cal = Calendar.getInstance();
         cal.setTime(endDate);
         int firstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
         int today = cal.get(Calendar.DAY_OF_MONTH);
         cal.set(Calendar.DAY_OF_MONTH,firstDayOfMonth);
         System.out.println("firstDayOfMonth:" +firstDayOfMonth);
         System.out.println("ddyy:"+sdf.format(cal.getTime()));
         List<Person3> result = new ArrayList<>(personIds.size());
         for (int i = 0; i <today ; i++){
             for (Integer personID:personIds) {
                 map.put("personID",personID);
                 List<Record> people = recordDao.findPerson(map);
                 System.out.println("people:" +people.size());
                 System.out.println("Pid:" +personID);
                 Person3 person = personImp.findByPersonId(personID);
                 if (person != null) {
                     // person.setDays(people.size()/2);
                     person.setDays(people.size()/2);
                     result.add(person);
                 }

             }
         }
         return  ResultVo.success();
     }

    /**
     * 计算每个班组的出勤人数
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("/groupCount")
    public ResultVo groupCount(@RequestParam("startTime") long startTime,@RequestParam("endTime") long endTime){
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);
        //System.out.println("start:" +startDate);
        Map<String,Object> map = new HashMap();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        //得到所有时间范围内的人名
        List<Integer> personIds = recordDao.findDistinctPersons(map);
        System.out.println("records:" +personIds.size());
        int[] groupNumber = new int[8];
        //根据人名得到人数及分组
        for (Integer personID:personIds) {
            map.put("personID",personID);
            List<Record> people = recordDao.findPerson(map);
            System.out.println("people:" +people.size());
            System.out.println("Pid:" +personID);
            int groupNo = 0;
            groupNo = recordDao.findGroup(personID);
            if (groupNo != 0) {
                System.out.println("groupNo:"+groupNo);
                groupNumber[groupNo] = groupNumber[groupNo]+people.size();
            }

        }
        int[] result = new int[7];
        System.arraycopy(groupNumber,1,result,0,7);
         return  ResultVo.success(result);
    }

    /**
     * 计算上周每天出勤率
     * @return
     */
    @PostMapping("/week-attendence")
    public ResultVo weekAttendence(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        System.out.println("now time:" +cal.getTime());
        cal.add(Calendar.WEEK_OF_YEAR,-1);
        System.out.println("last week day:"+cal.getTime());
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        if(1 == dayWeek) {
            cal.add(Calendar.DATE, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        //日历设置为上周周一
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = sdf.format(cal.getTime());
        System.out.println("所在周星期一的日期："+imptimeBegin);
        int[] result =new int[7];
        Map<String,Object> map = new HashMap();
        for (int i = 0; i <7 ; i++) {
            Date startDate = cal.getTime();
            cal.add(Calendar.DATE,1);
            Date endDate = cal.getTime();
            map.put("startDate",startDate);
            map.put("endDate",endDate);
            int number = recordDao.countWitninDay(map);
            result[i] =number;

        }
        return ResultVo.success(result);
    }

    /**
     * 计算某个人一个月的出勤率
     * @param startTime
     * @param endTime
     * @param projectCode 项目编码
     * @param personName
     * @return
     */
    @PostMapping("/day-attendance")
    public ResultVo dayAttendance(@RequestParam("startTime") long startTime,
                                  @RequestParam("endTime") long endTime,
                                   String projectCode,
                                  String personName){
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);
        System.out.println("start:" +startDate);
        Map<String,Object> map = new HashMap();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        //得到时间范围内所有人的person_id
        List<Integer> personIds = null;
        //是查询所有项目还是查询某个项目
        if (projectCode == null) {
            //考勤管理页面（无项目部），搜索record表
            personIds = recordDao.findDistinctPersons(map);
        }else {
            //有项目编码，为项目部的考勤页面，搜索project_detail表或者person表
            if (personName != null){
                personIds =recordDao.findIdByName(personName);
            }else {
                map.put("projectCode",projectCode);
                //map.put("personName",personName);
                personIds = recordDao.personInproject(map);
            }

        }


       // Map<String,Object> map = new HashMap();
        //int personID = recordDao.findId(idCardNumber);
        List<Person3> result;
        if (personIds != null)
        {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            int firstDayOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            int today = cal.get(Calendar.DAY_OF_MONTH);
            //设置时间为当月第一天的零点
            cal.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            System.out.println("firstDayOfMonth:" +firstDayOfMonth);
            System.out.println("ddyy:"+sdf.format(cal.getTime()));
            //int[] result = new int[today];
            //map.put("personID",personID);
            //Map<Integer,Integer> attendanceDays = new HashMap<>(personIds.size());
            //根据人数创建二维数组行数，根据当月已经过完天数创建列数（存储每天的出勤小时），多加一列用于存储出勤天数
            double[][] hours = new double[personIds.size()][today+1];
            //遍历当月所有人每天的出勤情况
            for (int i = 0; i <today ; i++) {
                Date startDate2 = cal.getTime();
                cal.add(Calendar.DATE,1);
                Date endDate2 = cal.getTime();
                map.put("startDate",startDate2);
                map.put("endDate",endDate2);
                //遍历每个人当天是否出勤及出勤小时
                for (int k=0; k < personIds.size();k++) {
                    int personID2 = personIds.get(k);
                    map.put("personID",personID2);
                    List<Record> records = recordDao.countOnePersonWithinDay(map);
                    try {
                        //计算出勤小时（一天可能进出多次），出勤天数加一
                        if (records != null) {
                            //该人出勤天数加1
                            hours[k][today] +=1;
                            //计算该人当天出勤小时，可能有多次出入，records已经按时间排序
                            /*for (int j = 0; j < records.size(); j = j+2) {
                                Record in = records.get(j);
                                Date inDate = in.getTime();
                                if (j+1<records.size()){
                                    Record out = records.get(j+1);
                                    Date outDte = out.getTime();
                                    double diff =outDte.getTime() - inDate.getTime();
                                    //当天出勤小时
                                    hours[k][i] +=  (diff/(1000 * 60 * 60 ));
                                }


                            }*/
                            //result[i] = hours;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //int number = recordDao.countOnePersonWithinDay(map);
                //result[i] =number;
            }
            result = new ArrayList<>(personIds.size());
            DecimalFormat df = new DecimalFormat("#.0");
            for (int i = 0; i < personIds.size(); i++) {
               int personID3 = personIds.get(i);
               Person3 person3 = personImp.findByPersonId(personID3);
               if (person3 != null){
                   person3.setDays((int)hours[i][today]);
                   List<Double> hours2 = new ArrayList<Double>();
                   for (int j = 0; j < hours[i].length-1; j++) {
                       //保留一位小数
                       hours2.add((double) Math.round( hours[i][j]*10)/10);
                   }
                   person3.setAttendanceHours(hours2);
                   result.add(person3);
               }

           }
            return ResultVo.success(result);
        }else {
            return ResultVo.success();
        }

    }
}
