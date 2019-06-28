package com.real.name.project.service.implement;

import com.real.name.person.entity.Person;
import com.real.name.project.entity.ProjectPersonDetail;
import com.real.name.project.service.ProjectPersonDetailService;
import com.real.name.project.service.repository.ProjectPersonDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectPersonDetailServiceImpl implements ProjectPersonDetailService {

    @Autowired
    private ProjectPersonDetailRepository repository;

    @Override
    public Optional<ProjectPersonDetail> findByPerson(Person person) {
        return repository.findByPerson(person);
    }
}
