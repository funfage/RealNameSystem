package com.real.name.contract.service;

import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.query.ContractInfoQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractInfoService {

    /**
     * 添加合同信息
     */
    void saveContractInfo(ContractInfo contractInfo, String projectCode, Integer personId);

    /**
     * 保存合同和文件的关联
     */
    void saveContractFileList(Integer contractId, MultipartFile[] contractFiles);

    /**
     * 更新合同
     */
    void updateContractInfo(ContractInfo contractInfo);

    /**
     * 删除合同信息
     */
    void deleteContractInfo(Integer contractId);

    /**
     * 根据合同id和文件名删除合同文件关联
     */
    void deleteContractFileList(Integer contractId, List<String> fileNameList);

    /**
     * 查询所有合同记录
     */
    List<ContractInfo> getAllContractInfo();

    /**
     * 搜索合同
     */
    List<ContractInfo> searchContractInfo(ContractInfoQuery contractInfoQuery);

}
