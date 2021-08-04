package com.wang.money.service;

/**
 * redis接口
 * @author 毛能能
 */
public interface RedisService {


    /**
     * 短信验证码存入redis中
     * @param phone 注册的手机号
     * @param messageCode 验证码
     */
    void saveMessageCode(String phone, String messageCode);

    /**
     * 通过手机号获取验证码
     * @param phone 用户提交的手机号
     * @return 验证结果
     */
    String getMessageCode(String phone);
}
