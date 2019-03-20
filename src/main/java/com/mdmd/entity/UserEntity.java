package com.mdmd.entity;

import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.util.Set;


public class UserEntity implements Serializable {
    private int userid;
    private String userOpenid;
    private double gold;
    private double commission;
    private Integer superUserId_a;
    private Integer superUserId_b;
    private Integer superUserId_c;
    private Integer superUserId_d;
    private Integer superUserId_e;
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
        this.superUserId_a = 0;
        this.superUserId_b = 0;
        this.superUserId_c = 0;
        this.superUserId_d = 0;
        this.superUserId_e = 0;
    }

    public UserEntity(String userOpenid,UserEntity superUser) {
        this.userOpenid = userOpenid;
        this.superUserId_a = superUser.getUserid();
        this.superUserId_b = superUser.getSuperUserId_a();
        this.superUserId_c = superUser.getSuperUserId_b();
        this.superUserId_d = superUser.getSuperUserId_c();
        this.superUserId_e = superUser.getSuperUserId_d();

    }

    public UserEntity(int userid, String userOpenid, double gold, double commission,Integer superUserId_a,Integer superUserId_b,Integer superUserId_c,Integer superUserId_d,Integer superUserId_e, byte loginBan, byte takeoutBan, byte commissionGiveBan, byte commissionGetBan, byte topupBan, int takeoutTime,int calcuDay) {
        this.userid = userid;
        this.userOpenid = userOpenid;
        this.gold = gold;
        this.commission = commission;
        this.superUserId_a = superUserId_a;
        this.superUserId_b = superUserId_b;
        this.superUserId_c = superUserId_c;
        this.superUserId_d = superUserId_d;
        this.superUserId_e = superUserId_e;
        this.loginBan = loginBan;
        this.takeoutBan = takeoutBan;
        this.commissionGiveBan = commissionGiveBan;
        this.commissionGetBan = commissionGetBan;
        this.topupBan = topupBan;
        this.takeoutTime = takeoutTime;
        this.calcuDay = calcuDay;
    }

    public UserEntity(int userid, String userOpenid, double gold, double commission, Integer superUserId_a,Integer superUserId_b,Integer superUserId_c,Integer superUserId_d,Integer superUserId_e, byte loginBan, byte takeoutBan, byte commissionGiveBan, byte commissionGetBan, byte topupBan, int takeoutTime,int calcuDay , Set<GoldEntity> goldEntitySet,Set<CommissionEntity> commissionEntitySet) {
        this.userid = userid;
        this.userOpenid = userOpenid;
        this.gold = gold;
        this.commission = commission;
        this.superUserId_a = superUserId_a;
        this.superUserId_b = superUserId_b;
        this.superUserId_c = superUserId_c;
        this.superUserId_d = superUserId_d;
        this.superUserId_e = superUserId_e;
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

    public Integer getSuperUserId_a() {
        return superUserId_a;
    }

    public void setSuperUserId_a(Integer superUserId_a) {
        this.superUserId_a = superUserId_a;
    }

    public Integer getSuperUserId_b() {
        return superUserId_b;
    }

    public void setSuperUserId_b(Integer superUserId_b) {
        this.superUserId_b = superUserId_b;
    }

    public Integer getSuperUserId_c() {
        return superUserId_c;
    }

    public void setSuperUserId_c(Integer superUserId_c) {
        this.superUserId_c = superUserId_c;
    }

    public Integer getSuperUserId_d() {
        return superUserId_d;
    }

    public void setSuperUserId_d(Integer superUserId_d) {
        this.superUserId_d = superUserId_d;
    }

    public Integer getSuperUserId_e() {
        return superUserId_e;
    }

    public void setSuperUserId_e(Integer superUserId_e) {
        this.superUserId_e = superUserId_e;
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


}
