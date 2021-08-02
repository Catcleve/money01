package com.wang.money.mapper;

import model.User;

/**
 * User实现类
 * @author 毛能能
 */
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询所有用户数量
     * @return 数量
     */
    Long selectAllCount();
}