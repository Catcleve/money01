package com.wang.money.controller;


import com.alibaba.dubbo.config.annotation.Reference;

import com.wang.utils.Result;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

/**
 * 用户控制层类
 *
 * @author 毛能能
 */
@Controller
public class UserController {

    /**
     * 远程获取UserService服务
     */
    @Reference(interfaceClass = UserService.class, version = "1.0.0", timeout = 20000)
    private UserService userService;

    @GetMapping("loan/page/register")
    public String toRegister() {
        return "register";
    }

    @GetMapping("loan/page/login")
    public String login() {
        return "login";
    }


    /**
     * 验证手机号码是否重复
     *
     * @param phone 注册手机号码
     * @return 封装结果
     */
    @GetMapping("/loan/page/checkRegisterPhone")
    @ResponseBody
    public Result checkRegisterPhone(@RequestParam("phone") String phone) {

        boolean result = userService.queryUserByPhone(phone);
        if (result) {
            return Result.success("");
        }

        return Result.unSuccess("手机号码已被使用");
    }

    /**
     * 注册用户
     * @param user 用户提交的数据封装
     * @return 跳转视图
     */
    @PostMapping("/loan/page/register")
    public String register(User user) {

        user = userService.register(user);

        return "index";
    }

}
