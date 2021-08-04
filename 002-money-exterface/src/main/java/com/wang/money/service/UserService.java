package com.wang.money.service;
import com.wang.money.model.User;


/**
 * 用户公共接口
 * @author 毛能能
 */
public interface UserService {
    /**
     * 查询所有用户数量
     * @return 用户数量
     */
    Long queryAllUser();

    /**
     * 注册页面：通过手机号查询用户
     * @param phone 注册的手机号
     * @return 有或者没有
     */
    boolean queryUserByPhone(String phone);

    /**
     * 注册用户
     * @param user 用户提交的数据封装
     * @return 跳转视图
     */
    User register(User user);

    /**
     * 实名认证
     * @param tempUser 更新认证信息
     * @return 是否成功
     */
    boolean updateUser(User tempUser);
}
