package com.mdmd.entity;


import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mdmd.constant.SystemConstant.DATEFORMET__yyMMdd;


public class GoldEntity {
    private int id;
    private Integer calcDate;
    private double preGold;
    private double todayWater;
    private double todayGold;

    public GoldEntity() {
       this.calcDate = Integer.valueOf(new SimpleDateFormat(DATEFORMET__yyMMdd).format(new Date()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCalcDate() {
        return calcDate;
    }

    public void setCalcDate(Integer calcDate) {
        this.calcDate = calcDate;
    }

    public double getPreGold() {
        return preGold;
    }

    public void setPreGold(double preGold) {
        this.preGold = preGold;
    }

    public double getTodayWater() {
        return todayWater;
    }

    public void setTodayWater(double todayWater) {
        this.todayWater = todayWater;
    }

    public double getTodayGold() {
        return todayGold;
    }

    public void setTodayGold(double todayGold) {
        this.todayGold = todayGold;
    }

}
