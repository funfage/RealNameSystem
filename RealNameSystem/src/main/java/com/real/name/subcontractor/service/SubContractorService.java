package com.real.name.subcontractor.service;

import com.real.name.subcontractor.entity.SubContractor;

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
     * 查询项目下的参建单位
     */
    List<SubContractor> findByProjectCode(String projectCode);

    /**
     * 根据参建单位id查询
     */
    SubContractor findById(Integer subContractorId);

    /**
     * 根据id判断参建单位是否存在
     */
    boolean judgeEmptyById(Integer subContractorId);

}
