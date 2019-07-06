package com.real.name.issue.controller;

import com.real.name.common.result.ResultVo;
import com.real.name.issue.service.IssueDetailService;
import com.real.name.person.entity.Person2;
import com.real.name.person.service.Person2Servcice;
import com.real.name.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("issue")
public class IssueDetailController {
    private Logger logger = LoggerFactory.getLogger(IssueDetailController.class);

    @Autowired
    private IssueDetailService issueDetailService;

    @Autowired
    private PersonService personService;

    @Autowired
    private Person2Servcice person2Servcice;

    @GetMapping("getFailureIssue")
    public ResultVo getFailureIssue(@RequestParam(value = "deviceId", defaultValue = "") String deviceId,
                                    @RequestParam(value = "projectCode", defaultValue = "") String projectCode,
                                    @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        try {
            PageRequest page = PageRequest.of(pageIndex, pageSize);
            Page<Integer> pageIssue = issueDetailService.findFailureIssue(projectCode, deviceId, page);
            List<Integer> failurePersonIds = pageIssue.getContent();
            List<Person2> personList = person2Servcice.findByPersonIdIn(failurePersonIds);
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("pageSize", pageIssue.getPageable().getPageSize());
            modelMap.put("totalSize", pageIssue.getTotalPages());
            modelMap.put("personList", personList);
            return ResultVo.success(modelMap);
        } catch (Exception e) {
            logger.error("getFailureIssue error e:{}", e);
            return ResultVo.failure();
        }
    }

}
