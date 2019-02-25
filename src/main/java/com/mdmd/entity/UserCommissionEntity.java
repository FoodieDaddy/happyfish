package com.mdmd.entity;

import java.sql.Timestamp;


public class UserCommissionEntity {
    private int id;
    private Timestamp time;
    private String commissionType;
    private double commissionResult;
    private int nodeUserId;
    private double baseNumber;
    private int baseScale;
    private int childNodeIndex;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCommissionEntity that = (UserCommissionEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.commissionResult, commissionResult) != 0) return false;
        if (nodeUserId != that.nodeUserId) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (commissionType != null ? !commissionType.equals(that.commissionType) : that.commissionType != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (commissionType != null ? commissionType.hashCode() : 0);
        temp = Double.doubleToLongBits(commissionResult);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + nodeUserId;
        return result;
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
