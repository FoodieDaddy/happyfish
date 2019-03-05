package com.mdmd.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTakeoutEntity{
    private int id;
    private Timestamp time;
    private String takeoutType;
    private String takeoutResult;
    private int takeoutQuantity;
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

    public String getTakeoutType() {
        return takeoutType;
    }

    public void setTakeoutType(String takeoutType) {
        this.takeoutType = takeoutType;
    }

    public String getTakeoutResult() {
        return takeoutResult;
    }

    public void setTakeoutResult(String takeoutResult) {
        this.takeoutResult = takeoutResult;
    }

    public int getTakeoutQuantity() {
        return takeoutQuantity;
    }

    public void setTakeoutQuantity(int takeoutQuantity) {
        this.takeoutQuantity = takeoutQuantity;
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
