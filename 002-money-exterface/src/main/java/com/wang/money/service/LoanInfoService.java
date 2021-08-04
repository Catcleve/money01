package com.wang.money.service;


import com.github.pagehelper.PageInfo;
import com.wang.money.model.LoanInfo;

import java.util.List;
import java.util.Map;

/**
 * @author 毛能能
 */
public interface LoanInfoService {

    /**
     * 查询平均年利率
     * @return
     */
    Double queryAvgRate();

    /**
     * 首页：通过类型和数量查询产品信息
     * @param queryLoan
     * @return
     */
    List<LoanInfo> queryByTypeAndCount(Map<String, Object> queryLoan);

    /**
     * 产品列表页：分页查询产品
     * @return
     * @param pType
     * @param pageNum
     * @param pageSize
     */
    PageInfo<LoanInfo> queryByTypeAndPageInfo(String pType, Integer pageNum, Integer pageSize);

    /**
     * 通过ID查询产品详情
     * @return 产品详情
     * @param loanId 产品ID
     */
    LoanInfo queryLoanInfoById(Integer loanId);
}
