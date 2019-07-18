package com.real.name.person.service.repository;

import com.real.name.person.entity.PersonQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonQueryMapper {

    public PersonQuery find();

}
