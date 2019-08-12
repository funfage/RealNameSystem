package com.real.name.Group;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.query.GroupQuery;
import com.real.name.group.service.repository.GroupQueryMapper;
import com.real.name.group.service.repository.GroupRepository;
import com.real.name.others.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GroupTest extends BaseTest {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private GroupQueryMapper groupQueryMapper;

    @Test
    public void deleteByTeamSysNo() {
        int i = groupQueryMapper.deleteByTeamSysNo(123);
        Assert.assertEquals(1, i);
    }

    @Test
    public void searchGroup() {
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setTeamSysNo(1500162326);
        groupQueryMapper.searchGroup(groupQuery);
    }

    @Test
    public void judgeExistByTeamName() {
        Integer integer = groupQueryMapper.judgeExistByTeamNameAndSubContractor("梦之队", 1);
        System.out.println(integer);
    }

    @Test
    public void findByProjectCode() {
        List<GroupQuery> byProjectCode = groupQueryMapper.findByProjectCode("44010620190510008");
        System.out.println(byProjectCode);
    }

    @Test
    public void judgeExistAdminGroupByProjectCode() {
        Integer integer = groupQueryMapper.judgeExistAdminGroupByProjectCode("7gU44Drw914wQe8aYK245ks8v32Iu50X");
        System.out.println(integer);
    }

    @Test
    public void setProGroupRemoveStatus() {
        int i = groupQueryMapper.setProGroupRemoveStatus(1563331590);
        System.out.println(i);
    }

    @Test
    public void findRemoveGroupInContract() {
        List<WorkerGroup> removeGroupInContract = groupQueryMapper.findRemoveGroupInContract(1);
        System.out.println(removeGroupInContract);
    }

}
