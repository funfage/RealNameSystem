package com.real.name.group.service.repository;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupQueryMapper {

    List<WorkerGroup> searchGroup(@Param("groupQuery") GroupQuery groupQuery);

}

