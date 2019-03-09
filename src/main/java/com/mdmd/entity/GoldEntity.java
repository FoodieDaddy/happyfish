package com.mdmd.entity;


import com.mdmd.controller.GameAction;
import com.mdmd.util.DateFormatUtil;

import java.io.Serializable;
import java.util.Date;



public class GoldEntity implements Serializable {
    private int id;
    private Integer calcDate;
    private double preGold;
    private double todayWater;
    private double todayGold;
    private int takeoutGold;

    public GoldEntity() {
       this.calcDate = DateFormatUtil.now_yyMMdd_intVal();
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

    public int getTakeoutGold() {
        return takeoutGold;
    }

    public void setTakeoutGold(int takeoutGold) {
        this.takeoutGold = takeoutGold;
    }

    public double todayWater (){
        int todayInt = DateFormatUtil.now_yyMMdd_intVal();
        if(todayInt != calcDate)
            return 0.0;
        return todayWater;
    }
}
