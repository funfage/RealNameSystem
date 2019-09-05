package com.real.name.subcontractor.service.repository;

import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.entity.search.SubContractorSearchInPro;
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
     * 查询参建单位的名字
     */
    List<SubContractorQuery> findCorpName(@Param("projectCode") String projectCode,
                                          @Param("contractorStatus") Integer contractorStatus);

    /**
     * 查询项目下的参建单位
     */
    List<SubContractorQuery> findByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 查询项目中未被移除的参建单位的参建单位名称
     */
    List<SubContractorQuery> findContractCorpNameInPro(@Param("projectCode") String projectCode,
                                                       @Param("status") Integer status);

    /**
     * 查询项目下未被移除的参建单位信息
     */
    List<SubContractorQuery> findUnRemoveInProject(@Param("projectCode") String projectCode);

    /**
     * 根据参建单位id查询
     */
    SubContractorQuery findById(@Param("subContractorId") Integer subContractorId);

    /**
     * 根据id集合查询
     */
    List<SubContractorQuery> findByIdList(@Param("idList") List<Integer> subContractorIdList);

    /**
     * 查询某个参建单位下所有的人员数
     */
    Integer getPersonNumInContractor(@Param("subContractorId") Integer subContractorId);

    /**
     * 查询某个参建单位下所有签订合同的人员数
     */
    Integer getPersonContractSignNum(@Param("subContractorId") Integer subContractorId);

    /**
     * 根据id判断参建单位是否存在
     */
    Integer judgeEmptyById(@Param("subContractorId") Integer subContractorId);

    /**
     * 设置参建单位移除标识
     */
    int setProSubContractorRemoveStatus(Integer subContractorId);

    /**
     * 搜索项目中的参建单位
     */
    List<SubContractorQuery> searchContractorInPro(@Param("subContractorSearchInPro") SubContractorSearchInPro subContractorSearchInPro);

    /**
     * 查询参建单位的上传标识
     */
    Integer findUploadStatusById(@Param("subContractorId") Integer subContractorId);



}
