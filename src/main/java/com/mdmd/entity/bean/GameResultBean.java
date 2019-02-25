package com.mdmd.entity.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyyyMMddHHmmss;

public class GameResultBean {

    private boolean gameResult;//记录游戏结果
    private double gameCost;//游戏输赢金额
    private String gameTime;//游戏时间

    public GameResultBean(boolean gameResult, double gameCost) {
        this.gameResult = gameResult;
        this.gameCost = gameCost;
        this.gameTime = new SimpleDateFormat(DATEFORMAT__yyyyMMddHHmmss).format(new Date());
    }

    public boolean getGameResult() {
        return gameResult;
    }

    public double getGameCost() {
        return gameCost;
    }

    public String getGameTime() {
        return gameTime;
    }
}
