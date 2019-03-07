package com.mdmd.entity.JO;

import com.mdmd.entity.SysPropEntity;

import java.io.Serializable;

public class SysPropResultJO implements Serializable {
    private int id;
    private int sysNum;
    private String sysName;
    private String sysValue;

    public SysPropResultJO() {
    }

    public SysPropResultJO(SysPropEntity sysPropEntity) {
        this.id = sysPropEntity.getId();
        this.sysNum = sysPropEntity.getSysNum();
        this.sysName = sysPropEntity.getSysName();
        this.sysValue = sysPropEntity.getSysValue();
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

}
