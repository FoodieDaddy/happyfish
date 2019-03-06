package com.mdmd.entity;


import java.io.Serializable;
import java.util.Calendar;



public class GoldEntity implements Serializable {
    private int id;
    private Integer calcDate;
    private double preGold;
    private double todayWater;
    private double todayGold;
    private int takeoutGold;

    public GoldEntity() {
        Calendar calendar = Calendar.getInstance();
       this.calcDate = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
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
        Calendar calendar = Calendar.getInstance();
        int todayInt = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
        if(todayInt != calcDate)
            return 0.0;
        return todayWater;
    }
}
