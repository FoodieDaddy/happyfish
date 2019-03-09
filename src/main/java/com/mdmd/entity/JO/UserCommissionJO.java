package com.mdmd.entity.JO;

import com.mdmd.entity.UserCommissionEntity;
import com.mdmd.entity.UserEntity;
import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserCommissionJO implements Serializable {
    private int id;
    private String time;
    private String commissionType;
    private double commissionResult;
    private int nodeUserId;
    private double baseNumber;
    private int baseScale;
    private int childNodeIndex;
    private int userId;

    public UserCommissionJO(UserCommissionEntity userCommissionEntity) {
        this.id = userCommissionEntity.getId();
        if(userCommissionEntity.getTime() != null)
        {
            this.time = userCommissionEntity.getTime().toString().split("\\.")[0];
        }
        else
        {
            this.time = DateFormatUtil.now_yyyyMMddHHmmss();
        }
        this.commissionType = userCommissionEntity.getCommissionType();
        this.commissionResult = userCommissionEntity.getCommissionResult();
        this.nodeUserId = userCommissionEntity.getNodeUserId();
        this.baseNumber = userCommissionEntity.getBaseNumber();
        this.baseScale = userCommissionEntity.getBaseScale();
        this.childNodeIndex = userCommissionEntity.getChildNodeIndex();
        this.userId = userCommissionEntity.getUserEntity().getUserid();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public double getCommissionResult() {
        return commissionResult;
    }

    public void setCommissionResult(double commissionResult) {
        this.commissionResult = commissionResult;
    }

    public int getNodeUserId() {
        return nodeUserId;
    }

    public void setNodeUserId(int nodeUserId) {
        this.nodeUserId = nodeUserId;
    }

    public double getBaseNumber() {
        return baseNumber;
    }

    public void setBaseNumber(double baseNumber) {
        this.baseNumber = baseNumber;
    }

    public int getBaseScale() {
        return baseScale;
    }

    public void setBaseScale(int baseScale) {
        this.baseScale = baseScale;
    }

    public int getChildNodeIndex() {
        return childNodeIndex;
    }

    public void setChildNodeIndex(int childNodeIndex) {
        this.childNodeIndex = childNodeIndex;
    }
}
