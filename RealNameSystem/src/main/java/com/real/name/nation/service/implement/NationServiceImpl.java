package com.real.name.nation.service.implement;

import com.alibaba.fastjson.JSONObject;
import com.real.name.common.constant.NationConstant;
import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.entity.query.ContractInfoQuery;
import com.real.name.contract.service.ContractInfoService;
import com.real.name.contract.service.repository.ContractInfoMapper;
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

import java.util.ArrayList;
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

    @Autowired
    private ContractInfoService contractInfoService;

    @Override
    public List<String> uploadProject(Set<String> projectCodeList) {
        List<String> failNameList = new ArrayList<>();
        //根据projectCode集合查询所有项目信息
        List<Project> projectList = projectService.findAllInProjectCode(projectCodeList);
        for (Project project : projectList) {
            try {
                if (project.getIsUpload() == null || project.getIsUpload() == NationConstant.unUpload) { //项目上传
                    JSONObject jsonObject = NationalUtils.uploadProject(project);
                    //获取返回的projectCode
                    String newProjectCode = jsonObject.getString("data");
                    //修改项目的编码
                    Integer integer = projectService.updateProjectCode(project.getProjectCode(), newProjectCode);
                    if (integer <= 0) {
                        logger.error("全国平台上传后，修改项目名为：{}的项目编码失败", project.getName());
                        failNameList.add(project.getName());
                    }
                } else if (project.getIsUpload() == NationConstant.updateUnUpload) { //修改未上传
                    NationalUtils.uploadUpdateProject(project);
                    //修改项目修改未上传的标识
                    project.setIsUpload(1);
                } else {
                    continue;
                }
                //修改上传成标识
                Project update = new Project();
                update.setProjectCode(project.getProjectCode());
                update.setIsUpload(1);
                projectService.updateByProjectCode(project);
            } catch (Exception e) {
                logger.error("项目编码为：{}，项目名为：{}的项目上传到全国平台失败", project.getProjectCode(), project.getName());
                failNameList.add(project.getName());
            }
            return failNameList;
        }
        return failNameList;
    }

    @Override
    public List<String> uploadSubContractor(List<Integer> subContractorIdList, String projectCode) {
        Integer status = projectService.findUploadStatusByProjectCode(projectCode);
        //如果参建单位所在的项目未上传到全国平台则返回错误
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.PROJECT_UN_UPLOAD_NATION);
        }
        List<String> failNameList = new ArrayList<>();
        //获取所有需要上传的参建单位信息
        List<SubContractorQuery> subContractorQueryList = subContractorService.findByIdList(subContractorIdList);
        for (SubContractorQuery sub : subContractorQueryList) {
            try {
                if (sub.getUploadStatus() == null || sub.getUploadStatus() == NationConstant.unUpload) { //参建单位上传
                    NationalUtils.uploadSubContractor(sub);
                } else if (sub.getUploadStatus() == NationConstant.updateUnUpload) {//修改上传
                    NationalUtils.uploadUpdateSubContractor(sub);
                } else { //已经上传
                    continue;
                }
                //修改上传标识
                SubContractor update = new SubContractor();
                update.setSubContractorId(sub.getSubContractorId());
                update.setUploadStatus(NationConstant.uploadSuccess);
                subContractorService.updateSubContractorById(update);
            } catch (Exception e) {
                logger.error("参建单位id为：{}, 参建单位名称为：{}的参建单位上传到全国平台失败");
                failNameList.add(sub.getCorpName());
            }
        }
        return failNameList;
    }

    @Override
    public List<String> uploadWorkerGroup(List<Integer> groupIdList, Integer subContractorId) {
        Integer status = subContractorService.findUploadStatusById(subContractorId);
        //如果班组所在的参建单位未上传到全国平台则返回错误
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.SUBCONTRACTOR_UN_UPLOAD_NATION);
        }
        List<String> failNameList = new ArrayList<>();
        //根据id集合查询班组信息
        List<GroupQuery> groupQueryList = groupService.findByIdList(groupIdList);
        for (GroupQuery group : groupQueryList) {
            try {
                WorkerGroup update = new WorkerGroup();
                if (group.getUploadStatus() == null || group.getUploadStatus() == NationConstant.unUpload) {
                    JSONObject jsonObject = NationalUtils.uploadGroup(group);
                    //获取全国平台返回的teamSysNo
                    Integer newTeamSysNo = (Integer) jsonObject.get("data");
                    //修改班组的teamSysNo
                    Integer integer = groupService.updateTeamSysNo(group.getTeamSysNo(), newTeamSysNo);
                    if (integer <= 0) {
                        logger.error("修改班组名为：{}的班组编码失败", group.getTeamName());
                        failNameList.add(group.getTeamName());
                    }
                } else if (group.getUploadStatus() == NationConstant.updateUnUpload) { //修改未上传
                    NationalUtils.uploadUpdateGroup(group);
                } else { //已经上传
                    continue;
                }
                update.setTeamSysNo(group.getTeamSysNo());
                update.setGroupStatus(1);
                groupService.updateByTeamSysNo(update);
            } catch (Exception e) {
                logger.error("班组号为：{}，班组名为：{}的班组上传到全国平台失败", group.getTeamSysNo(), group.getTeamName());
                failNameList.add(group.getTeamName());
            }
        }
        return failNameList;
    }

    @Override
    public List<String> uploadPerson(List<Integer> personIdList, Integer teamSysNo, String projectCode) {
        //判断人员所在的班组是否上传
        Integer status = groupService.findUploadStatusById(teamSysNo);
        if (status == null || status == NationConstant.unUpload) {
            throw new AttendanceException(ResultError.WORKER_GROUP_UN_UPLOAD_NATION);
        }
        List<String> failNameList = new ArrayList<>();
        List<ProjectDetailQuery> projectDetailQueryList = personService.findPeopleUploadInfo(projectCode, teamSysNo, personIdList);
        for (ProjectDetailQuery projectDetailQuery: projectDetailQueryList) {
            Person person = projectDetailQuery.getPerson();
            try {
                if (person.getUploadStatus() == null || person.getUploadStatus() == NationConstant.unUpload) { //人员上传
                    NationalUtils.uploadPerson(projectDetailQuery);
                } else if (person.getUploadStatus() == NationConstant.updateUnUpload) {//修改上传
                    NationalUtils.uploadUpdatePerson(projectDetailQuery);
                } else { //否则已经上传，无需在上传
                    continue;
                }
                //修改上传标识
                Person update = new Person();
                update.setPersonId(person.getPersonId());
                update.setUploadStatus(NationConstant.uploadSuccess);
                personService.updatePerson(update);
            } catch (Exception e) {
                logger.error("人员id为：{}，人员姓名为：{}的人员上传到全国平台失败", person.getPersonId(), person.getPersonName());
                failNameList.add(person.getPersonName());
            }
        }
        return failNameList;
    }

    @Override
    public List<String> uploadContractor(List<Integer> contractorIdList) {
        //获取所有需要上传的人员合同信息
        List<ContractInfoQuery> contractInfoQueryList = contractInfoService.findUploadContractsInfo(contractorIdList);
        List<String> failNameList = new ArrayList<>();
        for (ContractInfoQuery contractInfoQuery : contractInfoQueryList) {
            //判断人员信息是否上传
            Person person = contractInfoQuery.getProjectDetailQuery().getPerson();
            if (person.getUploadStatus() == null || person.getUploadStatus() == NationConstant.unUpload) {
                failNameList.add(person.getPersonName());
            } else { //人员信息已经上传
                try {
                    if (contractInfoQuery.getUploadStatus() == null || contractInfoQuery.getUploadStatus() == NationConstant.unUpload) {
                        //合同信息上传
                        NationalUtils.uploadContractInfo(contractInfoQuery);
                    }else { //合同信息已经上传（不需要考虑修改上传）
                        continue;
                    }
                    //修改合同信息上传标识
                    ContractInfo update = new ContractInfo();
                    update.setContractId(contractInfoQuery.getContractId());
                    update.setUploadStatus(NationConstant.uploadSuccess);
                    contractInfoService.updateContractInfo(update);
                } catch (Exception e) {
                    logger.error("合同id为：{}，人员名为：{}的合同上传失败", contractInfoQuery.getContractId(), person.getPersonName());
                    failNameList.add(person.getPersonName());
                }
            }
        }
        return failNameList;
    }

}
