package com.wang.money.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户账户表对应实体类
 * @author 毛能能
 */
@Data
public class FinanceAccount implements Serializable {
    private Integer id;

    private Integer uid;

    private Double availableMoney;

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

    public Double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(Double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}