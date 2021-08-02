package com.wang.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wang.money.mapper.LoanInfoMapper;
import model.LoanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.LoanInfoService;

import java.util.List;
import java.util.Map;

/**
 * 首页平均年利率查询
 * @author 毛能能
 */
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0", timeout = 20000)
@Component
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Override
    public Double queryAvgRate() {
        return loanInfoMapper.selectAvgRate();
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
