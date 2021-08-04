package com.wang.money.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wang.money.mapper.LoanInfoMapper;
import com.wang.money.model.LoanInfo;
import com.wang.utils.Constant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 首页平均年利率查询
 * @author 毛能能
 */
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0", timeout = 20000)
@Component
public class LoanInfoServiceImpl implements LoanInfoService {

    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询平均年化收益率
     * @return 收益率
     */
    @Override
    public Double queryAvgRate() {

        Double avgRate = (Double) redisTemplate.opsForValue().get(Constant.AVG_RATE);
        if (ObjectUtil.isNull(avgRate)) {
            synchronized (this){
                if (ObjectUtil.isNull(redisTemplate.opsForValue().get(Constant.AVG_RATE))) {
                    avgRate = loanInfoMapper.selectAvgRate();
                    redisTemplate.opsForValue().set(Constant.AVG_RATE,avgRate,1, TimeUnit.DAYS);
                }
            }
        }

        return avgRate;
    }

    /**
     * 首页：通过类型和数量查询产品信息
     * @param queryLoan 查询条件，里面是类型和条数
     * @return LoanInfo集合
     */
    @Override
    public List<LoanInfo> queryByTypeAndCount(Map<String, Object> queryLoan) {

        return loanInfoMapper.selectByTypeAndCount(queryLoan);
    }

    /**
     * 产品列表页：分页查询产品
     * @return pageInfo 封装了所有信息
     * @param pType 产品类型
     * @param pageNum   当前页
     * @param pageSize  每页条数
     */
    @Override
    public PageInfo<LoanInfo> queryByTypeAndPageInfo
    (String pType, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<LoanInfo> loanInfos = loanInfoMapper.selectByTypeAndPageInfo(pType);
        return new PageInfo<>(loanInfos);
    }

    /**
     * 通过ID查询产品信息
     * @return 产品
     * @param loanId 产品ID
     */
    @Override
    public LoanInfo queryLoanInfoById(Integer loanId) {

        return loanInfoMapper.selectByPrimaryKey(loanId);
    }
}
