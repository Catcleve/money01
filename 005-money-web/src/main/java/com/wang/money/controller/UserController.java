package com.wang.money.controller;


import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.wang.money.service.RedisService;
import com.wang.utils.Constant;
import com.wang.utils.Result;
import com.wang.money.model.User;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wang.money.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 远程获取redisService服务
     */
    @Reference(interfaceClass = RedisService.class, version = "1.0.0", timeout = 20000)
    private RedisService redisService;

    /**
     * 跳转注册页面
     *
     * @return 视图名称
     */
    @GetMapping("loan/page/register")
    public String toRegister(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        request.getSession().setAttribute(Constant.LAST_URL, referer);
        return "register";
    }

    /**
     * 跳转登录页面
     *
     * @return 视图名称
     */
    @GetMapping("loan/page/login")
    public String login() {
        return "login";
    }

    /**
     * 跳转实名认证页面
     *
     * @return 视图名称
     */
    @GetMapping("loan/page/realName")
    public String realName() {
        return "realName";
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
     *
     * @param user 用户提交的数据封装
     * @return 注册结果封装
     */
    @PostMapping("/loan/page/register")
    @ResponseBody
    public Result register(User user, HttpSession session) {

//        返回带有主键的user
        user = userService.register(user);
        if (ObjectUtil.isEmpty(user.getId())) {
            return Result.unSuccess("注册失败");
        }
        session.setAttribute(Constant.LOGIN_USER, user);

        return Result.success("");
    }

    /**
     * 给用户发送验证码
     *
     * @param phone 用户提交的手机号
     * @return 发送结果
     */
    @GetMapping("/loan/page/messageCode")
    @ResponseBody
    public Result messageCode(String phone) {

        //        放参数的map
        Map<String, Object> params = new HashMap<>(10);
        //        平台提供的接口地址
        String url = "https://way.jd.com/kaixintong/kaixintong";
        params.put("appkey", "4794d03032e04800e505cf6f385171dd");
        params.put("mobile", phone);

        //        获取随机码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String messageCode = randomGenerator.generate();
        params.put("content", "【凯信通】您的验证码是：" + messageCode);

        //          模拟返回值测试
        String result = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 0,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-7545211</remainpoint>\\n <taskID>189522046</taskID>\\n <successCounts>1</successCounts></returnsms>\",\n" +
                "    \"requestId\": \"7ae7a5f6b2dc4157861284093dbd2177\"\n" +
                "}";

        //        调用平台发短信
        try {
            //            String result = HttpClientUtils.doPost(url, params);

            //            解析json文件
            JSONObject jsonObject = JSONObject.parseObject(result);
            //            获取返回code进行验证，如果不是10000说明与平台通信失败
            String code = jsonObject.getString("code");
            if (!StrUtil.equals(Constant.MESSAGE_RETURN_CODE, code)) {
                return Result.unSuccess("连接服务商失败");
            }

            //            获取返回的结果，是一个xml文件
            String resultXml = jsonObject.getString("result");
            Document document = DocumentHelper.parseText(resultXml);
            String returnstatus = document.getRootElement().element("returnstatus").getText();

            //            如果不是Success，说明失败了
            if (!StrUtil.equals(returnstatus, Constant.MESSAGE_RETURN_STATUS)) {
                return Result.unSuccess("短信发送失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.unSuccess("短信发送失败");
        }

        //        把验证码放入redis，有效期为5分钟
        redisService.saveMessageCode(phone, messageCode);


        return Result.success(messageCode);
    }


    /**
     * 验证码 正确性验证
     *
     * @param messageCode 验证码
     * @param phone       用户提交的手机号
     * @return 验证结果
     */
    @GetMapping("/loan/page/checkMessage")
    @ResponseBody
    public Result checkMessage(String phone, String messageCode) {

        String realMessageCode = redisService.getMessageCode(phone);
        if (!StrUtil.equals(realMessageCode, messageCode)) {
            return Result.unSuccess("验证码错误");
        }
        return Result.success("");
    }

    /**
     * 身份证号 正确性验证
     *
     * @param realName 姓名
     * @param idCard   身份证号
     * @return 验证结果
     */
    @PostMapping("/loan/page/idCardCheck")
    @ResponseBody
    public Result idCardCheck(String realName, String idCard, HttpServletRequest request) {

        //         放参数的map
        Map<String, Object> params = new HashMap<>(10);
        params.put("appkey", "4794d03032e04800e505cf6f385171dd");
        params.put("cardno", idCard);
        params.put("name", realName);

        String result = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 0,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"data\": {\n" +
                "            \"sex\": \"男\",\n" +
                "            \"address\": \"山东省-潍坊市-寒亭区\",\n" +
                "            \"birthday\": \"1981-11-30\"\n" +
                "        },\n" +
                "        \"resp\": {\n" +
                "            \"code\": 0,\n" +
                "            \"desc\": \"匹配\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"requestId\": \"90ad6779fca646db9b51da1e3456bd2f\"\n" +
                "}";


        //        平台提供的接口地址
        String url = "https://way.jd.com/yingyan/idcard";


        try {
            //            String result = HttpClientUtils.doPost(url, params);

            //            解析返回值
            JSONObject jsonObject = JSONObject.parseObject(result);
            //            获取返回code进行验证，如果不是10000说明与平台通信失败
            String code = jsonObject.getString("code");
            if (!StrUtil.equals(Constant.MESSAGE_RETURN_CODE, code)) {
                return Result.unSuccess("连接失败，请重试");
            }
            //            获取返回值中的结果
            String desc = jsonObject.getJSONObject("result").getJSONObject("resp").getString("desc");
            if (!StrUtil.equals(Constant.MESSAGE_RETURN_DESC, desc)) {
                return Result.unSuccess("匹配失败，请重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.unSuccess("连接失败，请重试");

        }

        //        验证成功，把数据放入session的user中
        User user = (User) request.getSession().getAttribute(Constant.LOGIN_USER);
        if (!ObjectUtil.isNotNull(user)) {
        //            时间过长提示用户重新登录
            return Result.unSuccess("登录失效，请登陆后重试");
        }
        //        存入数据库中
        User tempUser = user;
        tempUser.setIdCard(idCard);
        tempUser.setName(realName);
        boolean b = userService.updateUser(tempUser);
        if (!b) {
            return Result.unSuccess("认证失败");
        }
        user = tempUser;

        //        获取上一次访问的地址
        String referer = (String) request.getSession().getAttribute(Constant.LAST_URL);

        return Result.success(referer);
    }

}
