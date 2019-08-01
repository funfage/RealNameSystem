package com.real.name.contract.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.common.utils.FileTool;
import com.real.name.common.utils.PathUtil;
import com.real.name.contract.entity.ContractFile;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.query.ContractInfoQuery;
import com.real.name.contract.service.ContractInfoService;
import com.real.name.contract.service.repository.ContractInfoMapper;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.repository.ProjectDetailQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractInfoServiceImpl implements ContractInfoService {

    @Autowired
    private ContractInfoMapper contractInfoMapper;

    @Autowired
    private ProjectDetailQueryMapper projectDetailQueryMapper;

    @Transactional
    @Override
    public void saveContractInfo(ContractInfo contractInfo, String projectCode, Integer personId) {
        Integer projectDetailId = projectDetailQueryMapper.getIdByProjectCodeAndPersonId(projectCode, personId);
        if (projectDetailId == null) {
            throw new AttendanceException(ResultError.NO_FIND_PROJECT_DETAIL);
        }
        contractInfo.setProjectDetailQuery(new ProjectDetailQuery(projectDetailId));
        int i = contractInfoMapper.saveContractInfo(contractInfo);
        if (i <= 0) {
            throw new AttendanceException(ResultError.INSERT_ERROR);
        }
    }

    @Transactional
    @Override
    public void saveContractFileList(Integer contractId, MultipartFile [] contractFiles) {
        //保存文件
        List<ContractFile> contractFileList = new ArrayList<>();
        try {
            for (MultipartFile file : contractFiles) {
                String fileName = FileTool.getRandomPrefixName() + FileTool.getSuffixName(file);
                FileTool.generateFile(file, PathUtil.getContractFilePath(), fileName);
                contractFileList.add(new ContractFile(contractId, fileName));
            }
        } catch (Exception e) {
            //将已经生成的文件删除
            for (ContractFile contractFile : contractFileList) {
                FileTool.deleteFile(PathUtil.getContractFilePath(), contractFile.getFileName());
            }
            throw new AttendanceException(ResultError.GENERATE_FILE_ERROR);
        }
        if (contractFileList.size() > 0) {
            //保存合同信息和文件的一对多关联
            int i = contractInfoMapper.saveContractFileList(contractFileList);
            if (i <= 0) {
                throw new AttendanceException(ResultError.OPERATOR_ERROR);
            }
        }
    }

    @Transactional
    @Override
    public void updateContractInfo(ContractInfo contractInfo) {
        int i = contractInfoMapper.updateContractInfoById(contractInfo);
        if (i <= 0) {
            throw new AttendanceException((ResultError.UPDATE_ERROR));
        }
    }

    @Transactional
    @Override
    public void deleteContractInfo(Integer contractId) {
        //根据contractId查询出合同关联的文件名
        List<String> fileNameList = contractInfoMapper.findFileNameListByContractId(contractId);
        //删除合同的所有文件信息
        deleteContractFileList(contractId, fileNameList);
        //删除合同信息
        int i = contractInfoMapper.deleteContractInfoById(contractId);
        if (i <= 0) {
            throw new AttendanceException(ResultError.DELETE_ERROR);
        }
    }

    @Transactional
    @Override
    public void deleteContractFileList(Integer contractId, List<String> fileNameList) {
        if (fileNameList != null && fileNameList.size() > 0) {
            for (String fileName : fileNameList) {
                //删除文件
                FileTool.deleteFile(PathUtil.getContractFilePath(), fileName);
                //删除合同与文件的关联
                contractInfoMapper.deleteContractFileByIdAndFileName(contractId, fileName);
            }
        }
    }

    @Override
    public List<ContractInfo> getAllContractInfo() {
        return contractInfoMapper.getAllContractInfo();
    }

    @Override
    public List<ContractInfo> searchContractInfo(ContractInfoQuery contractInfoQuery) {
        return contractInfoMapper.searchContractInfo(contractInfoQuery);
    }
}
