package com.wang.money.mapper;

import com.wang.money.model.BidInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 首页：查询投资总额
     * @return
     */
    Double selectSumBidMoney();


    /**
     * 产品详情页：通过产品id查询差评投资记录，包含用户信息
     * @return 投标记录
     * @param loanId
     */
    List<BidInfo> selectContainsUserPhoneByLoanId(Integer loanId);


    /**
     * 产品列表页：查询投资排行榜
     * @return FinanceAccount集合
     */
    List<BidInfo> selectMoneyAndUserPhone();
}