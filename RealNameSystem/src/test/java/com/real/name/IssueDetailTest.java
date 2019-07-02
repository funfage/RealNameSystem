package com.real.name;

import com.real.name.common.result.ResultVo;
import com.real.name.project.service.repository.IssueDetailRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
