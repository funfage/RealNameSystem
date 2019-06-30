package com.real.name.project.service;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectPersonDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectPersonDetailService {
    Optional<ProjectPersonDetail> findByPerson(Person person);

    Optional<ProjectPersonDetail> findByPerson_PersonId(Integer personId);

    int deleteByPerson(Person person);

    int deleteByProject(Project project);

    int deleteByWorkerGroup(WorkerGroup workerGroup);


}
