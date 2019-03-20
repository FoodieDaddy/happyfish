package com.mdmd.entity.JO;

import com.mdmd.util.CommonUtil;

import java.io.Serializable;

public class UserChildsDataJO implements Serializable {
    private int levelNum;//几级子类
    private double commissionSum;//子类佣金总数
    private int childsCount;//子类个数

    public UserChildsDataJO() {
    }

    public UserChildsDataJO(int levelNum, double commissionSum, int childsCount) {
        this.levelNum = levelNum;
        this.commissionSum = CommonUtil.formatDouble_three(commissionSum);
        this.childsCount = childsCount;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public double getCommissionSum() {
        return commissionSum;
    }

    public void setCommissionSum(double commissionSum) {
        this.commissionSum = commissionSum;
    }

    public int getChildsCount() {
        return childsCount;
    }

    public void setChildsCount(int childsCount) {
        this.childsCount = childsCount;
    }
}
