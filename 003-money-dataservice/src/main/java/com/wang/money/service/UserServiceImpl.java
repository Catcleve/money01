package com.wang.money.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.wang.money.mapper.FinanceAccountMapper;
import com.wang.money.mapper.UserMapper;
import com.wang.money.model.FinanceAccount;
import com.wang.money.model.User;
import com.wang.utils.Constant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 用户接口实现类
 * @author 毛能能
 */
@Service(interfaceClass = UserService.class,version = "1.0.0", timeout = 20000)
@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final FinanceAccountMapper financeAccountMapper;

    private final UserMapper userMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 查询所有用户
     * @return 用户数量
     */
    @Override
    public Long queryAllUser() {

        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());

        //先判断redis中有没有数据
        Long allUserCount = (Long) redisTemplate.opsForValue().get(Constant.ALL_USER_COUNT);
        //如果没有就加锁从数据库查询
        if (ObjectUtil.isNull(allUserCount)) {
            synchronized (this){
                //再次判断redis中有没有数据，以免多线程情况下多个线程阻塞在锁外面，造成多次从数据库查询
                if (ObjectUtil.isNull(redisTemplate.opsForValue().get(Constant.ALL_USER_COUNT))) {
                    //数据库查询
                    allUserCount = userMapper.selectAllCount();
                    //放入redis的缓存中
                    redisTemplate.opsForValue().set(Constant.ALL_USER_COUNT,allUserCount,1, TimeUnit.DAYS);
                }
            }
        }
        return allUserCount;
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

        return user;
    }



    /**
     * 实名认证
     * @param tempUser 更新认证信息
     * @return 是否成功
     */
    @Override
    public boolean updateUser(User tempUser) {
        int i = userMapper.updateByPrimaryKeySelective(tempUser);
        return i == 1;
    }
}
