package com.wang.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis接口实现类
 * @author 毛能能
 */
@Service(interfaceClass = RedisService.class,version = "1.0.0", timeout = 20000)
@Component
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 短信验证码存入redis中
     * @param phone 注册的手机号
     * @param messageCode 验证码
     */
    @Override
    public void saveMessageCode(String phone, String messageCode) {

        redisTemplate.opsForValue().set(phone, messageCode,5, TimeUnit.MINUTES);

    }


    /**
     * 通过手机号获取验证码
     * @param phone 用户提交的手机号
     * @return 验证结果
     */
    @Override
    public String getMessageCode(String phone) {

        return (String) redisTemplate.opsForValue().get(phone);
    }
}
