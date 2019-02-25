package com.mdmd.entity;

import java.io.Serializable;

public class SysPropEntity {
    private int id;
    private int sysNum;
    private String sysName;
    private String sysValue;

    public SysPropEntity() {
    }

    public SysPropEntity(int sysNum, String sysName, String sysValue) {
        this.sysNum = sysNum;
        this.sysName = sysName;
        this.sysValue = sysValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSysNum() {
        return sysNum;
    }

    public void setSysNum(int sysNum) {
        this.sysNum = sysNum;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysPropEntity that = (SysPropEntity) o;

        if (id != that.id) return false;
        if (sysNum != that.sysNum) return false;
        if (sysName != null ? !sysName.equals(that.sysName) : that.sysName != null) return false;
        if (sysValue != null ? !sysValue.equals(that.sysValue) : that.sysValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + sysNum;
        result = 31 * result + (sysName != null ? sysName.hashCode() : 0);
        result = 31 * result + (sysValue != null ? sysValue.hashCode() : 0);
        return result;
    }
}
