package com.real.name.nation.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.constant.NationConstant;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.entity.query.GroupQuery;
import com.real.name.group.service.GroupService;
import com.real.name.nation.service.NationService;
import com.real.name.nation.utils.NationalUtils;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectService;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.service.SubContractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NationServiceImpl implements NationService {

    private Logger logger = LoggerFactory.getLogger(NationServiceImpl.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SubContractorService subContractorService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PersonService personService;

    @Override
    public void uploadProject(Set<String> projectCodeList) {
        //根据projectCode集合查询所有项目信息
        List<Project> projectList = projectService.findAllInProjectCode(projectCodeList);
        for (Project project : projectList) {
            Project update = new Project();
            if (project.getIsUpload() == null || project.getIsUpload() == 0) { //项目上传
                JSONObject jsonObject = NationalUtils.uploadProject(project);
                //获取返回的projectCode
                String newProjectCode = jsonObject.getString("data");
                //修改项目的编码
                Integer integer = projectService.updateProjectCode(project.getProjectCode(), newProjectCode);
                if (integer <= 0) {
                    logger.error("全国平台上传后，修改项目编码失败");
                    throw new AttendanceException(ResultError.OPERATOR_ERROR);
                }
            } else if (project.getIsUpload() == -1) { //修改未上传
                NationalUtils.uploadUpdateProject(project);
                //修改项目修改未上传的标识
                project.setIsUpload(1);
            }
            //修改上传成标识
            update.setProjectCode(project.getProjectCode());
            update.setIsUpload(1);
            if (projectService.updateByProjectCode(project) == null) {
                logger.error("修改上传标识失败，项目编码为：{}，项目名为：{}", project.getProjectCode(), project.getName());
                throw new AttendanceException(ResultError.NATION_UPLOAD_FAILURE);
            }
        }

    }

    @Override
    public void uploadSubContractor(List<Integer> subContractorIdList, String projectCode) {
        Integer status = projectService.findUploadStatusByProjectCode(projectCode);
        //如果参建单位所在的项目未上传到全国平台则返回错误
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.PROJECT_UN_UPLOAD_NATION);
        }
        //获取所有需要上传的参建单位信息
        List<SubContractorQuery> subContractorQueryList = subContractorService.findByIdList(subContractorIdList);
        for (SubContractorQuery sub : subContractorQueryList) {
            if (sub.getUploadStatus() != null || sub.getUploadStatus() == NationConstant.unUpload) { //参建单位上传
                NationalUtils.uploadSubContractor(sub);
            } else if (sub.getUploadStatus() == NationConstant.updateUnUpload) {//修改上传
                NationalUtils.uploadUpdateSubContractor(sub);
            }
            //修改上传标识
            SubContractor update = new SubContractor();
            update.setSubContractorId(sub.getSubContractorId());
            update.setUploadStatus(NationConstant.uploadSuccess);
            subContractorService.updateSubContractorById(update);
        }
    }

    @Override
    public void uploadWorkerGroup(List<Integer> groupIdList, Integer subContractorId) {
        Integer status = subContractorService.findUploadStatusById(subContractorId);
        //如果班组所在的参建单位未上传到全国平台则返回错误
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.SUBCONTRACTOR_UN_UPLOAD_NATION);
        }
        //根据id集合查询班组信息
        List<GroupQuery> groupQueryList = groupService.findByIdList(groupIdList);
        for (GroupQuery group : groupQueryList) {
            WorkerGroup update = new WorkerGroup();
            if (group.getTeamSysNo() == null || group.getTeamSysNo() == 0) {
                JSONObject jsonObject = NationalUtils.uploadGroup(group);
                //获取全国平台返回的teamSysNo
                Integer newTeamSysNo = (Integer) jsonObject.get("data");
                //修改班组的teamSysNo
                Integer integer = groupService.updateTeamSysNo(group.getTeamSysNo(), newTeamSysNo);
                if (integer <= 0) {
                    throw new AttendanceException(ResultError.OPERATOR_ERROR);
                }
            } else if (group.getUploadStatus() == -1) {
                NationalUtils.uploadUpdateGroup(group);
            }
            update.setTeamSysNo(group.getTeamSysNo());
            update.setGroupStatus(1);
            groupService.updateByTeamSysNo(update);
        }
    }

    @Override
    public void uploadPerson(List<Integer> personIdList, Integer teamSysNo, String projectCode) {
        //判断人员所在的班组是否上传
        Integer status = groupService.findUploadStatusById(teamSysNo);
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.WORKER_GROUP_UN_UPLOAD_NATION);
        }
        List<ProjectDetailQuery> projectDetailQueryList = personService.findPeopleUploadInfo(projectCode, teamSysNo, personIdList);
        for (ProjectDetailQuery projectDetailQuery: projectDetailQueryList) {
            Person person = projectDetailQuery.getPerson();
            if (person.getUploadStatus() == null || person.getUploadStatus() == 0) { //人员上传
                NationalUtils.uploadPerson(projectDetailQuery);
            } else if (person.getUploadStatus() == -1) {//修改上传
                NationalUtils.uploadUpdatePerson(projectDetailQuery);
            }
            //修改上传标识
            Person update = new Person();
            update.setPersonId(person.getPersonId());
            update.setUploadStatus(NationConstant.uploadSuccess);
            personService.updatePerson(update);
        }
    }

    @Override
    public void uploadContractor(List<Integer> contractorIdList) {
        //获取所有需要上传的人员合同信息

    }

}
