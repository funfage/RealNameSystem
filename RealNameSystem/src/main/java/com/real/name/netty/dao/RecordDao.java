package com.real.name.netty.dao;


import com.real.name.record.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
//@DependsOn("SpringUtil")
public interface RecordDao {
    @Select("select DISTINCT(person_id) from `record` where time>=#{startDate} and time<= #{endDate}")
    List<Integer> findDistinctPersons(Map map);

    @Select("select * from `record` where time>=#{startDate} and time<= #{endDate} " +
            "and person_id = #{personID}")
    List<Record> findPerson(Map map);

    //ifnull语句避免因为groupNO为空而报错
    @Select("select IFNULL(MAX(group_no),0)  from `person` WHERE person_id =#{personID}")
    Integer findGroup(Integer personID);

    @Select("select COUNT(*)  from `record` WHERE time>=#{startDate} and time< #{endDate}")
    Integer countWitninDay(Map map);

    @Select("select  person_id from `person` WHERE id_card_number =#{idCardNumber}")
    Integer findId(String idCardNumber);

    @Select("select *  from `record` WHERE person_id =#{personID} AND time>=#{startDate} " +
            "and time< #{endDate} ORDER BY time ASC")
    List<Record> countOnePersonWithinDay(Map map);

    //还在改项目组的人
    @Select("select person_id  from `project_detail`" +
            "WHERE person_status =1 and project_code = #{projectCode}")
    List<Integer> personInproject(Map map);

    @Select("select  person_id from `person` WHERE person_name =#{personName}")
    List<Integer> findIdByName(String personName);









}
