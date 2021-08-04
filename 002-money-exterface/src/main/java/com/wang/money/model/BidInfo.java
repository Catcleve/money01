package com.wang.money.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品投资记录
 * @author 毛能能
 */
@Data
public class BidInfo implements Serializable {
    private Integer id;

    private Integer loanId;

    private Integer uid;

    private Double bidMoney;

    private Date bidTime;

    private Integer bidStatus;

    private User user;

}