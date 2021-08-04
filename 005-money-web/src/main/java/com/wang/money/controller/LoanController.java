package com.wang.money.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;

import com.wang.money.model.BidInfo;
import com.wang.money.model.LoanInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wang.money.service.BidInfoService;
import com.wang.money.service.FinanceAccountService;
import com.wang.money.service.LoanInfoService;

import java.util.Map;

/**
 * 产品信息控制器
 * @author 毛能能
 */
@Controller
public class LoanController {

    /**
     * 远程获取产品信息服务
     */
    @Reference(interfaceClass = LoanInfoService.class , version = "1.0.0" , timeout = 20000)
    private LoanInfoService loanInfoService;

    /**
     * 远程获取用户账户服务
     */
    @Reference(interfaceClass = FinanceAccountService.class , version = "1.0.0" , timeout = 20000)
    private FinanceAccountService financeAccountService;


    /**
     * 远程获取投资记录服务
     */
    @Reference(interfaceClass = BidInfoService.class , version = "1.0.0" , timeout = 20000)
    private BidInfoService bidInfoService;


    /**
     * 跳转到详情页
     * @param pType 产品类别
     * @param pageNum 第几页产品
     * @param model model对象
     * @return 详情页
     */
    @GetMapping("/loan/{pType}/page/{pageNum}")
    public String loan (@PathVariable(value = "pType") String pType,
                        @PathVariable(value = "pageNum") Integer pageNum,
                        Model model){

//      每页数量，固定9条
        Integer pageSize = 9;

//        防止恶意输入
        if (pageNum <= 0 ) {
            pageNum = 1;
        }

//        查询出产品信息集合
        PageInfo<LoanInfo> pageInfo =
                loanInfoService.queryByTypeAndPageInfo(pType,pageNum,pageSize);
//        投资排行榜，只显示前五位
        Map<Object, Double> rankMap =  bidInfoService.queryMoneyRank();


        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("rankMap", rankMap);

        return "loan";
    }


    /**
     * 产品详情页
     * @return 跳转详情页
     */
    @GetMapping("/loan/loanInfo/{loanId}")
    public String loanInfo(@PathVariable Integer loanId,Model model,
                           @RequestParam(defaultValue = "1") Integer pageNum){

//        查询当前产品信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(loanId);

//        查询产品投资记录，包含用户手机号

        PageInfo<BidInfo> bidInfos =
                bidInfoService.queryBidInfoContainsUserPhoneByLoanId(loanId,pageNum);

        model.addAttribute("loanInfo", loanInfo);
        model.addAttribute("bidInfos", bidInfos);
        return "loanInfo";
    }


    @GetMapping("/loan/loanInfo/bidInfo/{loanId}/pageNum/{pageNum}")
    @ResponseBody
    public PageInfo<BidInfo> bidInfoList(@PathVariable Integer loanId,Model model,
                           @PathVariable(required = false) Integer pageNum){

        pageNum = pageNum == null ? 1 : pageNum;

        PageInfo<BidInfo> bidInfos =
                bidInfoService.queryBidInfoContainsUserPhoneByLoanId(loanId,pageNum);

        model.addAttribute("bidInfos", bidInfos);
        return bidInfos;
    }


}
