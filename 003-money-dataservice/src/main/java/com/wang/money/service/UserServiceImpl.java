package com.wang.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.money.mapper.FinanceAccountMapper;
import com.wang.money.mapper.UserMapper;
import model.FinanceAccount;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import service.FinanceAccountService;
import service.UserService;

import java.util.Date;

/**
 * 用户接口实现类
 * @author 毛能能
 */
@Service(interfaceClass = UserService.class,version = "1.0.0", timeout = 20000)
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long queryAllUser() {
        return userMapper.selectAllCount();
    }

    /**
     * 注册页面：通过手机号查询用户
     * @param phone 注册的手机号
     * @return 有或者没有
     */
    @Override
    public boolean queryUserByPhone(String phone) {

        int i = userMapper.selectByPhone(phone);

        return i == 0;
    }

    /**
     * //@Transient 这里可以使用事务，因为是两条语句
     * 但是出于业务考虑，需要让用户先注册，所以不要加事务，尽量让用户注册成功
     * 注册用户,同时赠送金额
     * @param user 用户提交的数据封装
     * @return 跳转视图
     */
    @Override
    public User register(User user) {

//        设置创建日期和最后登录日期
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
//        注册用户
        userMapper.insertSelective(user);
//        用返回值判断是否成功，如果不成功抛出异常，事务回滚

//        赠送金额
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(user.getId());
        financeAccount.setAvailableMoney(888.0);
        financeAccountMapper.insertSelective(financeAccount);
//        用返回值判断是否成功，如果不成功抛出异常，事务回滚

        return null;
    }
}
