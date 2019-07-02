package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.service.repository.ProjectDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ProjectDetailTest extends BaseTest {
    @Autowired
    private ProjectDetailRepository repository;

    @Test
    public void findByProjectCodeAndPersonIdAAndTeamSysNo() {
        Optional<ProjectDetail> op = repository.findByProjectCodeAndPersonIdAndTeamSysNo("44010620190510008", 76, 1500162326);
        System.out.println(op.get());
    }

}
