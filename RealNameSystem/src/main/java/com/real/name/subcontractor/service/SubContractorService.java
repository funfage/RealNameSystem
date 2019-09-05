package com.real.name.subcontractor.service;

import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.entity.query.GroupPeople;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.entity.search.SubContractorSearchInPro;

import java.util.List;

public interface SubContractorService {

    /**
     * 保存参建单位
     */
    void saveSubContractor(SubContractor subContractor);

    /**
     * 修改参建单位信息
     */
    void updateSubContractorById(SubContractor subContractor);

    /**
     * 将参建单位从项目中移除
     */
    void removeSubContractorInProject(Integer subContractorId, String projectCode);

    /**
     * 将参建单位重新加入项目
     */
    List<String> contractorReJoinToProject(String projectCode, Integer subContractorId, List<GroupPeople> groupPeopleList);

    /**
     * 查询参建单位的名字
     */
    List<SubContractorQuery> findCorpName(String projectCode, Integer contractorStatus);

    /**
     * 查询项目下的参建单位
     */
    List<SubContractorQuery> findByProjectCode(String projectCode);

    /**
     * 查询项目下未被移除的参建单位
     */
    List<SubContractorQuery> findContractCorpNameInPro(String projectCode, Integer status);

    /**
     * 查询项目下未被移除的参建单位信息
     */
    List<SubContractorQuery> findUnRemoveInProject(String projectCode);

    /**
     * 根据参建单位id查询
     */
    SubContractorQuery findById(Integer subContractorId);

    /**
     * 获取合同签订率
     */
    void setContractInfo(SubContractorQuery sub);

    /**
     * 根据id判断参建单位是否存在
     */
    boolean judgeEmptyById(Integer subContractorId);

    /**
     * 搜索项目中的参建单位
     */
    List<SubContractorQuery> searchContractorInPro(SubContractorSearchInPro subContractorSearchInPro);


    /**
     * 查询参建单位的上传标识
     */
    Integer findUploadStatusById(Integer subContractorId);

    /**
     * 根据id集合查询
     */
    List<SubContractorQuery> findByIdList(List<Integer> subContractorIdList);

}
