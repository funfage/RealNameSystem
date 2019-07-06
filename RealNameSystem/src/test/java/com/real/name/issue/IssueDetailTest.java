package com.real.name.issue;

import com.real.name.BaseTest;
import com.real.name.common.utils.CommonUtils;
import com.real.name.issue.entity.IssueDetail;
import com.real.name.issue.service.repository.IssueDetailRepository;
import com.real.name.person.entity.Person;
import com.real.name.person.service.repository.PersonRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueDetailTest extends BaseTest {

    @Autowired
    private IssueDetailRepository repository;

    @Autowired
    private PersonRepository personRepository;

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
    public void findByPageTest() {
        Page<Integer> page = repository.findFailureIssue("067R9HQR0dmw178890C4BKubNU2d9gG7", "6668", PageRequest.of(0, 2));
        List<Integer> content = page.getContent();
        System.out.println(content);
       /* List<Integer> newList = CommonUtils.getIntegerList(new ArrayList<Integer>());
        System.out.println(newList);
        List<Person> personList = personRepository.findByPersonIdIn(newList);
        System.out.println(personList);*/
    }
}
