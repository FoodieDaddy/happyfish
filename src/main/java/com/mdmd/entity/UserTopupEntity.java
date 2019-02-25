package com.mdmd.entity;

import java.sql.Timestamp;

public class UserTopupEntity {
    private int id;
    private Timestamp time;
    private String orderNumber;
    private String topupType;
    private String topupResult;
    private int topupQuantity;
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

    public String getTopupResult() {
        return topupResult;
    }

    public void setTopupResult(String topupResult) {
        this.topupResult = topupResult;
    }

    public int getTopupQuantity() {
        return topupQuantity;
    }

    public void setTopupQuantity(int topupQuantity) {
        this.topupQuantity = topupQuantity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
