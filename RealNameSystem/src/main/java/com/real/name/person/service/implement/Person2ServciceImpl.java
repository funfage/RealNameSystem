package com.real.name.person.service.implement;

import com.real.name.person.entity.Person2;
import com.real.name.person.service.Person2Servcice;
import com.real.name.person.service.repository.Person2Rep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Person2ServciceImpl implements Person2Servcice {
    @Autowired
    private Person2Rep rep;

    @Override
    public List<Person2> findByPersonIdIn(List<Integer> personIds) {
        return rep.findByPersonIdIn(personIds);
    }
}
