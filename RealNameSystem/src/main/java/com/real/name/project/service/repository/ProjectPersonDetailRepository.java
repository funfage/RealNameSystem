package com.real.name.project.service.repository;

import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectPersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectPersonDetailRepository extends JpaRepository<ProjectPersonDetail, Integer> {
    Optional<ProjectPersonDetail> findByPerson(Person person);
}
