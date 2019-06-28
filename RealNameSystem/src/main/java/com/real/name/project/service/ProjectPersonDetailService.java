package com.real.name.project.service;

import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectPersonDetail;

import java.util.Optional;

public interface ProjectPersonDetailService {
    Optional<ProjectPersonDetail> findByPerson(Person person);
}
