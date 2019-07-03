package com.real.name.IssueDetai;

import com.real.name.BaseTest;
import com.real.name.common.result.ResultVo;
import com.real.name.face.entity.Device;
import com.real.name.project.entity.IssueDetail;
import com.real.name.project.service.repository.IssueDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueDetailTest extends BaseTest {

    @Autowired
    private IssueDetailRepository repository;

    @Test
    public void findIssueStatus() {
        List<Integer> ids1 = repository.findIdByIssueStatus(1, 1);
        List<Integer> ids2 = repository.findIdByIssuePersonStatus(1);
        List<Integer> ids3 = repository.findIdByIssueImageStatus(1);
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("ids1", ids1);
        map.put("ids2", ids2);
        map.put("ids3", ids3);
        System.out.println(map);
    }

    @Test
    public void findByConditionTest() {
        List<IssueDetail> issueDetailList = repository.findByCondition(87, "6667", "44010620190510008");
        List<Long> issueDeIntegers = new ArrayList<>();
        if (issueDetailList.size() > 0) {
            for (int i = 0; i < issueDetailList.size(); i++) {
                if (i == 0) {
                    IssueDetail select = issueDetailList.get(0);
                    select.setIssueImageStatus(1);
                    select.setPersonId(88);
                    select.setIssuePersonStatus(1);
                    repository.save(select);
                } else {
                    issueDeIntegers.add(issueDetailList.get(i).getIssueId());
                }
            }
            if (issueDeIntegers.size() > 0) {
                repository.deleteByIssueIdIn(issueDeIntegers);
            }
        } else {
            IssueDetail issueDetail = new IssueDetail(88, new Device("6667"), "44010620190510008");
            repository.save(issueDetail);
        }

    }
}
