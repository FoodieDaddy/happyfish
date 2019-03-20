package com.mdmd.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTakeoutEntity implements Serializable{
    private int id;
    private Timestamp time;
    private int takeoutType;
    private int takeoutResult;
    private int takeoutQuantity;
    private double realUse;
    private String tradeOrderNum;
    private String wxOrderNum;
    private UserEntity userEntity;

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

    public int getTakeoutType() {
        return takeoutType;
    }

    public void setTakeoutType(int takeoutType) {
        this.takeoutType = takeoutType <= 0 ? 0 : 1;
    }

    public int getTakeoutResult() {
        return takeoutResult;
    }

    public void setTakeoutResult(int takeoutResult) {
        this.takeoutResult = takeoutResult <= 0 ? 0 : 1;
    }

    public int getTakeoutQuantity() {
        return takeoutQuantity;
    }

    public void setTakeoutQuantity(int takeoutQuantity) {
        this.takeoutQuantity = takeoutQuantity;
    }

    public double getRealUse() {
        return realUse;
    }

    public void setRealUse(double realUse) {
        this.realUse = realUse;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getTradeOrderNum() {
        return tradeOrderNum;
    }

    public void setTradeOrderNum(String tradeOrderNum) {
        this.tradeOrderNum = tradeOrderNum;
    }

    public String getWxOrderNum() {
        return wxOrderNum;
    }

    public void setWxOrderNum(String wxOrderNum) {
        this.wxOrderNum = wxOrderNum;
    }
}
