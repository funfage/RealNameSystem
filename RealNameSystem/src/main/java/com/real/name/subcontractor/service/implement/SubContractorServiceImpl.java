package com.real.name.subcontractor.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.group.service.GroupService;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.service.SubContractorService;
import com.real.name.subcontractor.service.repository.SubContractorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubContractorServiceImpl implements SubContractorService {

    @Autowired
    private SubContractorMapper subContractorMapper;

    @Autowired
    private GroupService groupService;

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

    @Override
    public void removeSubContractorInProject(Integer subContractorId, String projectCode) {
        //查询该参建单位下所有未移出的班组
        List<WorkerGroup> workerGroupList = groupService.findRemoveGroupInContract(subContractorId);
        //依次移除班组
        for (WorkerGroup workerGroup : workerGroupList) {
            groupService.removeGroupInProject(workerGroup.getTeamSysNo(), projectCode);
        }
        //设置参建单位移除标识
        subContractorMapper.setProSubContractorRemoveStatus(subContractorId);
    }

    @Override
    public List<SubContractor> findByProjectCode(String projectCode) {
        return subContractorMapper.findByProjectCode(projectCode);
    }

    @Override
    public SubContractor findById(Integer subContractorId) {
        return subContractorMapper.findById(subContractorId);
    }

    @Override
    public boolean judgeEmptyById(Integer subContractorId) {
        Integer integer = subContractorMapper.judgeEmptyById(subContractorId);
        return integer == null || integer <= 0;
    }
}
