package com.real.name.person.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.PathUtil;
import com.real.name.device.service.DeviceService;
import com.real.name.issue.service.IssueFaceService;
import com.real.name.person.entity.Person;
import com.real.name.person.entity.PersonQuery;
import com.real.name.person.service.PersonService;
import com.real.name.project.service.ProjectDetailQueryService;
import com.real.name.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    private List<String> suffixList = new ArrayList<>();

    @Value("${customize.file-size}")
    private Long imgFileMaxSize;

    @Autowired
    private PersonService personService;


    public PersonController() {
        suffixList.add(".jpg");
        suffixList.add(".png");
        suffixList.add(".jpeg");
    }

    /**
     * 添加工人
     */
    @Transactional
    @PostMapping("/savePerson")
    public ResultVo savePerson(@RequestParam("person") String personStr,
                               @RequestParam("imageFile") MultipartFile imageFile) {
        Person person;
        try {
            person = JSONObject.parseObject(personStr, Person.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("将personStr转换为json字符串出现异常");
            throw AttendanceException.errorMessage(ResultError.OPERATOR_ERROR);
        }
        if (personService.findByIdCardNumber(person.getIdCardNumber()) != null) {
            throw new AttendanceException(ResultError.PERSON_EXIST);
        }
        //判断传入的照片是否为空
        if (imageFile.isEmpty()) {
            throw AttendanceException.emptyMessage("照片");
        }
        //验证输入的参数是否正确
        verifyPerson(person, imageFile);
        //设置图片信息给人员
        setImageInfoToPerson(person, imageFile);
        //保存人员信息到数据库
        Person selectPerson = personService.createPerson(person);
        if (StringUtils.isEmpty(selectPerson.getSuffixName())) {
            throw new AttendanceException(ResultError.OPERATOR_ERROR);
        }
        //保存照片信息
        FileTool.generateFile(imageFile, PathUtil.getImgBasePath(), selectPerson.getPersonId() + selectPerson.getSuffixName());
        Map<String, Object> m = new HashMap<>();
        m.put("personId", selectPerson.getPersonId());
        return ResultVo.success(m);
    }

    /**
     * 删除工人信息并把头像也删除
     * @param personId 员工id
     */
    @Transactional
    @GetMapping("/deletePerson")
    public ResultVo deletePerson(@RequestParam("id") Integer personId,
                                @RequestParam("idCardIndex")String idCardIndex,
                                @RequestParam("suffixName") String suffixName,
                                @RequestParam("workRole") Integer workRole){
        if(personId <= 0){
            throw AttendanceException.errorMessage("人员编号");
        }
        Person person = new Person();
        person.setPersonId(personId);
        person.setWorkRole(workRole);
        person.setIdCardIndex(idCardIndex);
        //删除设备人员信息
        personService.deleteDevicesPersonInfo(person);
        //删除头像
        FileTool.deleteFile(PathUtil.getImgBasePath(), personId + suffixName);
        //删除人员信息
        int effectNum = personService.deleteByPersonId(personId);
        if (effectNum <= 0) {
            throw AttendanceException.errorMessage(ResultError.DELETE_ERROR, "工人");
        } else {
            return ResultVo.success("删除人员信息成功");
        }
    }

    /**
     * 将人员从项目中移除
     */
    @GetMapping("/removePersonInProject")
    public ResultVo removePersonInProject(@RequestParam("personId") Integer personId,
                                          @RequestParam("projectCode") String projectCode) {
        Person person = personService.findRemovePerson(personId);
        personService.removePersonInProject(person, projectCode);
        return ResultVo.success();
    }

    /**
     * 修改员工信息
     * @param personStr 人员的json字符串
     */
    @Transactional
    @PostMapping("updatePerson")
    public ResultVo updatePerson(@RequestParam("person") String personStr,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile){
        Person person;
        try {
            person = JSONObject.parseObject(personStr, Person.class);
        } catch (Exception e) {
            logger.error("updatePerson 人员的json字符串有误");
            return ResultVo.failure();
        }
        if(person.getPersonId() == null || person.getPersonId() <= 0){
            throw AttendanceException.errorMessage("person_id");
        }
        if (person.getWorkRole() == null) {
            throw AttendanceException.emptyMessage("workRole");
        }

        Optional<Person> personOptional = personService.findById(person.getPersonId());
        //人员不存在
        if(!personOptional.isPresent()){
            return ResultVo.failure(ResultError.PERSON_NOT_EXIST);
        }
        Person selectPerson = personOptional.get();
        //判断人员信息是否下发成功, 如果不成功则不允许修改
        String projectName = personService.judgeIssueSuccessToDevices(selectPerson);
        if (projectName != null) {
            return ResultVo.failure(projectName);
        }
        //获取旧的人员名称和人员类型
        String oldName = selectPerson.getPersonName();
        Integer oldWorkRole = selectPerson.getWorkRole();
        //合并信息
        mergePersonInfo(selectPerson, person);
        //校验输入的信息是否正确
        verifyPerson(selectPerson, imageFile);
        //如果传入的照片信息不为空则重新保存照片
        if (!imageFile.isEmpty()) {
            setImageInfoToPerson(selectPerson, imageFile);
        }
        //修改本地工人信息
        Person updatePerson = personService.updatePerson(selectPerson);
        if (updatePerson == null) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
        //判断人员信息是否发生改变, 如果改变则重新下发信息到人脸设备
        personService.updateDevicesPersonInfo(updatePerson, oldName, oldWorkRole);
        //如果传入的照片信息不为空则重新下发照片信息到设备
        if (!imageFile.isEmpty()) {
            personService.updateDevicesImage(updatePerson);
        }
        return ResultVo.success();
    }



    /**
     * ========================================以下只与查询有关===============================================
     */

    /**
     * 根据id查找人员，若id=-1则查找全部人员
     */
    @GetMapping("/findPerson")
    public ResultVo findPerson(@RequestParam("personId") Integer personId,
                         @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "workRole", defaultValue = "0") Integer workRole) {
        return personService.findMainPagePerson(personId, pageNum, pageSize, workRole);
    }

    @PostMapping("/searchPerson")
    public ResultVo searchPerson(PersonQuery personQuery) {
        PageHelper.startPage(personQuery.getPageNum() + 1, personQuery.getPageSize());
        List<Person> personList = personService.searchPerson(personQuery);
        PageInfo<Person> pageInfo = new PageInfo<>(personList);
        Map<String, Object> map = new HashMap<>();
        map.put("personList", personList);
        map.put("pageSize", pageInfo.getPageSize());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("total", pageInfo.getTotal());
        return ResultVo.success(map);
    }

    private void mergePersonInfo(Person selectPerson, Person person) {
        //非空值设置
        if(!StringUtils.isEmpty(person.getPersonName())){
            selectPerson.setPersonName(person.getPersonName());
        }
        if (!StringUtils.isEmpty(person.getSubordinateCompany())) {
            selectPerson.setSubordinateCompany(person.getSubordinateCompany());
        }
        if(!StringUtils.isEmpty(person.getIdCardNumber())){
            //判断输入的身份证号码是否正确
            if (person.getIdCardNumber().trim().length() != 18) {
                throw new AttendanceException(ResultError.ID_CARD_ERROR);
            }
            selectPerson.setIdCardNumber(person.getIdCardNumber());
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
    }

    private void verifyPerson(Person person, MultipartFile imageFile) {
        if (person.getIdCardNumber() == null || person.getIdCardNumber().trim().length() != 18) {
            throw new AttendanceException(ResultError.ID_CARD_ERROR);
        } else if (!StringUtils.hasText(person.getPersonName())) {
            throw new AttendanceException(ResultError.PERSON_NAME_ERROR);
        } else if (!StringUtils.hasText(person.getSubordinateCompany())) {
            throw new AttendanceException(ResultError.COMPANY_EMPTY);
        } else if (!StringUtils.hasText(person.getNation())) {
            throw AttendanceException.emptyMessage("名族");
        } else if (person.getGender() == null || person.getGender() > 3) {
            throw AttendanceException.errorMessage("性别");
        } else if (!StringUtils.hasText(person.getGrantOrg())) {
            throw AttendanceException.emptyMessage("签发机关");
        } else if (!StringUtils.hasText(person.getAddress())) {
            throw AttendanceException.emptyMessage("地址");
        } else if (person.getStartDate() != null) {
            if (person.getExpiryDate() == null) {
                throw AttendanceException.errorMessage("证件有效期开始日期不能大于结束日期");
            }
            if (person.getStartDate().getTime() > person.getExpiryDate().getTime()) {
                throw AttendanceException.errorMessage("证件有效期开始日期不能大于结束日期");
            }
        }
    }

    private void setImageInfoToPerson(Person person, MultipartFile imageFile) {
        String suffixName = FileTool.getSuffixName(imageFile);
        if (!suffixList.contains(suffixName)) {
            throw new AttendanceException(ResultError.IMAGE_TYPE_ERROR);
        }
        //设置文件后缀名
        person.setSuffixName(suffixName);
        //判断照片大小是否符合要求
        if (imageFile.getSize() > imgFileMaxSize) {
            throw new AttendanceException(ResultError.IMAGE_SIZE_ERROR);
        }
        String imageStr = null;
        try {
            imageStr = FileTool.fileToBase64(imageFile.getInputStream());
        } catch (IOException e) {
            logger.error("照片转换成base64编码失败, e:{}", e.getMessage());
            throw new AttendanceException("照片转换成base64编码失败");
        }
        if (!StringUtils.hasText(imageStr)) {
            logger.error("照片转换成base64编码为空");
            throw new AttendanceException("照片转换成base64编码为空");
        }
        if (person.getPersonId() != null) {
            String fileName = person.getPersonId() + person.getSuffixName();
            String basePath = PathUtil.getImgBasePath();
            //将原有的图片信息删除
            FileTool.deleteFile(basePath, fileName);
            //保存照片信息
            FileTool.generateFile(imageFile, basePath, fileName);
        }
        person.setHeadImage(imageStr.trim());
    }
}