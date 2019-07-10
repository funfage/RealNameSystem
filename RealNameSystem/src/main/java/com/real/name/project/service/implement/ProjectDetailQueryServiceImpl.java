package com.real.name.project.service.implement;

import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectDetailQueryServiceImpl implements ProjectDetailQueryService {

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Override
    public List<ProjectDetailQuery> findOtherAdmins(String projectCode) {
        return projectDetailQueryMapper.findOtherAdmins(projectCode);
    }

    @Override
    public List<ProjectDetailQuery> findOtherWorker() {
        return projectDetailQueryMapper.findOtherWorker();
    }

    @Override
    public List<ProjectDetailQuery> getPersonAndWorkerGroupInfo(String projectCode) {
        return projectDetailQueryMapper.getPersonAndWorkerGroupInfo(projectCode);
    }

    @Override
    public List<String> getProjectIdsByPersonId(Integer personId) {
        return projectDetailQueryMapper.getProjectIdsByPersonId(personId);
    }
}
