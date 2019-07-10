package com.real.name.Group;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.others.BaseTest;
import com.real.name.group.service.repository.GroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class GroupTest extends BaseTest {

    @Autowired
    private GroupRepository repository;

    @Test
    public void deleteByTeamSysNo() {
        int i = repository.deleteByTeamSysNo(1234);
        Assert.assertEquals(1, i);
    }

    @Test
    public void findByIsAdminGroup() {
        Optional<WorkerGroup> groupOptional = repository.findByIsAdminGroup(1);
        System.out.println(groupOptional.get());
    }

    @Test
    public void findByIsAdminGroupAndProjectCode() {
        Optional<WorkerGroup> workerGroup = repository.findByIsAdminGroupAndProjectCode(1, "44010620190510008");
        System.out.println(workerGroup.get());
    }

}
