package com.wang.money.service;

import com.github.pagehelper.PageInfo;
import com.wang.money.model.BidInfo;

import java.util.Map;

/**
 * @author 毛能能
 */
public interface BidInfoService {
    /**
     * 查询投资总额
     * @return 总额
     */
    Double queryBidMoneySum();


    /**
     * 产品详情页：通过产品id查询差评投资记录，包含用户信息
     * @param loanId 产品id
     * @param pageNum 第几页
     * @return 投标记录
     */
    PageInfo<BidInfo> queryBidInfoContainsUserPhoneByLoanId(Integer loanId , Integer pageNum);

    /**
     * 查询投资排行榜
     * @return 电话号码和金额
     */
    Map<Object, Double> queryMoneyRank();
}
