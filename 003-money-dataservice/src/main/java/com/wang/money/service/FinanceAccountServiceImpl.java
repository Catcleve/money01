package com.wang.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.money.mapper.FinanceAccountMapper;
import org.springframework.stereotype.Component;
import service.FinanceAccountService;

import javax.annotation.Resource;

/**
 * 用户财务资金业务
 * @author 毛能能
 */
@Service(interfaceClass = FinanceAccountService.class,version = "1.0.0", timeout = 20000)
@Component
public class FinanceAccountServiceImpl implements FinanceAccountService {



    @Resource
    private FinanceAccountMapper financeAccountMapper;


}
