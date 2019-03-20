package com.mdmd.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTopupEntity  implements Serializable{
    private int id;
    private Timestamp time;
    private String orderNumber;
    private String foreignOrderNumber;
    private String topupType;
    private int topupResult;//结果  数据库默认为1 充值中
    private int topupQuantity;
    private double realQuantity;
    private String collector;
    private UserEntity userEntity;

    public UserTopupEntity() {
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

    public String getForeignOrderNumber() {
        return foreignOrderNumber;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public void setForeignOrderNumber(String foreignOrderNumber) {
        this.foreignOrderNumber = foreignOrderNumber;
    }

    public double getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(double realQuantity) {
        this.realQuantity = realQuantity;
    }

    public String getTopupType() {
        return topupType;
    }

    public void setTopupType(String topupType) {
        this.topupType = topupType;
    }

    public int getTopupResult() {
        return topupResult;
    }

    public void setTopupResult(int topupResult) {
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
