package com.real.name.contract.service;

import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.entity.query.ContractInfoQuery;
import com.real.name.contract.entity.search.ContractInfoSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractInfoService {

    /**
     * 添加合同信息
     */
    void saveContractInfo(ContractInfo contractInfo, String projectCode, Integer teamSysNo, Integer personId);

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
    List<ContractInfoQuery> getAllContractInfo();

    /**
     * 搜索合同
     */
    List<ContractInfoQuery> searchContractInfo(ContractInfoSearch contractInfoSearch);

    /**
     * 获取所有需要上传的人员合同信息
     */
    List<ContractInfoQuery> findUploadContractsInfo(List<Integer> contractInfoIdList);

}
