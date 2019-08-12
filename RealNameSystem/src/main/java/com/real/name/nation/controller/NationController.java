package com.real.name.nation.controller;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.nation.utils.NationalUtils;
import com.real.name.subcontractor.entity.SubContractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("nation")
public class NationController {

    /**
     * 上传项目信息
     */
    @PostMapping("/uploadProject")
    public ResultVo uploadProject() {
        return ResultVo.success();
    }

    /**
     * 修改全国平台项目信息信息
     * @return
     */
    @PostMapping("/updateProject")
    public ResultVo updateProject() {
        return ResultVo.success();
    }

    /**
     * 上传参建单位信息
     */
    @PostMapping("/uploadSubContractor")
    public ResultVo uploadSubContractor(SubContractor subContractor) {
        return ResultVo.success();
    }

    /**
     * 修改全国平台参建单位信息
     */
    @PostMapping("/updateSubContractor")
    public ResultVo updateSubContractor() {
        return ResultVo.success();
    }

    /**
     * 上传班组信息
     */
    @PostMapping("/uploadWorkerGroup")
    public ResultVo uploadWorkerGroup() {
        /*// 判断是否需要上传到全国平台
        if (project.getIsUpload() != null && project.getIsUpload() == 1) {
            JSONObject gr = NationalUtils.uploadGroup(group);
            //判断是否上传成功
            if(gr.getBoolean("error")){
                return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + ":" + gr.getString("message"));
            }
            JSONObject data = gr.getJSONObject("data");
            if (data.toString().contains("teamSysNo")) {
                JSONObject result = data.getJSONObject("result");
                Integer teamSysNo = result.getInteger("teamSysNo");
                if (teamSysNo != null && teamSysNo > 0) {
                    group.setTeamSysNo(teamSysNo);
                    groupService.create(group);
                    return ResultVo.success(group);
                } else {
                    logger.error("uploadGroup error 没有班组编号信息");
                    return ResultVo.failure(ResultError.NATIONAL_ERROR);
                }
            } else {
                return ResultVo.failure(ResultError.NATIONAL_ERROR);
            }
        } else {//否则只存到本地数据库

        }*/
        return ResultVo.success();
    }

    /**
     * 修改全国平台班组信息
     */
    @PostMapping("/updateWorkerGroup")
    public ResultVo updateWorkerGroup() {
        /*try {
            //修改全国平台班组信息
            JSONObject jsonObject = NationalUtils.updateGroup(selectWorkerGroup);
            if (jsonObject.getBoolean("error")) {
                return ResultVo.failure(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + jsonObject.getString("message"));
            }

            return ResultVo.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.failure(e.getMessage());
        }*/
        return ResultVo.success();
    }

    /**
     * 上传人员信息
     */
    @PostMapping("/uploadPerson")
    public ResultVo uploadPerson() {
        //查询用户是否绑定项目
        return ResultVo.success();
    }

    /**
     * 修改全国平台班组信息
     */
    @PostMapping("/updatePerson")
    public ResultVo updatePerson() {
        //boolean attendProject = true;
        //查询工人是否参加项目
        /*Optional<ProjectPersonDetail> projectPerson = projectPersonDetailService.findByPerson(person);
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
        }*/
         /*if(attendProject){
            ProjectPersonDetail projectPersonDetail = projectPerson.get();
            //设置修改后的person
            projectPersonDetail.setPerson(selectPerson);
            //修改全国项目工人信息
            JSONObject jsonObject = NationalUtils.updateProjectPerson(projectPersonDetail);
            if(jsonObject.getBoolean("error")){
                throw new AttendanceException(ResultError.NATIONAL_ERROR.getCode(), ResultError.NATIONAL_ERROR.getMessage() + jsonObject.getString("message"));
            }
        }*/
        return ResultVo.success();
    }

}
