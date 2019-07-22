package com.real.name.person.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonQueryMapper {

    List<Person> searchPerson(@Param("personQuery") PersonQuery personQuery);

}
