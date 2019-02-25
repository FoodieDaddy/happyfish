package com.mdmd.entity.JO;

import com.mdmd.entity.SysPropEntity;

import java.io.Serializable;

public class SysPropResultJO implements Serializable {
    private int sysNum;
    private String sysName;
    private Object sysValue;

    public SysPropResultJO() {
    }

    public SysPropResultJO(int sysNum, String sysName) {
        this.sysNum = sysNum;
        this.sysName = sysName;
    }

    public SysPropResultJO(int sysNum, String sysName, Object sysValue) {
        this.sysNum = sysNum;
        this.sysName = sysName;
        this.sysValue = sysValue;
    }

    public SysPropResultJO(SysPropEntity sysPropEntity) {
        this.sysNum = sysPropEntity.getSysNum();
        this.sysName = sysPropEntity.getSysName();
        this.sysValue = sysPropEntity.getSysValue();
    }
    public SysPropResultJO(SysPropEntity sysPropEntity,Object sysValue) {
        this.sysNum = sysPropEntity.getSysNum();
        this.sysName = sysPropEntity.getSysName();
        this.sysValue = sysValue;
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

    public Object getSysValue() {
        return sysValue;
    }

    public void setSysValue(Object sysValue) {
        this.sysValue = sysValue;
    }
}
