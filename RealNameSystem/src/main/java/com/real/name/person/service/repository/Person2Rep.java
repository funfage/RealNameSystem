package com.real.name.person.service.repository;

import com.real.name.person.entity.Person2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Person2Rep extends JpaRepository<Person2, Integer> {

    List<Person2> findByPersonIdIn(List<Integer> personIds);
}
