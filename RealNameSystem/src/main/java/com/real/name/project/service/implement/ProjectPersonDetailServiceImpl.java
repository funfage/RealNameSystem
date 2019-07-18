package com.real.name.project.service.implement;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
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

    @Override
    public Optional<ProjectPersonDetail> findByPerson_PersonId(Integer personId) {
        return repository.findByPerson_PersonId(personId);
    }

    @Override
    public int deleteByPerson(Person person) {
        return repository.deleteByPerson(person);
    }

    @Override
    public int deleteByProject(Project project) {
        return repository.deleteByProject(project);
    }

    @Override
    public int deleteByWorkerGroup(WorkerGroup workerGroup) {
        return repository.deleteByWorkerGroup(workerGroup);
    }

}
