package com.real.name.subcontractor.service.repository;

import com.real.name.subcontractor.entity.SubContractor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubContractorMapper {

    /**
     * 保存参建单位
     */
    int saveSubContractor(@Param("subContractor") SubContractor subContractor);

    /**
     * 修改参建单位信息
     */
    int updateSubContractorById(@Param("subContractor") SubContractor subContractor);

    /**
     * 查询项目下的参建单位
     */
    List<SubContractor> findByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据参建单位id查询
     */
    SubContractor findById(@Param("subContractorId") Integer subContractorId);

    /**
     * 根据id判断参建单位是否存在
     */
    Integer judgeEmptyById(@Param("subContractorId") Integer subContractorId);

    /**
     * 设置参建单位移除标识
     */
    int setProSubContractorRemoveStatus(Integer subContractorId);


}
