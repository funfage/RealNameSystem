package com.real.name.project.service.repository;

import com.real.name.project.entity.Project;
import com.real.name.project.query.ProjectQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectQueryMapper {

    List<Project> searchProject(@Param("projectQuery") ProjectQuery projectQuery);

}
