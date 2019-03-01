package com.mdmd.entity.JO;

import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserTopupEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTopupJO implements Serializable {
    private int id;
    private Timestamp time;
    private String orderNumber;
    private String topupType;
    private String topupResult;
    private int topupQuantity;
    private int userId;

    public UserTopupJO() {
    }

    public UserTopupJO(UserTopupEntity userTopupEntity) {
        this.id = userTopupEntity.getId();
        this.time = userTopupEntity.getTime();
        this.orderNumber = userTopupEntity.getOrderNumber();
        this.topupType = userTopupEntity.getTopupType();
        this.topupResult = userTopupEntity.getTopupResult();
        this.topupQuantity = userTopupEntity.getTopupQuantity();
        this.userId = userTopupEntity.getUserEntity().getUserid();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
