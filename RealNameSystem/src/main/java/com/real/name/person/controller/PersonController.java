package com.real.name.person.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.HTTPTool;
import com.real.name.common.utils.ImageTool;
import com.real.name.common.utils.NationalUtils;
import com.real.name.face.entity.Device;
import com.real.name.face.entity.Record;
import com.real.name.face.service.RecordService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.person.service.WebSocket;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectPersonDetail;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.project.service.ProjectPersonDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/person")
public class PersonController {
    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ProjectPersonDetailService projectPersonDetailService;

    @Autowired
    private ProjectDetailService projectDetailService;

    /**
     * 添加工人
     * @param person
     * @return
     */
    @PostMapping("/savePerson")
    public ResultVo savePerson(@RequestParam(name = "person") String person) {

        if (person.contains("data:image/jpeg;base64,")) {
            person = person.replace("data:image/jpeg;base64,", "");
        }
        Person p = JSON.parseObject(person, Person.class);
        //保存人员信息到数据库
        p = personService.create(p);
        //保存头像
        ImageTool.generateImage(p.getHeadImage(), p.getPersonId().toString());
        Map<String, Object> m = new HashMap<>();
        m.put("personId", p.getPersonId());
        return ResultVo.success(m);
    }

    /**
     * 删除工人信息并把头像也删除
     * @param personId 员工id
     * @return
     */
    @GetMapping("deletePerson")
    public ResultVo deletePerson(@RequestParam("id") Integer personId){
        if(personId <= 0){
            throw AttendanceException.errorMessage("人员编号");
        }
        try {
            //删除personDetail中的信息
            projectDetailService.deleteByPersonId(personId);
            int effectNum = personService.deleteByPersonId(personId);
            if (effectNum <= 0) {
                throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "工人");
            }else{
                //删除头像
                ImageTool.deleteImage(personId.toString());
                return ResultVo.success("删除人员信息成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }
    }

    /**
     * 修改员工信息
     * @param person
     * @return
     */
    @PostMapping("updatePerson")
    public ResultVo updatePerson(@RequestBody Person person){
        if(person.getPersonId() == null || person.getPersonId() <= 0){
            throw AttendanceException.errorMessage("person_id");
        }
        Person selectPerson = null;
        boolean attendProject = true;
        //查询工人是否参加项目
        Optional<ProjectPersonDetail> projectPerson = projectPersonDetailService.findByPerson(person);
        //工人没有参加项目
        if(!projectPerson.isPresent() || projectPerson.get().getPerson() == null){
            //从数据库中查询工人信息
            Optional<Person> optionalPerson = personService.findById(person.getPersonId());
            if(optionalPerson.isPresent()){
                selectPerson = optionalPerson.get();
            }
            //false 标识未参加项目
            attendProject = false;
        }else{
            selectPerson = projectPerson.get().getPerson();
        }
        //人员不存在
        if(selectPerson == null){
            return ResultVo.failure(ResultError.PERSON_NOT_EXIST);
        }
        //非空值设置
        if(!StringUtils.isEmpty(person.getPersonName())){
            selectPerson.setPersonName(person.getPersonName());
        }
        if(!StringUtils.isEmpty(person.getIdCardNumber())){
            //判断输入的身份证号码是否正确
            if (person.getIdCardNumber().trim().length() != 18) {
                throw new AttendanceException(ResultError.ID_CARD_ERROR);
            }else if (personService.findByIdCardNumber(person.getIdCardNumber()).isPresent()) {
                throw new AttendanceException(ResultError.ID_CARD_REPEAT);
            }else{
                selectPerson.setIdCardNumber(person.getIdCardNumber());
            }
        }
        if(!StringUtils.isEmpty(person.getCellPhone())){
            //判断输入的电话号码是否正确
            selectPerson.setCellPhone(person.getCellPhone());
        }
        if(!StringUtils.isEmpty(person.getGender())){
            //判断输入的性别是否正确
            if(person.getGender() > 3){
                throw AttendanceException.errorMessage("性别");
            }
            selectPerson.setGender(person.getGender());
        }
        if(!StringUtils.isEmpty(person.getAge())){
            selectPerson.setAge(person.getAge());
        }
        if(!StringUtils.isEmpty(person.getNation())){
            selectPerson.setNation(person.getNation());
        }
        if(!StringUtils.isEmpty(person.getAddress())){
            selectPerson.setAddress(person.getAddress());
        }
        try {
            if(attendProject){
                ProjectPersonDetail projectPersonDetail = projectPerson.get();
                //设置修改后的person
                projectPersonDetail.setPerson(selectPerson);
                //修改全国项目工人信息
                JSONObject jsonObject = NationalUtils.updateProjectPerson(projectPersonDetail);
                //判断是否修改成功
                String code = jsonObject.getString("code");
                if(!StringUtils.isEmpty(code) && code.equals("-1")){
                    return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + jsonObject.getString("message"));
                }
            }
            //修改本地工人信息
            personService.updateByPersonId(selectPerson);
            return ResultVo.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }
    }

    @PostMapping("takeImg")
    public ResultVo takeImg(@RequestParam(name = "personId") String personId) {
        String url = "face/takeImg";
        Map<String, String> map = new HashMap<>();
        map.put("personId", personId);
        ResultVo rvo = HTTPTool.sendDataTo(url, map);
        System.out.println(rvo);
        return rvo;
    }


    /**
     * 给人员添加照片，必须先创建人员，后添加该人员的照片
     */
    @PostMapping("/face/create")
    public ResultVo faceCreate(@RequestParam("pass") String pass, @RequestParam("personId") Integer personId,
                               @RequestParam("imgBase64") String imgBase64) {

        Person person = personService.saveImgBase64(personId, imgBase64);

        String url = "face/create";

        Map<String, String> map = new HashMap<>();
        map.put("pass", pass);
        map.put("personId", person.getPersonId().toString());
//        map.put("faceId", face.getFaceId().toString());
        map.put("imgBase64", imgBase64);

        return HTTPTool.sendDataTo(url, map);
    }


    /**
     * 根据id查找人员，若id=-1则查找全部人员
     */
    @GetMapping("/find")
    public ResultVo find(@RequestParam("id") Integer id,
                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                         @RequestParam(name = "size", defaultValue = "10") Integer size,
                         @RequestParam("workRole") Integer workRole) {
        PageRequest p = PageRequest.of(page, size);
        //若id为-1则查询所有，否则根据id查询
        return id == -1 ? ResultVo.success(personService.findByWorkRole(p, workRole)) : ResultVo.success(personService.findById(id));
    }

    /**
     * 查询人员考勤记录
     */
    @GetMapping("/findAttendance")
    public ResultVo findAttendance(@RequestParam("personId") Integer personId,
                                   @RequestParam("beginTime") Long beginTime,
                                   @RequestParam("endTime") Long endTime) {

        List<Record> records = recordService.findByPersonIdAndTimeBetween(personId, new Date(beginTime), new Date(endTime));

        return ResultVo.success(records);
    }

    @GetMapping("test")
    public ResultVo test(){
        logger.debug("日志开始");
        return ResultVo.success();
    }
}