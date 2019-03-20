package com.mdmd.entity.JO;

import com.mdmd.util.CommonUtil;

import java.io.Serializable;

public class AccountDatasJO implements Serializable {
    private String time;
    private int gameTime;//游戏次数
    private double topups;//充值总数
    private int takeoutGold;//金币提现总数
    private int takeoutCommission;//佣金提现总数
    private double earnings;//收益

    public AccountDatasJO(String time, int gameTime, double topups, int takeoutGold, int takeoutCommission) {
        this.time = time;
        this.gameTime = gameTime;
        this.topups = topups;
        this.takeoutGold = takeoutGold;
        this.takeoutCommission = takeoutCommission;
        this.earnings = CommonUtil.formatDouble_two(topups - gameTime - takeoutCommission - takeoutGold);
    }

    public String getTime() {
        return time;
    }

    public int getGameTime() {
        return gameTime;
    }

    public double getTopups() {
        return topups;
    }

    public int getTakeoutGold() {
        return takeoutGold;
    }

    public int getTakeoutCommission() {
        return takeoutCommission;
    }

    public double getEarnings() {
        return earnings;
    }
}
