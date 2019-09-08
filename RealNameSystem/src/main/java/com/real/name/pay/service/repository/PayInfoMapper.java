package com.real.name.pay.service.repository;

import com.real.name.pay.entity.PayInfo;
import com.real.name.pay.entity.query.PayInfoQuery;
import com.real.name.pay.entity.search.PayInfoSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayInfoMapper {

    /**
     * 保存薪资记录
     */
    int savePayInfo(@Param("payInfo") PayInfo payInfo);

    /**
     * 更新薪资信息
     */
    int updatePayInfoById(@Param("payInfo") PayInfo payInfo);

    /**
     * 删除薪资信息
     */
    int deletePayInfoById(@Param("payId") Integer payId);

    /**
     * 查询所有薪资记录
     */
    List<PayInfoQuery> getAllPayInfo();

    /**
     * 薪资搜索
     */
    List<PayInfo> searchPayInfo(@Param("payInfoSearch") PayInfoSearch payInfoSearch);

}
