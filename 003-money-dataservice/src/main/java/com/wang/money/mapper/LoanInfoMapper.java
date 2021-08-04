package com.wang.money.mapper;


import com.wang.money.model.LoanInfo;

import java.util.List;
import java.util.Map;

/**
 * 产品信息表接口
 * @author 毛能能
 */

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 查询平均年利率
     * @return
     */
    Double selectAvgRate();

    /**
     * 首页：通过类型和数量查询产品信息
     * @param queryLoan
     * @return
     */
    List<LoanInfo> selectByTypeAndCount(Map<String, Object> queryLoan);

    /**
     * 产品列表页：分页查询产品
     * @return pageInfo 封装了所有信息
     * @param pType 产品类型
     */
    List<LoanInfo> selectByTypeAndPageInfo(String pType);
}