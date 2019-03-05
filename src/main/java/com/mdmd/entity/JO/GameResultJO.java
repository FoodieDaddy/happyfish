package com.mdmd.entity.JO;

import java.io.Serializable;

public class GameResultJO implements Serializable {
    private boolean gameResult;//记录游戏结果
    private double gameCost;//游戏输赢金额
    private double gold;//剩余金币
    private double commission;//剩余佣金
    private String gameAccording;//游戏依据（例如：订单号）
    private String msg;//提示 错误时才添加有

    public GameResultJO(String msg) {
        this.msg = msg;
    }

    public GameResultJO(boolean gameResult, double gameCost, double gold, double commission) {
        this.gameResult = gameResult;
        this.gameCost = gameCost;
        this.gold = gold;
        this.commission = commission;
        this.msg = null;
    }

    public GameResultJO(boolean gameResult, double gameCost, double gold, double commission, String gameAccording) {
        this.gameResult = gameResult;
        this.gameCost = gameCost;
        this.gold = gold;
        this.commission = commission;
        this.gameAccording = gameAccording;
        this.msg = null;
    }

    public boolean isGameResult() {
        return gameResult;
    }

    public void setGameResult(boolean gameResult) {
        this.gameResult = gameResult;
    }

    public double getGameCost() {
        return gameCost;
    }

    public void setGameCost(double gameCost) {
        this.gameCost = gameCost;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGameAccording() {
        return gameAccording;
    }

    public void setGameAccording(String gameAccording) {
        this.gameAccording = gameAccording;
    }

    public boolean isSuccess(){
        if(this.msg == null)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "GameResultJO{" +
                "gameResult=" + gameResult +
                ", gameCost=" + gameCost +
                ", gold=" + gold +
                ", commission=" + commission +
                ", gameAccording='" + gameAccording + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
