package com.wang.money.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.wang.money.model.LoanInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.wang.money.service.BidInfoService;
import com.wang.money.service.LoanInfoService;
import com.wang.money.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页操作
 * @author 毛能能
 */
@Controller
public class IndexController {


    /**
     * 远程获取LoanInfoService服务
     */
    @Reference(interfaceClass = LoanInfoService.class , version = "1.0.0" , timeout = 20000)
    private LoanInfoService loanInfoService;

    /**
     * 远程获取UserService服务
     */
    @Reference(interfaceClass = UserService.class , version = "1.0.0" , timeout = 20000)
    private UserService userService;

    /**
     * 远程获取BidInfoService服务
     */
    @Reference(interfaceClass = BidInfoService.class , version = "1.0.0" , timeout = 20000)
    private BidInfoService bidInfoService;

    /**
     * 查询平均年收益率，平台用户数，累计成交金额
     * @param model 返回给视图
     * @return 到首页
     */
    @GetMapping({"/","/index"})
    public String toIndex(Model model){

        //        查询平均年化收益率
        Double rateAvg = loanInfoService.queryAvgRate();

        //        查询注册人数
        Long userCount = userService.queryAllUser();

        //        查询累计成交金额
        Double sumBidMoney = bidInfoService.queryBidMoneySum();

        /*首页新手宝，因为还要查询其他的，这里把查询条件封装为map
        当属性没有互相关联时，使用map，有关联则使用object
        */


        //        新手包查询
        Map<String, Object> queryLoan = new HashMap<>(10);
        queryLoan.put("type", 0);
        queryLoan.put("start", 0);
        queryLoan.put("count", 1);
        List<LoanInfo> loanInfoX = loanInfoService.queryByTypeAndCount(queryLoan);

        //        满月包查询
        queryLoan.put("type", 1);
        queryLoan.put("start", 0);
        queryLoan.put("count", 4);
        List<LoanInfo> loanInfoY = loanInfoService.queryByTypeAndCount(queryLoan);

        //        散标查询
        queryLoan.put("type", 2);
        queryLoan.put("start", 0);
        queryLoan.put("count", 8);
        List<LoanInfo> loanInfoS = loanInfoService.queryByTypeAndCount(queryLoan);



        model.addAttribute("rateAvg", rateAvg);
        model.addAttribute("userCount", userCount);
        model.addAttribute("sumBidMoney", sumBidMoney);
        model.addAttribute("LoanInfoX", loanInfoX);
        model.addAttribute("loanInfoY", loanInfoY);
        model.addAttribute("loanInfoS", loanInfoS);

        return "index";
    }
}
