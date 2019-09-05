package com.real.name.nation.service;

import java.util.List;
import java.util.Set;

public interface NationService {

    /**
     * 上传项目到全国平台/修改上传到全国平台
     */
    void uploadProject(Set<String> projectCodeList);

    /**
     * 参建单位上传到全国平台/修改上传到全国平台
     * @param projectCode 参建单位所在的项目编码
     */
    void uploadSubContractor(List<Integer> subContractorIdList, String projectCode);

    /**
     * 删除班组信息到全国平台
     */
    void uploadWorkerGroup(List<Integer> groupIdList, Integer subContractorId);

    /**
     * 人员信息上传到全国平台/修改上传到全国平台
     */
    void uploadPerson(List<Integer> personIdList, Integer teamSysNo, String projectCode);

    /**
     * 合同信息上传到全国平台/修改上传到全国平台
     */
    void uploadContractor(List<Integer> contractorIdList);

}
