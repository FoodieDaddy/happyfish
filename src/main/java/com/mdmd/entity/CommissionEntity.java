package com.mdmd.entity;

public class CommissionEntity {
    private int id;
    private double preCommission;
    private double todayCommission;
    private int calcuDate;
    private int takeoutCommission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreCommission() {
        return preCommission;
    }

    public void setPreCommission(double preCommission) {
        this.preCommission = preCommission;
    }

    public double getTodayCommission() {
        return todayCommission;
    }

    public void setTodayCommission(double todayCommission) {
        this.todayCommission = todayCommission;
    }

    public int getCalcuDate() {
        return calcuDate;
    }

    public void setCalcuDate(int calcuDate) {
        this.calcuDate = calcuDate;
    }

    public int getTakeoutCommission() {
        return takeoutCommission;
    }

    public void setTakeoutCommission(int takeoutCommission) {
        this.takeoutCommission = takeoutCommission;
    }


}
