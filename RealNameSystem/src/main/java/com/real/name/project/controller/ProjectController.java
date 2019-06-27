package com.real.name.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.NationalUtils;
import com.real.name.face.entity.Record;
import com.real.name.face.service.RecordService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.person.entity.Person2;
import com.real.name.project.entity.Project;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private RecordService recordService;

    /**
     * 创建项目
     */
    @PostMapping("/create")
    public ResultVo create(Project project) {

        Project p = projectService.create(project);
        return ResultVo.success(p);
    }

    /**
     * 查询项目
     */
    @GetMapping("/find")
    public Object find(@RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {

        // 从全国对接平台查询项目
        Object projects = NationalUtils.queryProject(pageIndex, pageSize);

        if (projects instanceof Map) {
            Map map = (Map) projects;
            Map data = (Map) map.get("data");
            List rows = (List) data.get("rows");
            for (Object row : rows) {
                JSONObject j = (JSONObject)row;
                Project project = j.toJavaObject(Project.class);
                System.out.println(project);
                projectService.create(project);
            }
        }
        return projects;
    }

    /**
     * 项目添加人员
     */
    @PostMapping("/addPerson")
    public ResultVo addPerson(@RequestParam(value = "persons[]") Integer[] persons,
                              @RequestParam("projectCode") String projectCode,
                              @RequestParam("teamSysNo") Integer teamSysNo) {
        // 判断是存在该项目
        Optional<Project> project = projectService.findByProjectCode(projectCode);
        if (!project.isPresent()) {
            throw new AttendanceException(ResultError.PROJECT_NOT_EXIST);
        }

        // 判断是否存在该班组
        Optional<WorkerGroup> group = groupService.findById(teamSysNo);
        if (!group.isPresent()) {
            throw new AttendanceException(ResultError.GROUP_NOT_EXIST);
        }

        // 添加人员到项目中
        for (Integer personId : persons) {
            projectDetailService.addPersonToProject(projectCode, teamSysNo, personId);
        }
        return ResultVo.success();
    }

    /**
     * 获取项目中的人员
     */
    @GetMapping("/personInProject")
    public ResultVo personInProject(@RequestParam("projectCode") String  projectCode,
                                    @RequestParam(name = "pageIndex", defaultValue = "0") Integer page,
                                    @RequestParam(name = "pageSize", defaultValue = "20") Integer size) {

        Map<String, Object> personList = projectDetailService.getPersonInProject(projectCode, PageRequest.of(page, size));
        return ResultVo.success(personList);
    }

    @GetMapping("/test")
    public ResultVo test() {

        Map<String, String> m = new HashMap<>();
        m.put("pass", "12345678");
        m.put("id", "-1");

        return ResultVo.success(m);
    }


//    /**
//     * 查询项目考勤记录
//     */
//    @GetMapping("/attendancePerson")
//    public ResultVo attendancePerson(@RequestParam("projectId") Integer projectId,
//                                     @RequestParam("beginTime") Long beginTime,
//                                     @RequestParam("endTime") Long endTime /*,
//                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                     @RequestParam(name = "size", defaultValue = "20") Integer size*/) {
//        // 获取项目中全部人员
//        Map<String, Object> personMap = projectDetailService.getPersonInProject(projectId, PageRequest.of(0, 999));
//        List<Person2> personList = (List<Person2>) personMap.get("personList");
//
//        List<Object> recordList = new ArrayList<>();
//
//        // 遍历人员，在时间范围内是否有考勤记录
//        for (Person2 person : personList) {
//            List<Record> records = recordService.findByPersonIdAndTimeBetween(person.getPersonId(), new Date(beginTime), new Date(endTime));
//            if (records != null && records.size() > 0) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("person", person);
//                map.put("records", records);
//                recordList.add(map);
//            }
//        }
//        return ResultVo.success(recordList);
//    }
}