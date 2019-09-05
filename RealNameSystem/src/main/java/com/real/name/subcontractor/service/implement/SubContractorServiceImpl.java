package com.real.name.subcontractor.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.device.entity.Device;
import com.real.name.device.service.DeviceService;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.person.entity.Person;
import com.real.name.person.service.PersonService;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.entity.query.GroupPeople;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.entity.search.SubContractorSearchInPro;
import com.real.name.subcontractor.service.SubContractorService;
import com.real.name.subcontractor.service.repository.SubContractorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubContractorServiceImpl implements SubContractorService {

    @Autowired
    private SubContractorMapper subContractorMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PersonService personService;

    @Autowired
    private DeviceService deviceService;

    @Override
    public void saveSubContractor(SubContractor subContractor) {
        int i = subContractorMapper.saveSubContractor(subContractor);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Override
    public void updateSubContractorById(SubContractor subContractor) {
        int i = subContractorMapper.updateSubContractorById(subContractor);
        if (i <= 0) {
            throw new AttendanceException(ResultError.UPDATE_ERROR);
        }
    }

    @Transactional
    @Override
    public void removeSubContractorInProject(Integer subContractorId, String projectCode) {
        //设置参建单位移除标识
        int i = subContractorMapper.setProSubContractorRemoveStatus(subContractorId);
        if (i <= 0) {
            throw new AttendanceException(ResultError.REMOVE_FROM_PROJECT_FAILURE);
        }
        //查询该参建单位下所有未移除的班组名和班组id
        List<WorkerGroup> workerGroupList = groupService.getGroupNameInContractor(subContractorId, 1);
        //依次移除班组
        for (WorkerGroup workerGroup : workerGroupList) {
            groupService.removeGroupInProject(workerGroup.getTeamSysNo(), projectCode);
        }

    }

    @Override
    public List<String> contractorReJoinToProject(String projectCode, Integer subContractorId, List<GroupPeople> groupPeopleList) {
        //修改移除标识
        SubContractor subContractor = new SubContractor();
        subContractor.setSubContractorId(subContractorId);
        subContractor.setContractorStatus(1);
        int i = subContractorMapper.updateSubContractorById(subContractor);
        if (i <= 0) {
            throw new AttendanceException(ResultError.REJOIN_PROJECT_FAILURE);
        }
        //获取所有下发的设备
        List<Device> allIssueDevice = deviceService.findAllIssueDevice();
        //获取项目绑定的下发的设备
        List<Device> allProjectIssueDevice = deviceService.findAllProjectIssueDevice(projectCode);
        List<String> failNameList = new ArrayList<>();
        //将班组依次重新加入项目
        for (GroupPeople groupPeople : groupPeopleList) {
            //查询需要重新加入项目的人员
            List<Person> personList = personService.findIssuePeopleImagesInfo(groupPeople.getPersonIds());
            //将参建单位下的班组重新加入项目，并返回重新加入失败的人员姓名
            List<String> list = groupService.groupReJoinToProject(projectCode, subContractorId, groupPeople.getTeamSysNo(), personList, allIssueDevice, allProjectIssueDevice);
            failNameList.addAll(list);
        }
        return failNameList;
    }

    @Override
    public List<SubContractorQuery> findCorpName(String projectCode, Integer contractorStatus) {
        return subContractorMapper.findCorpName(projectCode, contractorStatus);
    }

    @Override
    public List<SubContractorQuery> findByProjectCode(String projectCode) {
        List<SubContractorQuery> subContractorList = subContractorMapper.findByProjectCode(projectCode);
        for (SubContractorQuery sub : subContractorList) {
            setContractInfo(sub);
        }
        return subContractorList;
    }

    @Override
    public List<SubContractorQuery> findContractCorpNameInPro(String projectCode, Integer status) {
        return subContractorMapper.findContractCorpNameInPro(projectCode, status);
    }

    @Override
    public List<SubContractorQuery> findUnRemoveInProject(String projectCode) {
        List<SubContractorQuery> subContractorList = subContractorMapper.findUnRemoveInProject(projectCode);
        for (SubContractorQuery sub : subContractorList) {
            setContractInfo(sub);
        }
        return subContractorList;
    }

    @Override
    public SubContractorQuery findById(Integer subContractorId) {
        SubContractorQuery subContractor = subContractorMapper.findById(subContractorId);
        setContractInfo(subContractor);
        return subContractor;
    }

    @Override
    public void setContractInfo(SubContractorQuery sub) {
        Integer personContractSignNum = subContractorMapper.getPersonContractSignNum(sub.getSubContractorId());
        Integer personNumInContractor = subContractorMapper.getPersonNumInContractor(sub.getSubContractorId());
        if (personNumInContractor == 0) {
            sub.setContractSignRate(0f);
        } else {
            sub.setContractSignRate((personContractSignNum + 0.0f)/ personNumInContractor);
        }
        sub.setAttendNum(personNumInContractor);
    }

    @Override
    public boolean judgeEmptyById(Integer subContractorId) {
        Integer integer = subContractorMapper.judgeEmptyById(subContractorId);
        return integer == null || integer <= 0;
    }

    @Override
    public List<SubContractorQuery> searchContractorInPro(SubContractorSearchInPro subContractorSearchInPro) {
        List<SubContractorQuery> subContractorQueryList = subContractorMapper.searchContractorInPro(subContractorSearchInPro);
        return setContractorInfoList(subContractorQueryList);
    }

    @Override
    public Integer findUploadStatusById(Integer subContractorId) {
        return subContractorMapper.findUploadStatusById(subContractorId);
    }

    @Override
    public List<SubContractorQuery> findByIdList(List<Integer> subContractorIdList) {
        return subContractorMapper.findByIdList(subContractorIdList);
    }

    private List<SubContractorQuery> setContractorInfoList(List<SubContractorQuery> subContractorQueryList) {
        for (SubContractorQuery subContractorQuery : subContractorQueryList) {
            setContractInfo(subContractorQuery);
        }
        return subContractorQueryList;
    }


}
