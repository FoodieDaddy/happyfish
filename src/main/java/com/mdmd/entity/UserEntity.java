package com.mdmd.entity;

import java.io.Serializable;
import java.util.Set;

public class UserEntity implements Serializable {
    private int userid;
    private String userOpenid;
    private double gold;
    private double commission;
    private Integer superUserId;
    private byte loginBan;
    private byte takeoutBan;
    private byte commissionGiveBan;
    private byte commissionGetBan;
    private byte topupBan;
    private int takeoutTime;
    private int calcuDay;
    private Set<GoldEntity> goldEntitySet;
    private Set<CommissionEntity> commissionEntitySet;
    public UserEntity() {
    }

    public UserEntity(String userOpenid) {
        this.userOpenid = userOpenid;
    }

    public UserEntity(String userOpenid, Integer superUserId) {
        this.userOpenid = userOpenid;
        this.superUserId = superUserId;
    }

    public UserEntity(int userid, String userOpenid, double gold, double commission, Integer superUserId, byte loginBan, byte takeoutBan, byte commissionGiveBan, byte commissionGetBan, byte topupBan, int takeoutTime,int calcuDay) {
        this.userid = userid;
        this.userOpenid = userOpenid;
        this.gold = gold;
        this.commission = commission;
        this.superUserId = superUserId;
        this.loginBan = loginBan;
        this.takeoutBan = takeoutBan;
        this.commissionGiveBan = commissionGiveBan;
        this.commissionGetBan = commissionGetBan;
        this.topupBan = topupBan;
        this.takeoutTime = takeoutTime;
        this.calcuDay = calcuDay;
    }

    public UserEntity(int userid, String userOpenid, double gold, double commission, Integer superUserId, byte loginBan, byte takeoutBan, byte commissionGiveBan, byte commissionGetBan, byte topupBan, int takeoutTime,int calcuDay , Set<GoldEntity> goldEntitySet,Set<CommissionEntity> commissionEntitySet) {
        this.userid = userid;
        this.userOpenid = userOpenid;
        this.gold = gold;
        this.commission = commission;
        this.superUserId = superUserId;
        this.loginBan = loginBan;
        this.takeoutBan = takeoutBan;
        this.commissionGiveBan = commissionGiveBan;
        this.commissionGetBan = commissionGetBan;
        this.topupBan = topupBan;
        this.takeoutTime = takeoutTime;
        this.calcuDay = calcuDay;
        this.goldEntitySet = goldEntitySet;
        this.commissionEntitySet = commissionEntitySet;
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserOpenid() {
        return userOpenid;
    }

    public void setUserOpenid(String userOpenid) {
        this.userOpenid = userOpenid;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public Integer getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(Integer superUserId) {
        this.superUserId = superUserId;
    }


    public byte getLoginBan() {
        return loginBan;
    }

    public void setLoginBan(byte loginBan) {
        this.loginBan = loginBan;
    }

    public byte getTakeoutBan() {
        return takeoutBan;
    }

    public void setTakeoutBan(byte takeoutBan) {
        this.takeoutBan = takeoutBan;
    }

    public byte getCommissionGiveBan() {
        return commissionGiveBan;
    }

    public void setCommissionGiveBan(byte commissionGiveBan) {
        this.commissionGiveBan = commissionGiveBan;
    }

    public byte getCommissionGetBan() {
        return commissionGetBan;
    }

    public void setCommissionGetBan(byte commissionGetBan) {
        this.commissionGetBan = commissionGetBan;
    }

    public byte getTopupBan() {
        return topupBan;
    }

    public void setTopupBan(byte topupBan) {
        this.topupBan = topupBan;
    }


    public Set<GoldEntity> getGoldEntitySet() {
        return goldEntitySet;
    }

    public void setGoldEntitySet(Set<GoldEntity> goldEntitySet) {
        this.goldEntitySet = goldEntitySet;
    }

    public int getTakeoutTime() {
        return takeoutTime;
    }

    public void setTakeoutTime(int takeoutTime) {
        this.takeoutTime = takeoutTime;
    }

    public int getCalcuDay() {
        return calcuDay;
    }

    public void setCalcuDay(int calcuDay) {
        this.calcuDay = calcuDay;
    }


    public Set<CommissionEntity> getCommissionEntitySet() {
        return commissionEntitySet;
    }

    public void setCommissionEntitySet(Set<CommissionEntity> commissionEntitySet) {
        this.commissionEntitySet = commissionEntitySet;
    }

    public GoldEntity singleGoldEntity(){
        if(goldEntitySet == null)
            return null;
        if(goldEntitySet.size() == 0)
            return null;
        return (GoldEntity) goldEntitySet.toArray()[0];
    }
    public CommissionEntity singleCommissionEntity(){
        if(commissionEntitySet == null)
            return null;
        if(commissionEntitySet.size() == 0)
            return null;
        return (CommissionEntity) commissionEntitySet.toArray()[0];
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userid=" + userid +
                ", userOpenid='" + userOpenid + '\'' +
                ", gold=" + gold +
                ", commission=" + commission +
                ", superUserId=" + superUserId +
                ", loginBan=" + loginBan +
                ", takeoutBan=" + takeoutBan +
                ", commissionGiveBan=" + commissionGiveBan +
                ", commissionGetBan=" + commissionGetBan +
                ", topupBan=" + topupBan +
                ", takeoutTime=" + takeoutTime +
                ", calcuDay=" + calcuDay +
                ", goldEntitySet=" + goldEntitySet +
                ", commissionEntitySet=" + commissionEntitySet +
                '}';
    }
}
