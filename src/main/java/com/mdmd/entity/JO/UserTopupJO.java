package com.mdmd.entity.JO;

import com.mdmd.entity.UserEntity;
import com.mdmd.entity.UserTopupEntity;
import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTopupJO implements Serializable {
    private int id;
    private String time;
    private String orderNumber;
    private String topupType;
    private String topupResult;
    private int topupQuantity;
    private int userId;

    public UserTopupJO() {
    }

    public UserTopupJO(UserTopupEntity userTopupEntity) {
        this.id = userTopupEntity.getId();
        if(userTopupEntity.getTime() != null)
        {
            this.time = userTopupEntity.getTime().toString().split("\\.")[0];
        }
        else
        {
            this.time = DateFormatUtil.now_yyyyMMddHHmmss();
        }
        this.orderNumber = userTopupEntity.getOrderNumber();
        this.topupType = userTopupEntity.getTopupType();
        int topupResult = userTopupEntity.getTopupResult();
        if(topupResult == 0)
        {
            this.topupResult = "充值未到账";
        }
        else if(topupResult == 1)
        {
            this.topupResult = "充值成功";
        }
        this.topupQuantity = userTopupEntity.getTopupQuantity();
        this.userId = userTopupEntity.getUserEntity().getUserid();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
