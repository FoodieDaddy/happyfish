package com.mdmd.entity;

public class AdminUserEntity {
    private int adminid;
    private String openid;
    private String aliasName;
    private int setMoney;
    private int setSys;

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public int getSetMoney() {
        return setMoney;
    }

    public void setSetMoney(int setMoney) {
        this.setMoney = setMoney;
    }

    public int getSetSys() {
        return setSys;
    }

    public void setSetSys(int setSys) {
        this.setSys = setSys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminUserEntity that = (AdminUserEntity) o;

        if (adminid != that.adminid) return false;
        if (setMoney != that.setMoney) return false;
        if (setSys != that.setSys) return false;
        if (openid != null ? !openid.equals(that.openid) : that.openid != null) return false;
        if (aliasName != null ? !aliasName.equals(that.aliasName) : that.aliasName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = adminid;
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (aliasName != null ? aliasName.hashCode() : 0);
        result = 31 * result + setMoney;
        result = 31 * result + setSys;
        return result;
    }
}
