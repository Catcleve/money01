package com.wang.money.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 毛能能
 */
@Data
public class RechargeRecord implements Serializable {
    private Integer id;

    private Integer uid;

    private String rechargeNo;

    private String rechargeStatus;

    private Double rechargeMoney;

    private Date rechargeTime;

    private String rechargeDesc;

    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public Double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(Double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getRechargeDesc() {
        return rechargeDesc;
    }

    public void setRechargeDesc(String rechargeDesc) {
        this.rechargeDesc = rechargeDesc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}