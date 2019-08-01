package com.real.name.contract.service.repository;

import com.real.name.contract.entity.ContractFile;
import com.real.name.contract.entity.ContractInfo;
import com.real.name.contract.query.ContractInfoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractInfoMapper {

    /**
     * 添加合同信息
     */
    int saveContractInfo(@Param("contractInfo") ContractInfo contractInfo);

    /**
     * 保存合同和文件的关联
     */
    int saveContractFileList(@Param("contractFileList") List<ContractFile> contractFileList);


    /**
     * 修改合同信息
     */
    int updateContractInfoById(@Param("contractInfo") ContractInfo contractInfo);

    /**
     * 根据合同id查询所有文件名
     */
    List<String> findFileNameListByContractId(@Param("contractId") Integer contractId);

    /**
     * 删除合同信息
     */
    int deleteContractInfoById(@Param("contractId") Integer contractId);

    /**
     * 根据合同id和文件名删除合同文件关联
     */
    int deleteContractFileByIdAndFileName(@Param("contractId") Integer contractId,
                                          @Param("fileName") String fileName);

    /**
     * 查询所有合同信息
     */
    List<ContractInfo> getAllContractInfo();

    /**
     * 搜索合同
     */
    List<ContractInfo> searchContractInfo(@Param("contractInfoQuery") ContractInfoQuery contractInfoQuery);

}
