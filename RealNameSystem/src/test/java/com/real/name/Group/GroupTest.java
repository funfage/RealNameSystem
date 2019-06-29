package com.real.name.Group;

import com.real.name.BaseTest;
import com.real.name.group.service.repository.GroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupTest extends BaseTest {

    @Autowired
    private GroupRepository repository;

    @Test
    public void deleteByTeamSysNo() {
        int i = repository.deleteByTeamSysNo(1234);
        Assert.assertEquals(1, i);
    }

}
