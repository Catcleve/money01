package service;

import model.BidInfo;

import java.util.List;
import java.util.Map;

/**
 * @author 毛能能
 */
public interface BidInfoService {
    /**
     * 查询投资总额
     * @return 总额
     */
    Double querySumBidMoney();


    /**
     * 产品详情页：通过产品id查询差评投资记录，包含用户信息
     * @param loanId 产品id
     * @return 投标记录
     */
    List<BidInfo> queryBidInfoContainsUserPhoneByLoanId(Integer loanId);

    /**
     * 查询投资排行榜
     * @return 电话号码和金额
     */
    Map<Object, Double> queryMoneyRank();
}
