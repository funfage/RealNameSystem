package com.real.name.Project;

import com.real.name.BaseTest;
import com.real.name.project.service.repository.ProjectDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectDetailTest extends BaseTest {
    @Autowired
    private ProjectDetailRepository repository;

    @Test
    public void deleteByProjectCodeTest(){
        int i = repository.deleteByProjectCode("44010620190510008");
        System.out.println(i);
    }

    @Test
    public void deleteByTeamSysNo() {
        int i = repository.deleteByTeamSysNo(123);
    }
}
