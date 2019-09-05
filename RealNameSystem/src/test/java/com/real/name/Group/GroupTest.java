package com.real.name.Group;

import com.alibaba.fastjson.JSON;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.entity.query.GroupQuery;
import com.real.name.group.entity.search.GroupSearch;
import com.real.name.group.entity.search.GroupSearchInPro;
import com.real.name.group.service.repository.GroupQueryMapper;
import com.real.name.group.service.repository.GroupRepository;
import com.real.name.others.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupTest extends BaseTest {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private GroupQueryMapper groupQueryMapper;

    @Test
    public void insertGroup() {
        WorkerGroup workerGroup = new WorkerGroup();
        workerGroup.setTeamName("teamName");
        workerGroup.setTeamSysNo(3984387);
        workerGroup.setResponsiblePersonName("responsiblePersonName");
        workerGroup.setResponsiblePersonPhone("233233");
        workerGroup.setResponsiblePersonIdCardType(1);
        workerGroup.setResponsiblePersonIdNumber("2323232");
        workerGroup.setRemark("remark");
        workerGroup.setCorpCode("corpCode");
        workerGroup.setCorpName("corpName");
        workerGroup.setSubContractorId(1);
        workerGroup.setEntryTime(new Date());
        workerGroup.setExitTime(new Date());
        workerGroup.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        int i = groupQueryMapper.insertGroup(workerGroup);
        System.out.println(i);
    }

    @Test
    public void deleteByTeamSysNo() {
        int i = groupQueryMapper.deleteByTeamSysNo(123);
        Assert.assertEquals(1, i);
    }

    @Test
    public void searchGroup() {
        GroupSearch groupSearch = new GroupSearch();
        groupSearch.setTeamSysNo(1500162326);
        groupQueryMapper.searchGroup(groupSearch);
    }

    @Test
    public void judgeExistByTeamName() {
        Integer integer = groupQueryMapper.judgeExistByTeamNameAndSubContractor("梦之队", 1);
        System.out.println(integer);
    }

    @Test
    public void findAll() {
        List<GroupQuery> all = groupQueryMapper.findAll();
        System.out.println(all);
    }

    @Test
    public void findById() {
        GroupQuery byId = groupQueryMapper.findById(3984387);
        System.out.println(byId);
        System.out.println(JSON.toJSONString(byId));
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
        List<WorkerGroup> removeGroupInContract = groupQueryMapper.getGroupNameInContractor(1, 1);
        System.out.println(removeGroupInContract);
    }

    @Test
    public void findByProjectCodeAndPersonId() {
        WorkerGroup workerGroup = groupQueryMapper.findByProjectCodeAndPersonId("7gU44Drw914wQe8aYK245ks8v32Iu50X", 89);
        System.out.println(workerGroup);
    }

    @Test
    public void updateWorkerGroup() {
        GroupQuery group = new GroupQuery();
        group.setTeamSysNo(3984387);
        group.setCorpCode("2222222222");
        group.setCorpName("2323232323");
        group.setEntryAttachments("7878787878787");
        group.setSubContractorId(1);
        group.setEntryTime(new Date());
        group.setExitTime(new Date());
        group.setExitAttachments("47447474747");
        group.setGroupStatus(1);
        group.setIsAdminGroup(0);
        group.setUploadStatus(1);
        int i = groupQueryMapper.updateWorkerGroup(group);
        System.out.println(i);
    }

    @Test
    public void getGroupInContractor() {
        List<WorkerGroup> groupInContractor = groupQueryMapper.getGroupInContractor(1, 1);
        System.out.println(groupInContractor);
    }

    @Test
    public void findTeamNameByTeamSysNo() {
        String teamNameByTeamSysNo = groupQueryMapper.findTeamNameByTeamSysNo(3984387);
        System.out.println(teamNameByTeamSysNo);
    }

    @Test
    public void searchGroupInPro() {
        GroupSearchInPro groupQuery = new GroupSearchInPro();
        groupQuery.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        List<GroupQuery> groupQueries = groupQueryMapper.searchGroupInPro(groupQuery);
        System.out.println(groupQueries);
        groupQuery.setCorpName("设有");
        List<GroupQuery> groupQueries1 = groupQueryMapper.searchGroupInPro(groupQuery);
        System.out.println(groupQueries1);
        groupQuery.setTeamSysNo(1562649811);
        List<GroupQuery> groupQueries2 = groupQueryMapper.searchGroupInPro(groupQuery);
        System.out.println(groupQueries2);
    }

    @Test
    public void findUploadByTeamSysNo() {
        Integer uploadByTeamSysNo = groupQueryMapper.findUploadStatusById(1562649811);
        System.out.println(uploadByTeamSysNo);
    }

    @Test
    public void findByIdList() {
        List<Integer> idList = new ArrayList<>();
        idList.add(1562649690);
        idList.add(1562649811);
        List<GroupQuery> byIdList = groupQueryMapper.findByIdList(idList);
        System.out.println(byIdList);
    }

    @Test
    public void updateTeamSysNo() {
        Integer integer = groupQueryMapper.updateTeamSysNo(1534837444, 1534837555);
        System.out.println(integer);
    }


}





















