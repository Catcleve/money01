package com.wang.money.mapper;

import model.User;
import org.springframework.stereotype.Repository;

/**
 * User实现类
 * @author 毛能能
 */
@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    /**
     * 注册用户
     */
    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询所有用户数量
     * @return 数量
     */
    Long selectAllCount();

    /**
     * 注册页面：通过手机号查询用户
     * @param phone 注册的手机号
     * @return 有或者没有
     */
    int selectByPhone(String phone);
}