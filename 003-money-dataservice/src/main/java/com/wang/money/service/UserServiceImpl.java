package com.wang.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.money.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.UserService;

/**
 * 用户接口实现类
 * @author 毛能能
 */
@Service(interfaceClass = UserService.class,version = "1.0.0", timeout = 20000)
@Component
public class UserServiceImpl implements UserService {



    @Autowired
    private UserMapper userMapper;

    @Override
    public Long queryAllUser() {
        return userMapper.selectAllCount();
    }
}
