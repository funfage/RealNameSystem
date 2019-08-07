package com.real.name.contract;

import com.real.name.common.utils.TimeUtil;
import com.real.name.contract.entity.ContractFile;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.query.ContractInfoQuery;
import com.real.name.contract.service.repository.ContractInfoMapper;
import com.real.name.others.BaseTest;
import com.real.name.project.entity.ProjectDetailQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContractTest extends BaseTest {

    @Autowired
    private ContractInfoMapper contractInfoMapper;

    @Test
    public void saveContractInfo() {
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setProjectDetailQuery(new ProjectDetailQuery(103));
        contractInfo.setContractPeriodType(1);
        contractInfo.setStartDate(new Date());
        contractInfo.setEndDate(new Date());
        int i = contractInfoMapper.saveContractInfo(contractInfo);
        System.out.println(i);
        System.out.println(contractInfo);
    }

    @Test
    public void saveContractFileList() {
        List<ContractFile> contractFileList = new ArrayList<>();
        contractFileList.add(new ContractFile(1, "1.jpg"));
        contractFileList.add(new ContractFile(1, "2.jpg"));
        int i = contractInfoMapper.saveContractFileList(contractFileList);
        System.out.println(i);
    }

    @Test
    public void updateContractInfoById() {
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setContractId(1);
        contractInfo.setContractPeriodType(1);
        contractInfo.setStartDate(TimeUtil.getMonthFirstDay());
        contractInfo.setEndDate(TimeUtil.getNextMonthFirstDay());
        contractInfo.setUploadStatus(1);
        int i = contractInfoMapper.updateContractInfoById(contractInfo);
        System.out.println(i);
    }

    @Test
    public void findFileNameListByContractId() {
        List<String> fileNameListByContractId = contractInfoMapper.findFileNameListByContractId(1);
        System.out.println(fileNameListByContractId);
    }

    @Test
    public void deleteContractInfoById() {
        int i = contractInfoMapper.deleteContractInfoById(1);
        System.out.println(i);
    }

    @Test
    public void deleteContractFileByIdAndFileName() {
        int i = contractInfoMapper.deleteContractFileByIdAndFileName(5, "5.jpg");
        System.out.println(i);
    }

    @Test
    public void getAllContractInfo() {
        List<ContractInfo> allContractInfo = contractInfoMapper.getAllContractInfo();
        System.out.println(allContractInfo);
    }

    @Test
    public void searchContractInfo() {
        ContractInfoQuery query = new ContractInfoQuery();
        query.setPersonName("陈祺荣");
        query.setProjectName("新兴");
        List<ContractInfo> contractInfos = contractInfoMapper.searchContractInfo(query);
        System.out.println(contractInfos);
    }

}

















