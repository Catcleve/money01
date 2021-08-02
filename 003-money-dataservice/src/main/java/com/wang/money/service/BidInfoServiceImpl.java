package com.wang.money.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.wang.money.mapper.BidInfoMapper;
import model.BidInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import service.BidInfoService;

import javax.annotation.Resource;
import java.util.*;

/**
 * 投资历史接口
 *
 * @author 毛能能
 */
@Service(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 20000)
@Component
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 首页：查询投资总额
     * @return 总额
     */
    @Override
    public Double querySumBidMoney() {
        return bidInfoMapper.selectSumBidMoney();
    }


    /**
     * 产品详情页：通过产品id查询差评投资记录，包含用户信息
     * @return 投标记录
     */
    @Override
    public List<BidInfo> queryBidInfoContainsUserPhoneByLoanId(Integer loanId) {

        return bidInfoMapper.selectContainsUserPhoneByLoanId(loanId);
    }

    /**
     * 查询投资排行榜，使用缓存
     * key为 moneyRank
     * value为用户电话号码，号码需要处理，中间四位数隐藏
     * score为金额
     * @return 电话号码和金额的排行
     */
    @Override
    public Map<Object, Double> queryMoneyRank() {
//        用来存放排行榜的的集合，使用LinkedHashMap保持排序
        HashMap<Object, Double> moneyRankMap = new LinkedHashMap<>(5);

        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
//        先在缓存中查询，如果没有从数据库中查出来放入缓存中
        Set<Object> moneyRank = zSet.reverseRange("moneyRank", 0, -1);

        if (moneyRank == null||moneyRank.size() == 0) {
//            从数据库查询
            System.out.println("-------------------------数据库查询");
            List<BidInfo> bidInfoAccounts = bidInfoMapper.selectMoneyAndUserPhone();

//            循环隐藏电话号码，放入缓存中
            bidInfoAccounts.forEach(bidInfoAccount -> {
//                获取并处理电话号码
                String phone = bidInfoAccount.getUser().getPhone();
                phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                放入缓存中
                zSet.add("moneyRank", phone, bidInfoAccount.getBidMoney());
            });
//            只保留前五个数据
            zSet.removeRange("moneyRank", 0,zSet.zCard("moneyRank")-6);

            moneyRank = zSet.reverseRange("moneyRank", 0, 4);
        }
        System.out.println("-----------缓存查询");
//            缓存中有数据，放入map中
        for (Object o : moneyRank) {
            Double score = zSet.score("moneyRank", o);
            moneyRankMap.put(o, score);
        }
        return moneyRankMap;
    }
}
