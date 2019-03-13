package com.mdmd.entity;

import java.sql.Timestamp;

public class UserOrderTopupEntity {
    private int id;
    private Timestamp time;
    private String orderNumber;
    private String topupType;
    private int topupQuantity;
    private int doCheck;
    private String doCheckResult;
    private UserEntity userEntity;

    public UserOrderTopupEntity() {
    }

    public UserOrderTopupEntity(String orderNumber, String topupType, int topupQuantity, UserEntity userEntity) {
        this.orderNumber = orderNumber;
        this.topupType = topupType;
        this.topupQuantity = topupQuantity;
        this.userEntity = userEntity;
        this.doCheckResult = " ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTopupType() {
        return topupType;
    }

    public void setTopupType(String topupType) {
        this.topupType = topupType;
    }

    public int getTopupQuantity() {
        return topupQuantity;
    }

    public void setTopupQuantity(int topupQuantity) {
        this.topupQuantity = topupQuantity;
    }

    public int getDoCheck() {
        return doCheck;
    }

    public void setDoCheck(int doCheck) {
        this.doCheck = doCheck;
    }

    public String getDoCheckResult() {
        return doCheckResult;
    }

    public void setDoCheckResult(String doCheckResult) {
        this.doCheckResult = doCheckResult;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
