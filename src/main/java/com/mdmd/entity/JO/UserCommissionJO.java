package com.mdmd.entity.JO;

import com.mdmd.entity.UserCommissionEntity;
import com.mdmd.entity.UserEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserCommissionJO implements Serializable {
    private int id;
    private Timestamp time;
    private String commissionType;
    private double commissionResult;
    private int nodeUserId;
    private double baseNumber;
    private int baseScale;
    private int childNodeIndex;
    private int userId;

    public UserCommissionJO(UserCommissionEntity userCommissionEntity) {
        this.id = userCommissionEntity.getId();
        this.time = userCommissionEntity.getTime();
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
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
