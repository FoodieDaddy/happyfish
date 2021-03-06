package com.mdmd.entity;

import java.io.Serializable;
import java.sql.Timestamp;


public class UserCommissionEntity{
    private int id;
    private Timestamp time;
    private String commissionType;
    private double commissionResult;
    private int nodeUserId;
    private double baseNumber;
    private int baseScale;
    private int childNodeIndex;
    private UserEntity userEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
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
