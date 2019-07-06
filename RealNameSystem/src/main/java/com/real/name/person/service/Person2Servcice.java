package com.real.name.person.service;

import com.real.name.person.entity.Person2;

import java.util.List;

public interface Person2Servcice {

    List<Person2> findByPersonIdIn(List<Integer> personIds);

}
